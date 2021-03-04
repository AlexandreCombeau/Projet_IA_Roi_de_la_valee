package games.LVR;

import iialib.games.model.IRole;

public enum LVRRole implements IRole {
	BLANC, 	// For the player playing its tiles ally
	NOIR;		// For the player playing its tiles vertically
	
	public LVRRole inverse() {
		if(this == BLANC)
			return NOIR;
		else
			return BLANC;
	}
}
