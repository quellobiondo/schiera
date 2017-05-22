package it.jar.mulino.logic;

import it.jar.mulino.model.Mossa;
import it.jar.mulino.model.MossaPosiziona;
import it.jar.mulino.model.MossaSposta;
import it.jar.mulino.model.Stato;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreUmano extends Giocatore {

    private Stato stato;

    private static Mossa leggiMossaFaseUno(){
        return new MossaPosiziona((byte)1);
    }

    private static Mossa leggiMossaFaseDue(){
        return new MossaSposta((byte)0, (byte)1);
    }

    private static Mossa leggiMossaFaseTre(){
        return new MossaSposta((byte)0, (byte)1);
    }

    @Override
    public Mossa getMove() {
        if(stato.isFirstPhase()){
            System.out.println("posizionePedina [Posizione pedina rimossa]");
            return leggiMossaFaseUno();
        }else{
            if(stato.canJump(Stato.NOI)){
                System.out.println("da a [Posizione pedina rimossa]");
                return leggiMossaFaseTre();
            }else {
                System.out.println("da a [Posizione pedina rimossa]");
                return leggiMossaFaseDue();
            }
        }
    }

    @Override
    public void updateState(Stato stato) {
        this.stato = stato;
    }
}
