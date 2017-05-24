package it.jar.mulino.model;

import java.io.*;
import java.util.*;
import static it.jar.mulino.utils.NineMensMorrisSetting.*;
import static it.jar.mulino.utils.PositionConverter.*;

/**
 * Created by ziro on 09/05/17.
 */
public class Stato implements Serializable, Comparable<Stato> {
	private static final long serialVersionUID=1;
	public final int[] board=new int[2];
	public final byte[] played=new byte[2];
	public final byte[] count=new byte[2];
	public byte currentPlayer;
	public byte opponentPlayer;
	protected List<Mossa> movesHistory=new Vector<>();

	public Stato(){
		super();
		this.board[PLAYER_W]=0;
		this.board[PLAYER_B]=0;
		this.played[PLAYER_W]=0;
		this.played[PLAYER_B]=0;
		this.count[PLAYER_W]=0;
		this.count[PLAYER_B]=0;
		this.currentPlayer=PLAYER_W;
		this.opponentPlayer=PLAYER_B;
	}

    private Stato(byte currentPlayer, byte opponentPlayer, byte[] played, byte[] count, int[]board) {
        this.currentPlayer = currentPlayer;
        this.opponentPlayer = opponentPlayer;
        this.played[PLAYER_W] = played[PLAYER_W];
        this.played[PLAYER_B] = played[PLAYER_B];
        this.count[PLAYER_W]=count[PLAYER_W];
        this.count[PLAYER_B]=count[PLAYER_B];
        this.board[PLAYER_W]=board[PLAYER_W];
        this.board[PLAYER_B]=board[PLAYER_B];
    }

    public Stato copia(){
        return new Stato(currentPlayer, opponentPlayer, played, count, board);
    }

    public void setPlayed(byte white, byte black){
		this.played[PLAYER_W]=white;
		this.played[PLAYER_B]=black;
	}
	public void setCount(byte white, byte black){
		this.count[PLAYER_W]=white;
		this.count[PLAYER_B]=black;
	}
	/**chiamare con shift*/
	public void setGridPosition(byte player, int i){
		if (player==FREE){
			this.board[PLAYER_W]&=~i;
			this.board[PLAYER_B]&=~i;
		} else{
			this.board[player]|=i;
		}
	}
	public boolean isOver(){
		return this.hasWon(PLAYER_W)||this.hasWon(PLAYER_B)||this.isADraw();
	}

	public boolean willWin(Mossa mossa){
		int board=this.board[currentPlayer]|(1<<mossa.getTo());
		if (mossa.getFrom()!=Byte.MAX_VALUE){
			board&=~(1<<mossa.getFrom());
		}
		//ho vinto se:
		//- è stata completata la fase uno e:
		//    - ho fatto un mulino e il mio avversario ha tre pezzi
		//    - ho bloccato tutte le pedine del mio avversario
		return this.phase1completed()&&((this.isMill(board, mossa.getTo()) && count[opponentPlayer] == 3) ||
				this.numberOfPiecesBlocked(opponentPlayer) == count[opponentPlayer]);
	}

	public boolean hasWon(byte player){
		return this.phase1completed()&&(this.count[1-player]<3|| // L'avversario ha meno di 3 pezzi
				(this.currentPlayer==1-player&&this.numberOfPiecesBlocked((byte)(1-player))==this.count[1-player])); // L'avversario non puo' muoversi
	}
	public boolean isADraw(){
		return this.movesHistory.size()>12&&this.movesHistory.get(0).equals(this.movesHistory.get(4))&&this.movesHistory.get(0).equals(this.movesHistory.get(8))&&this.movesHistory.get(1).equals(this.movesHistory.get(5))&&this.movesHistory.get(1).equals(this.movesHistory.get(9))&&this.movesHistory.get(2).equals(this.movesHistory.get(6))&&this.movesHistory.get(2).equals(this.movesHistory.get(10))&&this.movesHistory.get(3).equals(this.movesHistory.get(7))
				&&this.movesHistory.get(3).equals(this.movesHistory.get(11));
	}
	public boolean phase1completed(){
		return (this.played[PLAYER_B]+this.played[PLAYER_W])==PIECES*2;
	}
	private boolean phase2completed(){
		return this.phase1completed()&&(this.count[PLAYER_W]==3||this.count[PLAYER_B]==3);
	}
	public boolean shouldAvoidRepetitions(){
		return this.phase1completed();
	}
	public void makeMove(Mossa move){
		this.movesHistory.add(0,move);
		this.setGridPosition(this.currentPlayer,(1<<move.getTo()));
		if (move.isPutMove()){
			this.played[this.currentPlayer]++;
			this.count[this.currentPlayer]++;
		} else{
			this.setGridPosition(FREE,(1<<move.getFrom()));
		}
		if (move.isRemoveMove()){
			this.count[this.opponentPlayer]--;
			this.setGridPosition(FREE,(1<<move.getRemove()));
		}
		this.next();
	}
	public void unmakeMove(Mossa move){
		this.movesHistory.remove(0);
		this.previous();
		this.setGridPosition(FREE,(1<<move.getTo()));
		if (move.isPutMove()){
			this.played[this.currentPlayer]--;
			this.count[this.currentPlayer]--;
		} else{
			this.setGridPosition(this.currentPlayer,(1<<move.getFrom()));
		}
		if (move.isRemoveMove()){
			this.count[this.opponentPlayer]++;
			this.setGridPosition(this.opponentPlayer,(1<<move.getRemove()));
		}
	}

