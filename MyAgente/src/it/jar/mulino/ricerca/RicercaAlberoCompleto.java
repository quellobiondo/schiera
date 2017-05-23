package it.jar.mulino.ricerca;

import java.util.*;

import it.jar.mulino.model.*;

public class RicercaAlberoCompleto {
	public static void main(String[] q) throws Exception{
		/*Problem p=new Problem(new StatoRicky(),s->new HashSet<Action>(
				((StatoRicky)s).getMosse()),(s, a)->((StatoRicky)s).muovi((Mossa)a),
				s->((StatoRicky)s).vittoria(Casella.BIANCA)==1);
		Game<StatoRicky,Mossa,Byte> g=new Game<StatoRicky,Mossa,Byte>(){
			public boolean isTerminal(StatoRicky s){
				return s.vittoria(Casella.BIANCA)!=0;
			}
			public double getUtility(StatoRicky s, Byte g){
				return s.vittoria(g);	////////////????????????
			}
			public StatoRicky getResult(StatoRicky s, Mossa m){
				return s.muovi(m);
			}
			public Byte[] getPlayers(){
				return new Byte[]{Casella.BIANCA,Casella.NERA};
			}
			public Byte getPlayer(StatoRicky s){
				return s.getGiocatoreDiTurno();
			}
			public StatoRicky getInitialState(){
				return new StatoRicky();
			}
			public List<Mossa> getActions(StatoRicky s){
				return s.getMosse();
			}
		};
		MinimaxSearch<StatoRicky,Mossa,Byte> ric=new MinimaxSearch<StatoRicky,Mossa,Byte>(g);*/
		
/*		SearchForActions ric=new Mini(new TreeSearch());
		SearchAgent a=new SearchAgent(p,ric);*/
		
		/*System.out.println(ric.getMetrics());
		System.out.println(ric.makeDecision(g.getInitialState()));
		System.out.println(ric.getMetrics());*/
		Nodo n=creaAlbero(new StatoRicky());
		System.out.println(n);
		
	}
	public static Nodo creaAlbero(StatoRicky si){
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
				n.espandi(p->p.getStato().getMosse().stream().map(m->p.getStato().muovi(m))
						.map(s->s.getGiocatoreDiTurno()==Casella.BIANCA?
								new Nodo.Max(n,s):new Nodo.Min(n,s)));
				q.addAll(n.getFigli());
			}
		} while (!q.isEmpty());
		return rad;
	}
}
