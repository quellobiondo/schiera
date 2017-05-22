package it.jar.mulino.logic;

import it.jar.mulino.model.Mossa;
import it.jar.mulino.model.Stato;
import it.unibo.ai.didattica.mulino.domain.State;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private Mossa mossaKiller;
    private Stato statoAttuale;

    private boolean primoAGiocare;

    private GiocatoreAI(Stato stato, boolean isBianco){
        statoAttuale = stato;
        primoAGiocare = isBianco;
    }

    //crea uno pseudo-attore attivo
    public static GiocatoreAI create(Stato stato, boolean isBianco){
        GiocatoreAI giocatore = new GiocatoreAI(stato, isBianco);
        new Thread(giocatore).start();
        return giocatore;
    }

    @Override
    public Mossa getMove() {
        return mossaKiller;
    }

    @Override
    public void updateState(Stato stato) {
        this.statoAttuale = stato;
    }

    @Override
    public void run() {
        while(true){
            //esplora l'albero per ottenere la mossa migliore
            //setta la mossa migliore
            //pota l'albero in base allo stato attuale (può essere modificato anche dall'avversario)
        }
    }
}
