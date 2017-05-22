package it.jar.mulino.utils;

import it.jar.mulino.model.MossaPosiziona;
import it.jar.mulino.model.MossaSposta;
import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;

/**
 * Created by ziro on 09/05/17.
 */
public class MossaConverter {

    public static Action convertMossaPosiziona(MossaPosiziona mossa){
        Phase1Action result = new Phase1Action();
        result.setPutPosition(
                PositionConverter.convertPosition(
                        mossa.getPosizione()));
        if(mossa.getRimuovi() != -1){
            result.setRemoveOpponentChecker(
                    PositionConverter.convertPosition(
                            mossa.getRimuovi()));
        }
        return result;
    }

    public static Action convertMossaMuovi(MossaSposta mossa){
        Phase2Action result = new Phase2Action();
        result.setFrom(PositionConverter.convertPosition(mossa.getFrom()));
        result.setTo(PositionConverter.convertPosition(mossa.getTo()));
        if(mossa.getRimuovi() != -1){
            result.setRemoveOpponentChecker(
                    PositionConverter.convertPosition(
                            mossa.getRimuovi()));
        }
        return result;
    }

}
