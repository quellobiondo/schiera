package it.jar.mulino.utils;

import it.jar.mulino.model.Stato;
import it.unibo.ai.didattica.mulino.domain.State;
import static it.jar.mulino.utils.PositionConverter.string2byte;
import static it.jar.mulino.utils.NineMensMorrisSetting.PIECES;

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
                    result.setGridPosition(NineMensMorrisSetting.PLAYER_W,string2byte(position));
                    break;
                    case BLACK:
                    result.setGridPosition(NineMensMorrisSetting.PLAYER_B,string2byte(position));
                    break;
                }
            }
        );
        return result;
    }
}
