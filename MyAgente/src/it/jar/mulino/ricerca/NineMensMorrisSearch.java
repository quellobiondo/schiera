package it.jar.mulino.ricerca;

import java.util.*;
import it.jar.mulino.logic.*;
import it.jar.mulino.model.*;
import it.jar.mulino.utils.*;
import org.slf4j.*;

/**
 * Created by ziro on 23/05/17.
 */
public class NineMensMorrisSearch extends TranspositionMinimax<Long, NineMensMorrisSearch.GroupEntry> {
	private static final Logger logger = LoggerFactory.getLogger(NineMensMorrisSearch.class);

    private Stato stato;
	private int numeroMossa;

	public NineMensMorrisSearch(Algorithm algoritmo, Stato stato){
		super(algoritmo);
		this.stato=stato;
		this.numeroMossa = 1;
	}

	@Override
	public Long getTransposition(){
		return stato.getTransposition();
	}

	private int lastDepth = 1;

    @Override
    public Mossa getBestMove(int depth) {
        lastDepth = depth;
        return super.getBestMove(depth);
	}

        @Override
	public GroupEntry getGroup(){
		return new GroupEntry(!stato.phase1completed(),
                stato.count[NineMensMorrisSetting.PLAYER_W],
                stato.count[NineMensMorrisSetting.PLAYER_B],
                lastDepth);
	}
	@Override
	public boolean isOver(){
		return stato.isOver();
	}
	@Override
	public void makeMove(Mossa move){
		stato.makeMove(move);
	}
	@Override
	public void unmakeMove(Mossa move){
		stato.unmakeMove(move);
	}
	@Override
	public List<Mossa> getPossibleMoves(){
		return stato.getPossibleMoves();
	}
	@Override
	public int evaluate(){
		return ValutatoreStato.valutaStato(stato);
	}
	@Override
	public int maxEvaluateValue(){
		return Integer.MAX_VALUE;
	}
	@Override
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

	@Override
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
		public int compareTo(GroupEntry o){
			if (this.faseUno && !o.faseUno)
				return -1; // io < altro
			if (!this.faseUno && o.faseUno)
				return 1;
			if (this.faseUno ||(this.numeroPedineBianche==o.numeroPedineBianche && this.numeroPedineNere==o.numeroPedineNere))
				return depth-o.depth;
			else
				return (o.numeroPedineNere+o.numeroPedineNere)-(this.numeroPedineBianche+this.numeroPedineNere);
		}
    }
}
