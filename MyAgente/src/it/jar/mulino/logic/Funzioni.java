package it.jar.mulino.logic;

public class Funzioni {

	/*public int MTDF(Nodo root, int f, int d){
		int g=f;
		int upperbound=Integer.MAX_VALUE;
		int lowerbound=Integer.MIN_VALUE;
		int beta;
		do{
			if (g==lowerbound)
				beta=g+1;
			else
				beta=g;
			g=AlphaBetaWithMemory(root,beta-1,beta,d);
			if (g<beta)
				upperbound=g;
			else
				lowerbound=g;
		} while (lowerbound<upperbound);
		return g;
	}
	private int AlphaBetaWithMemory(Nodo n, int alpha, int beta, int d){
		int g, a, b;
		if (retrieve(n)){*//* Transposition table lookup *//*
			if (n.lowerbound>=beta)
				return n.lowerbound;
			if (n.upperbound<=alpha)
				return n.upperbound;
			alpha=Math.max(alpha,n.lowerbound);
			beta=Math.min(beta,n.upperbound);
		}
		if (d==0)
			g=evaluate(n); *//* leaf node *//*
		else if (n==MAXNODE){
			g=Integer.MIN_VALUE;
			a=alpha; *//* save original alpha value *//*
			Nodo c=n.firstchild();
			while (g<beta&&c!=null){
				g=Math.max(g,AlphaBetaWithMemory(c,a,beta,d-1));
				a=Math.max(a,g);
				c=c.nextbrother();
			}
		} else{ *//* n is a MINNODE *//*
			g=Integer.MAX_VALUE;
			b=beta; *//* save original beta value *//*
			Nodo c=n.firstchild();
			while (g>alpha&&c!=null){
				g=Math.min(g,AlphaBetaWithMemory(c,alpha,b,d-1));
				b=Math.min(b,g);
				c=c.nextbrother();
			}
		}
		*//* Traditional transposition table storing of bounds *//*
		*//* Fail low result implies an upper bound *//*
		if (g<=alpha){
			n.upperbound=g;
			store(n.upperbound);
		}
		*//* Found an accurate minimax value – will not occur if called with zero window *//*
		if (g>alpha&&g<beta){
			n.lowerbound=g;
			n.upperbound=g;
			store(n.lowerbound,n.upperbound);
		}
		*//* Fail high result implies a lower bound *//*
		if (g>=beta){
			n.lowerbound=g;
			store(n.lowerbound);
		}
		return g;
	}
	public int iterative_deepening(Nodo root){
		int firstguess=0;
		for (int d=1;d<MAX_SEARCH_DEPTH;d++){
			firstguess=MTDF(root,firstguess,d);
			// if (times_up()) break;
		}
		return firstguess;
	}
	*//*
	 * public Nodo BNS(Nodo node, int alfa, int beta){
	 *  int subtreeCount = node.nFigli;
	 *  Nodo bestNode=node;
	 *  do{ int test = NextGuess(alfa, beta, subtreeCount);
	 *   int betterCount = 0;
	 *   for (Nodo child : node){
	 *    int bestVal = -AlphaBeta(child, -test, -(test - 1));
	 *    if (bestVal >= test){
	 *     betterCount++;
	 *     bestNode = child;
	 *    }
	 *   }
	 *   update number of sub-trees that exceeds separation test value, update alpha-beta range
	 *  } while not((beta - alfa < 2) or (betterCount = 1))
	 *  return bestNode;
	 * }
	 *//*
	public Nodo BNS(Nodo node, int alfa, int beta, int quality){
		Nodo bestNode=node;
		int betterCount;
		do{
			int test=NextGuess(node,alfa,beta);
			betterCount=0;
			for (Nodo child : node){
				int bestVal=-AlphaBeta(child,-test,-(test-1));
				if (bestVal>=test){
					betterCount++;
					bestNode=child;
					if (expectedQuality>=quality)
						return bestNode;
				}
			}
			store(node.lowerbound,node.upperbound);
		} while (!((beta-alfa<2||betterCount==1)));
		return bestNode;
	}*/
}
