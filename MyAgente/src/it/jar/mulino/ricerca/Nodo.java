package it.jar.mulino.ricerca;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public abstract class Nodo implements Iterable<Nodo> {
	public final static Nodo FINE=new Nodo.Max(null,null);
	private final Nodo p; // padre
	private final StatoRicky s;
	private List<Nodo> f;
	private byte v;
	private Nodo m;
	private boolean vp;	//valore gi� calcolato
	public Nodo(Nodo p, StatoRicky s){
		this.p=p;
		this.s=s;
	}
	public Nodo getPadre(){
		return p;
	}
	public StatoRicky getStato(){
		return s;
	}
	public void espandi(Function<Nodo,Stream<Nodo>> e){
		f=e.apply(this).collect(Collectors.toList());
	}
	public List<Nodo> getFigli(){
		if (f==null)
			throw new IllegalStateException("Il nodo non � ancora espanso");
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
	public abstract Nodo merge();
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
		public Max(Nodo p, StatoRicky s){
			super(p,s);
		}
		public Nodo merge(){
			return getFigli().stream().max(Comparator.comparing(Nodo::getValore)).get();
		}
	}
	public static class Min extends Nodo {
		public Min(Nodo p, StatoRicky s){
			super(p,s);
		}
		public Nodo merge(){
			return getFigli().stream().min(Comparator.comparing(Nodo::getValore)).get();
		}
	}
	public static class Foglia extends Nodo {
		private byte v;
		public Foglia(Nodo p, byte v){
			super(p,null);
			this.v=v;
		}
		public Nodo merge(){
			return this;
		}
		public byte getValore(){
			return v;
		}
	}
}
