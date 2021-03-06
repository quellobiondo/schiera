package it.jar.mulino.ricerca;

import it.jar.mulino.model.Mossa;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;


/**
 * An {@link Minimax} backed by a <a href="http://en.wikipedia.org/wiki/Transposition_table">transposition table</a>
 * to speed up the search of the game tree.<br/>
 * The transposition table will not be serialized with this instance.
 *
 * @author antoine vianey
 *
 * @param <M> the {@link Move} grid
 * @param <T> the transposition table key
 * @param <G> the transposition group grid or {@link Void} if grouping is not necessary.
 * @see Transposition
 */
public abstract class TranspositionMinimax<T, G extends NineMensMorrisSearch.GroupEntry> extends Minimax<Mossa> {

	/**
	 * Factory for transposition table.
	 * Unless {@link TranspositionMinimax#TranspositionIA(Algorithm, TranspositionTableFactory)}
	 * is used as super constructor, an {@link HashMap} is used as default grid.
	 * 
	 * @author antoine vianey
	 *
	 * @param <X>
	 */
	public interface TranspositionTableFactory<X> {
		Map<X, Double> newTransposition();
	}
    
    private final transient TreeMap<G, Map<T, Double>> transpositionTableMap;
    private final transient TranspositionTableFactory<T> transpositionTableFactory;

    public TranspositionMinimax() {
        this(()->new HashMap<T, Double>());	//TranspositionTableFactory<T>::Map<T, Double> newTransposition()
    }

    public TranspositionMinimax(TranspositionTableFactory<T> transpositionTableFactory) {
        this.transpositionTableMap = initTranspositionTableMap();
        this.transpositionTableFactory = transpositionTableFactory;
    }

    public TranspositionMinimax(Algorithm algo) {
        this(algo, ()->new HashMap<>(30000));
    }

    public TranspositionMinimax(Algorithm algo, TranspositionTableFactory<T> transpositionTableFactory) {
        super(algo);
        this.transpositionTableMap = initTranspositionTableMap();
        this.transpositionTableFactory = transpositionTableFactory;
    }

    public TranspositionMinimax(Algorithm algo, final int initialCapacity) {
        super(algo);
        this.transpositionTableMap = initTranspositionTableMap();
        this.transpositionTableFactory = ()->new HashMap<T, Double>(initialCapacity);
    }

    public TranspositionMinimax(Algorithm algo, final int initialCapacity, final float loadFactor) {
        super(algo);
        this.transpositionTableMap = initTranspositionTableMap();
        this.transpositionTableFactory = ()->new HashMap<T, Double>(initialCapacity, loadFactor);
    }

    /**
     * Initialize the map of transposition table classified by groups. 
     * @return
     * 		A {@link TreeMap} storing transposition tables by group
     */
    @SuppressWarnings("unchecked")
	private TreeMap<G, Map<T, Double>> initTranspositionTableMap() {
        Type t = getClass().getGenericSuperclass();
        // search for the Group class within class hierarchy
        while (!(t instanceof ParameterizedType 
                && TranspositionMinimax.class.getSimpleName().equals(
                        ((Class<?>) ((ParameterizedType) t).getRawType()).getSimpleName()))) {
            t = ((Class<?>) t).getGenericSuperclass();
        } 
    	Class<G> cls = (Class<G>) ((ParameterizedType) t).getActualTypeArguments()[1];
        if (Comparable.class.isAssignableFrom(cls)) {
        	// the transposition Group type is Comparable
        	return new TreeMap<G, Map<T,Double>>();
        } else if (cls.isAssignableFrom(Void.class)) {
        	// no transposition Group required
        	// use everything-is-equal Comparator
        	return new TreeMap<G, Map<T,Double>>((a,b)->0);
        } else {
        	throw new IllegalArgumentException("The transposition group type : " + cls.getSimpleName() + " is neither Void nor implement the java.lang.Comparable interface.");
        }
	}

    public TreeMap<G, Map<T, Double>> getTranspositionTableMap() {
        return this.transpositionTableMap;
    }
    
