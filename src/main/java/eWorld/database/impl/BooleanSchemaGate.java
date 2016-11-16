package eWorld.database.impl;

import eWorld.datatypes.containers.EvDataTypeContainer;
import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;
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
public abstract class BooleanSchemaGate<
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends EvDataType<Ev, COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends EvDataType<?, SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends EvDataType<Ev, SHORT_IDENT>,
		DATA_TYPE_CONTAINER extends EvDataTypeContainer<SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_SHORT_IDENTIFIED>
	> extends EvSchemaGate<
		COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, 
		DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED, DATA_TYPE_CONTAINER,
		Boolean
	> {

			
	// attributes
	
	private final DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema;
	private final UpDownCounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema;
	private final BooleanVoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT> voteSchema;
	
	
	// constructors
	
	public BooleanSchemaGate(
			DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema, 
			UpDownCounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema, 
			BooleanVoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT> voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}
	
	
	// methods
	
	public void vote(VoteIdentifier<COMPLETE_IDENT> identifier, boolean vote, int votes) throws EvRequestException {
		assert null != identifier;

		COMPLETE_IDENT eIdent = identifier.getEvIdent();
		
		// ensure that entry does exist
		if (!dataSchema.exists(eIdent)) {
			throw new EvRequestException("item does not exist");
		}
		
		UpDownVote voteBefore = voteSchema.getBooleanVote(identifier);
		if (UpDownVote.OPEN == voteBefore) {
			// user has not yet voted for the item
			voteSchema.vote(identifier, vote);
			if (true == vote) {
				counterSchema.upVote(eIdent, votes);
			} else if (false == vote) {
				counterSchema.downVote(eIdent, votes);
			}
		} else if ((UpDownVote.UP == voteBefore && false == vote) || (UpDownVote.DOWN == voteBefore && true == vote)) {
			// user has already voted for the item and wants to make another choice
			voteSchema.vote(identifier, vote);
			if (false == vote) { // true == voteBefore
				counterSchema.upVote(eIdent, -votes);
				counterSchema.downVote(eIdent, votes);
			} else if (true == vote) { // false == voteBefore
				counterSchema.upVote(eIdent, votes);
				counterSchema.downVote(eIdent, -votes);
			}
		}	// else: userVote is redundant => do nothing
	}
	
	
	// overridden methods
	
	@Override
	protected void setVoteToItem(DATA_SHORT_IDENTIFIED item, Boolean vote) {
		if (null == vote) {
			item.getEv().setVote(UpDownVote.OPEN);
		} else if (true == vote) {
			item.getEv().setVote(UpDownVote.UP);
		} else {
			item.getEv().setVote(UpDownVote.DOWN);
		}
	}
	
	
	// abstract methods
	
}
