package eWorld.database.impl;

import java.util.ArrayList;
import java.util.HashMap;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import eWorld.datatypes.containers.IdDataTypeContainer;
import eWorld.datatypes.data.IdDataType;
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
 * @param <SHORT_IDENT_COLUMN_TYPE>
 * @param <DATA_COMPLETE_IDENTIFIED> data that is contained by {@code DATA_TYPE_CONTAINER}
 * @param <DATA_SUPER_IDENTIFIED>
 * @param <DATA_SHORT_IDENTIFIED>
 * @param <DATA_TYPE_CONTAINER> EvDataTypeContainer that can be requested
 * @param <VOTE_TYPE> 
 */
public abstract class EvSchemaGate<
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends IdDataType<COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends IdDataType<SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends IdDataType<SHORT_IDENT>,
		DATA_TYPE_CONTAINER extends IdDataTypeContainer<SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_SHORT_IDENTIFIED>,
		VOTE_TYPE
	> {
	
	// attributes
	
	private final DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema;
	private final CounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema;
	private final VoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT, VOTE_TYPE> voteSchema;
	
	
	// constructors
	
	public EvSchemaGate(
			DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema, 
			CounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> counterSchema, 
			VoteSchema<VoteIdentifier<COMPLETE_IDENT>, VoteIdentifier<SUPER_IDENT>, SHORT_IDENT, COMPLETE_IDENT, SUPER_IDENT, VOTE_TYPE> voteSchema) {
		assert null != dataSchema;
		assert null != counterSchema;
		assert null != voteSchema;
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}
	
	public EvSchemaGate(
			DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> dataSchema 
			) {
		
		this.dataSchema = dataSchema;
		this.counterSchema = null;
		this.voteSchema = null;
	}
	
	
	// methods
	
	/**
	 * returns a {@code DATA_TYPE_CONTAINER} that lists items in ranked order
	 * @param userIdentifier identifies the user whose votes should be loaded or null if user is not signed in
	 * @param superIdentifier specifies the items that should be loaded
	 * @return a {@code DATA_TYPE_CONTAINER} that contains elements of the type {@code DATA_TYPE}
	 * 
	 * TODO? check if superIdentifier is valid?
	 */
	public DATA_TYPE_CONTAINER listItems(UserIdentifier userIdentifier, SUPER_IDENT superIdentifier) {
		assert null != superIdentifier;
		
		ResultSet dataSet = dataSchema.listItems(superIdentifier);
		
		ResultSet votesSet = null;
		if (null != userIdentifier) {
			votesSet = voteSchema.listVotes(new VoteIdentifier<SUPER_IDENT>(userIdentifier, superIdentifier));
		}
		
		return constructEvDataTypeContainer(superIdentifier, joinResultSets(dataSet, votesSet));
	}
	
	/**
	 * 
	 * @param dataSet ResultSet from dataSchema
	 * @param votesSet ResultSet from voteSchema, can be null
	 * @return
	 * 
	 * TODO the part "join into data" may be inefficient, implement that in a better way
	 */
	protected ArrayList<DATA_SHORT_IDENTIFIED> joinResultSets(ResultSet dataSet, ResultSet votesSet) {
		assert null != dataSet;
		
		ArrayList<DATA_SHORT_IDENTIFIED> data = new ArrayList<DATA_SHORT_IDENTIFIED>();
		for (Row row : dataSet) {
			data.add(dataSchema.constructEvDataTypeShortIdentified(row));
		}
		
		// join votes into data if user is signed in
		if (null != votesSet) {
			// user is signed in
			// get votes
			HashMap<Object, VOTE_TYPE> votes = new HashMap<Object, VOTE_TYPE>();	// type Object because type of shortIdentifierColumn can vary
			for (Row row : votesSet) {
				votes.put(
						getShortIdentifierOfRow(voteSchema.getShortIdentifierColumn(), row),
						voteSchema.getVoteFromRow(row));
			}
			
			// join into data
			for (DATA_SHORT_IDENTIFIED item : data) {
				VOTE_TYPE vote = votes.get(item.getIdentifier().getShortId());
				setVoteToItem(item, vote);
			}
		}	// else user is not signed in

		
		return data;
	}
	
	
	// abstract methods
	
	/**
	 * IMPORTANT: override this method if the type of the shortIdentifierColumn is something else than Long
	 * @param shortIdentifierColumn specifies the name of the the shortIdentifierColumn
	 * @param row the row of which the shortIdentifier should be returned
	 * @return the shortIdentifier of the row
	 * TODO parameterize with SHORT_IDENT_COLUMN_TYPE
	 */
	protected Object getShortIdentifierOfRow(Column shortIdentifierColumn, Row row) {
		return row.getLong(shortIdentifierColumn.getName());
	}
	
	/**
	 * sets vote to item
	 * @param item the item the vote should be set to
	 * @param vote the vote that is to set, notice that vote is null when user has not yet voted for the item
	 */
	protected abstract void setVoteToItem(DATA_SHORT_IDENTIFIED item, VOTE_TYPE vote);
	
	/**
	 * constructs a {@code DATA_TYPE_CONTAINER} with the given attributes and returns it
	 * @param superIdentifier
	 * @param data
	 * @return
	 */
	protected abstract DATA_TYPE_CONTAINER constructEvDataTypeContainer(
			SUPER_IDENT superIdentifier, 
			ArrayList<DATA_SHORT_IDENTIFIED> data);
}
