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

<<<<<<< HEAD
    private static final Logger logger = LoggerFactory.getLogger(GiocatoreAI.class);

    private Mossa mossaKiller;
    private Stato statoAttuale;
=======
    private static final long DURATA_MOSSA=15000;//58000;
	private Mossa mossaKiller;
    //private Stato stato;
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
    private NineMensMorrisSearch ricerca;

    private final Lock lock = new ReentrantLock();
    private final Condition condizioneRisposta = lock.newCondition();
    private final int tempoTurno;

    private boolean statoCambiato;

<<<<<<< HEAD
    private GiocatoreAI(Stato stato, boolean isBianco, int secondiTurno){
        statoAttuale = stato;
        this.tempoTurno = secondiTurno;
        //statoAttuale.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;
        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.NEGASCOUT, stato);
=======
    private GiocatoreAI(Stato stato, boolean isBianco){
    	super(stato);
        stato.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;

        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.BNS, stato);
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
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
    public Mossa getMossa(){
    	System.out.println("Dammi la tua mossa");
<<<<<<< HEAD
        try {
            lock.lock();
            condizioneRisposta.await(tempoTurno, TimeUnit.SECONDS);
            lock.unlock();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        System.out.println("La mia mossa killer e' "+mossaKiller);
=======
    	try{
			Thread.sleep(DURATA_MOSSA);
			
			if (!validaMossa(mossaKiller)){
				System.err.println("mossa non valida");
			}
		} catch (InterruptedException e){}
		System.out.println("La mia mossa killer è "+mossaKiller);
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
        return mossaKiller;
    }

    @Override
<<<<<<< HEAD
    public void updateState(Stato stato){
        logger.debug("Aggiorna lo stato");
        this.statoAttuale = stato;
        checkMossaKiller(stato);
=======
    public void aggiornaStato(Stato stato){
        this.stato = stato;
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
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
<<<<<<< HEAD
            logger.debug("ProfonditÃ  "+depth+" mossa migliore: "+mossaKiller+" stato cambiato? "+statoCambiato);
            if(statoAttuale.willWin(mossaKiller) && !statoCambiato){
                logger.debug("Con questa mossa si vince!");
                //se vinco con quella mossa e lo stato non Ã¨ cambiato allora facciamola!
                condizioneRisposta.signal();
            }
            lock.unlock();
            //pota l'albero se lo stato attuale della partita Ã¨ cambiato
            if(statoCambiato){
                logger.debug("Lo stato Ã¨ cambiato");
                checkMossaKiller(statoAttuale);
                ricerca.statoAttualeAggiornato(statoAttuale);
                statoCambiato = false;
                depth = 8; //ricominciamo ad esplorare a profonditÃ  limitata
=======

            //pota l'albero se lo stato attuale della partita è cambiato
            if(statoCambiato){
                checkMossaKiller(stato);
                ricerca.statoAttualeAggiornato(stato);
                statoCambiato = false;
                depth = 3; //ricominciamo ad esplorare a profondità limitata
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
            }
        }
    }
}
