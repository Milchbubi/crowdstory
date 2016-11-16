package eWorld.datatypes.data;

import java.io.Serializable;

import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public class EvComment <EV extends EvAbstract, IDENT extends EvIdentifier> extends EvDataType<EV, IDENT> implements Serializable {

	public static final int MAX_COMMENT_LENGTH = 5000;
	
	// attributes
	
	private String authorPseudonym = null;
	
	private WoString comment;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvComment() {
	}
	
	/**
	 * 
	 * @param ev
	 * @param identifier
	 * @param author can be null
	 * @param authorPseudonym can be null
	 * @param comment
	 */
	public EvComment(EV ev, IDENT identifier, UserIdentifier author, String authorPseudonym, WoString comment) {
		super(ev, identifier, author, null);	// TODO add authorTime to arguments
		
		assert null != comment;
		
		this.authorPseudonym = authorPseudonym;
		this.comment = comment;
	}
	
	
	// methods
	
	public String getAuthorPseudonym() {
		return authorPseudonym;
	}
	
	public void setAuthorPseudonym(String authorPseudonym) {
		assert null != authorPseudonym;
		
		this.authorPseudonym = authorPseudonym;
	}
	
	public WoString getComment() {
		return comment;
	}
	
	public static <EV extends EvAbstract> EvComment<EV, CommentShortIdentifier> shortCast(EvComment<EV, CommentIdentifier> comment) {
		return new EvComment<EV, CommentShortIdentifier>(
				comment.getEv(), 
				comment.getIdentifier().getShortIdentifier(), 
				comment.getAuthor(), 
				comment.getAuthorPseudonym(),
				comment.getComment()
				);
	}
	
	/**
	 * checks if the attributes are valid
	 * @throws EvIllegalDataException if attributes are not valid
	 */
	public void check() throws EvIllegalDataException {
		if (MAX_COMMENT_LENGTH < comment.getString().length()) {
			throw new EvIllegalDataException("the maximum length of a comment is " + MAX_COMMENT_LENGTH);
		}
		if (1 > comment.getString().length()) {
			throw new EvIllegalDataException("a comment should have at least one character");
		}
	}
	
}
