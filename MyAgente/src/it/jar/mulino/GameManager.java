package it.jar.mulino;

import it.jar.mulino.model.MossaPosiziona;
import it.jar.mulino.model.MossaSposta;
import it.jar.mulino.model.Stato;
import it.jar.mulino.utils.MossaConverter;
import it.jar.mulino.utils.StatoConverter;
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

    private Stato leggiStato() throws IOException, ClassNotFoundException {
        return StatoConverter.convertExternalStatoToInternal(read());
    }

    private void scriviMossa(MossaSposta mossa) throws IOException, ClassNotFoundException {
        write(MossaConverter.convertMossaMuovi(mossa));
    }

    private void scriviMossa(MossaPosiziona mossa) throws IOException, ClassNotFoundException {
        write(MossaConverter.convertMossaPosiziona(mossa));
    }

    private void doWork() throws IOException, ClassNotFoundException {
        System.out.println("You are player " + getPlayer().toString() + "!");
        System.out.println("Current model:");
        Stato currentState = leggiStato();
        System.out.println(currentState.toString());
        while (true) {
            System.out.println("Player " + getPlayer().toString() + ", do your move: ");
            Action action = actionChooser.chooseNextAction(currentState);
            write(action);
            currentState = leggiStato();
            System.out.println("Effect of your move: ");
            System.out.println(currentState.toString());
            System.out.println("Waiting for your opponent move... ");
            currentState = leggiStato();
            System.out.println("Your Opponent did his move, and the result is: ");
            System.out.println(currentState.toString());
        }
    }
}
