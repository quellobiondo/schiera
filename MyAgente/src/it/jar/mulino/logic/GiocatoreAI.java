package it.jar.mulino.logic;

import java.util.*;
//import java.util.concurrent.*;
//import java.util.concurrent.locks.*;
import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.*;
import it.jar.mulino.utils.*;
import org.slf4j.*;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(GiocatoreAI.class);

    private Mossa mossaKiller;
    private NineMensMorrisSearch ricerca;
    
//    private final Lock lock = new ReentrantLock();
//    private final Condition condizioneRisposta = lock.newCondition();
    private final int tempoTurno;
    private final byte colore;	//0=bianco,1=nero
    private boolean statoCambiato;
    private Thread thread;

    private GiocatoreAI(Stato stato, boolean isBianco, int secondiTurno) {
        super(stato);
        colore=(isBianco? NineMensMorrisSetting.PLAYER_W : NineMensMorrisSetting.PLAYER_B);
        //statoAttuale = stato;
        this.tempoTurno = secondiTurno;
        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.BNS, stato);
    }

    /**crea uno pseudo-attore attivo*/
    public static GiocatoreAI create(Stato stato, boolean isBianco, int durataTurno){
        return new GiocatoreAI(stato, isBianco, durataTurno-2);
    }

	@Override
	public Mossa getMossa(){
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("Ai-R");
        //thread.setPriority(Thread.MAX_PRIORITY);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        thread.start();

		logger.debug("Dammi la tua mossa entro "+tempoTurno+" s");
		try{
			Thread.sleep(tempoTurno*1000);
		} catch (InterruptedException e){}

		if(thread.isAlive()){
		    thread.interrupt();
        }

		checkMossaKiller(stato);
		logger.debug("La mia mossa killer e' "+mossaKiller);
		return mossaKiller;
    }

    @Override
    public void aggiornaStato(Stato stato){
    	if (stato.currentPlayer!=colore){
    		logger.error("Lo stato ha come giocatore corrente l'avversario, quindi non verrà aggiornato");
    		System.err.println("Lo stato ha come giocatore corrente l'avversario, quindi non verrà aggiornato");
    		return;
    	}
    	logger.debug("Aggiorna lo stato");
        this.stato = stato;
        //checkMossaKiller(stato);
        statoCambiato=true;
    }

    private void checkMossaKiller(Stato stato){
    	if (stato.currentPlayer!=colore){
    		logger.error("Ho lo stato dell'altro giocatore! Mossa non controllata");
    		System.err.println("Ho lo stato dell'altro giocatore! Mossa non controllata");
    		return;
    	}
        List<Mossa> mossePossibili = stato.getPossibleMoves();
        logger.debug("Mosse "+mossePossibili+" --> "+mossePossibili.contains(mossaKiller));
        if(mossaKiller==null || !stato.getPossibleMoves().contains(mossaKiller)){
            logger.error("Mossa killer "+mossaKiller+" non idonea con il nuovo stato");
            List<Mossa> mosse = stato.getPossibleMoves();
            mosse.sort((mossa1, mossa2) -> {
                Stato s1 = stato.copia();
                s1.makeMove(mossa1);
                int valueUno = ValutatoreStato.valutaStato(s1);
                s1.unmakeMove(mossa1);
                s1.makeMove(mossa2);
                int valueDue = ValutatoreStato.valutaStato(s1);
                return valueUno - valueDue;
            });
            //la migliore delle mosse possibili
            mossaKiller = mosse.get(0);//patch
            logger.debug("Nuova mossa killer: "+mossaKiller);
        }
    }

    @Override
    public void run(){
        int depth = 7;
        while(true){
            depth++;
            //esplora l'albero iterativamente per ottenere la mossa migliore
            logger.debug("Profondità "+depth+" ... ");
            long t=System.currentTimeMillis();

            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore
            logger.debug("... mossa migliore per profondità "+depth+": "+mossaKiller+" ("+(System.currentTimeMillis()-t)/1000.0+"s)\tstato cambiato? "+statoCambiato);

            //lock.lock();
            if(!statoCambiato && stato.willWin(mossaKiller)){
                logger.debug("Con questa mossa si vince!");
                //se vinco con quella mossa e lo stato non è cambiato allora facciamola!
                //condizioneRisposta.signal();
            }
           // lock.unlock();
            //pota l'albero se lo stato attuale della partita è cambiato
            if(statoCambiato){
                logger.debug("Lo stato è cambiato");
                //checkMossaKiller(stato);
                ricerca.statoAttualeAggiornato(stato);
                statoCambiato = false;
                depth = 7; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
