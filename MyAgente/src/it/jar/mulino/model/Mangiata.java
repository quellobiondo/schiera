package it.jar.mulino.model;

public class Mangiata extends Mossa {
	private Casella c;
	public Mangiata(Scacchiera s, Casella c){
		super(s);
		this.c=c;
	}
	protected void muovi(Scacchiera s){
		s.togli(c.i,c.j);
	}
	protected Casella partenza(){
		return null;
	}
	protected Casella destinazione(){
		return c;
	}
	public boolean tris(){
		return false;
	}
	public String toString(){
		return "mangiata "+c;
	}
}
