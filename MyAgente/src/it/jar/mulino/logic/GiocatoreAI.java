package it.jar.mulino.logic;

import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.Minimax;
import it.jar.mulino.ricerca.NineMensMorrisSearch;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final long DURATA_MOSSA=15000;//58000;
	private Mossa mossaKiller;
    //private Stato stato;
    private NineMensMorrisSearch ricerca;

    private boolean statoCambiato;

    private GiocatoreAI(Stato stato, boolean isBianco){
    	super(stato);
        stato.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;

        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.BNS, stato);
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
    @Override
    public Mossa getMossa(){
    	System.out.println("Dammi la tua mossa");
    	try{
			Thread.sleep(DURATA_MOSSA);
			
			if (!validaMossa(mossaKiller)){
				System.err.println("mossa non valida");
			}
		} catch (InterruptedException e){}
		System.out.println("La mia mossa killer è "+mossaKiller);
        return mossaKiller;
    }

    @Override
    public void aggiornaStato(Stato stato){
        this.stato = stato;
        statoCambiato=true;
    }

    private void checkMossaKiller(Stato stato){
        if(mossaKiller==null || !stato.getPossibleMoves().contains(mossaKiller)){
            mossaKiller = stato.getPossibleMoves().get(0); //patch
        }
    }

    @Override
    public void run(){
        int depth = 3;
        while(true){
            //esplora l'albero iterativamente per ottenere la mossa migliore
            depth++;
            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore

            //pota l'albero se lo stato attuale della partita è cambiato
            if(statoCambiato){
                checkMossaKiller(stato);
                ricerca.statoAttualeAggiornato(stato);
                statoCambiato = false;
                depth = 3; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
