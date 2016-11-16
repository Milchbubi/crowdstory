package eWorld.datatypes.packages;

import java.io.Serializable;

import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.containers.EvStory;

@SuppressWarnings("serial")
public class RootPackage implements Serializable {

	// attributes
	
	/** the number of registered users */
	private int registeredUsers;
	
	/** the number of entries */
	private int entries;
	
	private EvStory eStory;
	
	private EvCommentContainer eCommentContainer;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public RootPackage() {
	}
	
	public RootPackage(int registeredUsers, int entries, EvStory eStory, EvCommentContainer eCommentContainer) {
		this.registeredUsers = registeredUsers;
		this.entries = entries;
		this.eStory = eStory;
		this.eCommentContainer =eCommentContainer;
	}
	
	
	// methods
	
	public int getRegisteredUsers() {
		return registeredUsers;
	}
	
	public int getEntries() {
		return entries;
	}
	
	public EvStory getStory() {
		return eStory;
	}
	
	public EvCommentContainer getCommentContainer() {
		return eCommentContainer;
	}
	
}
