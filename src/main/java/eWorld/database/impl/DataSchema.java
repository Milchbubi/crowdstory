package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.data.IdDataType;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class DataSchema <
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends IdDataType<COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends IdDataType<SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends IdDataType<SHORT_IDENT>
	> extends EvSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> {
	
	// static finals
	
	
	// preparedStatements
	
	private PreparedStatement selectOneStatement;
	
	
	// constructors
	
	public DataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
	}
	
	// methods
	
	private void prepareStatements() {
		selectOneStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
	}
	
	/**
	 * executes a select statement
	 * @param identifier
	 * @return the object that matches to the given identifier or null if there is none
	 */
	public DATA_COMPLETE_IDENTIFIED selectOne(COMPLETE_IDENT identifier) {
		assert null != identifier;
		
		Row row = selectOneRow(identifier);
		
		if (null != row) {
			return constructEvDataTypeCompleteIdentified(row);
		} else {
			return null;
		}
	}
	
	/**
	 * executes a select statement
	 * @param identifier
	 * @return the row that matches to the given identifier or null if there is none
	 */
	protected Row selectOneRow(COMPLETE_IDENT identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(selectOneStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getIdentifierValues(identifier)));
		
		Row row = results.one();	// returns null if there is no such row
		
		assert results.isExhausted();	// there should be only one row that matches the given identifier
		
		return row;
	}
	
	
	// abstract methods
	
	/**
	 * 
	 * @param identifier specifies the class which items should be listed
	 * @return ResultSet of the columns specified in {@code getListItemsSelectColumns()}
	 */
	public abstract ResultSet listItems(SUPER_IDENT identifier);
	
	/**
	 * converts the given item to one of the type that is returned
	 * does only up-casting because of limits of generics
	 * @param item the item that should be restrained
	 * @return the restrained item
	 * TODO FIXME is there no better way to achieve the same (up-casting), move to datatype classes could be a solution
	 */
	protected abstract DATA_SUPER_IDENTIFIED restrainEvDataTypeCompleteIdentifiedToSuperIdentified(DATA_COMPLETE_IDENTIFIED item);
	
	/**
	 * constructs a {@code DATA_TYPE} with the given attributes and returns it
	 * @param dataRow
	 * @return
	 */
	protected abstract DATA_COMPLETE_IDENTIFIED constructEvDataTypeCompleteIdentified(Row dataRow);
	
	/**
	 * constructs a {@code DATA_TYPE} with the given attributes and returns it
	 * @param ev
	 * @param dataRow
	 * @return
	 * TODO redundant to constructEvDataTypeCompleteIdentified,
	 * implement a generic "construct(..)" method in datatype classes to construct objects with arbitrary identifiers and evs,
	 * but leave the publicity of "Row" in this classes (database)!
	 */
	protected abstract DATA_SHORT_IDENTIFIED constructEvDataTypeShortIdentified(Row dataRow);
	
}
