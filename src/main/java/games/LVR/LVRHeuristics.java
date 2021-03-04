package games.LVR;


import iialib.games.algs.IHeuristic;

public class LVRHeuristics {
	
		public static IHeuristic<LVRBoard,LVRRole>  hBlanc = (board,role) -> {
			return 0;
	    };
	    

		public static IHeuristic<LVRBoard,LVRRole> hNoir = (board,role) -> {
			return 0;
		};
}
