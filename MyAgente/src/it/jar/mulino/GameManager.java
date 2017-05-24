package it.jar.mulino;

import java.io.*;
import java.net.*;
import it.jar.mulino.logic.*;
import it.jar.mulino.model.*;
import static it.jar.mulino.utils.MossaConverter.convertMossa;
import static it.jar.mulino.utils.StatoConverter.convertState;

import it.unibo.ai.didattica.mulino.client.*;
import it.unibo.ai.didattica.mulino.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gestore del loop di gioco
 * Created by ziro on 26/04/17.
 */
public class GameManager extends MulinoClient{

    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private Giocatore giocatore;
    private Stato currentState;
    private State.Phase fase;

    public GameManager(State.Checker player, Giocatore giocatore) throws UnknownHostException, IOException {
        super(player);
        this.giocatore = giocatore;
        this.currentState = new Stato();
    }

    private Stato leggiStato() throws IOException, ClassNotFoundException {
        State statoEsterno = read();
        fase = statoEsterno.getCurrentPhase();
        Stato nuovo = convertState(statoEsterno);
        nuovo.currentPlayer = currentState.currentPlayer;
        nuovo.opponentPlayer = currentState.opponentPlayer;
        return nuovo;
    }

    private void scriviMossa(Mossa mossa) throws IOException, ClassNotFoundException {
        write(convertMossa(fase, mossa));
    }

    private void turnoAvversario() throws IOException, ClassNotFoundException {
        currentState = leggiStato();
        currentState.next(); //è cambiato il turno
        giocatore.aggiornaStato(currentState.copia());
    }

    private void turnoMio() throws IOException, ClassNotFoundException {
    	long t=System.currentTimeMillis();
    	Mossa m=giocatore.getMossa();
        logger.debug("invio mossa dopo "+(System.currentTimeMillis()-t)/1000.0+" s");
        scriviMossa(m);
        logger.debug((System.currentTimeMillis()-t)/1000.0+" s di turno");
        currentState = leggiStato(); //leggo l'effetto della mia mossa
        currentState.next();
        //giocatore.aggiornaStato(currentState.copia());
    }

    /**Loop di gioco
     * - Lettura dello stato della partita
     * - Scrittura della mossa
     * - Lettura del risultato della mossa
     */
    public void loopGioco(){
        try {
            logger.debug(String.format("You are player %s!", getPlayer().toString()));
            currentState = leggiStato();
            giocatore.aggiornaStato(currentState);
            logger.debug(String.format("Current model: %s", currentState));
            if(getPlayer()== State.Checker.BLACK){
                //allora deve aspettare prima la mossa dell'avversario
                turnoAvversario();
                logger.debug(String.format("Your Opponent did his move, and the result is:\n %s", currentState));
            }

            while (true) {
                logger.debug(String.format("Player %s, do your move: ", getPlayer().toString()));
                turnoMio();
                logger.debug(String.format("Effect of the move\n %s\n\n Waiting for your opponent move...", currentState));
                turnoAvversario();
                logger.debug(String.format("Your Opponent did his move, and the result is:\n %s", currentState));
            }
        }catch (IOException ex){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
