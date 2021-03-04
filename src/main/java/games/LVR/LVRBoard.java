package games.LVR;

import java.util.ArrayList;
import java.util.Arrays;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class LVRBoard implements IBoard<LVRMove, LVRRole, LVRBoard> {

	// joueur HAUT : 0 - 5
	// joueur BAS :  6 - 11
			
	
				/* Attributs */
	
	//Sens de parcours des indices du tableau(plateau) en anti horaire
	private static ArrayList<Integer> parcourtAntiHoraire = new ArrayList(Arrays.asList(5, 4, 3, 2, 1, 0, 6, 7, 8, 9, 10, 11));
	
	//Sens de parcours des indices du tableau(plateau) en horaire
	private static ArrayList<Integer> parcourtHoraire = new ArrayList(Arrays.asList(0, 1, 2, 3, 4, 5, 11, 10, 9, 8, 7, 6));

	private Integer [] board;
	
	private Joueur joueurBlanc;
	private Joueur joueurNoir;
	
	private boolean plus_de_coups;
	
				/* Constructeur */

	public LVRBoard() {
		board = new Integer [12];
		
		joueurBlanc = new Joueur(LVRRole.BLANC, 0, new Paire<Integer,Integer>(0,5));
		joueurNoir = new Joueur(LVRRole.NOIR, 0, new Paire<Integer,Integer>(6,11));
		
		plus_de_coups = false;

		
		for(int i=0; i<12; i++) {
			board[i] = 4;
		}
			
	}



	
	/**
	 * On parcours les l'ensemble des cases appartenant au joueur et vérifie si on peut la jouer
	 */
	@Override
	public ArrayList<LVRMove> possibleMoves(LVRRole playerRole) {
		ArrayList<LVRMove> moves = new ArrayList<LVRMove>();
		Joueur joueur = this.getJoueur(playerRole);

		return moves;
	}


	/**
	 * On prend un move et un role et on renvoi le nouveau plateau apres le tour d'un joueur 
	 */
	@Override
	public LVRBoard play(LVRMove move, LVRRole playerRole) {
		
		LVRBoard new_board = this.copyAwaleBoard();

		return new_board;
	}
	
	

	/**
	 * Test de la validité d'un move par un joueur
	 */
	@Override
	public boolean isValidMove(LVRMove move, LVRRole playerRole) {
		return true;
	}

	// Detection de la fin de partie
	@Override
	public boolean isGameOver() {
		return false;
	}

	
	@Override
	public ArrayList<Score<LVRRole>> getScores() {
		ArrayList<Score<LVRRole>> scores = new ArrayList<Score<LVRRole>>();
		return scores;
	}

	/**
	 * Fonction de copie d'un AwaleBoard 
	 */
	public LVRBoard copyAwaleBoard() {
		LVRBoard board_copy = new LVRBoard();
		for (int i=0; i<board_copy.board.length; i++)
			board_copy.board[i] = this.board[i];
		board_copy.joueurBlanc = joueurBlanc.copyJoueur();
		board_copy.joueurNoir = joueurNoir.copyJoueur();
		board_copy.plus_de_coups = plus_de_coups;
		return board_copy;
		
	}
	


	/**
	 * Renvoie le nombre de graines dans le plateau à partir de l'indice index_debut à index_fin
	 */
	public int nombre_graines_plateau(int index_debut, int index_fin) {
		int tmp = 0;
		for (int i = index_debut; i <= index_fin; i++) {
			tmp += board[i];
		}
		return tmp;
	}

	public String toString() {
		String str = new String();
		for (int i = 0; i < 12; i++) {
			str += "[" + board[i] + "]";
			str += " ";
			if (i == 5)
				str += "\n";
		}
		
		str+="\n Grenier_HAUT = "+getGrenier(LVRRole.BLANC)+"\n Grenier_BAS = "+ getGrenier(LVRRole.NOIR)+"\n\n";
		return str;
	}
	
											/**  Classe paire **/

	//Sous classe servant à stocker les indices dans le tableau de chaque joueur
	private class Paire<D,F> {
		public D debut;
		public F fin;

		Paire(D debut, F fin) {
			this.debut = debut;
			this.fin = fin;
		}
	}
	
											/**  Classe joueur **/

	// Representation d'un joueur en compilant tous les variables à son sujet
	private class Joueur {
		public LVRRole role;
		public int grenier;
		Paire<Integer,Integer> index;

		Joueur(LVRRole role, int grenier, Paire index) {
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
	/**
	 * Fonction renvoie un joueur selon le role
	 */
	private Joueur getJoueur(LVRRole role) {
		if (role == LVRRole.BLANC)
			return joueurBlanc;
		else
			return joueurNoir;
	}


	/**
	 * Fonction renvoie le grenier d'un joueur d'un role
	 */
	public int getGrenier(LVRRole role) {
		return getJoueur(role).grenier;
	}
}
