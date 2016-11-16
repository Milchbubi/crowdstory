package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CommentShortIdentifier extends EvShortIdentifier<Long> implements Serializable {

	// attributes
	
	private long commentId;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public CommentShortIdentifier() {
	}
	
	public CommentShortIdentifier(long commentId) {
		this.commentId = commentId;
	}
	
	
	// methods
	
	@Override
	public Long getShortId() {
		return commentId;
	}

}
