package it.jar.mulino.logic;

import static it.jar.mulino.utils.NineMensMorrisSetting.*;
import it.jar.mulino.model.*;

/**
 * Un giocatore può ricevere aggiornamenti sullo stato della partita e può fare una mossa per modificare lo stato della partita
 *
 * Created by ziro on 22/05/17.
 */
public abstract class Giocatore {
	protected Stato stato;
	protected Giocatore(Stato statoIniziale){
		stato=statoIniziale;
	}
	public abstract Mossa getMossa();
	public void aggiornaStato(Stato stato){
		this.stato=stato;
	}
	public boolean validaMossa(Mossa m){	//bozza
		if ((stato.phase1completed() && m.isPutMove())||(!stato.phase1completed() && !m.isPutMove()))
			return false;
		if ((stato.board[PLAYER_W]>>>m.getFrom()&1)==1 || (stato.board[PLAYER_B]>>>m.getFrom()&1)==1)
			return false;
		if (stato.phase1completed() &&(stato.board[stato.currentPlayer]>>>m.getFrom()&1)==0)
			return false;
		return true;
	}
}
