package it.jar.mulino;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.domain.State;

/**
 * Created by ziro on 26/04/17.
 */
public abstract class ActionChooser {

    protected abstract Action chooseActionForPhaseOne(State currentState);
    protected abstract Action chooseActionForPhaseTwo(State currentState);
    protected abstract Action chooseActionForFinalPhase(State currentState);

    public Action chooseNextAction(State currentState){
        switch (currentState.getCurrentPhase()){
            case FIRST: return chooseActionForPhaseOne(currentState);
            case SECOND: return chooseActionForPhaseTwo(currentState);
            case FINAL: return chooseActionForFinalPhase(currentState);
        }
        throw new IllegalStateException("Fase sconosciuta");
    }
}
