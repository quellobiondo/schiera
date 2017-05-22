package it.jar.mulino.model;

import java.io.Serializable;
import java.util.BitSet;

/**
 * Created by ziro on 09/05/17.
 */
public class Stato implements Serializable{


    /**
     * Un BitSet rappresenta una schiera intera.
     * Ogni bit è ad uno se è presente una pedina, 0 se non è presente
     * Le pedine nostre e quelle dell'avversario sono rappresentate su schiere differenti
     */
    private static final byte NUMERO_CELLE_SCHIERA = 24;
    private static final byte NUMERO_PEDINE = 9;
    public static final byte NOI=0;
    private static final byte LORO=1;

    private BitSet[] stato = new BitSet[2];
    private byte[] posizionati = new byte[2];
    private byte[] ingioco = new byte[2];

    public Stato(){
        stato[NOI]=new BitSet(NUMERO_CELLE_SCHIERA);
        stato[LORO]=new BitSet(NUMERO_CELLE_SCHIERA);
    }

    private void put(byte posizione, byte chi){
        stato[chi].set(posizione);
        ingioco[chi]++;
        posizionati[chi]++;
    }

    private void remove(byte posizione, byte chi) {
        stato[chi].clear(posizione);
        ingioco[chi]--;
    }

    private void move(byte from, byte to, byte chi){
        stato[chi].clear(from);
        stato[chi].set(to);
    }

    public boolean isFirstPhase() {
        return posizionati[NOI]<NUMERO_PEDINE && posizionati[LORO]<NUMERO_PEDINE;
    }

    public boolean canJump(byte chi){
        return ingioco[chi] == 3;
    }
}
