package it.jar.mulino.model;

import java.io.*;
import java.util.*;
import java.util.stream.*;
import aima.core.agent.*;

public abstract class Mossa implements Action {
	protected final Scacchiera s;
	protected Mossa(Scacchiera s){
		this.s=s;
	}
	protected abstract void muovi(Scacchiera s);
	/**per consentire di eseguire la mossa su una copia della scacchiera*/
	public void apply(Scacchiera s){
		if (s.equals(this.s)){
			String s1=s.toString();
			muovi(s);
			String s2=s.toString();
			System.out.println(unifica(s1,s2));
		} else
			throw new IllegalArgumentException("Questa mossa non è stata pensata con le pedine in queste posizioni");
	}
	public void apply(){
		apply(s);
	}
	/**Se si restituisce {@code null}, bisogna fare l'override di {@code giocatoreDiTurno} */
	protected abstract Casella partenza();
	protected abstract Casella destinazione();
	/**Da reimplementare se la mossa non ha una casella di partenza*/
	protected byte giocatoreDiTurno(){
		Casella c=partenza();
		if (c!=null)
			return c.stato;
		else throw new IllegalStateException("Devi fare l'override di questo metodo!!");
	}
	public boolean tris(){
		Casella cp=partenza(),cd=destinazione();
		byte g;
		if (cp!=null){
			s.campo[cp.i][cp.j]=Casella.VUOTA;
			g=cp.stato;
		} else
			g=giocatoreDiTurno();
		boolean r;
		if (cd.i%2==0)
			r=(s.get((cd.i+1)%2,cd.j)==g && s.get((cd.i+2)%2,cd.j)==g)||(s.get((cd.i+6)%2,cd.j)==g && s.get((cd.i+7)%2,cd.j)==g);
		else
			r=(s.get((cd.i+1)%2,cd.j)==g && s.get((cd.i+7)%2,cd.j)==g)||(s.get(cd.i,(cd.j+1)%2)==g && s.get(cd.i,(cd.j+1)%2)==g);
		if (cp!=null)
			s.campo[cp.i][cp.j]=cp.stato;
		return r;
	}
	public String toString(){
		Scacchiera s2=(Scacchiera)s.clone();
		muovi(s2);
		StringBuilder sb=new StringBuilder(unifica(s.toString(),s2.toString()).trim());
		/*if (partenza()!=null){
			sb.append("Da ");
			sb.append(partenza());
			sb.append(" a ");
		}
		sb.append(destinazione());*/
		if (tris())
			sb.append(" , tris!");
		return sb.toString();
	}
	private String unifica(String s1, String s2){
		BufferedReader r1=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s1.getBytes())));
		BufferedReader r2=new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s2.getBytes())));
		StringBuffer s=new StringBuffer();
		String l;
		try {
			while ((l=r1.readLine())!=null){
				s.append(l);
				s.append('\t');
				s.append(r2.readLine());
				s.append(System.lineSeparator());
			}
		} catch (IOException e) {}
		return s.toString();
	}
	public boolean isNoOp(){
		return false;
	}
	public static class Factory{
		private Scacchiera s;
		public Factory(Scacchiera s){
			this.s=s;
		}
		public List<Mossa> getMangiate(byte g){
			return StreamSupport.stream(s.spliterator(),true).filter(c->c.stato==(g+1)%2).map(c->new Mangiata(s,c)).collect(Collectors.toList());
		}
		public List<Mossa> getMosse(byte g){
			ArrayList<Mossa> l=new ArrayList<>();
			if (s.nPedineGiocate(g)<9){
				for (Casella c : s)
					if (c.vuota())
						l.add(new Mossa1(s,g,c));
			} else if (s.nPedineInCampo(g)>2){
				for (Casella c : s)
					if (c.stato==g)
						for (Direzione d : Direzione.values())
							if (d.applicabile(s,c)&& d.getDestinazione(s,c).vuota())
								l.add(new Mossa2(s,c,d));
			} else {
				List<Casella> lv=StreamSupport.stream(s.spliterator(),true).filter(Casella::vuota).collect(Collectors.toList());
				for (Casella c : s)
					if (c.stato==g)
						for (Casella cd : lv)
							l.add(new Mossa3(s,c,cd));
			}
			return l;
		}
	}
}
