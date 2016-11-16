package eWorld.datatypes.data;

import java.io.Serializable;
import java.util.Date;

import eWorld.datatypes.evs.EvAbstract;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

@SuppressWarnings("serial")
public abstract class EvDataType <EV extends EvAbstract, IDENT extends EvIdentifier> extends IdDataType<IDENT> implements Serializable {

	// attributes

	private EV ev;
	
	/** specifies the user who suggested the entry or null if not specified */
	private UserIdentifier author = null;
	
	/** specifies the date when the item was written, null if not specified */
	private Date authorTime = null;
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvDataType() {
	}
	
	/**
	 * primarily used by client
	 * @param ev
	 * @param identifier
	 */
	public EvDataType(EV ev, IDENT identifier) {
		this(ev, identifier, null, null);
	}
	
	/**
	 * primarily used by server
	 * @param ev
	 * @param identifier
	 * @param author the id of the user who suggested the item, can be null
	 * @param authorTime
	 */
	public EvDataType(EV ev, IDENT identifier, UserIdentifier author, Date authorTime) {
		super(identifier);
		
		assert null != ev;
		
		this.ev = ev;
		this.author = author;
		this.authorTime = authorTime;
	}
	
	
	// methods

	public EV getEv() {
		return ev;
	}
	
	public UserIdentifier getAuthor() {
		return author;
	}
	
	public void setAuthor(UserIdentifier author) {
		this.author = author;
	}
	
	public Date getAuthorTime() {
		return authorTime;
	}
	
}
