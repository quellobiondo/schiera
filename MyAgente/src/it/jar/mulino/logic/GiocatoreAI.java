package it.jar.mulino.logic;

import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.Minimax;
import it.jar.mulino.ricerca.NineMensMorrisSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private GiocatoreAI(Stato stato, boolean isBianco, int secondiTurno){
        statoAttuale = stato;
        this.tempoTurno = secondiTurno;
        //statoAttuale.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;
        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.NEGASCOUT, stato);
    }

    /**crea uno pseudo-attore attivo*/
    public static GiocatoreAI create(Stato stato, boolean isBianco, int durataTurno){
        GiocatoreAI giocatore = new GiocatoreAI(stato, isBianco, durataTurno-2);
        Thread thread = new Thread(giocatore);
        thread.setDaemon(true);
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        return giocatore;
    }

    @Override
    public Mossa getMove(){
    	System.out.println("Dammi la tua mossa");
        try {
            lock.lock();
            condizioneRisposta.await(tempoTurno, TimeUnit.SECONDS);
            lock.unlock();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("La mia mossa killer e' "+mossaKiller);
        return mossaKiller;
    }

    @Override
    public void updateState(Stato stato){
        logger.debug("Aggiorna lo stato");
        this.statoAttuale = stato;
        checkMossaKiller(stato);
        statoCambiato=true;
    }

    private void checkMossaKiller(Stato stato){
        if(mossaKiller==null || !stato.getPossibleMoves().contains(mossaKiller)){
            logger.debug("Mossa killer "+mossaKiller+" non idonea con il nuovo stato");
            mossaKiller = stato.getPossibleMoves().get(0); //patch
            logger.debug("Nuova mossa killer: "+mossaKiller);
        }
    }

    @Override
    public void run(){
        int depth = 10;
        while(true){
            //esplora l'albero iterativamente per ottenere la mossa migliore
            lock.lock();
            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore
            logger.debug("Profondità "+depth+" mossa migliore: "+mossaKiller+" stato cambiato? "+statoCambiato);
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
                depth = 8; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
