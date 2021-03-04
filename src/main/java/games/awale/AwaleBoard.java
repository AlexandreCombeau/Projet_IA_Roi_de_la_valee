package games.awale;

import java.util.ArrayList;
import java.util.Arrays;

import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class AwaleBoard implements IBoard<AwaleMove, AwaleRole, AwaleBoard> {

	// joueur HAUT : 0 - 5
	// joueur BAS :  6 - 11
			
	
				/* Attributs */
	
	//Sens de parcours des indices du tableau(plateau) en anti horaire
	private static ArrayList<Integer> parcourtAntiHoraire = new ArrayList(Arrays.asList(5, 4, 3, 2, 1, 0, 6, 7, 8, 9, 10, 11));
	
	//Sens de parcours des indices du tableau(plateau) en horaire
	private static ArrayList<Integer> parcourtHoraire = new ArrayList(Arrays.asList(0, 1, 2, 3, 4, 5, 11, 10, 9, 8, 7, 6));

	private Integer [] board;
	
	private Joueur joueur_HAUT;
	private Joueur joueur_BAS;
	
	private boolean plus_de_coups;
	
				/* Constructeur */

	public AwaleBoard() {
		board = new Integer [12];
		
		joueur_HAUT = new Joueur(AwaleRole.HAUT, 0, new Paire<Integer,Integer>(0,5));
		joueur_BAS = new Joueur(AwaleRole.BAS, 0, new Paire<Integer,Integer>(6,11));
		
		plus_de_coups = false;

		
		for(int i=0; i<12; i++) {
			board[i] = 4;
		}
			
	}



	
	/**
	 * On parcours les l'ensemble des cases appartenant au joueur et vérifie si on peut la jouer
	 */
	@Override
	public ArrayList<AwaleMove> possibleMoves(AwaleRole playerRole) {
		ArrayList<AwaleMove> moves = new ArrayList<AwaleMove>();
		Joueur joueur = this.getJoueur(playerRole);

		for(int i=joueur.index.debut; i<=joueur.index.fin; i++) {
			AwaleMove new_move = new AwaleMove(i);
			// si un move est valide alors on l'ajoute à la liste des moves 
			if(this.isValidMove(new_move, playerRole))
				moves.add(new_move);
		}
		return moves;
	}


	/**
	 * On prend un move et un role et on renvoi le nouveau plateau apres le tour d'un joueur 
	 */
	@Override
	public AwaleBoard play(AwaleMove move, AwaleRole playerRole) {
		
		AwaleBoard new_board = this.copyAwaleBoard();
		int nombre_graines = board[move.x];
		new_board.board[move.x] = 0;
		
		
		int n = 0;
		int idxList = parcourtAntiHoraire.indexOf(move.x);
		int idxBoard = move.x;
		
		
		//Tant qu'on a pas semé toutes les graines 	
		while (n < nombre_graines) {								
			idxBoard = parcourtAntiHoraire.get(idxList);	//On recupere l'indice du plateau 
			if (idxBoard != move.x) {						//Si ce n'est pas la case d'ou on a pris les graines
				new_board.board[idxBoard]+=1;				//On sème une graine
				n++;
			}	
			idxList = (idxList+1)%12;
		}
		
		//On regarde si on peut faire une capture 
		
			//On recupere les indices des bornes du joueur adverse
		int indexDebutEnnemi = new_board.getJoueur(playerRole.inverse()).index.debut;
		int indexFinEnnemi = new_board.getJoueur(playerRole.inverse()).index.fin;
		
			//On fait une copie dans le cas ou la capture ne peut etre faite
		AwaleBoard board_copy = new_board.copyAwaleBoard();	

		idxList = parcourtHoraire.indexOf(idxBoard);
			//Tant qu'on est dans une case du joueur adverse et qu'il a 2-3 graines
		while(idxBoard >= indexDebutEnnemi && idxBoard <= indexFinEnnemi
			&& (new_board.board[idxBoard] == 2 || new_board.board[idxBoard] == 3)) {
			
			new_board.getJoueur(playerRole).grenier += new_board.board[idxBoard];
			new_board.board[idxBoard] = 0;
			
			idxList = (idxList+1)%12;
			idxBoard = parcourtHoraire.get(idxList);
		}
		
			//Si après la capture le joueur adverse n'a plus de graines on annule le coup
		int nbGrainesRestantesEnnemi = new_board.nombre_graines_plateau(indexDebutEnnemi, indexFinEnnemi);
			
		if(nbGrainesRestantesEnnemi == 0) {
			new_board = board_copy.copyAwaleBoard();	
		}
			//Si après qu'on ait joué le joueur adverse ne peut joueur cela signifie une fin de partie
		if(new_board.possibleMoves(playerRole.inverse()).size() == 0) {
			new_board.plus_de_coups = true;
		}
		
		return new_board;
	}
	
	

	/**
	 * Test de la validité d'un move par un joueur
	 */
	@Override
	public boolean isValidMove(AwaleMove move, AwaleRole playerRole) {
		// on ne peut pas ramasser une case vide
		if(board[move.x] == 0) {
			return false;
		}

		// on doit jouer dans les bornes du plateau
		if(move.x>=this.getJoueur(playerRole).index.debut && move.x<=this.getJoueur(playerRole).index.fin) {
			int idxDebut = this.getJoueur(playerRole.inverse()).index.debut;
			int idxFin = this.getJoueur(playerRole.inverse()).index.fin;
			
			int nb_graines_restante_avant = this.nombre_graines_plateau(idxDebut, idxFin);

			if(nb_graines_restante_avant == 0) {
				AwaleBoard board_valid = this.play(move, playerRole);
				int nb_graines_restante_apres = board_valid.nombre_graines_plateau(idxDebut, idxFin);
				//Si notre coup ne capture pas toutes les graines de l'adversaire alors il est valide
				if(nb_graines_restante_apres>0) 
					return true;
			}
			else {
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

	/**
	 * Fonction de copie d'un AwaleBoard 
	 */
	public AwaleBoard copyAwaleBoard() {
		AwaleBoard board_copy = new AwaleBoard();
		for (int i=0; i<board_copy.board.length; i++)
			board_copy.board[i] = this.board[i];
		board_copy.joueur_HAUT = joueur_HAUT.copyJoueur();
		board_copy.joueur_BAS = joueur_BAS.copyJoueur();
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
		
		str+="\n Grenier_HAUT = "+getGrenier(AwaleRole.HAUT)+"\n Grenier_BAS = "+ getGrenier(AwaleRole.BAS)+"\n\n";
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
		public AwaleRole role;
		public int grenier;
		Paire<Integer,Integer> index;

		Joueur(AwaleRole role, int grenier, Paire index) {
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
	private Joueur getJoueur(AwaleRole role) {
		if (role == AwaleRole.HAUT)
			return joueur_HAUT;
		else
			return joueur_BAS;
	}


	/**
	 * Fonction renvoie le grenier d'un joueur d'un role
	 */
	public int getGrenier(AwaleRole role) {
		return getJoueur(role).grenier;
	}
}
