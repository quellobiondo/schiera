package it.jar.mulino.model;

import java.util.BitSet;

/**
 * Created by ziro on 09/05/17.
 */
public class Stato implements Cloneable{

    /**
     * Un BitSet rappresenta una schiera intera.
     * Ogni bit è ad uno se è presente una pedina, 0 se non è presente
     * Le pedine nostre e quelle dell'avversario sono rappresentate su schiere differenti
     */
    private BitSet noi, altri;
    private static final int NUMERO_CELLE_SCHIERA = 24;

    public Stato(){
        altri=new BitSet(NUMERO_CELLE_SCHIERA);
        noi=new BitSet(NUMERO_CELLE_SCHIERA);
    }

    public void putAvversari(byte posizione){
        altri.set(posizione);
    }

    public void putMio(byte pos){
        noi.set(pos);
    }

}
