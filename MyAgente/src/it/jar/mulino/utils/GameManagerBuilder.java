package it.jar.mulino.utils;

import it.jar.mulino.GameManager;
import it.jar.mulino.logic.Giocatore;
import it.jar.mulino.logic.GiocatoreAI;
import it.unibo.ai.didattica.mulino.domain.State;

import java.io.IOException;

/**
 * Created by ziro on 22/05/17.
 */
public class GameManagerBuilder {

    private State.Checker checker = State.Checker.WHITE;
    private long MaxTime = 60*1000; //60s
    private long MaxMemSize = 2*1024*1024; //2 GB
    private Giocatore giocatore;

    public GameManagerBuilder setChecker(State.Checker checker){
        this.checker = checker;
        return this;
    }

    public GameManagerBuilder setGiocatore(Giocatore giocatore){
        this.giocatore = giocatore;
        return this;
    }

    public GameManagerBuilder setMaxTime(long time){
        this.MaxTime = time;
        return this;
    }

    public GameManager build() throws IOException {
        return new GameManager(checker, giocatore);
    }

    @Override
    public String toString() {
        return "GameManagerBuilder{" +
                "checker=" + checker +
                ", MaxTime=" + MaxTime +
                ", MaxMemSize=" + MaxMemSize +
                ", giocatore=" + giocatore +
                '}';
    }
}
