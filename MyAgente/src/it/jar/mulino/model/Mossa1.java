package it.jar.mulino.model;

public class Mossa1 extends Mossa {
	private Casella c;
	private byte g;
	public Mossa1(Scacchiera s, byte g, Casella c){
		super(s);
		assert g==Casella.BIANCA || g==Casella.NERA;
		this.c=c;
		this.g=g;
	}
	protected byte giocatoreDiTurno(){
		return g;
	}
	protected void muovi(Scacchiera s){
		s.aggiungi(c.i,c.j,g);
	}
	protected Casella partenza(){
		return null;
	}
	protected Casella destinazione(){
		return c;
	}
}
