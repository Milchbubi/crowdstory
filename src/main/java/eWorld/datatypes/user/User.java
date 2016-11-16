package eWorld.datatypes.user;

import java.io.Serializable;

import eWorld.datatypes.identifiers.AbstractUserIdentifier;

@SuppressWarnings("serial")
public class User<IDENT extends AbstractUserIdentifier> implements Serializable {

	// attributes

	private IDENT identifier;
	
	private String pseudonym;
	
	private int votes;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public User() {
	}
	
	public User(IDENT identifier, String pseudonym, int votes) {
		assert null != identifier;
		assert null != pseudonym;
		assert !pseudonym.trim().equals("");
		
		this.identifier = identifier;
		this.pseudonym = pseudonym;
		this.votes = votes;
	}
	
	
	// methods

	public IDENT getIdentifier() {
		return identifier;
	}
	
	public String getPseudonym() {
		return pseudonym;
	}
	
	public int getVotes() {
		return votes;
	}
	
}
