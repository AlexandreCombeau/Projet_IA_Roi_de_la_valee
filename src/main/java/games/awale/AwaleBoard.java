package games.awale;

import java.util.ArrayList;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class AwaleBoard implements IBoard<AwaleMove, AwaleRole, AwaleBoard> {

	// joueur HAUT : 0 - 5
	// joueur BAS : 6 - 11

	public Integer[] board = new Integer[12];

	public Joueur joueur_HAUT = new Joueur(AwaleRole.HAUT, 0, new Pair(0, 5));
	public Joueur joueur_BAS = new Joueur(AwaleRole.BAS, 0, new Pair(6, 11));

	public boolean plus_de_coups = false;

	public AwaleBoard() {
		for (int i = 0; i <= 11; i++) {
			board[i] = 4;
		}

	}


	//Fonction de copie d'un AwaleBoard 
	public AwaleBoard cloneBoard() {
		AwaleBoard board_copy = new AwaleBoard();
		for (int i = 0; i < board_copy.board.length; i++)
			board_copy.board[i] = this.board[i];
		board_copy.joueur_HAUT = joueur_HAUT.copyJoueur();
		board_copy.joueur_BAS = joueur_BAS.copyJoueur();
		board_copy.plus_de_coups = plus_de_coups;
		return board_copy;

	}

	private Joueur getJoueur(AwaleRole role) {
		if (role == AwaleRole.HAUT)
			return joueur_HAUT;
		else
			return joueur_BAS;
	}

	public int getGrenier(AwaleRole role) {
		return getJoueur(role).grenier;
	}

	public int nombre_graines_plateau(int index_debut, int index_fin) {
		int tmp = 0;
		for (int i = index_debut; i <= index_fin; i++) {
			tmp += board[i];
		}
		return tmp;
	}

	/**
	 * On parcours les l'ensemble de cases appartenant au joueur et vérifie si on peut la jouer
	 */
	@Override
	public ArrayList<AwaleMove> possibleMoves(AwaleRole playerRole) {
		ArrayList<AwaleMove> moves = new ArrayList<AwaleMove>();
		Joueur joueur = this.getJoueur(playerRole);

		for (int i = joueur.index.debut; i <= joueur.index.fin; i++) {
			AwaleMove new_move = new AwaleMove(i);
			// si un move est valide alors on l'ajoute à la liste des moves 
			if (this.isValidMove(new_move, playerRole))
				moves.add(new_move);
		}
		return moves;
	}

	/**
	 * On prend un move et un role et on renvoi le nouveau plateau apres le tour d'un joueur 
	 */
	@Override
	public AwaleBoard play(AwaleMove move, AwaleRole playerRole) {
		// on crée une copie du board actuel
		AwaleBoard new_board = this.cloneBoard();
		// le nombre de mouvement correspond au nombre de grains dans la case qu'il ramasse  
		int nombre_mouvements = board[move.x];
		new_board.board[move.x] = 0;

		// égrainage  
		for (int i = move.x + 1; i < move.x + nombre_mouvements; i++) {
			if (i % 12 == move.x)
				nombre_mouvements++;
			else
				new_board.board[i % 12] += 1;
		}

		// copie du board copié afin de valider ou pas son coup si jamais il y a un cas de "famine"
		AwaleBoard board_copy = new_board.cloneBoard();
		//coordonnée d'arrivée calculer en ajoutant l'indice de la case ramasser et en ajoutant le nombre de graine qu'elle contenait
		int coord_arrive = (move.x + nombre_mouvements) % 12;
		// tant qu'on est chez l'enemie et qu'il y a 2-3 graines on continue de ramasser et ajouter à notre grenier
		while ((coord_arrive >= new_board.getJoueur(playerRole.inverse()).index.debut)
				&& (coord_arrive <= new_board.getJoueur(playerRole.inverse()).index.fin)
				&& (new_board.board[coord_arrive] == 2 || new_board.board[coord_arrive] == 3)) {

			new_board.getJoueur(playerRole).grenier += new_board.board[coord_arrive];
			new_board.board[coord_arrive] = 0;
			coord_arrive = (coord_arrive - 1) % 12;
		}
		int nb_graines_restante = new_board.nombre_graines_plateau(
				new_board.getJoueur(playerRole.inverse()).index.debut,
				new_board.getJoueur(playerRole.inverse()).index.fin);

		// test du cas de famine
		if (nb_graines_restante == 0)
			new_board = board_copy;

		// si l'adversaire à ne vas pas pouvoir jouer et donc notre victoire
		if (new_board.possibleMoves(playerRole.inverse()).size() == 0)
			new_board.plus_de_coups = true;

		return new_board;
	}

	/**
	 * Test de la validité d'un move par un joueur
	 */
	@Override
	public boolean isValidMove(AwaleMove move, AwaleRole playerRole) {
		// on ne peut pas ramasser une case vide
		if (board[move.x] == 0) {
			return false;
		}
		// on doit jouer dans les bornes du plateau
		if (move.x >= this.getJoueur(playerRole).index.debut && move.x <= this.getJoueur(playerRole).index.fin) {
			int nb_graines_restante_avant = this.nombre_graines_plateau(
					this.getJoueur(playerRole.inverse()).index.debut, this.getJoueur(playerRole.inverse()).index.fin);
			// TODO je comprend plus bien la regle
			if (nb_graines_restante_avant == 0) {
				AwaleBoard board_valid = this.play(move, playerRole);
				int nb_graines_restante_apres = board_valid.nombre_graines_plateau(
						board_valid.getJoueur(playerRole.inverse()).index.debut,
						board_valid.getJoueur(playerRole.inverse()).index.fin);
				;
				if (nb_graines_restante_apres > 0)
					return true;
			} else {
				return true;
			}
		}

		return false;
	}

	// Detection de la fin de partie
	@Override
	public boolean isGameOver() {
		return this.joueur_BAS.grenier >= 25 || this.joueur_HAUT.grenier >= 25 || (nombre_graines_plateau(0, 11) <= 6)
				|| plus_de_coups;
	}

	
	@Override
	public ArrayList<Score<AwaleRole>> getScores() {
		ArrayList<Score<AwaleRole>> scores = new ArrayList<Score<AwaleRole>>();
		if (this.isGameOver()) {
			if (this.joueur_HAUT.grenier < this.joueur_BAS.grenier) {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT, Score.Status.LOOSE, 0));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS, Score.Status.WIN, 1));
			} else if (this.joueur_HAUT.grenier > this.joueur_BAS.grenier) {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT, Score.Status.WIN, 1));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS, Score.Status.LOOSE, 0));
			} else {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT, Score.Status.TIE, 0));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS, Score.Status.TIE, 0));
			}
		} else {

		}
		return scores;
	}

	public String toString() {
		String str = new String();
		for (int i = 0; i < 12; i++) {
			str += "[" + board[i] + "]";
			str += " ";
			if (i == 5)
				str += "\n";
		}
		return str;
	}

	//Sous classe servant à stocker les indices dans le tableau de chaque joueur
	private class Pair {
		public int debut;
		public int fin;

		Pair(int debut, int fin) {
			this.debut = debut;
			this.fin = fin;
		}

	}

	// Representation d'un joueur en compilant tous les variables à son sujet
	private class Joueur {
		public AwaleRole role;
		public int grenier;
		public Pair index;

		Joueur(AwaleRole role, int grenier, Pair index) {
			this.role = role;
			this.grenier = grenier;
			this.index = index;
		}

		//copie d'un joueur
		public Joueur copyJoueur() {
			Joueur newJ = new Joueur(this.role, this.grenier, this.index);
			return newJ;
		}

	}
}
