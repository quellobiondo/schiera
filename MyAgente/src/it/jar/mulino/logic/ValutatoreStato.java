package it.jar.mulino.logic;

import it.jar.mulino.model.Stato;

import static it.jar.mulino.utils.NineMensMorrisSetting.*;

/**
 * Created by ziro on 22/05/17.
 */
public class ValutatoreStato {

    private static Stato stato;

    public static int valutaStato(Stato stato){
        ValutatoreStato.stato = stato;

        if (stato.hasWon(stato.currentPlayer)) { // Se vinco e' la mossa migliore
            return maxEvaluateValue();
        } else if (stato.hasWon(stato.opponentPlayer)) { // Se perdo e' la mossa peggiore
            return -maxEvaluateValue();
        } else if (stato.isADraw()) {
            return 0;
        }

        if (!stato.phase1completed()) { // Fase 1
            return  24 * (stato.count[stato.currentPlayer] - stato.count[stato.opponentPlayer] - (stato.played[stato.currentPlayer] - stato.played[stato.opponentPlayer])) +
                    3 * (stato.numberOfPiecesBlocked(stato.opponentPlayer) - stato.numberOfPiecesBlocked(stato.currentPlayer)) +
                    18 * (numberOfPotential3PiecesConfiguration(stato.currentPlayer) - numberOfPotential3PiecesConfiguration(stato.opponentPlayer)) +
                    - 10 * numberOfMorrises(stato.opponentPlayer) +
                    10 * (stato.numberOf2PiecesConfiguration(stato.currentPlayer) - stato.numberOf2PiecesConfiguration(stato.opponentPlayer));
                    //7 * (numberOf3PiecesConfiguration(stato.currentPlayer) - numberOf3PiecesConfiguration(stato.opponentPlayer)) +
                    //20 *(numberOfHypotheticallyMoves(stato.currentPlayer) - numberOfHypotheticallyMoves(stato.opponentPlayer));
        }  else if (stato.count[PLAYER_B] > 3 && stato.count[PLAYER_W] > 3) { // Fase 2
            return  43 * (stato.count[stato.currentPlayer] - stato.count[stato.opponentPlayer]) +
                    10 * (stato.numberOfPiecesBlocked(stato.opponentPlayer) - stato.numberOfPiecesBlocked(stato.currentPlayer)) +
                    8 * (numberOfUnblockableMorrises(stato.currentPlayer) - numberOfUnblockableMorrises(stato.opponentPlayer)) +
                    11 * (numberOfMorrises(stato.currentPlayer) - numberOfMorrises(stato.opponentPlayer)) +
                    8 * (numberOfDoubleMorrises(stato.currentPlayer) - numberOfDoubleMorrises(stato.opponentPlayer)) +
                    (stato.numberOfReachablePositions(stato.currentPlayer) - stato.numberOfReachablePositions(stato.opponentPlayer));
        } else { // Fase 3
            return  43 * (4 + stato.count[stato.currentPlayer] - stato.count[stato.opponentPlayer] * 2) +
                    10 * (stato.numberOf2PiecesConfiguration(stato.currentPlayer) - stato.numberOf2PiecesConfiguration(stato.opponentPlayer)) +
                    (numberOf3PiecesConfiguration(stato.currentPlayer) - numberOf3PiecesConfiguration(stato.opponentPlayer)) +
                    (stato.numberOfReachablePositions(stato.currentPlayer) - stato.numberOfReachablePositions(stato.opponentPlayer));
        }
    }

    private static int maxEvaluateValue() {
        return Integer.MAX_VALUE;
    }


    public static int numberOfImpossibleMorrises() {
        int blackBoard = stato.board[PLAYER_B];
        int whiteBoard = stato.board[PLAYER_W];
        int numberOfImpossibleMorrises = 0;
        for (int mill : ALL_MILLS) {
            if ((whiteBoard & mill) != 0 && (blackBoard & mill) != 0) {
                numberOfImpossibleMorrises++;
            }
        }

        return numberOfImpossibleMorrises;
    }

