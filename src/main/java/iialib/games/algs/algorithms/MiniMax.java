package iialib.games.algs.algorithms;

import games.dominos.DominosHeuristics;
import games.dominos.DominosRole;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class MiniMax<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	/** Defaut value for depth limit 
     */
	private final static int DEPTH_MAX_DEFAUT = 4;

	// Attributes
	/** Role of the max player 
     */
	private final Role playerMaxRole;

	/** Role of the min player 
     */
	private final Role playerMinRole;

	/** Algorithm max depth
     */
	private int depthMax = DEPTH_MAX_DEFAUT;

	
	/** Heuristic used by the max player 
     */
	private IHeuristic<Board, Role> h;

	//
	/** number of internal visited (developed) nodes (for stats)
     */
	private int nbNodes;
	
	/** number of leaves nodes nodes (for stats)

     */
	private int nbLeaves;
	
	private Move bestMove;

	// --------- Constructors ---------

	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	//
	public MiniMax(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}

	/*
	 * IAlgo METHODS =============
	 */

	@Override
	public Move bestMove(Board board, Role playerRole) {
		System.out.println("[MiniMax]");
		if(playerRole == playerMinRole) {
			minimax(board,playerRole,0);
		}else {
			maximin(board, playerRole, 0);
		}
		
		// TODO
		return bestMove;
	}

	/*
	 * PUBLIC METHODS ==============
	 */

	public String toString() {
		return "MiniMax(ProfMax=" + depthMax + ")";
	}

	/*
	 * PRIVATE METHODS ===============
	 */
	private int maximin(Board board,Role role, int depth) {
		if(depth> depthMax || board.isGameOver()) {
			return h.eval(board, role);
		}else {
			int max = Integer.MIN_VALUE;
			for(Move m : board.possibleMoves(role)) {
				int value = minimax(board.play(m, role), playerMinRole,depth+1);
				if(max<value) {
					max = value;
					bestMove = m;
				}				
			}
			return max;
		}
	}
	
	private int minimax(Board board,Role role, int depth) {
		if(depth> depthMax || board.isGameOver()) {
			return h.eval(board, role);
		}else {
			int min = Integer.MAX_VALUE;
			for(Move m : board.possibleMoves(role)) {
				int value = maximin(board.play(m, role), playerMaxRole, depth+1);
				if(min>value) {
					min = value;
					bestMove = m;
				}	
			}
			return min;
		}
	}
}
