package eWorld.datatypes.identifiers;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CommentIdentifier extends EntryClassIdentifier implements Serializable {

	// attributes
	
	private CommentShortIdentifier commentShortIdent;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public CommentIdentifier() {
	}
	
	public CommentIdentifier(long entryId, long commentId) {
		super(entryId);
		
		this.commentShortIdent = new CommentShortIdentifier(commentId);
	}
	
	public CommentIdentifier(EntryClassIdentifier entryIdent, CommentShortIdentifier commentIdent) {
		super(entryIdent.getEntryClassId());
		
		assert null != commentIdent;
		
		this.commentShortIdent = commentIdent;
	}
	
	
	// methods
	
	public long getCommentId() {
		return commentShortIdent.getShortId();
	}
	
//	@Override
	public CommentShortIdentifier getShortIdentifier() {
		return commentShortIdent;
	}
	
}