	/**
	 * Ottiene le mosse possibili da parte del giocatore corrente
	 * (quindi quello che ha attualmente il turno)
	 * @return
	 */
	public List<Mossa> getPossibleMoves(){
		List<Mossa> moves=new ArrayList<>(3*(BOARD_SIZE-this.count[PLAYER_W]-this.count[PLAYER_B]));
		List<Mossa> capturesMoves=new ArrayList<>(24);
		if (this.played[this.currentPlayer]<PIECES){ // Fase 1
			for (byte to=0;to<BOARD_SIZE;to++){
				this.addMoves(moves,capturesMoves,to);
			}
		} else if (this.count[this.currentPlayer]==3){ // Fase 3
			for (byte from=0;from<BOARD_SIZE;from++){
				if (((this.board[this.currentPlayer]>>>from)&1)==1){
					for (byte to=0;to<BOARD_SIZE;to++){
						this.addMoves(moves,capturesMoves,from,to);
					}
				}
			}
		} else{ // Fase 2
			for (byte from=0;from<BOARD_SIZE;from++){
				if (((this.board[this.currentPlayer]>>>from)&1)==1){
					for (byte to : MOVES[from]){
						this.addMoves(moves,capturesMoves,from,to);
					}
				}
			}
		}
		moves.addAll(0,capturesMoves);
		return moves;
	}
	private void addMoves(List<Mossa> moves, List<Mossa> capturesMoves, byte to){
		this.addMoves(moves,capturesMoves,Byte.MAX_VALUE,to);
	}
	private void addMoves(List<Mossa> moves, List<Mossa> capturesMoves, byte from, byte to){
		int completeBoard=this.board[PLAYER_W]|this.board[PLAYER_B];
		if (((completeBoard>>>to)&1)==0){
			if (this.willMill(this.currentPlayer,from,to)){
				boolean onlyMills=this.onlyMills(this.opponentPlayer);
				int opponentBoard=this.board[this.opponentPlayer];
				for (byte remove=0;remove<BOARD_SIZE;remove++){
					if (((opponentBoard>>>remove)&1)==1&&(onlyMills||!this.isMill(opponentBoard,remove))){
						capturesMoves.add(0,new Mossa(this.currentPlayer,from,to,remove));
					}
				}
			} else{
				moves.add(new Mossa(this.currentPlayer,from,to));
			}
		}
	}
	private boolean willMill(byte player, byte from, byte to){
		int board=this.board[player]|(1<<to);
		if (from!=Byte.MAX_VALUE){
			board&=~(1<<from);
		}
		return this.isMill(board,to);
	}
	private boolean isMill(int board, byte pos){
		for (int mill : MILLS[pos]){
			if ((board&mill)==mill){
				return true;
			}
		}
		return false;
	}
	private boolean onlyMills(byte player){
		int board=this.board[player];
		for (byte i=0;i<BOARD_SIZE;i++){
			if (((board>>>i)&1)==1&&!this.isMill(board,i)){
				return false;
			}
		}
		return this.count[player]>0;
	}
	public int numberOf2PiecesConfiguration(byte player){
		int board=this.board[player];
		int opponentBoard=this.board[1-player];
		int tot2piecesConfiguration=0;
		for (int mill : ALL_MILLS){
			if ((opponentBoard&mill)==0&&Integer.bitCount((board&mill))==2){
				tot2piecesConfiguration++;
			}
		}
		return tot2piecesConfiguration;
	}
/*	public List<Mossa> getPossibleQuiescenceMoves(){
		if (this.phase1completed()&&!this.phase2completed()){
			byte currentPlayer=this.currentPlayer;
			byte opponentPlayer=this.opponentPlayer;
			int reachablePositions=this.numberOfReachablePositions(currentPlayer);
			int opponentReachablePositions=this.numberOfReachablePositions(opponentPlayer);
			List<Mossa> moves=this.getPossibleMoves();
			if (reachablePositions>opponentReachablePositions){
				List<Mossa> filteredMoves=new ArrayList<>();
				for (Mossa move : moves){
					this.makeMove(move);
					if (move.isRemoveMove()||this.numberOfReachablePositions(opponentPlayer)<opponentReachablePositions){
						filteredMoves.add(move);
					}
					this.unmakeMove(move);
				}
				return filteredMoves;
			}
			return moves;
		}
		return getPossibleMoves();
	}*/
	public int numberOfPiecesBlocked(byte player){
		if (this.phase1completed()&&this.count[player]==3){
			return 0;
		}
		int emptyBoard=this.board[PLAYER_W]|this.board[PLAYER_B];
		int totBlocked=0;
		for (byte i=0;i<BOARD_SIZE;i++){
			if (((this.board[player]>>>i)&1)==1&&pieceIsBlocked(emptyBoard,i)){
				totBlocked++;
			}
		}
		return totBlocked;
	}
	public boolean pieceIsBlocked(int board, byte piece){
		int moves=0;
		for (byte move : MOVES[piece]){
			moves|=(1<<move);
		}
		return (board&moves)==moves;
	}
	/*public boolean isQuiet(){
		if (!this.phase1completed()){ // Fase 1
			boolean lastMoveBlockedMill=false;
			int opponentBoard=this.board[this.currentPlayer];
			for (int mill : MILLS[this.movesHistory.get(0).getTo()]){
				if (Integer.bitCount((opponentBoard&mill))==2){
					lastMoveBlockedMill=true;
					break;
				}
			}
			return !lastMoveBlockedMill||this.numberOf2PiecesConfiguration(this.currentPlayer)==0;
		} else if (!this.phase2completed()){ // Fase 2
			int reachablePositions=this.numberOfReachablePositions(this.currentPlayer);
			int opponentReachablePositions=this.numberOfReachablePositions(this.opponentPlayer);
			int emptyPositions=(BOARD_SIZE-this.count[PLAYER_W]-this.count[PLAYER_B])/2;
			int blockedPieces=this.numberOfPiecesBlocked(this.currentPlayer);
			int opponentBlockedPieces=this.numberOfPiecesBlocked(this.opponentPlayer);
			return Math.min(this.count[this.currentPlayer]-blockedPieces,this.count[this.opponentPlayer]-opponentBlockedPieces)!=1||(Math.min(reachablePositions,opponentReachablePositions)>=emptyPositions&&Math.max(reachablePositions,opponentReachablePositions)>=emptyPositions);
		} else{ // Fase 3
			return true;
		}
	}*/

