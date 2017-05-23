package it.jar.mulino.ricerca;

import java.util.*;
import it.jar.mulino.model.*;

public class StatoRicky {
	private final byte g;
	private final Scacchiera s;
	private final boolean m;	//si deve mangiare
	private List<Mossa> lm;

	private StatoRicky(Scacchiera s, byte g, boolean m){
		assert g==Casella.BIANCA || g==Casella.NERA;
		this.g=g;
		this.s=s;
		this.m=m;
	}
	/**Lo stato con le pedine disposte come in {@code s} e in cui tocca a {@code g}
	 * @param s - la scacchiera con la disposizione attuale delle pedine
	 * @param g - il giocatore di turno
	 */
	public StatoRicky(Scacchiera s, byte g){
		this(s,g,false);
	}
	/**Lo stato iniziale del gioco*/
	public StatoRicky(){
		this(new Scacchiera(),Casella.BIANCA,false);
	}
	/**Prova a fare una mossa
	 * @param m - la mossa da fare
	 * @return lo stato raggiunto dopo la mossa
	 */
	public StatoRicky muovi(Mossa m){
		StatoRicky s=m.tris() ? new StatoRicky((Scacchiera)this.s.clone(),g,true) : new StatoRicky((Scacchiera)this.s.clone(),Casella.altroColore(g),false);
		m.apply(s.s);
		lm=null;
		return s;
	}
	public byte getGiocatoreDiTurno(){
		return g;
	}
	/**@return le mosse possibili */
	public List<Mossa> getMosse(){
		if (lm==null){
			if (s.nPedineInCampo(g)<3 && s.nPedineGiocate(g)==9)
				lm=Collections.emptyList();
			Mossa.Factory f=new Mossa.Factory(s);
			lm=m?f.getMangiate(g):f.getMosse(g);
		}
		return lm;
	}
	/**@param g - il giocatore di cui si vuole indagare la vittoria
	 * @return
	 * +1 se si ha vinto<br>
	 * 0 se il gioco non è finito<br>
	 * -1 se si ha perso
	 */
	public byte vittoria(byte g){
		if (m)
			return 0;
		byte a=Casella.altroColore(g);
		if (s.nPedineInCampo(g)<3 && s.nPedineGiocate(g)==9)
			return -1;
		if (s.nPedineInCampo(a)<3 && s.nPedineGiocate(a)==9)
			return 1;
		if (getMosse().size()==0)
			return (byte)(this.g==g?-1:1);
		return 0;
	}
	/*public Object clone(){
		try {
			StatoRicky s=(StatoRicky)super.clone();
			s.s=(Scacchiera)this.s.clone();
			return s;
		} catch (CloneNotSupportedException e){
			throw new Error("Copia dello stato non riuscita");
		}
	}*/
}
