package it.jar.mulino;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.client.MulinoClient;
import it.unibo.ai.didattica.mulino.client.MulinoHumanClient;
import it.unibo.ai.didattica.mulino.domain.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

/**
 * Created by ziro on 26/04/17.
 */
public class GameManager extends MulinoClient {

    private ActionChooser actionChooser;

    public GameManager(State.Checker player, ActionChooser actionChooser) throws UnknownHostException, IOException {
        super(player);
        this.actionChooser = actionChooser;
    }

    private void doWork() throws IOException, ClassNotFoundException {
        System.out.println("You are player " + getPlayer().toString() + "!");
        System.out.println("Current state:");
        State currentState = read();
        System.out.println(currentState.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Player " + getPlayer().toString() + ", do your move: ");
            Action action = actionChooser.chooseNextAction(currentState);
            write(action);
            currentState = read();
            System.out.println("Effect of your move: ");
            System.out.println(currentState.toString());
            System.out.println("Waiting for your opponent move... ");
            currentState = read();
            System.out.println("Your Opponent did his move, and the result is: ");
            System.out.println(currentState.toString());
        }
    }
}
