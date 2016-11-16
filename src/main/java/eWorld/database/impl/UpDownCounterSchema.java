package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.elementars.UpDownVotes;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class UpDownCounterSchema <
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<?>
	> extends CounterSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> {

	// static finals
	
	protected final static Column c_up = new Column("up", DataType.counter());
	
	protected final static Column c_down = new Column("down", DataType.counter());
	
	
	// preparedStatements
	
	private PreparedStatement voteUpStatement;
	private PreparedStatement voteDownStatement;
	private PreparedStatement selectOneStatement;
	
	
	// constructors
	
	public UpDownCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		assert Util.contains(getColumns(), c_up);
		assert Util.contains(getColumns(), c_down);
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		voteUpStatement = session.prepare(prepareStatementsVote(c_up));
		voteDownStatement = session.prepare(prepareStatementsVote(c_down));
		
		selectOneStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
	}
	/**
	 * assistant for {@code prepareStatements}
	 * @param upOrDown either {@code c_up} or {@code c_down}
	 * @return
	 */
	private String prepareStatementsVote(Column upOrDown) {
		assert c_up == upOrDown || c_down == upOrDown;
		
		String statement = "UPDATE " + getSchemaName()
			      + " SET " + upOrDown.getName() + " = " + upOrDown.getName() + " + ?"
			      + " WHERE ";
		statement += Util.composePreparedStatementWherePart(getIdentifierColumns());
		statement += ";";
		return statement;
	}
	
	/**
	 * 
	 * @param identifier identifies the row which is to update
	 * @param votes number which is to increment
	 */
	public final void upVote(COMPLETE_IDENT identifier, long votes) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(voteUpStatement);	// TODO create always new instance of BoundStatement?
//		session.execute(boundStatement.bind(votes, getPrimaryKeyValues(identifier)));	// doesn't work
		voteBindValuesAndExecute(boundStatement, votes, getIdentifierValues(identifier));	// FIXME dirty implementation
		
	}
	
	/**
	 * 
	 * @param identifier identifies the row which is to update
	 * @param votes number which is to increment
	 */
	public final void downVote(COMPLETE_IDENT identifier, long votes) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(voteDownStatement);	// TODO create always new instance of BoundStatement?
//		session.execute(boundStatement.bind(votes, getPrimaryKeyValues(identifier)));	// doesn't work
		voteBindValuesAndExecute(boundStatement, votes, getIdentifierValues(identifier));	// FIXME dirty implementation
		
	}
	
	/**
	 * assistant for {@code upVote} and {@code downVote}
	 * FIXME very stupid implemented but to assign array directly shines not to work
	 * @param boundStatement
	 * @param votes
	 * @param primaryKeyValues
	 * @return
	 */
	private final void voteBindValuesAndExecute(BoundStatement boundStatement, long votes, Object[] primaryKeyValues) {
		assert null != primaryKeyValues;
		
		if (2 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(votes, primaryKeyValues[0], primaryKeyValues[1]));
		} else if (3 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(votes, primaryKeyValues[0], primaryKeyValues[1], primaryKeyValues[2]));
		} else if (4 == primaryKeyValues.length) {
			session.execute(boundStatement.bind(votes, primaryKeyValues[0], primaryKeyValues[1], primaryKeyValues[2], primaryKeyValues[3]));
		} else {
			throw new UnsupportedOperationException(primaryKeyValues.length + " key-values not supportet");
		}
	}
	
	/**
	 * executes a select statement
	 * @param identifier
	 * @return the object that matches to the given identifier
	 */
	public UpDownVotes selectOne(COMPLETE_IDENT identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(selectOneStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getIdentifierValues(identifier)));
		
		Row row = results.one();
		
		UpDownVotes votes = new UpDownVotes(
				row.getLong(c_down.getName()),
				row.getLong(c_up.getName())
				);
		
		assert results.isExhausted();
		
		return votes;
	}
	
	
	// abstract methods
	
	
	// overridden methods
	
}
