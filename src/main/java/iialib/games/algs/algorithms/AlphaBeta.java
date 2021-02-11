package iialib.games.algs.algorithms;

import java.util.ArrayList;

import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.IHeuristic;
import iialib.games.model.IBoard;
import iialib.games.model.IMove;
import iialib.games.model.IRole;

public class AlphaBeta<Move extends IMove,Role extends IRole,Board extends IBoard<Move,Role,Board>> implements GameAlgorithm<Move,Role,Board> {

	// Constants
	/** Defaut value for depth limit */
	private final static int DEPTH_MAX_DEFAUT = 3;

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
		Move bestMove = maxMin(board, playerRole, 0,  IHeuristic.MIN_VALUE, IHeuristic.MAX_VALUE).moveReturn;
		System.out.println("nbNodes = " + nbNodes + "\t nbLeaves = " + nbLeaves);
		return bestMove;
	}

	
	private AlphaBetaReturn maxMin(Board board, Role role, int depth, int alpha, int beta) {
		if( depth> depthMax ||board.isGameOver()) {
			nbLeaves++;
			return new AlphaBetaReturn(h.eval(board, role),null);
		}else {
			ArrayList<Move> possibleMoves = board.possibleMoves(role);
			Move bestMove = possibleMoves.get(0);
			
			for(Move m : possibleMoves) {
				if(depth < depthMax) nbNodes++;
				
				int value = minMax(board.play(m,role), playerMinRole, depth+1, alpha, beta).heuristicReturn;
				if(alpha<value) {
					alpha = value;
					bestMove = m;
				}
				if(alpha>=beta) {
					return new AlphaBetaReturn(beta, bestMove);
				}
			}
			return new AlphaBetaReturn(alpha, bestMove);
		}
	}
	
	private AlphaBetaReturn minMax(Board board, Role role, int depth, int alpha, int beta) {
		if( depth> depthMax || board.isGameOver()) {
			nbLeaves++;
			return new AlphaBetaReturn(h.eval(board, role),null);
		}else {
			ArrayList<Move> possibleMoves = board.possibleMoves(role);
			Move bestMove = possibleMoves.get(0);
			
			for(Move m : possibleMoves) {
				if(depth < depthMax) nbNodes++;
				
				int value = maxMin(board.play(m, role), playerMaxRole, depth+1, alpha, beta).heuristicReturn;
				if(beta>value) {
					beta = value;
					bestMove = m;
				}
				if(alpha>=beta) {
					return new AlphaBetaReturn(alpha, bestMove);
				}
			}
			return new AlphaBetaReturn(beta, bestMove);
		}
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
