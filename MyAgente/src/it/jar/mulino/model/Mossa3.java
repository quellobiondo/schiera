package it.jar.mulino.model;

public class Mossa3 extends Mossa {
	private Casella cp,cd;
	public Mossa3(Scacchiera s, Casella p, Casella d){
		super(s);
		cp=p;
		cd=d;
	}
	protected void muovi(Scacchiera s){
		s.muovi(cp.i,cp.j,cd.i,cd.j);
	}
	protected Casella partenza(){
		return cp;
	}
	protected Casella destinazione(){
		return cd;
	}
}