    public static int numberOfHypotheticallyMoves(byte player) {
        byte position = 0;
        int count = stato.count[player];
        int board = stato.board[player];
        int numberOfHypotheticallyMoves = 0;

        while (count > 0) {
            if (((board >>> position) & 1) == 1) {
                count--;
                numberOfHypotheticallyMoves += MOVES[position].length;
            }
            position++;
        }

        return numberOfHypotheticallyMoves;
    }

    public static int numberOfUnblockableMorrises(byte player) {
        int board = stato.board[player];
        int opponentBoard = stato.board[1 - player];
        int numberOfUnblockableMorrises = 0;

        for (int mill : ALL_MILLS) {
            if ((opponentBoard & mill) == 0 && Integer.bitCount((board & mill)) == 2) {
                for (byte pos = 0; pos < 24; pos++) {
                    if (((mill >>> pos) & 1) == 1 && ((board >>> pos) & 1) == 0) {
                        byte countPlayer = 0;
                        byte countOpponent = 0;
                        for (byte adjacentPosition : MOVES[pos]) {
                            if ((mill & adjacentPosition) == 0 && ((board >>> adjacentPosition) & 1) == 1) {
                                countPlayer++;
                            } else if (((opponentBoard >>> adjacentPosition) & 1) == 1) {
                                countOpponent++;
                                break;
                            }
                        }
                        if (countPlayer >= 2 && countOpponent == 0) {
                            numberOfUnblockableMorrises++;
                        }
                        break;
                    }
                }
            }
        }

        return numberOfUnblockableMorrises;
    }

    public static int numberOfMorrises(byte player) {
        int board = stato.board[player];
        int numberOfMorrises = 0;

        for (int mill :  ALL_MILLS) {
            if ((board & mill) == mill) {
                numberOfMorrises++;
            }
        }

        return numberOfMorrises;
    }

    public static int numberOfDoubleMorrises(byte player) {
        int board = stato.board[player];
        int totDoubleMorris = 0;
        for (byte pos = 0; pos < BOARD_SIZE; pos++) {
            int doubleMill = 0;
            for (int mill : MILLS[pos]) {
                doubleMill |= mill;
            }
            if ((board & doubleMill) == doubleMill) {
                totDoubleMorris++;
            }
        }

        return totDoubleMorris;
    }

    public static int numberOf3PiecesConfiguration(byte player) {
        int board = stato.board[player];
        int opponentBoard = stato.board[1 - player];
        int tot3piecesConfiguration = 0;
        for (byte pos = 0; pos < BOARD_SIZE; pos++) {
            if (((board >>> pos) & 1) == 1) {
                boolean possibileConfiguration = true;
                for (int mill : MILLS[pos]) {
                    int pieces = board & mill;
                    int opponentPieces = opponentBoard & mill;
                    if (opponentPieces != 0 || pieces == mill || pieces == (1 << pos)) {
                        possibileConfiguration = false;
                        break;
                    }
                }
                if (possibileConfiguration) {
                    tot3piecesConfiguration++;
                }
            }
        }

        return tot3piecesConfiguration;
    }

    public static int numberOfPotential3PiecesConfiguration(byte player) {
        int board = stato.board[player];
        int opponentBoard = stato.board[1 - player];
        int totPotential3piecesConfiguration = 0;
        for (byte pos = 0; pos < BOARD_SIZE; pos++) {
            if (((board >>> pos) & 1) == 1) {
                for (int mill : MILLS[pos]) {
                    if ((opponentBoard & mill) == 0 && (board & mill) == (1 << pos)) {
                        int count = 0;
                        for (byte i = 0; i < BOARD_SIZE; i++) {
                            if (i == pos) {
                                continue;
                            }

                            if (((mill >>> i) & 1) == 1) {
                                count++;

                                for (int otherMill : MILLS[i]) {
                                    if (otherMill == mill) {
                                        continue;
                                    }

                                    if ((opponentBoard & otherMill) == 0 && Integer.bitCount((board & otherMill)) == 1) {
                                        totPotential3piecesConfiguration++;
                                    }
                                }
                            }

                            if (count == 2) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        return totPotential3piecesConfiguration / 2;
    }

}
