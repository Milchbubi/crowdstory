package eWorld.database.impl.stories;

import java.util.ArrayList;

import com.datastax.driver.core.ResultSet;

import eWorld.database.impl.BooleanSchemaGate;
import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.elementars.UpDownVotes;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class StorySchemaGate extends BooleanSchemaGate<
		SentenceIdentifier,
		EntryClassIdentifier,
		SentenceShortIdentifier,
		Long,
		EvSentence<Ev, SentenceIdentifier>,
		EvSentence<?, EntryClassIdentifier>,
		EvSentence<Ev, SentenceShortIdentifier>,
		EvStory
	> {

	// attributes
	
	private final StoryDataSchema dataSchema;
	private final StoryCounterSchema counterSchema;
	private final StoryVoteSchema voteSchema;
	
	
	// constructors
	
	public StorySchemaGate(
			StoryDataSchema dataSchema,
			StoryCounterSchema counterSchema,
			StoryVoteSchema voteSchema) {
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
	public void updateRating(SentenceIdentifier identifier) {
		assert null != identifier;
		
		// get position of sentence
		long position = dataSchema.selectOne(identifier).getPosition();
		
		// calculate the new rating
		UpDownVotes votes = counterSchema.selectOne(identifier);
		float newRating = votes.getUpVotes() - votes.getDownVotes();
		
		// update rating
		dataSchema.updateRating(identifier, position, newRating);
		
	}
	
	/**
	 * 
	 * @param userIdentifier identifies the user whose votes should be loaded or null if user is not signed in
	 * @param superIdentifier specifies the items that should be loaded
	 * @param fromPos
	 * @param toPos
	 * @return
	 * 
	 * TODO? check if superIdentifier is valid?
	 */
	public EvStory listItemsBounded(UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier, long fromPos, long toPos) {
		assert null != superIdentifier;
		
		ResultSet dataSet = dataSchema.listItemsBounded(superIdentifier, fromPos, toPos);
		
		ResultSet votesSet = null;
		if (null != userIdentifier) {
			votesSet = voteSchema.listVotes(new VoteIdentifier<EntryClassIdentifier>(userIdentifier, superIdentifier));
		}
		
		return constructEvDataTypeContainer(superIdentifier, joinResultSets(dataSet, votesSet));
	}
	
	
	// overridden methods

	@Override
	protected EvStory constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<EvSentence<Ev, SentenceShortIdentifier>> data) {
		return new EvStory(superIdentifier, data);
	}
	
}
