package eWorld.database.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.datastax.driver.core.Row;

import eWorld.datatypes.containers.EvDataTypeContainer;
import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.elementars.UpDownVotes;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

/**
 * 
 * @author michael
 *
 * @param <COMPLETE_IDENT> for the {@code EvSchema}s that are referenced by this class
 * @param <SUPER_IDENT> identifier that is contained by {@code DATA_TYPE_CONTAINER}
 * @param <SHORT_IDENT> identifier that identifies a {@code DATA_COMPLETE_IDENTIFIED}
 * @param <DATA_COMPLETE_IDENTIFIED> data that is contained by {@code DATA_TYPE_CONTAINER}
 * @param <DATA_TYPE_CONTAINER> EvDataTypeContainer that can be requested
 */
public abstract class RatedSchemaGate<
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends EvDataType<Ev, COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends EvDataType<?, SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends EvDataType<Ev, SHORT_IDENT>,
		DATA_TYPE_CONTAINER extends EvDataTypeContainer<SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_SHORT_IDENTIFIED>
	> extends BooleanSchemaGate<
		COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, 
		DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED, DATA_TYPE_CONTAINER
	> {

	
	// attributes
	
	private final RatedDataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema;
	private final UpDownCounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema;
	private final BooleanVoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT> voteSchema;
	
	
	// constructors
	
	public RatedSchemaGate(
			RatedDataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema, 
			UpDownCounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema, 
			BooleanVoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT> voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}
	
	
	// methods
	
	/**
	 * updates the rating of a row specified by the given identifier
	 * @param completeIdent
	 * @param shortIdent redundant to completeIdent, only to simplify generic stuff
	 */
	public void updateRating(COMPLETE_IDENT completeIdent, SHORT_IDENT shortIdent) {
		assert null != completeIdent;
		assert null != shortIdent;
		
		// calculate the new rating
		UpDownVotes votes = counterSchema.selectOne(completeIdent);
		float newRating = votes.getUpVotes() - votes.getDownVotes();
		
		// get the old rating
		DATA_COMPLETE_IDENTIFIED item = dataSchema.selectOne(completeIdent);
		float oldRating = item.getEv().getRating();
		
		// update if the rating has changed
		if (newRating != oldRating) {
			
			// insert new row
			dataSchema.insert(
					dataSchema.restrainEvDataTypeCompleteIdentifiedToSuperIdentified(item),
					shortIdent, 
					newRating, 
					item.getEv().getRank()
					);
			
			// delete old row
			dataSchema.deleteOne(completeIdent, oldRating);
			
			// check if inserted row exists now
			assert dataSchema.exists(completeIdent);
		}
	}
	
	
	// overridden methods
	
	
	// abstract methods
	
}
