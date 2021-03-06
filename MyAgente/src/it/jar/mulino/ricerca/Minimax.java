package it.jar.mulino.ricerca;

import it.jar.mulino.model.Mossa;
import java.util.*;

/*
 * This file is part of minimax4j.
 * <https://github.com/avianey/minimax4j>
 *  
 * Copyright (C) 2012, 2013, 2014 Antoine Vianey
 * 
 * minimax4j is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * minimax4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with minimax4j. If not, see <http://www.gnu.org/licenses/lgpl.html>
 */

/**
 * Abstract class implementing minimax and derivated decision rules for two-person 
 * <a href="http://en.wikipedia.org/wiki/Zero-sum_game">zero-sum</a> games of perfect information.
 * Extend this class to implement IA for several games, such as :
 * <ul>
 * <li>Chess</li>
 * <li>Reversi</li>
 * <li>Checkers</li>
 * <li>Go</li>
 * <li>Connect Four</li>
 * <li>Tic Tac Toe</li>
 * <li>...</li>
 * </ul>
 * 
 * @author antoine vianey
 *
 * @param <M> Implementation of the Move interface to use
 */
public abstract class Minimax<M extends Mossa> {
    
    private final Algorithm algo;
    //private final TranspositionTable transpositionTable = new TranspositionTable();

    static final class MoveWrapper<M extends Mossa> {
        public M move;
    }
    
    /**
     * Available decision rules
     * 
     * @author antoine vianey
     */
    public enum Algorithm {
        /** 
         * The Minimax algorithm (slowest) 
         * @see http://en.wikipedia.org/wiki/Minimax
         **/
        MINIMAX,
        /** 
         * The Mininma algorithm with alpha-beta pruning 
         * @see http://en.wikipedia.org/wiki/Alpha-beta_pruning
         **/
        ALPHA_BETA,
        /** 
         * The Negamax algorithm with alpha-beta pruning
         * @see http://en.wikipedia.org/wiki/Negamax
         **/
        NEGAMAX,
        /** 
         * The Negascout algorithm (fastest when strong move ordering is provided)<br/>
         * Also called Principal Variation Search...
         * @see Minimax#getPossibleMoves()
         * @see http://en.wikipedia.org/wiki/Negascout
         **/
        NEGASCOUT,
        /**
         * MTD-f
         * @see https://en.wikipedia.org/wiki/MTD-f
         */
        MTD,
        /**
         * Best Node Search
         * @see https://en.wikipedia.org/wiki/Best_Node_Search
         */
        BNS
    }
    
    /**
     * Creates a new IA using the {@link Algorithm#NEGAMAX} algorithm<br/>
     * {@link Algorithm#NEGASCOUT} performs slowly in case of a weak move ordering...
     */
    public Minimax() {
        this(Algorithm.NEGAMAX);
    }
    
    /**
     * Creates a new IA using the provided algorithm
     * @param algo The decision rule to use
     * @see Algorithm
     */
    public Minimax(Algorithm algo) {
        this.algo = algo;
    }
    
    /**
     * Get the best {@link Move} for the given search depth<br/>
     * This methods iterates over {@link #getPossibleMoves()} to find the best one.
     * If two or more {@link Move} lead to the same best evaluation, the first one is returned.
     * @param depth The search depth (must be > 0)
     * @return
     */
    public M getBestMove(final int depth) {
        if (depth <= 0) {
            throw new IllegalArgumentException("Search depth MUST be > 0");
        }

        MoveWrapper<M> wrapper = new MoveWrapper<>();
        switch (algo) {
        case MINIMAX:
            minimax(wrapper, depth, 1);
            break;
        case ALPHA_BETA:
            alphabeta(wrapper, depth, 1, -maxEvaluateValue(), maxEvaluateValue());
            break;
        case NEGAMAX:
            negamax(wrapper, depth, -maxEvaluateValue(), maxEvaluateValue());
            break;
        case NEGASCOUT:
            negascout(wrapper, depth, -maxEvaluateValue(), maxEvaluateValue());
            break;
        case MTD:
            MTD(wrapper, depth, maxEvaluateValue());
        case BNS:
            BNS(wrapper, depth, -maxEvaluateValue(), maxEvaluateValue(), 1);
        }
        return wrapper.move;
    }

