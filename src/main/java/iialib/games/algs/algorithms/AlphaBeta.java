package iialib.games.algs.algorithms;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	/** Defaut value for depth limit */
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
	
	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h) {
		this.playerMaxRole = playerMaxRole;
		this.playerMinRole = playerMinRole;
		this.h = h;
	}

	//
	public AlphaBeta(Role playerMaxRole, Role playerMinRole, IHeuristic<Board, Role> h, int depthMax) {
		this(playerMaxRole, playerMinRole, h);
		this.depthMax = depthMax;
	}
		
	@Override
	public Move bestMove(Board board, Role playerRole) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private AlphaBetaReturn alphabeta(Board board,Role role, int depth,int alpha, int beta) {
		return null;
	}
	
	private class AlphaBetaReturn{
		int heuristicReturn;
		Move moveReturn;
		AlphaBetaReturn(int h, Move m) {
			this.heuristicReturn = h;
			this.moveReturn = m;
		}
	}

}
