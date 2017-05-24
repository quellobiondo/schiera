package it.jar.mulino.model;

import static it.jar.mulino.utils.PositionConverter.byte2string;

/**
 * Mossa generica
 * Created by ziro on 22/05/17.
 */
public class Mossa {

    private byte player;

    private byte to;
    private byte from;
    private byte remove;

    public Mossa(byte player, byte from, byte to, byte remove) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.remove = remove;
    }



    public Mossa(byte player, byte from, byte to) {
        this(player, from, to, Byte.MAX_VALUE);
    }

    public Mossa(byte player, byte to) {
        this(player, Byte.MAX_VALUE, to, Byte.MAX_VALUE);
    }

    public byte getTo() {
        return to;
    }

    public byte getRemove() {
        return remove;
    }

    public byte getFrom() {
        return from;
    }

    public boolean isPutMove() {
        return this.from == Byte.MAX_VALUE;
    }

    public boolean isRemoveMove() {
        return this.remove != Byte.MAX_VALUE;
    }

    public void setPlayer(byte player){
        this.player = player;
    }

    public Mossa inverse() {
        if (isPutMove() || isRemoveMove()) {
            return null;
        }

        return new Mossa(this.player, this.to, this.from);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Mossa that = (Mossa) o;

        if (player != that.player) return false;
        if (to != that.to) return false;
        if (from != that.from) return false;
        return remove == that.remove;
    }

    @Override
    public int hashCode() {
        /*int result = player;
        result = 31 * result + to;
        result = 31 * result + from;
        result = 31 * result + remove;
        return result;*/
        return player<<21 | to<<14 | from<<7 | remove;
    }

    @Override
    public String toString() {
        return this.toStringMove();
    }

    public String toStringMove() {
        return byte2string(this.from) + byte2string(this.to) + byte2string(this.remove);
    }

}
