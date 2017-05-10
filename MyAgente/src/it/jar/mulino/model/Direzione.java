package it.jar.mulino.model;

import java.util.function.*;

public enum Direzione {
	DENTRO((s,c)->s.getCasella((c.i+1)%3,c.j),(s,c)->c.j%2!=0),
	FUORI((s,c)->s.getCasella((c.i+2)%3,c.j),(s,c)->c.j%2!=0),
	DESTRA((s,c)->s.getCasella(c.i,(c.j+1)%8),(s,c)->true),
	SINISTRA((s,c)->s.getCasella(c.i,(c.j+7)%8),(s,c)->true);
	private BiFunction<Scacchiera,Casella,Casella> d;
	private BiPredicate<Scacchiera,Casella> a;
	private Direzione(BiFunction<Scacchiera,Casella,Casella> destinazione, BiPredicate<Scacchiera,Casella> applicabile){
		d=destinazione;
		a=applicabile;
	}
	public Casella getDestinazione(Scacchiera s, Casella c){
		return d.apply(s,c);
	}
	public boolean applicabile(Scacchiera s, Casella c){
		return a.test(s,c);
	}
/*	void muovi(byte[][] campo, int i, int j){
		muovi.apply(campo,i,j);
	}
	boolean libera(byte[][] campo, int i, int j){
		return possibile.test(campo,i,j);
	}*/
}
