package games.LVR;


import java.util.ArrayList;


import iialib.games.algs.AIPlayer;
import iialib.games.algs.AbstractGame;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.AlphaBeta;
import iialib.games.algs.algorithms.MiniMax;

public class LVRGame extends AbstractGame<LVRMove, LVRRole, LVRBoard> {

	public LVRGame(ArrayList<AIPlayer<LVRMove, LVRRole, LVRBoard>> players, LVRBoard initialBoard) {
		super(players, initialBoard);
	}

	public static void main(String[] args) {

		LVRRole roleHaut = LVRRole.BLANC;
		LVRRole roleBas = LVRRole.NOIR;

		GameAlgorithm<LVRMove, LVRRole, LVRBoard> algV = new AlphaBeta<LVRMove, LVRRole, LVRBoard>(
				roleHaut, roleBas, LVRHeuristics.hBlanc, 2); // AlphaBeta depth 4

		GameAlgorithm<LVRMove, LVRRole, LVRBoard> algH = new AlphaBeta<LVRMove, LVRRole, LVRBoard>(
				roleBas, roleHaut, LVRHeuristics.hNoir, 4); // AlphaBeta depth 2

		AIPlayer<LVRMove, LVRRole, LVRBoard> playerV = new AIPlayer<LVRMove, LVRRole, LVRBoard>(
				roleHaut, algV);

		AIPlayer<LVRMove, LVRRole, LVRBoard> playerH = new AIPlayer<LVRMove, LVRRole, LVRBoard>(
				roleBas, algH);

		ArrayList<AIPlayer<LVRMove, LVRRole, LVRBoard>> players = new ArrayList<AIPlayer<LVRMove, LVRRole, LVRBoard>>();

		players.add(playerV); // First Player
		players.add(playerH); // Second Player

		// Setting the initial Board
		LVRBoard initialBoard = new LVRBoard();

		LVRGame game = new LVRGame(players, initialBoard);
		game.runGame();
	}
	
}
