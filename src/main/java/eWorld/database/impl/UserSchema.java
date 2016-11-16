package eWorld.database.impl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SecretUser;

public class UserSchema extends Schema<UserIdentifier> {

	// static finals
	
	private static final Column c_id = new Column("id", IdGenerator.idType);
	private static final Column c_pseudonym = new Column("pseudonym", DataType.varchar());
	private static final Column c_passwordSalt = new Column("passwordSalt", DataType.varchar());
	private static final Column c_passwordHash = new Column("passwordHash", DataType.varchar());
	private static final Column c_votes = new Column("votes", DataType.cint());
	
	private static final String index_pseudonym = DB.USER_SCHEMA + "_" + c_pseudonym.getName() + "_index";
	
	
	// preparedStatements
	
	private PreparedStatement countPseudonymsStatement;
	
	private PreparedStatement insertStatement;
	
	private PreparedStatement deleteStatement;
	
	private PreparedStatement selectSecretStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public UserSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}
	
	
	// methods
	
	private void prepareStatements() {
		
		countPseudonymsStatement = session.prepare(
				"SELECT COUNT(*)"
				+ "FROM " + getSchemaName()
				+ " WHERE "
				+ c_pseudonym.getName() + " = ?"
				+ ";");
		
		insertStatement = session.prepare(
			      "INSERT INTO " + getSchemaName()
			      + "("
			      + Util.composePreparedStatementSelectPart(new Column[] {c_id, c_pseudonym, c_passwordSalt, c_passwordHash, c_votes})
			      + ") "
			      + "VALUES (?, ?, ?, ?, ?)"
			      + ";");	// TODO use if not exists
		
		deleteStatement = session.prepare(
				"DELETE"
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ c_id.getName() + " = ?"
				+ ";");
		
		selectSecretStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ c_pseudonym.getName() + " = ?"
				+ ";");
	}
	
	@Override
	protected void createIndices() {
		super.createIndices();
		
		// index for pseudonym
		session.execute(
				"CREATE INDEX IF NOT EXISTS " + index_pseudonym
				+ " ON " + getSchemaName()
				+ "(" + c_pseudonym.getName() + ")"
				+ ";");
	}

	/**
	 * @param pseudonym the pseudonym that should be searched for
	 * @return the number of rows with given pseudonym as pseudonym
	 */
	public long countPseudonyms(final String pseudonym) {
		assert null != pseudonym;
		
		BoundStatement boundStatement = new BoundStatement(countPseudonymsStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(pseudonym));
		
		Row row = results.one();
		long count = row.getLong("count");
		
		assert results.isExhausted();
		
		return count;
	}

	/**
	 * stores the given object in a new row
	 * @param user the object that should be stored
	 * @param passwordSalt the salt of the password
	 * @param passwordHash the with the given salt hashed password
	 * @return the identifier of the row that was inserted
	 */
	public UserIdentifier insert(final RegisterUser user, final String passwordSalt, final String passwordHash, final int votes) {
		assert null != user;
		assert null != passwordSalt;
		assert null != passwordHash;
		assert 0 <= votes;
		
		long id = idGenerator.generateId();
		
		BoundStatement boundStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(
				id, user.getPseudonym(), passwordSalt, passwordHash, votes
			));
		
		return new UserIdentifier(id);
	}
	
	/**
	 * deletes the row specified through the given user
	 * @param user
	 */
	public void delete(final UserIdentifier user) {
		assert null != user;
		
		BoundStatement boundStatement = new BoundStatement(deleteStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(user.getUserId()));
	}
	
	/**
	 * @param pseudonym
	 * @return the user with the given pseudonym or null if there is no user with this pseudonym
	 */
	public SecretUser selectSecret(String pseudonym) {
		assert null != pseudonym;
		
		BoundStatement boundStatement = new BoundStatement(selectSecretStatement);	// TODO create always new instance of BoundStatement?
		ResultSet results = session.execute(boundStatement.bind(pseudonym));
		
		if (results.isExhausted()) {
			// there is no user with the specified pseudonym
			return null;
		}
		
		Row row = results.one();
		
		SecretUser user = new SecretUser(
				new AdvancedUser(
						new UserIdentifier(row.getLong(c_id.getName())),
						row.getString(c_pseudonym.getName()),
						row.getInt(c_votes.getName())
					),
				row.getString(c_passwordSalt.getName()),
				row.getString(c_passwordHash.getName())
				);
		
		if (!results.isExhausted()) {
			System.out.println("there are more then one occurences of users with pseudonym " + pseudonym);	// TODO log
		}
		
		return user;
	}
	
	
	// overridden methods
	
	@Override
	protected Column[] getColumns() {
		return new Column[] {c_id, c_pseudonym, c_passwordSalt, c_passwordHash, c_votes};
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
	protected Object[] getIdentifierValues(UserIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getUserId()};
	}
}
