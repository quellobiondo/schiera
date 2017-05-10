package it.jar.mulino.model;

public class Mossa2 extends Mossa {
	private Casella c;
	private Direzione d;
	public Mossa2(Scacchiera s, Casella c, Direzione d){
		super(s);
		this.c=c;
		this.d=d;
	}
	protected void muovi(Scacchiera s){
		s.muovi(c.i,c.j,d);
	}
	protected Casella partenza(){
		return c;
	}
	protected Casella destinazione(){
		return d.getDestinazione(s,c);
	}
}
