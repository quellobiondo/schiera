package it.unibo.ai.didattica.mulino.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import it.unibo.ai.didattica.mulino.actions.*;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;

public class MulinoHumanClient extends MulinoClient {

	public MulinoHumanClient(Checker player) throws UnknownHostException, IOException {
		super(player);
		// TODO Auto-generated constructor stub
	}
	
	private static Class<?> fase(State s){
		switch (s.getCurrentPhase()){
		case FIRST: return Phase1.class;
		case SECOND: return Phase2.class;
		case FINAL: return PhaseFinal.class;
		}
		return null;
	}
	private static Action chiediMossa(State cs, BufferedReader in) throws IOException{
		Action a=null;
		while (true){
			System.out.println("do your move: ");
			String actionString = in.readLine();
			a = stringToAction(actionString, cs.getCurrentPhase());
			try{
				fase(cs).getMethod("applyMove",State.class,Action.class,State.Checker.class).invoke(null,cs,a,Checker.WHITE);
				break;
			} catch (Exception e){
				e.printStackTrace();
				System.out.println("mossa scritta male");
			}
		}
		return a;
	}
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		State.Checker player;

		if (args.length == 0) {
			System.out.println("You must specify which player you are (Wthie or Black)!");
			System.exit(-1);
		}
		System.out.println("Selected client: " + args[0]);

		if ("White".equals(args[0]))
			player = State.Checker.WHITE;
		else
			player = State.Checker.BLACK;
		Action action;
		State currentState = null;

		if (player == State.Checker.WHITE) {
			MulinoClient client = new MulinoHumanClient(State.Checker.WHITE);
			System.out.println("You are player " + client.getPlayer().toString() + "!");
			System.out.println("Current model:");
			currentState = client.read();
			System.out.println(currentState.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				action=chiediMossa(currentState,in);
				client.write(action);
				currentState = client.read();
				System.out.println("Effect of your move: ");
				System.out.println(currentState.toString());
				System.out.println("Waiting for your opponent move... ");
				currentState = client.read();
				System.out.println("Your Opponent did his move, and the result is: ");
				System.out.println(currentState.toString());
			}
		} else {
			MulinoClient client = new MulinoHumanClient(State.Checker.BLACK);
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			currentState = client.read();
			System.out.println("You are player " + client.getPlayer().toString() + "!");
			System.out.println("Current model:");
			System.out.println(currentState.toString());
			while (true) {
				System.out.println("Waiting for your opponent move...");
				currentState = client.read();
				System.out.println("Your Opponent did his move, and the result is: ");
				System.out.println(currentState.toString());
				action=chiediMossa(currentState,in);
//				System.out.println("Player " + client.getPlayer().toString() + ", do your move: ");
//				actionString = in.readLine();
//				action = stringToAction(actionString, currentState.getCurrentPhase());
				client.write(action);
				currentState = client.read();
				System.out.println("Effect of your move: ");
				System.out.println(currentState.toString());
			}
		}

	}

	/**
	 * Converte una stringa testuale in un oggetto azione
	 * 
	 * @param actionString
	 *            La stringa testuale che esprime l'azione desiderata
	 * @param fase
	 *            La fase di gioco attuale
	 * @return L'oggetto azione da comunicare al server
	 */
	private static Action stringToAction(String actionString, Phase fase) {
		if (fase == Phase.FIRST) { // prima fase
			Phase1Action action;
			action = new Phase1Action();
			action.setPutPosition(actionString.substring(0, 2));
			if (actionString.length() == 4)
				action.setRemoveOpponentChecker(actionString.substring(2, 4));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else if (fase == Phase.SECOND) { // seconda fase
			Phase2Action action;
			action = new Phase2Action();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else { // ultima fase
			PhaseFinalAction action;
			action = new PhaseFinalAction();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		}
	}

}
