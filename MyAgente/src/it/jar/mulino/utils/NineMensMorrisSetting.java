package it.jar.mulino.utils;

import static it.jar.mulino.utils.PositionConverter.*;

/**
 * Created by ziro on 23/05/17.
 */
public class NineMensMorrisSetting {
    public static final int PIECES = 9;
    public static final int BOARD_SIZE = 24;
    public static final byte PLAYER_W = 0;
    public static final byte PLAYER_B = 1;
    public static final byte FREE     = 2;

    public static final int MILL_1 =   (1 << A1) | (1 << D1) | (1 << G1);
    public static final int MILL_2 =   (1 << B2) | (1 << D2) | (1 << F2);
    public static final int MILL_3 =   (1 << C3) | (1 << D3) | (1 << E3);
    public static final int MILL_4_1 = (1 << A4) | (1 << B4) | (1 << C4);
    public static final int MILL_4_2 = (1 << E4) | (1 << F4) | (1 << G4);
    public static final int MILL_5 =   (1 << C5) | (1 << D5) | (1 << E5);
    public static final int MILL_6 =   (1 << B6) | (1 << D6) | (1 << F6);
    public static final int MILL_7 =   (1 << A7) | (1 << D7) | (1 << G7);
    public static final int MILL_A =   (1 << A1) | (1 << A4) | (1 << A7);
    public static final int MILL_B =   (1 << B2) | (1 << B4) | (1 << B6);
    public static final int MILL_C =   (1 << C3) | (1 << C4) | (1 << C5);
    public static final int MILL_D_1 = (1 << D1) | (1 << D2) | (1 << D3);
    public static final int MILL_D_2 = (1 << D5) | (1 << D6) | (1 << D7);
    public static final int MILL_E =   (1 << E3) | (1 << E4) | (1 << E5);
    public static final int MILL_F =   (1 << F2) | (1 << F4) | (1 << F6);
    public static final int MILL_G =   (1 << G1) | (1 << G4) | (1 << G7);

    public static final int[] ALL_MILLS = { MILL_1, MILL_2, MILL_3, MILL_4_1, MILL_4_2, MILL_5, MILL_6, MILL_7,
            MILL_A, MILL_B, MILL_C, MILL_D_1, MILL_D_2, MILL_E, MILL_F, MILL_G };
    public static final int[][] MILLS = {
            { MILL_1, MILL_A },   // A1
            { MILL_2, MILL_B },   // B2
            { MILL_3, MILL_C },   // C3
            { MILL_1, MILL_D_1 }, // D1
            { MILL_2, MILL_D_1 }, // D2
            { MILL_3, MILL_D_1 }, // D3
            { MILL_1, MILL_G },   // G1
            { MILL_2, MILL_F },   // F2
            { MILL_3, MILL_E },   // E3
            { MILL_4_2, MILL_G }, // G4
            { MILL_4_2, MILL_F }, // F4
            { MILL_4_2, MILL_E }, // E4
            { MILL_7, MILL_G },   // G7
            { MILL_6, MILL_F },   // F6
            { MILL_5, MILL_E },   // E5
            { MILL_7, MILL_D_2 }, // D7
            { MILL_6, MILL_D_2 }, // D6
            { MILL_5, MILL_D_2 }, // D5
            { MILL_7, MILL_A },   // A7
            { MILL_6, MILL_B },   // B6
            { MILL_5, MILL_C },   // C5
            { MILL_4_1, MILL_A }, // A4
            { MILL_4_1, MILL_B }, // B4
            { MILL_4_1, MILL_C }  // C4
    };
    public static final byte[][] MOVES = {
            { A4, D1 },         // A1
            { B4, D2 },         // B2
            { C4, D3 },         // C3
            { A1, D2, G1 },     // D1
            { D1, B2, D3, F2 }, // D2
            { C3, D2, E3 },     // D3
            { D1, G4 },         // G1
            { D2, F4 },         // F2
            { D3, E4 },         // E3
            { G1, F4, G7 },     // G4
            { F2, E4, F6, G4 }, // F4
            { E3, E5, F4 },     // E4
            { G4, D7 },         // G7
            { F4, D6 },         // F6
            { E4, D5 },         // E5
            { D6, A7, G7 },     // D7
            { D5, B6, D7, F6 }, // D6
            { C5, D6, E5 },     // D5
            { A4, D7 },         // A7
            { B4, D6 },         // B6
            { C4, D5 },         // C5
            { A1, A7, B4 },     // A4
            { B2, A4, B6, C4 }, // B4
            { C3, B4, C5 }      // C4
    };

}
