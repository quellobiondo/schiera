package it.jar.mulino;

import it.unibo.ai.didattica.mulino.domain.State;

import java.io.IOException;

/**
 * Created by ziro on 26/04/17.
 */
public class Main {
    public static void main (String [] args) throws IOException {
        //TODO parse arguments to chose if black or white
        new GameManager(State.Checker.WHITE, new RandomActionChooser());
    }
}
