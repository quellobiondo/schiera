package it.jar.mulino.utils;

import it.jar.mulino.model.Mossa;
import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.domain.State;

import static it.jar.mulino.utils.PositionConverter.byte2string;
import static it.jar.mulino.utils.PositionConverter.string2byte;

/**
 * Created by ziro on 09/05/17.
 */
public class MossaConverter {

    public static Action convertMossa(State.Phase fase, Mossa mossa){
        switch (fase){
            case FIRST:
                Phase1Action resultFaseUno = new Phase1Action();
                resultFaseUno.setPutPosition(byte2string(mossa.getTo()));
                if(mossa.isRemoveMove()) resultFaseUno.setRemoveOpponentChecker(
                        byte2string(mossa.getRemove()));
                return resultFaseUno;
            case SECOND:
                Phase2Action resultFaseDue = new Phase2Action();
                resultFaseDue.setFrom(byte2string(mossa.getFrom()));
                resultFaseDue.setTo(byte2string(mossa.getTo()));
                if(mossa.isRemoveMove()) resultFaseDue.
                        setRemoveOpponentChecker(byte2string(mossa.getRemove()));
                return resultFaseDue;
            case FINAL:
                PhaseFinalAction resultFinal = new PhaseFinalAction();
                resultFinal.setFrom(byte2string(mossa.getFrom()));
                resultFinal.setTo(byte2string(mossa.getTo()));
                if(mossa.isRemoveMove()) resultFinal.
                        setRemoveOpponentChecker(byte2string(mossa.getRemove()));
                return resultFinal;
        }
        throw new IllegalStateException("Fase sconosciuta");
    }

}
