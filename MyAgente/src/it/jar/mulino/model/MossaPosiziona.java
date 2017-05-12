package it.jar.mulino.model;

/**
 * Created by ziro on 09/05/17.
 */
public class MossaPosiziona {

    private byte posizione;
    private byte rimuovi = -1;



    public MossaPosiziona(byte posizione) {
        this(posizione, (byte)-1);
    }

    public MossaPosiziona(byte posizione, byte rimuovi){
        this.posizione = posizione;
        this.rimuovi = rimuovi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MossaPosiziona)) return false;

        MossaPosiziona that = (MossaPosiziona) o;

        if (getPosizione() != that.getPosizione()) return false;
        return rimuovi == that.rimuovi;
    }

    @Override
    public int hashCode() {
        int result = (int) getPosizione();
        result = 31 * result + (int) rimuovi;
        return result;
    }

    public byte getPosizione() {
        return posizione;
    }

    public void setPosizione(byte posizione) {
        this.posizione = posizione;
    }

    public byte getRimuovi() {
        return rimuovi;
    }

    public void setRimuovi(byte rimuovi) {
        this.rimuovi = rimuovi;
    }

    @Override
    public String toString() {
        return "MossaPosiziona{" +
                "posizione=" + posizione +
                ", rimuovi=" + rimuovi +
                '}';
    }

}
