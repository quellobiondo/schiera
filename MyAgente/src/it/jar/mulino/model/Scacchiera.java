package it.jar.mulino.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Scacchiera implements Cloneable, Iterable<Casella> {
	byte[][] campo=new byte[3][8];
	private byte[] nc=new byte[2],ng=new byte[2];
	public Scacchiera(){
		for (byte i=0;i<3;i++)
			for (byte j=0;j<8;j++)
				campo[i][j]=VUOTA;
	}
	private Scacchiera(Scacchiera s){
		for (byte i=0;i<3;i++)
			campo[i]=Arrays.copyOf(s.campo[i],8);
		nc=Arrays.copyOf(s.nc,2);
		ng=Arrays.copyOf(s.ng,2);
	}
	public byte get(int i, int j){
		return campo[i][j];
	}
	public Casella getCasella(int i, int j){
		return new Casella(i,j,campo[i][j]);
	}
	void aggiungi(int i, int j, byte colore){
		assert (colore==BIANCA || colore==NERA)&& ng[colore]<9;
		campo[i][j]=colore;
		nc[colore]++;
		ng[colore]++;
		assert ng[0]>=ng[1];
	}
	void togli(int i, int j){
		assert campo[i][j]!=VUOTA;
		nc[campo[i][j]]--;
		campo[i][j]=VUOTA;
	}
	void muovi(int i, int j, Direzione d){
		assert d.getDestinazione(this,getCasella(i,j)).vuota() && campo[i][j]!=VUOTA && ng[campo[i][j]]==9;
		Casella c=d.getDestinazione(this,getCasella(i,j));
		muovi(i,j,c.i,c.j);
	}
	void muovi(int i, int j, int i2, int j2){
		assert campo[i2][j2]==VUOTA && campo[i][j]!=VUOTA && ng[campo[i][j]]==9;
		campo[i2][j2]=campo[i][j];
		campo[i][j]=VUOTA;
	}
	public byte nPedineGiocate(byte g){
		assert g==BIANCA || g==NERA;
		return ng[g];
	}
	public byte nPedineInCampo(byte g){
		assert g==BIANCA || g==NERA;
		return nc[g];
	}
	public Object clone(){
		return new Scacchiera(this);
	}
	public Iterator<Casella> iterator(){
		return new Iterator<Casella>(){
			private int i,j;
			public Casella next(){
				if (j<8)
					return new Casella(i,j,campo[i][j++]);
				else if (i<3){
					j=0;
					return new Casella(++i,j,campo[i][j]);
				} else
					throw new NoSuchElementException();
			}
			public boolean hasNext(){
				return !(i==2 && j==8);
			}
		};
	}
	public boolean equals(Object o){
		return (o instanceof Scacchiera) ? Arrays.deepEquals(campo,((Scacchiera)o).campo) : false;
	}
	public String toString(){
		String n=System.lineSeparator();
		return String.format(
					"%s--%s--%s"+n
				+	"|%s-%s-%s|"+n
				+	"||%s%s%s||"+n
				+ 	"%s%s%s %s%s%s"+n
				+	"||%s%s%s||"+n
				+	"|%s-%s-%s|"+n
				+	"%s--%s--%s"+n+n,
				c(0,0),c(0,1),c(0,2),
				c(1,0),c(1,1),c(1,2),
				c(2,0),c(2,1),c(2,2),
				c(0,7),c(1,7),c(2,7),c(2,3),c(1,3),c(0,3),
				c(2,6),c(2,5),c(2,4),
				c(1,6),c(1,5),c(1,4),
				c(0,6),c(0,5),c(0,4));
	}
	private char c(int i, int j){
		switch (campo[i][j]){
		case BIANCA: return 'O';
		case NERA: return 'X';
		default: return '*';
		}
	}

}
