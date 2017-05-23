package it.jar.mulino.model;

public class Mangiata extends Mossa {
	private Casella c;
	public Mangiata(Scacchiera s, Casella c){
		super(s);
		this.c=c;
	}
	public void muovi(Scacchiera s){
		s.togli(c.i,c.j);
	}
	public Casella partenza(){
		return null;
	}
	public Casella destinazione(){
		return c;
	}
	public boolean tris(){
		return false;
	}
	public String toString(){
		return "mangiata "+c;
	}
}
