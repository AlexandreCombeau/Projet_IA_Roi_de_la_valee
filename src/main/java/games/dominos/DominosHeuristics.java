package games.dominos;

import iialib.games.algs.IHeuristic;

public class DominosHeuristics {
	//Nombre d'emplacement amis disponible - Nombre d'emplacement ennemies disponible
	public static IHeuristic<DominosBoard,DominosRole>  hVertical = (board,role) -> {
        /* TODO */
		int NbPossibilitesAmis = board.nbVerticalMoves();
		int NbpossibilitesEnnemies = board.nbHorizontalMoves();
		if(NbPossibilitesAmis == 0)
			return IHeuristic.MIN_VALUE; //On a perdu
		if(NbpossibilitesEnnemies==0)
			return IHeuristic.MAX_VALUE; //On a gagner
		return NbPossibilitesAmis - NbpossibilitesEnnemies;
    };
    

	public static IHeuristic<DominosBoard,DominosRole> hHorizontal = (board,role) -> {
		int NbPossibilitesAmis = board.nbHorizontalMoves();
		int NbpossibilitesEnnemies = board.nbVerticalMoves();
		if(NbPossibilitesAmis == 0)
			return IHeuristic.MIN_VALUE; //On a perdu
		if(NbpossibilitesEnnemies==0)
			return IHeuristic.MAX_VALUE; //On a gagner
		return NbPossibilitesAmis - NbpossibilitesEnnemies;
	};
   
}
	