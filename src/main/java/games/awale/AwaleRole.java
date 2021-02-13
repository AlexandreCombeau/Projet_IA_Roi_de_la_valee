package games.awale;

import iialib.games.model.IRole;

public enum AwaleRole implements IRole {
	HAUT, 	// For the player playing its tiles ally
	BAS;		// For the player playing its tiles vertically
	
	public AwaleRole inverse() {
		if(this == HAUT)
			return BAS;
		else
			return HAUT;
	}
}
