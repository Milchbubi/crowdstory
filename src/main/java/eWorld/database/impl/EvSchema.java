package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class EvSchema<
		COMPLETE_IDENT extends EvIdentifier,
		SUPER_IDENT extends EvIdentifier,
		SHORT_IDENT extends EvShortIdentifier<?>
	> extends Schema<COMPLETE_IDENT> {

	// preparedStatements
	
	private PreparedStatement listItemsClassicStatement;
	
	
	// constructors
	
	public EvSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
		
		assert Util.contains(getListItemsSelectColumns(), getShortIdentifierColumn());
	}

	
	// methods
	
	private void prepareStatements() {
		listItemsClassicStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
//				+ " LIMIT ?"
				+ ";");
	}
	
	protected PreparedStatement getListItemsClassicStatement() {
		return listItemsClassicStatement;
	}
	
	/**
	 * 
	 * @param whereColumnsValues in correspondent order to {@code getListItemsWhereColumns()}
	 * @return ResultSet of the columns specified in {@code getListItemsSelectColumns()}
	 */
	public ResultSet listItemsClassic(Object... whereColumnsValues) {
		BoundStatement boundInsertStatement = new BoundStatement(listItemsClassicStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundInsertStatement.bind(whereColumnsValues));
	}
	
	
	// overridden methods
	
	
	// abstract methods
	
	/**
	 * TODO rename|refactor method (getContainerSelectColumns() ? getSuperSelectColumns())
	 * @return columns that should be requested from {@code EntrySchemaGate.listItemsClassic(..)} and other functions that list data
	 */
	protected abstract Column[] getListItemsSelectColumns();
	
	/**
	 * TODO rename|refactor method (getContainerWhereColumns() ? getSuperWhereColumns())
	 * @return columns that specify the rows that should be requested from {@code EntrySchemaGate.listItemsClassic(..)} and other functions that list data
	 */
	protected abstract Column[] getListItemsWhereColumns();
	
	/**
	 * 
	 * @return the most accurate or (usually) the last column of the primary key
	 */
	protected abstract Column getShortIdentifierColumn();
	
	/**
	 * @param superIdentifier specifies a class|container of items
	 * @return the values of the superIdentifier in correspondent order to the columns in {@code getListItemsWhereColumns()|getContainerWhereColumns()|getSuperWhereColumns()}
	 */
	protected abstract Object[] getSuperIdentifierValues(SUPER_IDENT superIdentifier);
	
}
