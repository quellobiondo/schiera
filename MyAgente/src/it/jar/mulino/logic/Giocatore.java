package it.jar.mulino.logic;

import it.jar.mulino.model.Mossa;
import it.jar.mulino.model.Stato;

/**
 * Un giocatore può ricevere aggiornamenti sullo stato della partita
 * e può fare una mossa per modificare lo stato della partita
 *
 * Created by ziro on 22/05/17.
 */
public abstract class Giocatore {

    public abstract Mossa getMove();
    public abstract void updateState(Stato stato);

}
