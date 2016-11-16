package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;

@SuppressWarnings("serial")
public class EvCommentContainer extends EvDataTypeContainer<
		EntryClassIdentifier,
		CommentShortIdentifier,
		Long,
		EvComment<Ev, CommentShortIdentifier>
	> implements Serializable  {
	
	// attributes
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public EvCommentContainer() {
	}
	
	public EvCommentContainer(
			EntryClassIdentifier containerIdentifier, 
			ArrayList<EvComment<Ev, CommentShortIdentifier>> data) {
		super(containerIdentifier, data);
	}
	
	
	// methods

}