	public int numberOfReachablePositions(byte player){
		if (this.phase1completed()&&this.count[player]==3){
			return BOARD_SIZE-(this.count[PLAYER_W]+this.count[PLAYER_B]);
		}
		int reachableMap=0;
		for (byte pos=0;pos<BOARD_SIZE;pos++){
			if (((reachableMap>>>pos)&1)==0){
				int map=this.mapReachablePositions(player,pos,0);
				if (((map>>>(BOARD_SIZE+1))&1)==1){
					reachableMap|=map;
				}
			}
		}
		reachableMap&=0xFFFFFF;
		return Integer.bitCount(reachableMap);
	}
	public int mapReachablePositions(byte player, byte position, int mappedPositions){
		int completeBoard=this.board[PLAYER_W]|this.board[PLAYER_B];
		int playerBoard=this.board[player];
		if (((completeBoard>>>position)&1)==1){
			return mappedPositions;
		}
		mappedPositions|=(1<<position);
		for (byte move : MOVES[position]){
			if (((playerBoard>>>move)&1)==1){
				mappedPositions|=(1<<(BOARD_SIZE+1));
			}
			if (((completeBoard>>>move)&1)==0&&((mappedPositions>>>move)&1)==0){
				mappedPositions|=mapReachablePositions(player,move,mappedPositions);
			}
		}
		return mappedPositions;
	}

