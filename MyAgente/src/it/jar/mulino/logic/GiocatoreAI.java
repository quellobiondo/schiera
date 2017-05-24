package it.jar.mulino.logic;

import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.Minimax;
import it.jar.mulino.ricerca.NineMensMorrisSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.GC;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(GiocatoreAI.class);

    private Mossa mossaKiller;
    private Stato statoAttuale;
    private NineMensMorrisSearch ricerca;

    private final Lock lock = new ReentrantLock();
    private final Condition condizioneRisposta = lock.newCondition();
    private final int tempoTurno;

    private boolean statoCambiato;

    private GiocatoreAI(Stato stato, boolean isBianco, int secondiTurno) {
        super(stato);
        statoAttuale = stato;
        this.tempoTurno = secondiTurno;
        //statoAttuale.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;
        //ricerca = new NineMensMorrisSearch(Minimax.Algorithm.NEGASCOUT, stato);
        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.MINIMAX, stato);
    }

    /**crea uno pseudo-attore attivo*/
    public static GiocatoreAI create(Stato stato, boolean isBianco, int durataTurno){
        GiocatoreAI giocatore = new GiocatoreAI(stato, isBianco, durataTurno-2);
        Thread thread = new Thread(giocatore);
        thread.setDaemon(true);
        thread.setName("Ai-R");
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        return giocatore;
    }
    @Override
    public Mossa getMossa(){
        logger.debug("Dammi la tua mossa");
        try {
            lock.lock();
            condizioneRisposta.await(tempoTurno, TimeUnit.SECONDS);
            if (!validaMossa(mossaKiller)){
                logger.error("mossa non valida");
            }

            lock.unlock();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        logger.debug("La mia mossa killer e' "+mossaKiller);
        return mossaKiller;
    }

    @Override
    public void aggiornaStato(Stato stato){
        logger.debug("Aggiorna lo stato");
        this.statoAttuale = stato;
        checkMossaKiller(stato);
        statoCambiato=true;
    }

    private void checkMossaKiller(Stato stato){
        if(mossaKiller==null || !stato.getPossibleMoves().contains(mossaKiller)){
            logger.debug("Mossa killer "+mossaKiller+" non idonea con il nuovo stato");
            List<Mossa> mosse = stato.getPossibleMoves();
            mosse.sort((mossa1, mossa2) -> {
                Stato s1 = statoAttuale.copia();
                s1.makeMove(mossa1);
                int valueUno = ValutatoreStato.valutaStato(s1);
                s1.unmakeMove(mossa1);
                s1.makeMove(mossa2);
                int valueDue = ValutatoreStato.valutaStato(s1);
                return valueUno - valueDue;
            });
            //la migliore delle mosse possibili
            mossaKiller = mosse.get(0);//patch
            logger.debug("Nuova mossa killer: "+mossaKiller);
        }
    }

    @Override
    public void run(){
        int depth = 3;
        while(true){
            depth++;
            //esplora l'albero iterativamente per ottenere la mossa migliore
            logger.debug("Profondità "+depth+" ... ");

            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore
            logger.debug("... mossa migliore: "+mossaKiller+" stato cambiato? "+statoCambiato);

            lock.lock();
            if(statoAttuale.willWin(mossaKiller) && !statoCambiato){
                logger.debug("Con questa mossa si vince!");
                //se vinco con quella mossa e lo stato non è cambiato allora facciamola!
                condizioneRisposta.signal();
            }
            lock.unlock();
            //pota l'albero se lo stato attuale della partita è cambiato
            if(statoCambiato){
                logger.debug("Lo stato è cambiato");
                checkMossaKiller(statoAttuale);
                ricerca.statoAttualeAggiornato(statoAttuale);
                statoCambiato = false;
                depth = 3; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
