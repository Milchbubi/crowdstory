package eWorld.database.impl.entries;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Schema;
import eWorld.database.impl.Util;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;

public class EntryRegisterSchema extends Schema<EntryShortIdentifier> {

	// static finals

	public static final long ROOT_ID = 1;
	public static final long SUPER_ROOT_ID = 42;
	public static final long ROOT_RATING_OR_VOTES = 42000;
	public static final long ROOT_AUTHOR_ID = 42;
	
	protected static final Column c_id = new Column("id", IdGenerator.idType);
	
	protected static final Column c_classId = new Column("classId", IdGenerator.idType);
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	private PreparedStatement selectClassStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public EntryRegisterSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
		
		insertRoot();	// TODO call only when schema becomes initialized
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ ") "
				+ "VALUES (?, ?)"
				+ ";");	// TODO use if not exists
		
		selectClassStatement = session.prepare(
				"SELECT " 
				+ c_classId.getName()
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ c_id.getName() + " = ?"
				+ " LIMIT 1"
				+ ";");
	}
	
	/**
	 * inserts the root
	 * FIXME only call/execute when schema is empty or root not exists
	 */
	private void insertRoot() {
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				ROOT_ID,
				SUPER_ROOT_ID
				));
	}
	
	/**
	 * 
	 * @param superIdent the id of the superClass
	 * @return the identifier of the inserted entry
	 * TODO check if inserting was successful (via {@code selectOne(..)}
	 */
	public EntryShortIdentifier insert(EntryClassIdentifier superIdent) {
		assert null != superIdent;
		
		long id = idGenerator.generateId();
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				id,
				superIdent.getEntryClassId()
				));
		
		return new EntryShortIdentifier(id);
	}
	
	/**
	 * 
	 * @param identifier
	 * @return the superClass of the given identifier or null if row for the given identifier does not exist
	 */
	public EntryClassIdentifier getClassIdentifier(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(selectClassStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(identifier.getEntryId()));
		
		Row row = results.one();
		
		if (null == row) {
			// there is no row for the given identifier
			return null;
		}
		
		EntryClassIdentifier classIdentifier;
		try {
			classIdentifier = new EntryClassIdentifier(row.getLong(c_classId.getName()));
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("entry does not exist (" + identifier.getEntryId() + "), " + e);
		}
		
		assert results.isExhausted();
		
		return classIdentifier;
	}
	
	
	// overridden methods

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_id, c_classId};
	}

	@Override
	protected String getPrimaryKey() {
		return c_id.getName();
	}
	
	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_id};
	}

	@Override
	protected Object[] getIdentifierValues(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryId()};
	}

}
