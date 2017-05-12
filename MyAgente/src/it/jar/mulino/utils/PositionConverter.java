package it.jar.mulino.utils;

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


    public static byte convertPosition(String position){
        switch (position){
            case "a7": return 0;
            case "d6": return 1;
            case "g6": return 2;
            case "b6": return 3;
            case "d5": return 4;
            case "f5": return 5;
            case "c5": return 6;
            case "c6": return 7;
            case "c7": return 8;
            case "a4": return 9;
            case "b4": return 10;
            case "c4": return 11;
            case "e4": return 12;
            case "f4": return 13;
            case "g4": return 14;
            case "c3": return 15;
            case "d3": return 16;
            case "e3": return 17;
            case "b2": return 18;
            case "d2": return 19;
            case "f2": return 20;
            case "a1": return 21;
            case "d1": return 22;
            case "g1": return 23;
        }
        throw new IllegalStateException("Posizione non appartenente alla schiera "+position);
    }

    /**
     *  @param position
     * @return
     */

    public static String convertPosition(byte position){
        switch (position){
            case 0: return "a7";
            case 1: return "d6";
            case 2: return "g6";
            case 3: return "b6";
            case 4: return "d5";
            case 5: return "f5";
            case 6: return "c5";
            case 7: return "c6";
            case 8: return "c7";
            case 9: return "a4";
            case 10: return "b4";
            case 11: return "c4";
            case 12: return "e4";
            case 13: return "f4";
            case 14: return "g4";
            case 15: return "c3";
            case 16: return "d3";
            case 17: return "e3";
            case 18: return "b2";
            case 19: return "d2";
            case 20: return "f2";
            case 21: return "a1";
            case 22: return "d1";
            case 23: return "g1";
        }
        throw new IllegalStateException("Posizione non appartenente alla schiera "+position);
    }

}
