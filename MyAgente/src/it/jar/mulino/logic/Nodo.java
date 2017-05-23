package it.jar.mulino.logic;

import java.util.*;
import it.jar.mulino.model.*;

public class Nodo implements Iterable<Nodo> {
	final Stato s;
	int upperbound,lowerbound;
	Nodo figlio,fratello;
	public Nodo(Stato s){
		this.s=s;
	}
	/*public StatoRicky getStato(){
		return s;
	}*/
	public Nodo firstchild(){
		return figlio;
	}
	public Nodo nextbrother(){
		return fratello;
	}
	@Override
	public Iterator<Nodo> iterator(){
		return new Iterator<Nodo>(){
			private Nodo n=figlio;
			public Nodo next(){
				Nodo n2=n;
				n=n.fratello;
				return n2;
			}
			public boolean hasNext(){
				return n!=null;
			}
		};
	}
}