    /**
     * Minimax algorithm grid :
     * <pre>
     * function minimax(node, depth, maximizingPlayer)
     *     if depth = 0 or node is a terminal node
     *         return the heuristic value of node
     *     if maximizingPlayer
     *         bestValue := -&#8734;
     *         for each child of node
     *             val := minimax(child, depth - 1, FALSE)
     *             bestValue := max(bestValue, val)
     *         return bestValue
     *     else
     *         bestValue := +&#8734;
     *         for each child of node
     *             val := minimax(child, depth - 1, TRUE)
     *             bestValue := min(bestValue, val)
     *         return bestValue
     * </pre>
     * 
     * Initial call for maximizing player
     * <pre>minimax(origin, depth, TRUE)</pre>
     * 
     * @param wrapper
     * @param depth
     * @param DEPTH
     * @return
     */
    private final double minimax(final MoveWrapper<M> wrapper, final int depth, final int who) {
        if (depth == 0 || isOver())
            return who * evaluate();
        M bestMove = null;
        Collection<M> moves = getPossibleMoves();
        if (moves.isEmpty()) {
        	next();
            double score = minimaxScore(depth, who);
            previous();
            return score;
        }
        if (who > 0) {
            double score = -maxEvaluateValue();
            double bestScore = -maxEvaluateValue();
            for (M move : moves) {
                makeMove(move);
                score = minimaxScore(depth, who);
                unmakeMove(move);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return bestScore;
        } else {
            double score = maxEvaluateValue();
            double bestScore = maxEvaluateValue();
            for (M move : moves) {
                makeMove(move);
                score = minimaxScore(depth, who);
                unmakeMove(move);
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return bestScore;
        }
    }
    
    public double minimaxScore(final int depth, final int who) {
		return minimax(null, depth - 1, -who);
	}

	/**
     * Minimax with alpha beta algorithm :
     * <pre>
     * function alphabeta(node, depth, &#945;, &#946;, maximizingPlayer)
	 *     if depth = 0 or node is a terminal node
	 *         return the heuristic value of node
	 *     if maximizingPlayer
	 *         for each child of node
	 *             &#945; := max(&#945;, alphabeta(child, depth - 1, &#945;, &#946;, FALSE))
	 *             if &#946; <= &#945;
	 *                 break (* &#946; cut-off *)
	 *         return a
	 *     else
	 *         for each child of node
	 *             &#946; := min(&#946;, alphabeta(child, depth - 1, &#945;, &#946;, TRUE))
	 *             if &#946; <= &#945;
	 *                 break (* &#945; cut-off *)
	 *         return &#946;
	 * </pre>
	 * Initial call for maximizing player
     * <pre>alphabeta(origin, depth, -8, +8, TRUE)</pre>
     * 
     * @param wrapper
     * @param depth
     * @param who
     * @param alpha
     * @param beta
     * @return
     */
    private final double alphabeta(final MoveWrapper<M> wrapper, final int depth, final int who, double alpha, double beta) {
        if (depth == 0 || isOver()) {
            return who * evaluate();
        }
        M bestMove = null;
        double score;
        Collection<M> moves = getPossibleMoves();
        if (moves.isEmpty()) {
        	next();
            score = alphabetaScore(depth, who, alpha, beta);
            previous();
            return score;
        }
        if (who > 0) {
            for (M move : moves) {
                makeMove(move);
                score = alphabetaScore(depth, who, alpha, beta);
                unmakeMove(move);
                if (score > alpha) {
                    alpha = score;
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return alpha;
        } else {
            for (M move : moves) {
                makeMove(move);
                score = alphabetaScore(depth, who, alpha, beta);
                unmakeMove(move);
                if (score < beta) {
                    beta = score;
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return beta;
        }
    }

    public double alphabetaScore(final int depth, final int who, final double alpha, final double beta) {
		return alphabeta(null, depth - 1, -who, alpha, beta);
	}
    
    /**
     * Negamax algorithm :
     * <pre>
     * function negamax(node, depth, color)
     *     if depth = 0 or node is a terminal node
     *         return color * the heuristic value of node
     *     bestValue := -&#8734;
     *     foreach child of node
     *         val := -negamax(child, depth - 1, -color)
     *         bestValue := max( bestValue, val )
     *     return bestValue
     * </pre>
     * 
     * Initial call for Player A's root node
     * <pre>
     * rootNegamaxValue := negamax( rootNode, depth, 1)
     * rootMinimaxValue := rootNegamaxValue
     * </pre>
     * 
     * Initial call for Player B's root node
     * <pre>
     * rootNegamaxValue := negamax( rootNode, depth, -1)
     * rootMinimaxValue := -rootNegamaxValue
     * </pre>
     * 
     * This grid use alpha-beta cut-offs.
     * 
     * @param wrapper
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private double negamax(final MoveWrapper<M> wrapper, final int depth, double alpha, double beta) {
        if (depth == 0 || isOver()) {
            return evaluate();
        }
        M bestMove = null;
        Collection<M> moves = getPossibleMoves();
        if (moves.isEmpty()) {
        	next();
        	double score = negamaxScore(depth, alpha, beta);
        	previous();
        	return score;
        } else {
            double score = -maxEvaluateValue();
            for (M move : moves) {
                makeMove(move);
                score = negamaxScore(depth, alpha, beta);
                unmakeMove(move);
                if (score > alpha) {
                    alpha = score;
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return alpha;
        }
    }

    public double negamaxScore(final int depth, final double alpha, final double beta) {
		return -negamax(null, depth - 1, -beta, -alpha);
	}
    
    /**
     * Negascout PVS algorithm :
     * <pre>
     * function pvs(node, depth, &#945;, &#946;, color)
     *     if node is a terminal node or depth = 0
     *         return color x the heuristic value of node
     *     for each child of node
     *         if child is not first child
     *             score := -pvs(child, depth-1, -&#945;-1, -&#945;, -color)       (* search with a null window *)
     *             if &#945; < score < &#946;                                      (* if it failed high,
     *                 score := -pvs(child, depth-1, -&#946;, -score, -color)         do a full re-search *)
     *         else
     *             score := -pvs(child, depth-1, -&#946;, -&#945;, -color)
     *         &#945; := max(&#945;, score)
     *         if &#945; >= &#946;
     *             break                                            (* beta cut-off *)
     *     return &#945;
     * </pre>
     * 
     * @param wrapper
     * @param depth
     * @param alpha
     * @param beta
     * @return
     */
    private double negascout(final MoveWrapper<M> wrapper, final int depth, double alpha, double beta) {
        if (depth == 0 || isOver()) {
            return evaluate();
        }
        List<M> moves = getPossibleMoves();
        double b = beta;
        M bestMove = null;
        if (moves.isEmpty()) {
        	next();
            double score = negascoutScore(true, depth, alpha, beta, b);
            previous();
            return score;
        } else {
            double score;
            boolean first = true;
            for (M move : moves) {
                makeMove(move);
                score = negascoutScore(first, depth, alpha, beta, b);
                unmakeMove(move);
                if (score > alpha) {
                    alpha = score;
                    bestMove = move;
                    if (alpha >= beta) {
                        break;
                    }
                }
                b = alpha + 1;
                first = false;
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return alpha;
        }
    }

    public double negascoutScore(final boolean first, final int depth, final double alpha, final double beta, final double b) {
    	double score = -negascout(null, depth - 1, -b, -alpha);
        if (!first && alpha < score && score < beta) {
            // fails high... full re-search
            score = -negascout(null, depth - 1, -beta, -alpha);
        }
        return score;
	}
    
    /**
     * Minimax algorithm grid :
     * <pre>
     * function minimax(node, depth, maximizingPlayer)
     *     if depth = 0 or node is a terminal node
     *         return the heuristic value of node
     *     if maximizingPlayer
     *         bestValue := -&#8734;
     *         for each child of node
     *             val := minimax(child, depth - 1, FALSE)
     *             bestValue := max(bestValue, val)
     *         return bestValue
     *     else
     *         bestValue := +&#8734;
     *         for each child of node
     *             val := minimax(child, depth - 1, TRUE)
     *             bestValue := min(bestValue, val)
     *         return bestValue
     * </pre>
     * 
     * Initial call for maximizing player
     * <pre>minimax(origin, depth, TRUE)</pre>
     * 
     * @param wrapper
     * @param depth
     * @param DEPTH
     * @return
     */
    /*private final double minimax(final MoveWrapper<M> wrapper, final int depth, final int who) {
        if (depth == 0 || isOver()) {
            return who * evaluate();
        }
        M bestMove = null;
        Collection<M> moves = getPossibleMoves();
        if (moves.isEmpty()) {
        	next();
            double score = minimaxScore(depth, who);
            previous();
            return score;
        }
        if (who > 0) {
            double score = -maxEvaluateValue();
            double bestScore = -maxEvaluateValue();
            for (M move : moves) {
                makeMove(move);
                score = minimaxScore(depth, who);
                unmakeMove(move);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return bestScore;
        } else {
            double score = maxEvaluateValue();
            double bestScore = maxEvaluateValue();
            for (M move : moves) {
                makeMove(move);
                score = minimaxScore(depth, who);
                unmakeMove(move);
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
            if (wrapper != null) {
                wrapper.move = bestMove;
            }
            return bestScore;
        }
    }*/
    /**
     * <pre>
     * function AlphaBetaWithMemory(n : node_type; alpha , beta , d : integer) : integer;
	 *     if retrieve(n) == OK then // Transposition table lookup 
	 *         if n.lowerbound >= beta then return n.lowerbound;
	 *         if n.upperbound <= alpha then return n.upperbound;
	 *         alpha := max(alpha, n.lowerbound);
	 *         beta := min(beta, n.upperbound);
	 *     if d == 0 then g := evaluate(n); // leaf node 
	 *     else if n == MAXNODE then 
	 *         g := -&#8734; a := alpha; // save original alpha value 
	 *         c := firstchild(n);
	 *         while  (g < beta) and (c != NOCHILD) do
	 *             g := max(g, AlphaBetaWithMemory(c, a, beta, d � 1));
	 *             a := max(a, g);
	 *             c := nextbrother(c);
	 *     else // n is a MINNODE 
	 *         g := +&#8734; b := beta; // save original beta value 
	 *         c := firstchild(n);
	 *         while (g > alpha) and (c != NOCHILD) do
	 *             g := min(g, AlphaBetaWithMemory(c, alpha, b, d � 1));
	 *             b := min(b, g);
	 *             c := nextbrother(c);
	 *     // Traditional transposition table storing of bounds
	 *     // Fail low result implies an upper bound 
	 *     if g <= alpha then n.upperbound := g; store n.upperbound;
	 *     // Found an accurate minimax value � will not occur if called with zero window 
	 *     if g > alpha and g < beta then n.lowerbound := g; n.upperbound := g; store n.lowerbound, n.upperbound;
	 *     // Fail high result implies a lower bound 
	 *     if g >= beta then n.lowerbound := g; store n.lowerbound;
	 *     return g;
	 * </pre>
     */
	protected int AlphaBetaWithMemory(MoveWrapper<M> out, int d, int who, int alpha, int beta){
		if (d==0)
			return who*(int)evaluate(); // leaf node
		else {
			Collection<M> moves=getPossibleMoves();
			M bestMove=null;
			int score, a, b;
			if (moves.isEmpty()){
				next();
				score=alphaBetaWithMemoryScore(d,who,alpha,beta);
				previous();
			} else if (who>0){
				score=(int)-maxEvaluateValue();
				a=alpha; // save original alpha value
				for (M move : moves){
					makeMove(move);
					score=alphaBetaWithMemoryScore(d,who,a,beta);
					unmakeMove(move);
					if (score>a){
						a=score;
						bestMove=move;
						if (a>=beta)
							break;
					}
				}
				if (out!=null){
					out.move=bestMove;
				}
			} else { // n is a MINNODE
				score=maxEvaluateValue();
				b=beta; // save original beta value
				for (M move : moves){
					makeMove(move);
					score=alphaBetaWithMemoryScore(d,who,alpha,b);
					unmakeMove(move);
					if (score<b){
						b=score;
						bestMove=move;
						if (alpha>=b)
							break;
					}
				}
				if (out!=null){
					out.move=bestMove;
				}
			}
			return score;
		}
	}

	protected int alphaBetaWithMemoryScore(int d, int who, int alpha, int beta){
		return AlphaBetaWithMemory(null,d-1,-who,alpha,beta);
	}
    /**
     * <pre>
     * function MTD-F(root : node_type; f : integer; d: integer) : integer;
	 *     g := f;
	 *     upperbound := +&#8734;
	 *     lowerbound := -&#8734;
	 *     repeat
	 *         if g == lowerbound then beta := g + 1 else beta := g;
	 *         g := AlphaBetaWithMemory(root, beta � 1, beta, d);
	 *         if g < beta then upperbound := g else lowerbound := g;
	 *     until lowerbound >= upperbound;
	 *     return g;
	 * </pre>
     * @param out
     * @param d
     * @param f
     * @return
     */
	protected int MTD(MoveWrapper<M> out, int d, int f){
		int g=f;
		int upperbound=(int)maxEvaluateValue(),lowerbound=(int)-maxEvaluateValue();
		do {
			int beta=g==lowerbound ? g+1 : g;
			g=AlphaBetaWithMemory(out,d,1,beta-1,beta);
			if (g<beta)
				upperbound=g;
			else
				lowerbound=g;
		} while (lowerbound<upperbound);
		return g;
	}

	/**
	 * function BNS(node, alfa, beta, quality)
	 *     do
	 *         test := NextGuess(node, alfa, beta)
	 *         betterCount := 0
	 *         foreach child of node
	 *             bestVal := -AlphaBeta(child, -test, -(test - 1))
	 *             if bestVal >= test
	 *                 betterCount := betterCount + 1
	 *                 bestNode := child
	 *                 if expectedQuality >= quality
	 *                     return bestNode
	 *         update alpha-beta range
	 *     while not((beta - alfa < 2) or (betterCount = 1))
	 *     return bestNode
	 */
	protected int BNS(MoveWrapper<M> out, int d, int alfa, int beta, int quality){
    	if (out==null)
    		throw new IllegalArgumentException("Dove vuoi che ti metta la mossa??!?!");
    	Collection<M> moves=getPossibleMoves();
    	int betterCount,bestVal=alfa;
		do {
			int test=nextGuess(alfa,beta,moves.size());
			betterCount=0;
			if (moves.isEmpty()){
				next();
//				bestVal=alphaBetaWithMemoryScore(d,-1,alfa,beta);
                bestVal=(int) -alphabetaScore(d, -1, alfa, beta);
				previous();
			}
			for (M move : moves){
				makeMove(move);
				//bestVal=-alphaBetaWithMemoryScore(d,1,-test,1-test);///////////
                bestVal= (int) -alphabetaScore(d,1,-test,1-test);
				//bestVal=alphaBetaWithMemoryScore(d,who,alfa,beta);
				unmakeMove(move);
				if (bestVal>=test){
					betterCount++;
					out.move=move;
				} else
					moves.remove(move);
			}
			if (betterCount!=moves.size())
				new InternalError("betterCount!=mosse.size").printStackTrace();
			alfa=test;
//			store(node.lowerbound,node.upperbound);
		} while (beta-alfa>=2 && betterCount>quality);
		return bestVal;
	}

	protected int nextGuess(int alfa, int beta, int figli){
		return alfa+(beta-alfa)*(figli-1)/figli;
	}

	/**
     * Tell weather or not the game is over.
     * @return
     *         True if the game is over
     */
    public abstract boolean isOver();
    
    /**
     * Play the given move and modify the state of the game.<br/>
     * This function <strong>MUST</strong> set correctly the turn of the next player
     * ... by calling the next() method for example.
     * @param move
     *             The move to play
     * @see #next()
     */
    public abstract void makeMove(M move);
    
    /**
     * Undo the given move and restore the state of the game.<br/>
     * This function <strong>MUST</strong> restore correctly the turn of the previous player
     * ... by calling the previous() method for example.
     * @param move
     *             The move to cancel
     * @see #previous()
     */
    public abstract void unmakeMove(M move);
    
    /**
     * List every valid moves for the current player.<br><br>
     * <i>"Improvement (of the alpha beta pruning) can be achieved without 
     * sacrificing accuracy, by using ordering heuristics to search parts 
     * of the tree that are likely to force alpha-beta cutoffs early."</i>
     * <br>- <a href="http://en.wikipedia.org/wiki/Alpha-beta_pruning#Heuristic_improvements">Alpha-beta pruning on Wikipedia</a>
     * @return
     *         The list of the current player possible moves
     */
    public abstract List<M> getPossibleMoves();

    /**
     * Evaluate the state of the game <strong>for the current player</strong> after a move.
     * The greatest the value is, the better the position of the current player is.
     * @return
     *         The evaluation of the position for the current player
     * @see #maxEvaluateValue()
     */
    public abstract int evaluate();
    
    /**
     * The absolute maximal value for the evaluate function.
     * This value must not be equal to a possible return value of the evaluation function.
     * @return
     *         The <strong>non inclusive</strong> maximal value
     * @see #evaluate()
     */
    public abstract int maxEvaluateValue();
    
    /**
     * Change current turn to the next player.
     * This method must not be used in conjunction with the makeMove() method.
     * Use it to implement a <strong>pass</strong> functionality.
     * @see #makeMove(Move)
     */
    public abstract void next();
    
    /**
     * Change current turn to the previous player.
     * This method must not be used in conjunction with the unmakeMove() method.
     * Use it to implement an <strong>undo</strong> functionality.
     * @see #unmakeMove(Move)
     */
    public abstract void previous();

    /**
     * Get the grid used for tree search.
     * @return the {@link Algorithm} used.
     */
    public Algorithm getAlgo() {
        return algo;
    }
    
}
