package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.data.IdDataType;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class RatedDataSchema <
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends IdDataType<COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends IdDataType<SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends IdDataType<SHORT_IDENT>
	> extends DataSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT, SHORT_IDENT_COLUMN_TYPE, DATA_COMPLETE_IDENTIFIED, DATA_SUPER_IDENTIFIED, DATA_SHORT_IDENTIFIED> {

	// static finals
	
	protected static final Column c_containerId = new Column("containerId", IdGenerator.idType);
	protected static final Column c_id = new Column("id", IdGenerator.idType);
	protected static final Column c_rank = new Column("rank", DataType.cint());
	protected static final Column c_rating = new Column("rating", DataType.cfloat());
	
	
	// preparedStatements
	
	private PreparedStatement listItemsRatedStatement;
	
	private PreparedStatement getTopRatedItemStatement;
	
	private PreparedStatement deleteOneStatement;
	
	private PreparedStatement countShortIdentStatement;
	
	
	// constructors
	
	public RatedDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		assert Util.contains(getColumns(), c_rank);
		assert Util.contains(getColumns(), c_rating);
		
		prepareStatements();
	}
	
	
	// methods
	
	@Override
	protected void createIndices() {
		super.createIndices();
		
		// index for id
		session.execute(
				"CREATE INDEX IF NOT EXISTS " + getSchemaName() + "_" + c_id.getName() + "_index"
				+ " ON " + getSchemaName()
				+ "(" + c_id.getName() + ")"
				+ ";");
		
//		// index for rank
//		session.execute(
//				"CREATE INDEX IF NOT EXISTS " + getSchemaName() + "_" + c_rank.getName() + "_index"
//				+ " ON " + getSchemaName()
//				+ "(" + c_rank.getName() + ")"
//				+ ";");	not needed?
		
	}
	
	private void prepareStatements() {
		listItemsRatedStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
				+ " ORDER BY " + c_rating.getName() + " DESC"	// optional, c_rating is anyway cluster order
				+ " LIMIT " + getListItemsRankedLimit()
				+ ";");
		
		getTopRatedItemStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
				+ " ORDER BY " + c_rating.getName() + " DESC"	// optional, c_rating is anyway cluster order
				+ " LIMIT 1"
				+ ";");
		
		deleteOneStatement = session.prepare(
				"DELETE FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ " AND " + c_rating.getName() + " = ?"
				+ ";");
		
		countShortIdentStatement = session.prepare(
				"SELECT COUNT(*)"
				+ "FROM " + getSchemaName()
				+ " WHERE "
				+ getShortIdentifierColumn().getName() + " = ?"
				+ ";");
	}
	
	/**
	 * executes a select statement and returns the top rated item of a class
	 * @param identifier specifies the class of which the top rated item is searched
	 * @return the top rated item of the specified class or null if class contains no items (or does not exist)
	 */
	public DATA_COMPLETE_IDENTIFIED getTopRatedItem(SUPER_IDENT identifier) {
		BoundStatement boundStatement = new BoundStatement(getTopRatedItemStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getSuperIdentifierValues(identifier)));
		
		if (results.isExhausted()) {
			return null;
		}
		
		Row row = results.one();
		
		DATA_COMPLETE_IDENTIFIED item = constructEvDataTypeCompleteIdentified(row);
		
		assert results.isExhausted();
		
		return item;
	}
	
	/**
	 * executes a delete statement, (should) only delete one column
	 * @param identifier specifies the row that should be deleted
	 * @param rating the rating of the row that should be deleted
	 */
	public void deleteOne(COMPLETE_IDENT identifier, float rating) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(deleteOneStatement);	// TODO create always new instance of BoundStatement?
		boundStatement.bind(Util.appendToArray(getIdentifierValues(identifier), rating));	// FIXME is there no better way?
		session.execute(boundStatement);
	}
	
	/**
	 * @param identifier
	 * @return true if a item specified by identifier exists
	 */
	public boolean exists(SHORT_IDENT identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(countShortIdentStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(identifier.getShortId()));
		
		Row row = results.one();
		long count = row.getLong("count");
		
		assert results.isExhausted();
		
		if (1 <= count) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * constructs a new {@code Ev} object from the given dataRow
	 * @param dataRow contains the column-values
	 * @return an {@code Ev} object that is only filled with data that is stored in this schema (e.g. rank, rating)
	 */
	protected Ev constructEv(Row dataRow) {
		assert null != dataRow;
		
		return new Ev(
				dataRow.getInt(c_rank.getName()), 
				dataRow.getFloat(c_rating.getName())
				);
	}
	
	
	// abstract methods
	
	/**
	 * @param item the item which is to add, contains also the classId/pathId where the item is to add
	 * @param shortIdentifier the new itemId of the entry
	 * @param rating initial rating of the entry
	 * @param rank the rank of the entry or null
	 */
	public abstract void insert(DATA_SUPER_IDENTIFIED item, SHORT_IDENT shortIdentifier, float rating, Integer rank);
	
	/**
	 * 
	 * @return the number of elements that should listed in the {@code listItemsRankedStatement}
	 * f.e. 20 if 20 elements should be displayed
	 */
	protected abstract int getListItemsRankedLimit();
	
	
	// overridden methods
	
	@Override
	public ResultSet listItems(SUPER_IDENT identifier) {
		BoundStatement boundStatement = new BoundStatement(listItemsRatedStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundStatement.bind(getSuperIdentifierValues(identifier)));
	}
	
	@Override
	protected String getCreateSchemaWithPart() {
		return "WITH CLUSTERING ORDER BY (" + c_rating.getName() + " DESC)";
	}
}
