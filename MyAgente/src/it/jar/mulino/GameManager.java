package it.jar.mulino;

import java.io.*;
import java.net.*;
import it.jar.mulino.logic.*;
import it.jar.mulino.model.*;
import static it.jar.mulino.utils.MossaConverter.convertMossa;
import static it.jar.mulino.utils.StatoConverter.convertState;
import it.unibo.ai.didattica.mulino.client.*;
import it.unibo.ai.didattica.mulino.domain.*;

/**
 * Gestore del loop di gioco
 * Created by ziro on 26/04/17.
 */
public class GameManager extends MulinoClient{

    private Giocatore giocatore;
    private Stato currentState;
    private State.Phase fase;

    public GameManager(State.Checker player, Giocatore giocatore) throws UnknownHostException, IOException {
        super(player);
        this.giocatore = giocatore;
    }

    private Stato leggiStato() throws IOException, ClassNotFoundException {
        State statoEsterno = read();
        fase = statoEsterno.getCurrentPhase();
        return convertState(statoEsterno);
    }

    private void scriviMossa(Mossa mossa) throws IOException, ClassNotFoundException {
        write(convertMossa(fase, mossa));
    }

    /**Loop di gioco
     * - Lettura dello stato della partita
     * - Scrittura della mossa
     * - Lettura del risultato della mossa
     */
    public void loopGioco(){
        try {
            System.out.println("You are player " + getPlayer().toString() + "!");
            System.out.println("Current model:");
            currentState = leggiStato();
            giocatore.aggiornaStato(currentState);
            System.out.println(currentState);
            while (true) {
                System.out.println("Player " + getPlayer().toString() + ", do your move: ");
                scriviMossa(giocatore.getMossa());
                currentState = leggiStato();
                giocatore.aggiornaStato(currentState);
                System.out.println("Effect of your move: ");
                System.out.println(currentState);
                System.out.println("Waiting for your opponent move... ");
                currentState = leggiStato();
                giocatore.aggiornaStato(currentState);
                System.out.println("Your Opponent did his move, and the result is: ");
                System.out.println(currentState);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