    @Override
    public Mossa getBestMove(int depth) {
    	if (clearGroupsBeforeSearch()) {
    		clearGroups(getGroup());
    	}
        Mossa m = super.getBestMove(depth);
    	if (clearGroupsAfterSearch()) {
    		clearGroups(getGroup());
    	}
    	return m;
    }
    
    /**
     * Set it to false to stop the use of the transposition table<br/>
     * Default is true.
     * @return
     */
    public boolean useTranspositionTable() {
    	return true;
    }
    
    public final void clearGroups(G currentGroup) {
    	if (currentGroup != null) {
    		// free memory :
            // evict unnecessary transpositions
    		transpositionTableMap.headMap(currentGroup).clear();
    		if (abttm!=null)
    			abttm.headMap(currentGroup).clear();
    	}
    }
    
    /**
     * Whether or not the remove useless transposition before the tree exploration.<br/>
     * Default to false.
     * @return
     */
    public boolean clearGroupsBeforeSearch() {
    	return true;
    }
    
    /**
     * Whether or not the remove useless transposition after the tree exploration.<br/>
     * Default to false.
     * @return
     */
    public boolean clearGroupsAfterSearch() {
    	return true;
    }
    
    /**
     * Reset the content of the transposition table.
     * The preferred way to free memory is to use the grouping functionality.
     * 
     * @see #getGroup()
     */
    public final void clearTranspositionTable() {
    	transpositionTableMap.clear();
    }

    /**
     * Represent the current configuration by an int value.
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Zobrist_hashing">Zobrist hashing</a></li>
     * </ul>
     * The current player MUST be taken in account in the transposition's {@link Object#equals(Object)} function
     * otherwise the stored value for the transposition may reflect the strength of the other player...
     * @return
     *      the hash for the current configuration
     *      
     * @see Transposition
     */
    public abstract T getTransposition();
    
    /**
     * Returns all the {@link Transposition} representing the current game configuration.
     * @return
     *      a {@link Collection} of {@link Transposition}
     */
    public Collection<T> getSymetricTranspositions() {
        return Collections.singleton(getTransposition());
    }
    
    /**
     * Represent the group in which the current transposition belong.<br/>
     * Groups can be use to lower the number of transposition stored in memory :
     * <dl>
     * <dt>Reversi</dt>
     * <dd>Transpositions can be grouped by number of discs on board</dd>
     * <dt>Chess</dt>
     * <dd>Transpositions can be grouped by number of left pieces of each color on the board</dd>
     * <dt>Connect Four</dt>
     * <dd>Transpositions can be grouped by number of dropped discs</dd>
     * <dt>...</dt>
     * </dl>
     * Groups <b>MUST</b> be ordered such as when the current configuration hash belong to group
     * G1, transpositions that belongs to groups G < G1 can be forgiven... If you don't want to 
     * handle groups, let G be {@link Void} and return null groups.
     *
     * @return
     *      the group for the current position
     */
    public abstract G getGroup();
    
    private final void saveTransposition(Map<T, Double> transpositionTable, double score){
        if (transpositionTable == null) {
            transpositionTable = transpositionTableFactory.newTransposition();
            transpositionTableMap.put(getGroup(), transpositionTable);
        }
        // save transposition
        for (T st : getSymetricTranspositions()) {
            transpositionTable.put(st, score);
        }
    }
    
    /*=================*
     * IMPLEMENTATIONS *
     *=================*/

