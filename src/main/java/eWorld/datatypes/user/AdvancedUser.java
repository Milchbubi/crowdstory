package eWorld.datatypes.user;

import java.io.Serializable;

import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class AdvancedUser extends User<UserIdentifier> implements Serializable {

	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public AdvancedUser() {
	}
	
	public AdvancedUser(UserIdentifier identifier, String pseudonym, int votes) {
		super(identifier, pseudonym, votes);
		
	}
	
	
	// methods
	
	public User<UserIdentifier> constructUser() {
		return new User<UserIdentifier>(getIdentifier(), getPseudonym(), getVotes());
	}
}
