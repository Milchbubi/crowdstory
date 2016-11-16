package eWorld.database.impl;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public abstract class BooleanVoteSchema<
		COMPLETE_IDENT extends VoteIdentifier<COMPLETE_IDENT_INNER>,
		SUPER_IDENT extends VoteIdentifier<SUPER_IDENT_INNER>,
		SHORT_IDENT extends EvShortIdentifier<?>,
		COMPLETE_IDENT_INNER extends EvCompleteIdentifier,
		SUPER_IDENT_INNER extends EvCompleteIdentifier	// TODO is there no better solution for parameterizing? or is that parameter really needed?
	> extends VoteSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT,COMPLETE_IDENT_INNER, SUPER_IDENT_INNER, Boolean> {

	// static finals
	
	protected static final Column c_vote = new Column("vote", DataType.cboolean());
	
	
	// preparedStatements
	
	
	// constructors
		
	public BooleanVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods

	/**
	 * 
	 * @param identifier
	 * @return null if the user has not yet voted for the specified row, true if the user voted up, false if the user voted down
	 */
	public UpDownVote getBooleanVote(COMPLETE_IDENT identifier) {
		assert null != identifier;
		
		Boolean vote = getVote(identifier);
		
		if (null == vote) {
			return UpDownVote.OPEN;
		} else {
			if (true == vote) {
				return UpDownVote.UP;
			} else {
				return UpDownVote.DOWN;
			}
		}
	}
	
	
	// abstract methods
	
	
	// overridden methods
	
	@Override
	protected Column getVoteColumn() {
		return c_vote;
	}
	
	@Override
	protected Boolean getVoteFromRow(Row row) {
		return row.getBool(c_vote.getName());
	}
	
}
