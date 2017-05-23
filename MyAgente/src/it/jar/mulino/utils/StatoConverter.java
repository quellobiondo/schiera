package it.jar.mulino.utils;

import static it.jar.mulino.utils.NineMensMorrisSetting.*;
import static it.jar.mulino.utils.PositionConverter.*;
import it.jar.mulino.model.*;
import it.unibo.ai.didattica.mulino.domain.*;

/**
 * Created by ziro on 09/05/17.
 */
public class StatoConverter {

    public static Stato convertState(State stato){
        Stato result = new Stato();

        result.setCount((byte)stato.getWhiteCheckersOnBoard(), (byte)stato.getBlackCheckersOnBoard());
        result.setPlayed((byte)(PIECES - stato.getWhiteCheckers()), (byte)(PIECES - stato.getBlackCheckers()));

        stato.getBoard().forEach((position, checker) -> {
                switch (checker){
                    case WHITE:
                    result.setGridPosition(NineMensMorrisSetting.PLAYER_W,1<<string2byte(position));
                    break;
                    case BLACK:
                    result.setGridPosition(NineMensMorrisSetting.PLAYER_B,1<<string2byte(position));
                    break;
                }
            }
        );
        return result;
    }
}
