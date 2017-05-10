package it.jar.mulino.ricerca;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class Nodo implements Iterable<Nodo> {
	protected final static Nodo FINE=new Nodo.Max(null,null);
	private final Nodo p; // padre
	private final Stato s;
	private List<Nodo> f;
	private byte v;
	private Nodo m;
	private boolean vp;	//valore già calcolato
	public Nodo(Nodo p, Stato s){
		this.p=p;
		this.s=s;
	}
	public Nodo getPadre(){
		return p;
	}
	public Stato getStato(){
		return s;
	}
	public void espandi(Function<Nodo,Stream<Nodo>> e){
		f=e.apply(this).collect(Collectors.toList());
	}
	public List<Nodo> getFigli(){
		if (f==null)
			throw new IllegalStateException("Il nodo non è ancora espanso");
		return f;
	}
	public byte getValore(){
		tryMerge();
		return v;
	}
	public Nodo getMigliore(){
		tryMerge();
		return m;
	}
	public void foglia(int v){
		this.v=(byte)v;
		f=Collections.emptyList();
		vp=true;
	}
	protected abstract Nodo merge();
	public boolean tryMerge(){
		if (!vp){
			Nodo n=merge();
			if (n!=null){
				m=n;
				v=n.getValore();
				vp=true;
			}
		}
		return vp;
	}
	public Iterator<Nodo> iterator(){
		return getFigli().iterator();
	}
	public static class Max extends Nodo {
		public Max(Nodo p, Stato s){
			super(p,s);
		}
		protected Nodo merge(){
			return getFigli().stream().max(Comparator.comparing(Nodo::getValore)).get();
		}
	}
	public static class Min extends Nodo {
		public Min(Nodo p, Stato s){
			super(p,s);
		}
		protected Nodo merge(){
			return getFigli().stream().min(Comparator.comparing(Nodo::getValore)).get();
		}
	}
	public static class Foglia extends Nodo {
		private byte v;
		public Foglia(Nodo p, byte v){
			super(p,null);
			this.v=v;
		}
		protected Nodo merge(){
			return this;
		}
		public byte getValore(){
			return v;
		}
	}
}
