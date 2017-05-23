package it.jar.mulino.logic;

import java.util.concurrent.*;
import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.Minimax;
import it.jar.mulino.ricerca.NineMensMorrisSearch;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final long DURATA_MOSSA=20000;//58000;
	private Mossa mossaKiller;
    //private Stato stato;
    private NineMensMorrisSearch ricerca;

    private boolean statoCambiato;

    private GiocatoreAI(Stato stato, boolean isBianco){
    	super(stato);
        stato.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;

        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.NEGASCOUT, stato);
    }

    /**crea uno pseudo-attore attivo*/
    public static GiocatoreAI create(Stato stato, boolean isBianco){
        GiocatoreAI giocatore = new GiocatoreAI(stato, isBianco);
        Thread thread = new Thread(giocatore);
        thread.setDaemon(true);
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        return giocatore;
    }
    Semaphore s=new Semaphore(0);
    @Override
    public Mossa getMossa(){
    	try{
			Thread.sleep(DURATA_MOSSA);
			synchronized (this){
				wait();//58000-DURATA_MOSSA);
			}//s.tryAcquire(58000-DURATA_MOSSA,TimeUnit.MILLISECONDS);
			if (!validaMossa(mossaKiller)){
				System.err.println("mossa non valida");
			}
		} catch (InterruptedException e){}
        return mossaKiller;
    }

    @Override
    public void aggiornaStato(Stato stato){
        this.stato = stato;
        statoCambiato=true;
    }

    @Override
    public void run(){
        int depth = 3;
        while(true){
            //esplora l'albero iterativamente per ottenere la mossa migliore
            depth++;
            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore
            /*if (s.availablePermits()==0)
            	s.release();*/
            synchronized (this){
				notify();
			}
            //pota l'albero se lo stato attuale della partita Ë cambiato
            if(statoCambiato){
                ricerca.statoAttualeAggiornato(stato);
                statoCambiato = false;
                depth = 3; //ricominciamo ad esplorare a profondit√† limitata
            }
        }
    }
}
