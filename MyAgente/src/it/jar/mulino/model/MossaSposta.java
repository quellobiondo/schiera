package it.jar.mulino.model;

/**
 * Created by ziro on 09/05/17.
 */
public class MossaSposta extends Mossa  {

    private byte from, to, rimuovi;

    public MossaSposta(byte from, byte to){
        this(from, to, (byte)-1);
    }

    public MossaSposta(byte from, byte to, byte rimuovi) {
        this.from = from;
        this.to = to;
        this.rimuovi = rimuovi;
    }

    public byte getFrom() {
        return from;
    }

    public void setFrom(byte from) {
        this.from = from;
    }

    public byte getTo() {
        return to;
    }

    public void setTo(byte to) {
        this.to = to;
    }

    public byte getRimuovi() {
        return rimuovi;
    }

    public void setRimuovi(byte rimuovi) {
        this.rimuovi = rimuovi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MossaSposta)) return false;

        MossaSposta that = (MossaSposta) o;

        if (getFrom() != that.getFrom()) return false;
        if (getTo() != that.getTo()) return false;
        return rimuovi == that.rimuovi;
    }

    @Override
    public int hashCode() {
        int result = (int) getFrom();
        result = 31 * result + (int) getTo();
        result = 31 * result + (int) rimuovi;
        return result;
    }

    @Override
    public String toString() {
        return "MossaSposta{" +
                "from=" + from +
                ", to=" + to +
                ", rimuovi=" + rimuovi +
                '}';
    }
}
