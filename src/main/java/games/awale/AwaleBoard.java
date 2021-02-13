package games.awale;

import java.util.ArrayList;


import iialib.games.model.IBoard;
import iialib.games.model.Score;

public class AwaleBoard implements IBoard<AwaleMove, AwaleRole, AwaleBoard> {

	// joueur HAUT : 0 - 5
	// joueur BAS :  6 - 11
	
	public Integer [] board = new Integer [12];
	
	
	public int grenier_H;
	public int grenier_B;
	
	public boolean plus_de_coups = false;
	
	public AwaleBoard() {
		for(int i=0;i<12;i++) {
			board[i] = 4;
		}
			
	}
	
	public int nombre_graines_plateau(int index_debut, int index_fin) {
		int tmp=0;
		for(int i=index_debut;i<index_fin;i++) {
			tmp+=board[i];
		}
		return tmp;
	}
	
	@Override
	public ArrayList<AwaleMove> possibleMoves(AwaleRole playerRole) {
		//prend toutes les graines d'une des ses cases
		ArrayList<AwaleMove> moves = new ArrayList<AwaleMove>();
		if(AwaleRole.HAUT == playerRole) {
			for(int i=0;i<=5;i++) {
				AwaleMove new_move = new AwaleMove(i);
				if(this.isValidMove(new_move, playerRole))
					moves.add(new_move);

			}
		} else {
			for(int i=6;i<=11;i++) {
				AwaleMove new_move = new AwaleMove(i);
				if(this.isValidMove(new_move, playerRole))	
					moves.add(new_move);
			}
			
		}
		return moves;
	}

	@Override
	public AwaleBoard play(AwaleMove move, AwaleRole playerRole) {
		int nombre_mouvements;

		AwaleBoard new_board = new AwaleBoard();
		new_board.board = this.board.clone();
		new_board.grenier_H = this.grenier_H;
		new_board.grenier_B = this.grenier_B;
		
		nombre_mouvements = board[move.x];
		new_board.board[move.x] = 0;
		
		if(AwaleRole.HAUT == playerRole) {
			
			for(int i=move.x+1;i<move.x + nombre_mouvements;i++) {
				if(i%12 == move.x) {
					nombre_mouvements++;
				}
				else {
					new_board.board[i%12]+= 1;
				}
				
			}
	
			AwaleBoard board_copy = new AwaleBoard();
			board_copy.board = new_board.board.clone();
			board_copy.grenier_H = new_board.grenier_H;
			board_copy.grenier_B = new_board.grenier_B;
			
			int coord_arrive = (move.x+nombre_mouvements) %12;
			while((coord_arrive > 5) && (new_board.board[coord_arrive] == 2 || new_board.board[coord_arrive] == 3)) {
				new_board.grenier_H += new_board.board[coord_arrive];
				new_board.board[coord_arrive] = 0;
				coord_arrive = (coord_arrive -1) % 12;
			}
			int nb_graines_restante = 0;
			for(int i=6;i<12;i++) {
				nb_graines_restante += new_board.board[i];
			}
			if(nb_graines_restante == 0) {
				new_board = board_copy;
			}
			
			//possibleMoves(AwaleRole.BAS).size() == 0 && possibleMoves(AwaleRole.HAUT).size() == 0
		} else {
			
			for(int i=move.x+1;i<move.x + nombre_mouvements;i++) {
				if(i%12 == move.x) {
					nombre_mouvements++;
				}
				else {
					new_board.board[i%12]+= 1;
				}
				
			}
	
			AwaleBoard board_copy = new AwaleBoard();
			board_copy.board = new_board.board.clone();
			board_copy.grenier_H = new_board.grenier_H;
			board_copy.grenier_B = new_board.grenier_B;
			
			int coord_arrive = (move.x+nombre_mouvements) %12;

			while((coord_arrive < 6) && (new_board.board[coord_arrive] == 2 || new_board.board[coord_arrive] == 3)) {
				new_board.grenier_B += new_board.board[coord_arrive];
				new_board.board[coord_arrive] = 0;
				coord_arrive = (coord_arrive -1) % 12;
				if(coord_arrive == -1)
					coord_arrive = 11;
			}
			int nb_graines_restante = 0;
			for(int i=0;i<6;i++) {
				nb_graines_restante += new_board.board[i];
			}
			if(nb_graines_restante == 0) {
				new_board = board_copy;
			}
			
		}
		
		if(new_board.possibleMoves(playerRole.inverse()).size() == 0) {
			new_board.plus_de_coups = true;
		}
		
		return new_board;
	}

	@Override
	public boolean isValidMove(AwaleMove move, AwaleRole playerRole) {
		if(board[move.x] == 0) {
			return false;
		}
		if(playerRole == AwaleRole.HAUT) {
			if(move.x>=0 && move.x<6) {
				int nb_graines_restante_avant = 0;
				for(int i=6;i<12;i++) {
					nb_graines_restante_avant += this.board[i];
				}
				if(nb_graines_restante_avant == 0) {
					AwaleBoard board_valid = this.play(move, playerRole);
					int nb_graines_restante_apres = 0;
					for(int i=6;i<12;i++) {
						nb_graines_restante_apres += board_valid.board[i];
					}
					if(nb_graines_restante_apres>0) {
						return true;
					}
				}
				else {
					return true;
				}
			}	
			
		} else {
			if(move.x>=6 && move.x<12) {
				int nb_graines_restante_avant = 0;
				for(int i=0;i<6;i++) {
					nb_graines_restante_avant += this.board[i];
				}
				if(nb_graines_restante_avant == 0) {
					AwaleBoard board_valid = this.play(move, playerRole);
					int nb_graines_restante_apres = 0;
					for(int i=0;i<6;i++) {
						nb_graines_restante_apres += board_valid.board[i];
					}
					if(nb_graines_restante_apres>0) {
						return true;
					}
				}
				else {
					return true;
				}
			}
		}
		return false;
	}


	//
	@Override
	public boolean isGameOver() {
		return this.grenier_B >= 25 || this.grenier_H >= 25 || ( nombre_graines_plateau(0, 12) <= 6 ) || plus_de_coups;
	}

	@Override
	public ArrayList<Score<AwaleRole>> getScores() {
		ArrayList<Score<AwaleRole>> scores = new ArrayList<Score<AwaleRole>>();
		if(this.isGameOver()) {
			if (this.grenier_H < this.grenier_B) {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT,Score.Status.LOOSE,0));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS,Score.Status.WIN,1));
			}
			else if (this.grenier_H > this.grenier_B) {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT,Score.Status.WIN,1));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS,Score.Status.LOOSE,0));
			}
			else {
				scores.add(new Score<AwaleRole>(AwaleRole.HAUT,Score.Status.TIE,0));
				scores.add(new Score<AwaleRole>(AwaleRole.BAS,Score.Status.TIE,0));
			}
		}
		else {
			
		}
		return scores;
	}

	public String toString() {
		String str = new String();
		for(int i=0;i<12;i++) {
			str+="["+board[i]+"]";
			str+=" ";
			if(i==5)
				str+="\n";
		}
		return str;
	}
}
