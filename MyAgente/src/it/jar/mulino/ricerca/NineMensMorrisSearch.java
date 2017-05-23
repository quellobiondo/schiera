package it.jar.mulino.ricerca;

import it.jar.mulino.logic.ValutatoreStato;
import it.jar.mulino.model.Mossa;
import it.jar.mulino.model.Stato;

import java.util.List;

/**
 * Created by ziro on 23/05/17.
 */
public class NineMensMorrisSearch extends TranspositionMinimax<Long, Stato> {

    private Stato stato;

    public NineMensMorrisSearch(Algorithm algoritmo, Stato stato){
        super(algoritmo);
        this.stato = stato;
    }

    @Override
    public Long getTransposition() {
        return stato.getTransposition();
    }

    @Override
    public Stato getGroup() {
        return null;
    }

    @Override
    public boolean isOver() {
        return stato.isOver();
    }

    @Override
    public void makeMove(Mossa move) {
        stato.makeMove(move);
    }

    @Override
    public void unmakeMove(Mossa move) {
        stato.unmakeMove(move);
    }

    @Override
    public List<Mossa> getPossibleMoves() {
        return stato.getPossibleMoves();
    }

    @Override
    public int evaluate() {
        return ValutatoreStato.valutaStato(stato);
    }

    @Override
    public int maxEvaluateValue() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void next() {
        stato.next();
    }

    public void statoAttualeAggiornato(Stato stato){
        this.stato = stato;
        //dobbiamo ripulire la trasposition table da ciò che è inutile
        super.clearTranspositionTable();
    }

    @Override
    public void previous() {
        stato.previous();
    }
}
