package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserIdentifier extends AbstractUserIdentifier implements Serializable {

	// attributes

	private long userId;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public UserIdentifier() {
	}
	
	public UserIdentifier(final long userId) {
		assert 0 < userId;
		
		this.userId = userId;
	}
	
	
	// methods

	public long getUserId() {
		return userId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null != obj && obj instanceof UserIdentifier && ((UserIdentifier)obj).userId == userId) {
			return true;
		} else {
			return false;
		}
	}
}
