package it.jar.mulino;

import java.io.*;
import java.util.*;
import it.jar.mulino.logic.*;
import it.jar.mulino.model.*;
import it.jar.mulino.utils.*;
import it.unibo.ai.didattica.mulino.domain.*;
import org.apache.commons.cli.*;
import org.slf4j.*;

/**
 * Created by ziro on 26/04/17.
 */
public class Main {
	private static GameManagerBuilder builder=new GameManagerBuilder();
	private static final Logger logger=LoggerFactory.getLogger(Main.class);
	
	private static void abort(String message){
		System.out.println(message);
		System.exit(-1);
	}
	private static void parseArgs(String[] args){
		logger.debug("Parsing degli argomenti args="+Arrays.toString(args));
		// create the Options
		Options options=new Options();
		OptionGroup colore=new OptionGroup();
		colore.addOption(new Option("w","White",false,"Start as white player"));
		colore.addOption(new Option("b","Black",false,"Start as black player"));
		OptionGroup giocatore=new OptionGroup();
		giocatore.addOption(new Option("ai","The player is an AI [Default]"));
		giocatore.addOption(new Option("h","Human",false,"The player is human"));
		options.addOption("t", "time", true, "Durata turno");
		options.addOptionGroup(colore);
		options.addOptionGroup(giocatore);
        try {
			// parse the command line arguments
			CommandLine line=new DefaultParser().parse(options,args);
			logger.debug(line.toString());
			
			/**impostazione del colore del giocatore*/
			boolean whiteFlag=line.hasOption("w")||line.hasOption("White")||(args.length>0&&args[0].equals("White"));
			boolean blackFlag=line.hasOption("b")||line.hasOption("Black")||(args.length>0&&args[0].equals("Black"));
			if(!whiteFlag && !blackFlag) abort("Usage error: indicare se bianco (w) o nero (b)");
			if(whiteFlag && blackFlag)   abort("Usage error: il giocatore non può essere bianco e nero");
			if(whiteFlag)                builder.setChecker(State.Checker.WHITE);
			if(blackFlag)                builder.setChecker(State.Checker.BLACK);

			int durataTurno = 60; //60secondi
			if(line.hasOption("t")){
				try {
					durataTurno = Integer.parseInt(line.getOptionValue("t"));
				}catch (NumberFormatException ex){
					logger.error("Formato tempo errato", ex);
				}
			}

			/**Imposto il giocatore*/
			boolean giocatoreUmano = line.hasOption("h") || line.hasOption("Human");
<<<<<<< HEAD
			if(giocatoreUmano)  builder.setGiocatore(new GiocatoreUmano());
			else                builder.setGiocatore(GiocatoreAI.create(new Stato(), whiteFlag, durataTurno));
=======
			if(giocatoreUmano)  builder.setGiocatore(new GiocatoreUmano(new Stato()));
			else                builder.setGiocatore(GiocatoreAI.create(new Stato(), whiteFlag));
>>>>>>> 47f72a29bfb73adeef29b29429bc40686a6f2813
			
			logger.debug(builder.toString());
		} catch (ParseException exp){
			abort("Usage error: "+exp.getLocalizedMessage());
		}
	}
	private static void startGame() throws IOException{
		logger.debug("Inizia il gioco");
		builder.build().loopGioco(); // creo il manager della partita e avvio il gioco
	}
	public static void main(String[] args) throws IOException{
		logger.debug("Inizializzazione");
		parseArgs(args); // inizializzo leggendo gli argomenti
		startGame(); // avvio il gioco
	}
}
