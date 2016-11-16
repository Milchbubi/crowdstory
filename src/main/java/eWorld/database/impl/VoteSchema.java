package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

/**
 * 
 * @author michael
 *
 * @param <COMPLETE_IDENT>
 * @param <SUPER_IDENT>
 * @param <SHORT_IDENT>
 * @param <COMPLETE_IDENT_INNER>
 * @param <SUPER_IDENT_INNER>
 * @param <VOTE_TYPE> the type that is returned by getVote(), must fit to getVoteColumn()
 */
public abstract class VoteSchema<
		COMPLETE_IDENT extends VoteIdentifier<COMPLETE_IDENT_INNER>,
		SUPER_IDENT extends VoteIdentifier<SUPER_IDENT_INNER>,
		SHORT_IDENT extends EvShortIdentifier<?>,
		COMPLETE_IDENT_INNER extends EvCompleteIdentifier,
		SUPER_IDENT_INNER extends EvCompleteIdentifier,	// TODO is there no better solution for parameterizing? or is that parameter really needed?
		VOTE_TYPE
	> extends EvSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> {

	// static finals
	
	protected static final Column c_userId = new Column("userId", DataType.bigint());
	
	
	
	
	// preparedStatements
	
	private PreparedStatement voteStatement;
	private PreparedStatement getVoteStatement;
	
	private PreparedStatement listVotesStatement;
	
	
	// constructors
		
	public VoteSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		assert Util.contains(getColumns(), c_userId);
		assert Util.contains(getColumns(), getVoteColumn());
		assert Util.contains(getIdentifierColumns(), c_userId);
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		voteStatement = session.prepare(prepareStatementsVote());
		getVoteStatement = session.prepare(prepareStatementsGetVote());
		
		listVotesStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
//				+ " LIMIT ?"
				+ ";");
	}
	private String prepareStatementsVote() {
		String statement = "UPDATE " + getSchemaName()
			      + " SET " + getVoteColumn().getName() + " = ?"
			      + " WHERE ";
		statement += Util.composePreparedStatementWherePart(getIdentifierColumns());
		statement += ";";
		return statement;
	}
	private String prepareStatementsGetVote() {
		String statement = "SELECT " + getVoteColumn().getName()
				+ " FROM " + getSchemaName()
				+ " WHERE ";
		statement += Util.composePreparedStatementWherePart(getIdentifierColumns());
		statement += ";";
		return statement;
	}
	
	/**
	 * FIXME very stupid implemented but to assign array directly shines not to work
	 * @param identifier
	 * @param vote the voted value
	 */
	public void vote(COMPLETE_IDENT identifier, VOTE_TYPE vote) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(voteStatement);	// TODO create always new instance of BoundStatement?
		
//		session.execute(boundStatement.bind(vote, getPrimaryKeyValues(userIdentifier, identifier)));	// doesn't work
		
		Object[] primaryKeyValues = getIdentifierValues(identifier);
		if (3 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(vote, primaryKeyValues[0], primaryKeyValues[1], primaryKeyValues[2]));
		} else if (4 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(vote, primaryKeyValues[0], primaryKeyValues[1], primaryKeyValues[2], primaryKeyValues[3]));
		} else if (5 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(vote, primaryKeyValues[0], primaryKeyValues[1], primaryKeyValues[2], primaryKeyValues[3], primaryKeyValues[4]));
		} else {
			throw new UnsupportedOperationException(primaryKeyValues.length + " key-values not supportet");
		}
	}

	/**
	 * 
	 * @param identifier
	 * @return null if the user has not yet voted for the specified row, otherwise the value the user has voted for the specified row
	 */
	public VOTE_TYPE getVote(COMPLETE_IDENT identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(getVoteStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getIdentifierValues(identifier)));
		
		if (results.isExhausted()) {
			return null;
		} else {
			Row row = results.one();
			
			assert results.isExhausted() : "there should be only one row";
			
			return getVoteFromRow(row);
		}
	}
	
	/**
	 * 
	 * @param superIdentifier specifies the class|container of votes that should be listed
	 * @return ResultSet of the columns specified in {@code getListItemsSelectColumns()}
	 */
	public ResultSet listVotes(SUPER_IDENT superIdentifier) {
		BoundStatement boundInsertStatement = new BoundStatement(listVotesStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundInsertStatement.bind(getSuperIdentifierValues(superIdentifier)));
	}
	
	
	// abstract methods
	
	/**
	 * returns the column that stores the votes, must fit to VOTE_TYPE
	 * @return
	 */
	protected abstract Column getVoteColumn();
	
	/**
	 * calls dependent on VOTE_TYPE
	 * row.getBool(getVoteColumn().getName()),
	 * row.getLong(getVoteColumn().getName()),
	 * ...
	 * @param row
	 * @return the Vote of the given column
	 */
	protected abstract VOTE_TYPE getVoteFromRow(Row row);
	
	// overridden methods
	
}