	@Override
	public double minimaxScore(int depth, int who){
		if (!useTranspositionTable())
			return super.minimaxScore(depth,who);
		double score=0;
		T t=getTransposition();
		Map<T,Double> transpositionTable=transpositionTableMap.get(getGroup());
		if (transpositionTable!=null&&transpositionTable.containsKey(t)){
			// transposition found
			// we can stop here as we already know the value
			// returned by the evaluation function
			score=who*transpositionTable.get(t);
		} else{
			score=super.minimaxScore(depth,who);
			saveTransposition(transpositionTable,who*score);
		}
		return score;
	}
	@Override
	public double alphabetaScore(int depth, int who, double alpha, double beta){
		if (!useTranspositionTable())
			return super.alphabetaScore(depth,who,alpha,beta);
		double score=0;
		T t=getTransposition();
		Map<T,Double> transpositionTable=transpositionTableMap.get(getGroup());
		if (transpositionTable!=null&&transpositionTable.containsKey(t)){
			// transposition found
			// we can stop here as we already know the value
			// returned by the evaluation function
			if(getGroup().depth>=depth)
			score=who*transpositionTable.get(t);
		} else{
			score=super.alphabetaScore(depth,who,alpha,beta);
			saveTransposition(transpositionTable,who*score);
		}
		return score;
	}
	@Override
	public double negamaxScore(int depth, double alpha, double beta){
		if (!useTranspositionTable())
			return super.negamaxScore(depth,alpha,beta);
		double score=0;
		T t=getTransposition();
		Map<T,Double> transpositionTable=transpositionTableMap.get(getGroup());
		if (transpositionTable!=null&&transpositionTable.containsKey(t)){
			// transposition found
			// we can stop here as we already know the value
			// returned by the evaluation function
			if(getGroup().depth>=depth)
			score=transpositionTable.get(t);
		} else{
			score=super.negamaxScore(depth,alpha,beta);
			saveTransposition(transpositionTable,score);
		}
		return score;
	}
	@Override
	public double negascoutScore(boolean first, int depth, double alpha, double beta, double b){
		if (!useTranspositionTable())
			return super.negascoutScore(first,depth,alpha,beta,b);
		double score=0;
		T t=getTransposition();
		Map<T,Double> transpositionTable=transpositionTableMap.get(getGroup());
		if (transpositionTable!=null&&transpositionTable.containsKey(t)){
			// transposition found
			// we can stop here as we already know the value
			// returned by the evaluation function
			if(getGroup().depth>=depth)
			score=transpositionTable.get(t);
		} else{
			score=super.negascoutScore(first,depth,alpha,beta,b);
			saveTransposition(transpositionTable,score);
		}
		return score;
	}
	
	private class UpperLower {
		int upperbound=(int)maxEvaluateValue(),lowerbound=-upperbound;
	}
	private TreeMap<G,Map<T,UpperLower>> abttm=new TreeMap<>();
    private final void saveTransposition(Map<T, UpperLower> transpositionTable, UpperLower score) {
        if (transpositionTable == null) {
            transpositionTable = new HashMap<>();
            abttm.put(getGroup(), transpositionTable);
        }
        // save transposition
        for (T st : getSymetricTranspositions()) {
            transpositionTable.put(st, score);
        }
    }

	@Override
	protected int AlphaBetaWithMemory(MoveWrapper<Mossa> n, int d, int who, int alpha, int beta){
		//int score, a, b;
		T t=getTransposition();
		Map<T,UpperLower> abtt=abttm.get(getGroup());
		UpperLower e;
		if (abtt!=null && abtt.containsKey(t)){// Transposition table lookup
			e=abtt.get(t);
			if (e.lowerbound>=beta)
				return e.lowerbound;
			if (e.upperbound<=alpha)
				return e.upperbound;
			alpha=Math.max(alpha,e.lowerbound);
			beta=Math.min(beta,e.upperbound);
		} else
			e=new UpperLower();
 		int score=super.AlphaBetaWithMemory(n,d,who,alpha,beta);
		
		// Traditional transposition table storing of bounds 
		// Fail low result implies an upper bound 
		if (score<=alpha)
			e.upperbound=score;
		// Found an accurate minimax value � will not occur if called with zero window 
		else if (score>alpha&&score<beta){
			e.lowerbound=score;
			e.upperbound=score;
		}
		// Fail high result implies a lower bound 
		else if (score>=beta)
			e.lowerbound=score;
		saveTransposition(abtt,e);
		return score;
	}

	@Override
	protected int alphaBetaWithMemoryScore(int d, int who, int alpha, int beta){
		int score=0;
		T t=getTransposition();
		Map<T,Double> transpositionTable=transpositionTableMap.get(getGroup());
		if (transpositionTable!=null && transpositionTable.containsKey(t)){
			// transposition found
			// we can stop here as we already know the value
			// returned by the evaluation function
			score=who*(int)(double)transpositionTable.get(t);
		} else {
			score=super.alphaBetaWithMemoryScore(d,who,alpha,beta);
			saveTransposition(transpositionTable,who*score);
		}
		return score;
	}
}
