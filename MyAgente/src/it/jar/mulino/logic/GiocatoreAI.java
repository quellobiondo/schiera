package it.jar.mulino.logic;

import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.Minimax;
import it.jar.mulino.ricerca.NineMensMorrisSearch;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final long DURATA_MOSSA=20000;//58000;
	private Mossa mossaKiller;
    private Stato statoAttuale;
    private NineMensMorrisSearch ricerca;

    private boolean statoCambiato;

    private GiocatoreAI(Stato stato, boolean isBianco){
        statoAttuale = stato;
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

    @Override
    public Mossa getMove(){
    	try{
			Thread.sleep(DURATA_MOSSA);
		} catch (InterruptedException e){}
        return mossaKiller;
    }

    @Override
    public void updateState(Stato stato){
        this.statoAttuale = stato;
        statoCambiato=true;
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
                ricerca.statoAttualeAggiornato(statoAttuale);
                statoCambiato = !statoCambiato;
                depth = 3; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
