package it.jar.mulino.utils;

import it.jar.mulino.model.Stato;

/**
 *
 * internal schema
 * 0 ----- 1 ----- 2
 * |       |       |
 * | 3 --- 4 --- 5 |
 * | |     |     | |
 * | |  6--7--8  | |
 * | |  |     |  | |
 * 9-10-11   12-13-14
 * | |  |     |  | |
 * | |  15-16-17 | |
 * | |    |      | |
 * | 18---19--20 | |
 * |       |       |
 * 21-----22------23
 *
 * external schema
 *a7 ----- d6 -----g6
 * |       |       |
 * | b6 ---d5 ---f5|
 * | |     |     | |
 * | |  c5-d4-e5 | |
 * | |  |     |  | |
 *a4-b4-c4    e4-f4-g4
 * | |  |     |  | |
 * | |  c3-d3-e3 | |
 * | |     |     | |
 * | b2--- d2---f2 |
 * |       |       |
 * a1----- d1------g1
 *
 * Created by ziro on 09/05/17.
 */
public class PositionConverter {

   /* public static final byte A1 = 0;
    public static final byte B2 = 1;
    public static final byte C3 = 2;
    public static final byte D1 = 3;
    public static final byte D2 = 4;
    public static final byte D3 = 5;

    public static final byte G1 = 6;
    public static final byte F2 = 7;
    public static final byte E3 = 8;
    public static final byte G4 = 9;
    public static final byte F4 = 10;
    public static final byte E4 = 11;

    public static final byte G7 = 12;
    public static final byte F6 = 13;
    public static final byte E5 = 14;
    public static final byte D7 = 15;
    public static final byte D6 = 16;
    public static final byte D5 = 17;

    public static final byte A7 = 18;
    public static final byte B6 = 19;
    public static final byte C5 = 20;
    public static final byte A4 = 21;
    public static final byte B4 = 22;
    public static final byte C4 = 23;*/

    public static final byte A1 = 0;
    public static final byte B2 = 1;
    public static final byte C3 = 2;
    public static final byte D1 = 3;
    public static final byte D2 = 4;
    public static final byte D3 = 5;

    public static final byte G1 = 6;
    public static final byte F2 = 7;
    public static final byte E3 = 8;
    public static final byte G4 = 9;
    public static final byte F4 = 10;
    public static final byte E4 = 11;

    public static final byte G7 = 12;
    public static final byte F6 = 13;
    public static final byte E5 = 14;
    public static final byte D7 = 15;
    public static final byte D6 = 16;
    public static final byte D5 = 17;

    public static final byte A7 = 18;
    public static final byte B6 = 19;
    public static final byte C5 = 20;
    public static final byte A4 = 21;
    public static final byte B4 = 22;
    public static final byte C4 = 23;


    public static String byte2string(int bitPattern) {
        switch (bitPattern) {
            case A1: return "a1";
            case A4: return "a4";
            case A7: return "a7";
            case B2: return "b2";
            case B4: return "b4";
            case B6: return "b6";
            case C3: return "c3";
            case C4: return "c4";
            case C5: return "c5";
            case D1: return "d1";
            case D2: return "d2";
            case D3: return "d3";
            case D5: return "d5";
            case D6: return "d6";
            case D7: return "d7";
            case E3: return "e3";
            case E4: return "e4";
            case E5: return "e5";
            case F2: return "f2";
            case F4: return "f4";
            case F6: return "f6";
            case G1: return "g1";
            case G4: return "g4";
            case G7: return "g7";
            case Byte.MAX_VALUE: return "";
            default:
                throw new IllegalStateException("Posizione non appartenente alla schiera "+bitPattern);
        }
    }

    public static byte string2byte(String position) {
        switch (position.toLowerCase()) {
            case "a1": return A1;
            case "a4": return A4;
            case "a7": return A7;
            case "b2": return B2;
            case "b4": return B4;
            case "b6": return B6;
            case "c3": return C3;
            case "c4": return C4;
            case "c5": return C5;
            case "d1": return D1;
            case "d2": return D2;
            case "d3": return D3;
            case "d5": return D5;
            case "d6": return D6;
            case "d7": return D7;
            case "e3": return E3;
            case "e4": return E4;
            case "e5": return E5;
            case "f2": return F2;
            case "f4": return F4;
            case "f6": return F6;
            case "g1": return G1;
            case "g4": return G4;
            case "g7": return G7;
            default:
                throw new IllegalStateException("Posizione non appartenente alla schiera "+position);
        }
    }

}
