package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class VoteIdentifier<EV_IDENT extends EvCompleteIdentifier> implements EvIdentifier, Serializable{

	// attributes
	
	private UserIdentifier userIdent;
	
	private EV_IDENT eIdent;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) TODO not needed for this class, move class to other package*/
	public VoteIdentifier() {
	}
	
	public VoteIdentifier(UserIdentifier userIdent, EV_IDENT eIdent) {
		assert null != userIdent;
		assert null != eIdent;
		
		this.userIdent = userIdent;
		this.eIdent = eIdent;
	}
	
	
	// methods
	
	public UserIdentifier getUserIdent() {
		return userIdent;
	}
	
	public EV_IDENT getEvIdent() {
		return eIdent;
	}
}
