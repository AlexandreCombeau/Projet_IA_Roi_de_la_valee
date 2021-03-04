package games.awale;


import java.util.ArrayList;


import iialib.games.algs.AIPlayer;
import iialib.games.algs.AbstractGame;
import iialib.games.algs.GameAlgorithm;
import iialib.games.algs.algorithms.AlphaBeta;
import iialib.games.algs.algorithms.MiniMax;

public class AwaleGame extends AbstractGame<AwaleMove, AwaleRole, AwaleBoard> {

	public AwaleGame(ArrayList<AIPlayer<AwaleMove, AwaleRole, AwaleBoard>> players, AwaleBoard initialBoard) {
		super(players, initialBoard);
	}

	public static void main(String[] args) {

		AwaleRole roleHaut = AwaleRole.HAUT;
		AwaleRole roleBas = AwaleRole.BAS;

		GameAlgorithm<AwaleMove, AwaleRole, AwaleBoard> algV = new AlphaBeta<AwaleMove, AwaleRole, AwaleBoard>(
				roleHaut, roleBas, AwaleHeuristics.hHaut, 2); // AlphaBeta depth 4

		GameAlgorithm<AwaleMove, AwaleRole, AwaleBoard> algH = new AlphaBeta<AwaleMove, AwaleRole, AwaleBoard>(
				roleBas, roleHaut, AwaleHeuristics.hBas, 4); // AlphaBeta depth 2

		AIPlayer<AwaleMove, AwaleRole, AwaleBoard> playerV = new AIPlayer<AwaleMove, AwaleRole, AwaleBoard>(
				roleHaut, algV);

		AIPlayer<AwaleMove, AwaleRole, AwaleBoard> playerH = new AIPlayer<AwaleMove, AwaleRole, AwaleBoard>(
				roleBas, algH);

		ArrayList<AIPlayer<AwaleMove, AwaleRole, AwaleBoard>> players = new ArrayList<AIPlayer<AwaleMove, AwaleRole, AwaleBoard>>();

		players.add(playerV); // First Player
		players.add(playerH); // Second Player

		// Setting the initial Board
		AwaleBoard initialBoard = new AwaleBoard();

		AwaleGame game = new AwaleGame(players, initialBoard);
		game.runGame();
	}
	
}
