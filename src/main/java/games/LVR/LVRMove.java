package games.LVR;

import iialib.games.model.IMove;

public class LVRMove implements IMove {
	
	public enum Direction{
		HAUT,HAUT_DROITE,DROITE,BAS_DROITE,BAS,BAS_GAUCHE,GAUCHE,HAUT_GAUCHE
	}
	
	public final int lettrePion; //De 1 à 7 qui représente les cases de A à G
	public final int numPion;
	public final Direction direction;

    LVRMove(int numLettreCase, int numCase, Direction direction){
        this.lettrePion = numLettreCase;
        this.numPion = numCase;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Move{" + direction + "}";
    }
}
