package eWorld.database.impl;

import java.util.ArrayList;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.identifiers.AbstractIdentifier;
import eWorld.datatypes.identifiers.EvIdentifier;

/**
 * 
 * @author michael
 *
 * @param <IDENT> identifies a dataSet in the schema
 */
public abstract class Schema <IDENT extends AbstractIdentifier> {

	// final attributes
	
	/** session that is to use to execute cql-statements */
	protected final Session session;
	
	/** the name of the schema in the cql-statements */
	protected final String schemaName;
	
	
	// attributes
	
	
	// preparedStatements
	
	private PreparedStatement existsStatement;
	
	private PreparedStatement countStatement;
	
	
	// constructors
	
	/**
	 * calls the method 'createSchema()'
	 * calls the method 'prepareStatements()'
	 * @param session the session that is to use to execute cql-statements
	 */
	public Schema(Session session, String schemaName) {
		assert null != session;
		
		this.session = session;
		this.schemaName = schemaName;
		
		createSchema();
		createIndices();
		// TODO check if columns are correct
		// TODO call only when schema becomes initialized
		
		prepareStatements();
	}

	
	// implemented methods
	
	/**
	 * can be overridden by subclasses if needed
	 * used to create the schema when it doesn't exist
	 */
	protected void createIndices() {
	}
	
	/**
	 * prepares specified statements
	 */
	private void prepareStatements() {
		
		existsStatement = session.prepare(
				"SELECT COUNT(*)"
				+ "FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
		
		countStatement = session.prepare(
				"SELECT COUNT(*)"
				+ "FROM " + getSchemaName()
				+ ";");
		
	}
	
	/**
	 * creates the schema if it doesn't exist
	 */
	private void createSchema() {
		
		String statement = "CREATE TABLE IF NOT EXISTS "
				+ getSchemaName() + " (";
		
		for (Column col : getColumns()) {
			statement += col.getName() + " " + col.getType() + ", ";
		}
		
		statement += "PRIMARY KEY (";
		statement += getPrimaryKey();
		statement += "))";
		
		statement += getCreateSchemaWithPart();
		
		statement += ";";
		
		session.execute(statement);
		
		System.out.println("success: Table '" + getSchemaName() + "' initialized");	// TODO-note vary if created or already existed
	}
	
	/**
	 * @param identifier
	 * @return true if the entry specified by identifier exists
	 */
	public boolean exists(IDENT identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(existsStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(getIdentifierValues(identifier)));
		
		Row row = results.one();
		long count = row.getLong("count");
		
		assert results.isExhausted();
		assert 1 >= count : "count is " + count;
		
		if (1 <= count) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return the number of rows in the schema
	 */
	public int countRows() {
		BoundStatement boundStatement = new BoundStatement(countStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement);
		
		Row row = results.one();
		return (int)row.getLong("count");
	}
	
	/**
	 * @return the name of the schema in the cql-statements
	 */
	protected final String getSchemaName() {
		return schemaName;
	}
	
	/**
	 * can be overridden by subclasses if needed
	 * used to create the schema when it doesn't exist
	 * @return e.g. "WITH CLUSTERING ORDER BY key DESC"
	 */
	protected String getCreateSchemaWithPart() {
		return "";
	}
	
	
	// abstract methods
	
	/**
	 * to add further columns in subclass this method is to override and super.getColumns() should be used
	 * @return the columns of the schema including the primary key
	 */
	protected abstract Column[] getColumns();
	
	/**
	 * used to create the schema when it doesn't exist
	 * @return the primary key separated through commas (e.g. "key1, key2" or "(key1, key2), key3")
	 */
	protected abstract String getPrimaryKey();
	
	/**
	 * to add further columns in subclass this method is to override and super.getIdentifierColumns() should be used
	 * @return the columns that identify a row
	 */
	protected abstract Column[] getIdentifierColumns();
	
	/**
	 * @param identifier specifies a row
	 * @return the values of the identifier in correspondent order to the columns in {@code getIdentifierColumns()}
	 */
	protected abstract Object[] getIdentifierValues(IDENT identifier);
	
}
