package it.jar.mulino.logic;

import it.jar.mulino.model.MossaPosiziona;
import it.jar.mulino.model.Stato;

/**
 * Created by ziro on 09/05/17.
 */
public interface MossaChooser {

    MossaPosiziona scegliMossa(Stato stato);

}
