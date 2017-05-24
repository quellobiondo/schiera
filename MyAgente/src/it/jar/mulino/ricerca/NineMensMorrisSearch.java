package it.jar.mulino.ricerca;

import java.util.*;
import it.jar.mulino.logic.*;
import it.jar.mulino.model.*;
import it.jar.mulino.utils.*;
import org.slf4j.*;

/**
 * Created by ziro on 23/05/17.
 */
public class NineMensMorrisSearch extends Minimax<Mossa> {
	private static final Logger logger = LoggerFactory.getLogger(NineMensMorrisSearch.class);

    private Stato stato;
	private int numeroMossa;

	public NineMensMorrisSearch(Algorithm algoritmo, Stato stato){
		super(algoritmo);
		this.stato=stato;
		this.numeroMossa = 1;
	}

	public Long getTransposition(){
		return stato.getTransposition();
	}

	private int lastDepth = 1;

    public Mossa getBestMove(int depth) {
        lastDepth = depth;
        return super.getBestMove(depth);
	}

	public GroupEntry getGroup(){
		return new GroupEntry(!stato.phase1completed(),
                stato.count[NineMensMorrisSetting.PLAYER_W],
                stato.count[NineMensMorrisSetting.PLAYER_B],
                lastDepth);
	}

	public boolean isOver(){
		return stato.isOver();
	}
	public void makeMove(Mossa move){
		stato.makeMove(move);
	}
	public void unmakeMove(Mossa move){
		stato.unmakeMove(move);
	}
	public List<Mossa> getPossibleMoves(){
		return stato.getPossibleMoves();
	}
	public int evaluate(){
		return ValutatoreStato.valutaStato(stato);
	}
	public int maxEvaluateValue(){
		return Integer.MAX_VALUE;
	}
	public void next(){
		stato.next();
	}

	public void statoAttualeAggiornato(Stato stato){
		this.stato=stato;
		// dobbiamo ripulire la trasposition table da ciò che è inutile
        // -> mossa ulteriore
//        clearGroups(getGroup()); //ripuliamo tutti i gruppi inutili
//        getTranspositionTableMap().forEach((k, v)->k.depth-=2);
//        GC.requestLatency(200);
    }

	public void previous(){
		stato.previous();
	}

	public class GroupEntry implements Comparable<GroupEntry>{
	    public boolean faseUno;
	    public byte numeroPedineBianche;
	    public byte numeroPedineNere;
	    public int depth;

	    public GroupEntry(boolean faseUno, byte numeroPedineBianche, byte numeroPedineNere, int depth){
            this.faseUno = faseUno;
            this.numeroPedineNere = numeroPedineNere;
            this.numeroPedineBianche = numeroPedineBianche;
            this.depth = depth;
        }

        @Override
        public int compareTo(GroupEntry o) {
            if(this.faseUno && o.faseUno) return 0;
	    	if(this.faseUno && !o.faseUno) {
                return -1; // io < altro
            }
            if(!this.faseUno && o.faseUno) {
                return 1;
            }
            if(this.numeroPedineBianche != o.numeroPedineBianche && this.numeroPedineNere != o.numeroPedineNere){
//                return depth - o.depth;
				return 0;
            }else {
                return (o.numeroPedineNere + o.numeroPedineNere) - (this.numeroPedineBianche + this.numeroPedineNere);
            }
        }
    }
}