	/**
	 * Inversione dei giocatori (current player -> opponentPlayer)
	 */
	public void next(){
		this.currentPlayer=(byte)(1-this.currentPlayer);
		this.opponentPlayer=(byte)(1-this.opponentPlayer);
	}
	public void previous(){
		this.currentPlayer=(byte)(1-this.currentPlayer);
		this.opponentPlayer=(byte)(1-this.opponentPlayer);
	}
	public long getTransposition(){
		long whiteBoard=this.board[PLAYER_W];
		long blackBoard=this.board[PLAYER_B];
		long phase=(this.phase1completed()?(this.count[PLAYER_W]==3||this.count[PLAYER_B]==3?3:2):1);
		byte maxValue=-1;
		int maxPattern=0;
		for (byte i=0;i<4;i++){
			byte value=(byte)((whiteBoard>>>(6*i))&0b00111111);
			if (value>maxValue){
				maxValue=value;
				maxPattern=(1<<i);
			} else if (value==maxValue){
				maxPattern|=(1<<i);
			}
		}
		if ((maxPattern&0b0011)==0b0010){
			whiteBoard=rotateBoard90(whiteBoard);
			blackBoard=rotateBoard90(blackBoard);
		} else if ((maxPattern&0b0110)==0b0100){
			whiteBoard=rotateBoard180(whiteBoard);
			blackBoard=rotateBoard180(blackBoard);
		} else if ((maxPattern&0b1100)==0b1000){
			whiteBoard=rotateBoard270(whiteBoard);
			blackBoard=rotateBoard270(blackBoard);
		}
		long hash=this.currentPlayer;
		hash|=(phase<<1);
		hash|=(whiteBoard<<3); // [0..23] white board
		hash|=(blackBoard<<27); // [24..47] black board
		return hash;
	}

