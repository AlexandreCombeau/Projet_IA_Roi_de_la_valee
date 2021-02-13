package games.awale;

import iialib.games.model.IMove;

public class AwaleMove implements IMove {
	
	public final int x;

    AwaleMove(int x){
        this.x = x;
    }

    @Override
    public String toString() {
        return "Move{" + x + "}";
    }
}
