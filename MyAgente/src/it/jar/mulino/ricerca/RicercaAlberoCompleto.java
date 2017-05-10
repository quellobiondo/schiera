package it.jar.mulino.ricerca;

import java.util.*;
import aima.core.agent.*;
import aima.core.search.adversarial.*;
import aima.core.search.framework.*;
import aima.core.search.framework.problem.*;
import aima.core.search.framework.qsearch.*;
import aima.core.search.uninformed.*;
import it.jar.mulino.model.*;

public class RicercaAlberoCompleto {
	public static void main(String[] q) throws Exception{
		/*Problem p=new Problem(new Stato(),s->new HashSet<Action>(
				((Stato)s).getMosse()),(s, a)->((Stato)s).muovi((Mossa)a),
				s->((Stato)s).vittoria(Casella.BIANCA)==1);
		Game<Stato,Mossa,Byte> g=new Game<Stato,Mossa,Byte>(){
			public boolean isTerminal(Stato s){
				return s.vittoria(Casella.BIANCA)!=0;
			}
			public double getUtility(Stato s, Byte g){
				return s.vittoria(g);	////////////????????????
			}
			public Stato getResult(Stato s, Mossa m){
				return s.muovi(m);
			}
			public Byte[] getPlayers(){
				return new Byte[]{Casella.BIANCA,Casella.NERA};
			}
			public Byte getPlayer(Stato s){
				return s.getGiocatoreDiTurno();
			}
			public Stato getInitialState(){
				return new Stato();
			}
			public List<Mossa> getActions(Stato s){
				return s.getMosse();
			}
		};
		MinimaxSearch<Stato,Mossa,Byte> ric=new MinimaxSearch<Stato,Mossa,Byte>(g);*/
		
/*		SearchForActions ric=new Mini(new TreeSearch());
		SearchAgent a=new SearchAgent(p,ric);*/
		
		/*System.out.println(ric.getMetrics());
		System.out.println(ric.makeDecision(g.getInitialState()));
		System.out.println(ric.getMetrics());*/
		Nodo n=creaAlbero(new Stato());
		System.out.println(n);
		
	}
	public static Nodo creaAlbero(Stato si){
		Nodo rad=new Nodo.Max(null,si);
		Deque<Nodo> q=new LinkedList<>();
		q.addLast(rad);
		do {
			Nodo n=q.removeFirst();
			byte v=n.getStato().vittoria(Casella.BIANCA);
			if (v!=0){
				n.foglia(v);
				for (Nodo np=n;np.getPadre()!=null && np.tryMerge(););
			} else {
				n.espandi(p->p.getStato().getMosse().stream().map(m->p.getStato().muovi(m)).map(s->s.getGiocatoreDiTurno()==Casella.BIANCA?new Nodo.Max(n,s):new Nodo.Min(n,s)));
				q.addAll(n.getFigli());
			}
		} while (!q.isEmpty());
		return rad;
	}
}