	private long rotateBoard90(long board){
		long first=(board&0b111111);
		return ((board>>>6)|(first<<18));
	}
	private long rotateBoard180(long board){
		long first=(board&0b111111111111);
		return ((board>>>12)|(first<<12));
	}
	private long rotateBoard270(long board){
		long first=(board&0b111111111111111111);
		return ((board>>>18)|(first<<6));
	}
	private final StringBuilder sb=new StringBuilder();
	@Override
	public String toString(){
		if (sb.length()>0)
			sb.delete(0,sb.length());
		sb.append("7 ").append(this.playerString(A7)).append("--------").append(this.playerString(D7)).append("--------").append(this.playerString(G7)).append("\n");
		sb.append("6 |  ").append(this.playerString(B6)).append("-----").append(this.playerString(D6)).append("-----").append(this.playerString(F6)).append("  |\n");
		sb.append("5 |  |  ").append(this.playerString(C5)).append("--").append(this.playerString(D5)).append("--").append(this.playerString(E5)).append("  |  |\n");
		sb.append("4 ").append(this.playerString(A4)).append("--").append(this.playerString(B4)).append("--").append(this.playerString(C4)).append("     ").append(this.playerString(E4)).append("--").append(this.playerString(F4)).append("--").append(this.playerString(G4)).append("\n");
		sb.append("3 |  |  ").append(this.playerString(C3)).append("--").append(this.playerString(D3)).append("--").append(this.playerString(E3)).append("  |  |\n");
		sb.append("2 |  ").append(this.playerString(B2)).append("-----").append(this.playerString(D2)).append("-----").append(this.playerString(F2)).append("  |\n");
		sb.append("1 ").append(this.playerString(A1)).append("--------").append(this.playerString(D1)).append("--------").append(this.playerString(G1)).append("\n");
		sb.append("  a  b  c  d  e  f  g\n");
		sb.append("White Played Checkers: ").append(this.played[PLAYER_W]).append(";\n");
		sb.append("Black Played Checkers: ").append(this.played[PLAYER_B]).append(";\n");
		sb.append("White Checkers On Board: ").append(this.count[PLAYER_W]).append(";\n");
		sb.append("Black Checkers On Board: ").append(this.count[PLAYER_B]).append(";\n");
		sb.append("Current player: ").append(this.currentPlayer==PLAYER_W?"W":"B").append(";\n");
		sb.append("Opponent player: ").append(this.opponentPlayer==PLAYER_W?"W":"B").append(";\n");
		sb.append("White player won: ").append(this.hasWon(PLAYER_W)).append(";\n");
		sb.append("Black player won: ").append(this.hasWon(PLAYER_B)).append(";\n");
		sb.append("Is draw: ").append(this.isADraw()).append(";\n");
		sb.append("Is over: ").append(this.isOver()).append(";\n");
		sb.append("Phase 1 completed: ").append(this.phase1completed()).append(";\n");
		sb.append("Phase 2 completed: ").append(this.phase2completed()).append(";\n");
		sb.append("\n");
		/*
		 * sb.append("Number of morrises player (W): " + this.numberOfMorrises(PLAYER_W) + "\n"); sb.append("Number of morrises player (B): " + this.numberOfMorrises(PLAYER_B) + "\n");
		 * sb.append("Number of double morrises player (W): " + this.numberOfDoubleMorrises(PLAYER_W) + "\n"); sb.append("Number of double morrises player (B): " +
		 * this.numberOfDoubleMorrises(PLAYER_B) + "\n"); sb.append("Number of pieces blocked player (W): " + this.numberOfPiecesBlocked(PLAYER_W) + "\n");
		 * sb.append("Number of pieces blocked player (B): " + this.numberOfPiecesBlocked(PLAYER_B )+ "\n"); sb.append("Number of 2 pieces configurations player (W): " +
		 * this.numberOf2PiecesConfiguration(PLAYER_W) + "\n"); sb.append("Number of 2 pieces configurations player (B): " + this.numberOf2PiecesConfiguration(PLAYER_B) + "\n");
		 * sb.append("Number of 3 pieces configurations player (W): " + this.numberOf3PiecesConfiguration(PLAYER_W) + "\n"); sb.append("Number of 3 pieces configurations player (B): " +
		 * this.numberOf3PiecesConfiguration(PLAYER_B) + "\n"); sb.append("Number of potential 3 pieces configurations player (W): " + this.numberOfPotential3PiecesConfiguration(PLAYER_W) + "\n");
		 * sb.append("Number of potential 3 pieces configurations player (B): " + this.numberOfPotential3PiecesConfiguration(PLAYER_B) + "\n"); sb.append("Number of reachable positions player (W): " +
		 * this.numberOfReachablePositions(PLAYER_W) + "\n"); sb.append("Number of reachable positions player (B): " + this.numberOfReachablePositions(PLAYER_B) + "\n");
		 * sb.append("Number of unblockable morrises player (W): " + this.numberOfUnblockableMorrises(PLAYER_W) + "\n"); sb.append("Number of unblockable morrises player (B): " +
		 * this.numberOfUnblockableMorrises(PLAYER_B) + "\n"); sb.append("Number of hypothetically moves player (W): " + this.numberOfHypotheticallyMoves(PLAYER_W) + "\n");
		 * sb.append("Number of hypothetically moves player (B): " + this.numberOfHypotheticallyMoves(PLAYER_B) + "\n"); sb.append("\n");
		 */
		sb.append("Moves history: ");
		for (Mossa move : movesHistory.toArray(new Mossa[0])){
			sb.append(move.toStringMove()+", ");
		}
		sb.append("\n");
		return sb.toString();
	}
	private char playerString(byte i){
		if (((this.board[PLAYER_B]>>>i)&1)==1){
			return 'B';
		} else if (((this.board[PLAYER_W]>>>i)&1)==1){
			return 'W';
		} else{
			return 'O';
		}
	}
	@Override
	public boolean equals(Object o){
		if (this==o)
			return true;
		if (o==null||getClass()!=o.getClass())
			return false;
		Stato that=(Stato)o;
		if (this.currentPlayer!=that.currentPlayer)
			return false;
		if (this.opponentPlayer!=that.opponentPlayer)
			return false;
		return Arrays.equals(this.board,that.board)&&Arrays.equals(this.played,that.played)&&Arrays.equals(this.count,that.count);
	}
	@Override
	public int hashCode(){
		int result=Arrays.hashCode(this.board);
		result=31*result+Arrays.hashCode(this.played);
		result=31*result+Arrays.hashCode(this.count);
		result=31*result+this.currentPlayer;
		result=31*result+this.opponentPlayer;
		return result;
	}
	@Override
	public int compareTo(Stato o){
		return 0;
	}
}
