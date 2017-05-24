package it.jar.mulino.logic;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import it.jar.mulino.model.*;
import it.jar.mulino.ricerca.*;
import org.slf4j.*;

/**
 * Created by ziro on 22/05/17.
 */
public class GiocatoreAI extends Giocatore implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(GiocatoreAI.class);

    private Mossa mossaKiller;
    //private Stato stato;
    private NineMensMorrisSearch ricerca;
    
    private final Lock lock = new ReentrantLock();
    private final Condition condizioneRisposta = lock.newCondition();
    private final int tempoTurno;
    private final byte colore;	//0=bianco,1=nero
    private boolean statoCambiato;

    private GiocatoreAI(Stato stato, boolean isBianco, int secondiTurno) {
        super(stato);
        colore=(byte)(isBianco?0:1);
        //statoAttuale = stato;
        this.tempoTurno = secondiTurno;
        //statoAttuale.currentPlayer = isBianco ? stato.currentPlayer : stato.opponentPlayer;
        //ricerca = new NineMensMorrisSearch(Minimax.Algorithm.NEGASCOUT, stato);
        ricerca = new NineMensMorrisSearch(Minimax.Algorithm.MINIMAX, stato);
    }

    /**crea uno pseudo-attore attivo*/
    public static GiocatoreAI create(Stato stato, boolean isBianco, int durataTurno){
        GiocatoreAI giocatore = new GiocatoreAI(stato, isBianco, durataTurno-2);
        Thread thread = new Thread(giocatore);
        thread.setDaemon(true);
        //thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
        return giocatore;
    }
	@Override
	public Mossa getMossa(){
		/*logger.debug("Dammi la tua mossa entro "+tempoTurno+" s");
		long t=System.currentTimeMillis();
		try {
			if (lock.tryLock(tempoTurno,TimeUnit.SECONDS)){
				condizioneRisposta.await(tempoTurno*1000-(System.currentTimeMillis()-t),TimeUnit.MILLISECONDS);
				if (!validaMossa(mossaKiller)){
					logger.error("mossa non valida");
				}
				lock.unlock();
			} else
				logger.error("timeout dopo "+(System.currentTimeMillis()-t/1000.0)+" s");
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }*/
		try{
			Thread.sleep(tempoTurno*1000);
		} catch (InterruptedException e){}
		checkMossaKiller(stato);
		logger.debug("La mia mossa killer e' "+mossaKiller);
		return mossaKiller;
    }

    @Override
    public void aggiornaStato(Stato stato){
    	if (stato.currentPlayer!=colore){
    		logger.debug("Lo stato ha come giocatore corrente l'avversario, quindi non verrà aggiornato");
    		return;
    	}
    	logger.debug("Aggiorna lo stato");
        this.stato = stato;
        checkMossaKiller(stato);
        statoCambiato=true;
    }

    private void checkMossaKiller(Stato stato){
        if(mossaKiller==null || !stato.getPossibleMoves().contains(mossaKiller)){
            logger.debug("Mossa killer "+mossaKiller+" non idonea con il nuovo stato");
            mossaKiller = stato.getPossibleMoves().get(0); //patch
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

            mossaKiller = ricerca.getBestMove(depth); //setta la mossa migliore
            logger.debug("... mossa migliore: "+mossaKiller+" stato cambiato? "+statoCambiato);

            lock.lock();
            if(!statoCambiato && stato.willWin(mossaKiller)){
                logger.debug("Con questa mossa si vince!");
                //se vinco con quella mossa e lo stato non è cambiato allora facciamola!
                condizioneRisposta.signal();
            }
            lock.unlock();
            //pota l'albero se lo stato attuale della partita è cambiato
            if(statoCambiato){
                logger.debug("Lo stato è cambiato");
                checkMossaKiller(stato);
                ricerca.statoAttualeAggiornato(stato);
                statoCambiato = false;
                depth = 7; //ricominciamo ad esplorare a profondità limitata
            }
        }
    }
}
