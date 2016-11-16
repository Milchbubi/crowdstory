package eWorld.database.impl.comments;

import java.util.ArrayList;

import eWorld.database.impl.BooleanSchemaGate;
import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.elementars.UpDownVotes;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;

public class CommentSchemaGate extends BooleanSchemaGate<
		CommentIdentifier,
		EntryClassIdentifier,
		CommentShortIdentifier,
		Long,
		EvComment<Ev, CommentIdentifier>,
		EvComment<?, EntryClassIdentifier>,
		EvComment<Ev, CommentShortIdentifier>,
		EvCommentContainer
	> {
	
	// attributes
	
	private final CommentDataSchema dataSchema;
	private final CommentCounterSchema counterSchema;
	private final CommentVoteSchema voteSchema;
	
	
	// constructors

	public CommentSchemaGate(
			CommentDataSchema dataSchema,
			CommentCounterSchema counterSchema,
			CommentVoteSchema voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}

	
	// methods
	
	/**
	 * updates the rating of a row specified by the given identifier
	 * @param identifier
	 */
	public void updateRating(CommentIdentifier identifier) {
		assert null != identifier;
		
		// calculate the new rating
		UpDownVotes votes = counterSchema.selectOne(identifier);
		float newRating = votes.getUpVotes() - votes.getDownVotes();
		
		// update rating
		dataSchema.updateRating(identifier, newRating);
		
	}
	
	
	// overridden methods

	@Override
	protected EvCommentContainer constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<EvComment<Ev, CommentShortIdentifier>> data) {
		return new EvCommentContainer(superIdentifier, data);
	}
	
}