package it.jar.mulino;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Util;
import it.unibo.ai.didattica.mulino.domain.State;

/**
 * Created by ziro on 26/04/17.
 */
public class RandomActionChooser extends ActionChooser {

    private String getFreePosition(State currentState){
        return "";
    }

    private String getFreeAndAdjacentPosition(State currentState){
        return "";
    }

    private boolean isTris(Action mossa, State currentState){
        //Util.hasCompletedTriple()
        return false;
    }

    private String removeOpponentElement(){
        return "";
    }

    @Override
    protected Action chooseActionForPhaseOne(State currentState) {
        Phase1Action mossa = new Phase1Action();
        mossa.setPutPosition(getFreePosition(currentState));
        if(isTris(mossa, currentState)){
            mossa.setRemoveOpponentChecker(removeOpponentElement());
        }
        return mossa;
    }

    @Override
    protected Action chooseActionForPhaseTwo(State currentState) {

        Phase1Action mossa = new Phase1Action();
        mossa.setPutPosition(getFreeAndAdjacentPosition(currentState));
        if(isTris(mossa, currentState)){
            mossa.setRemoveOpponentChecker(removeOpponentElement());
        }
        return mossa;
    }

    @Override
    protected Action chooseActionForFinalPhase(State currentState) {
        return null;
    }
}
