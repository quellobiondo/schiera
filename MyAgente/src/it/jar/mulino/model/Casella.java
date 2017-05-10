package it.jar.mulino.model;

public class Casella {
	public static final byte BIANCA=0,NERA=1,VUOTA=-1;
	static final byte UNDEF=-2;
	public final byte i,j,stato;
	Casella(int i, int j, byte stato){
		this.i=(byte)i;
		this.j=(byte)j;
		this.stato=stato;
	}
	Casella(int i, int j){
		this(i,j,UNDEF);
	}
	public boolean vuota(){
		return stato==VUOTA;
	}
	public static byte altroColore(byte g){
		assert g==BIANCA || g==NERA;
		return (byte)((g+1)%2);
	}
}
