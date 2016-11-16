package eWorld.database.impl.comments;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.DataSchema;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class CommentDataSchema extends DataSchema<
		CommentIdentifier,
		EntryClassIdentifier,
		CommentShortIdentifier,
		Long,
		EvComment<Ev, CommentIdentifier>,
		EvComment<?, EntryClassIdentifier>,
		EvComment<Ev, CommentShortIdentifier>
	> {

	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	protected static final Column c_commentId = new Column("commentId", IdGenerator.idType);
	protected static final Column c_authorId = new Column("authorId", IdGenerator.idType);
	protected static final Column c_authorPseudonym = new Column("authorPseudonym", DataType.varchar());
	protected static final Column c_comment = new Column("comment", DataType.text());
	protected static final Column c_rating = new Column("rating", DataType.cfloat());
	protected static final Column c_rank = new Column("rank", DataType.cint());
	
	
	// prepared statements
	
	private PreparedStatement insertStatement;
	private PreparedStatement listCommentsStatement;
	private PreparedStatement updateRatingStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public CommentDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_entryId, c_commentId, c_authorId, c_authorPseudonym, c_comment, c_rating, c_rank})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
		listCommentsStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
				+ " ORDER BY " + c_commentId.getName() + " DESC"	// optional, c_commentId is anyway cluster order
				+ " LIMIT " + 30
				+ ";");
		
		updateRatingStatement = session.prepare(
				"UPDATE "
				+ getSchemaName()
				+ " SET "
				+ c_rating.getName() + " = ?"
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getIdentifierColumns())
				+ ";");
	}
	
	/**
	 * generates a new id that can be used for the {@code insert} method
	 * the generated id corresponds a timestamp, a chronologic order is important for comments
	 * @return the generated id
	 */
	public CommentShortIdentifier generateNewId() {
		return new CommentShortIdentifier(idGenerator.generateId());
	}
	
	public void insert(EvComment<?, EntryClassIdentifier> item,
			CommentShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				shortIdentifier.getShortId(),
				item.getAuthor().getUserId(),
				item.getAuthorPseudonym(),
				item.getComment().getString(),
				rating,
				rank
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}
	
	/**
	 * updates the rating of the specified row
	 * @param identifier specifies the row
	 * @param newRating the rating that should be set
	 */
	public void updateRating(CommentIdentifier identifier, float newRating) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(updateRatingStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(newRating, identifier.getEntryClassId(), identifier.getCommentId()));
	}
	
	/**
	 * constructs a new {@code Ev} object from the given dataRow
	 * @param dataRow contains the column-values
	 * @return an {@code Ev} object that is only filled with data that is stored in this schema (e.g. rank, rating)
	 * TODO redundant to method in RatedDataSchema, move to common superclass DataSchema?
	 */
	protected Ev constructEv(Row dataRow) {
		assert null != dataRow;
		
		return new Ev(
				dataRow.getInt(c_rank.getName()), 
				dataRow.getFloat(c_rating.getName())
				);
	}
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_commentId, c_authorId, c_authorPseudonym, c_comment, c_rating, c_rank};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_commentId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryId, c_commentId, c_authorId, c_authorPseudonym, c_comment, c_rating, c_rank};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_commentId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryId, c_commentId};
	}

	@Override
	protected Object[] getIdentifierValues(CommentIdentifier identifier) {
		return new Long[] {identifier.getEntryClassId(), identifier.getCommentId()};
	}
	
	@Override
	protected String getCreateSchemaWithPart() {
		return "WITH CLUSTERING ORDER BY (" + c_commentId.getName() + " DESC)";
	}

	@Override
	public ResultSet listItems(EntryClassIdentifier identifier) {
		BoundStatement boundStatement = new BoundStatement(listCommentsStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundStatement.bind(getSuperIdentifierValues(identifier)));
	}

	@Override
	protected EvComment<EvVoid, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvComment<Ev, CommentIdentifier> item) {
		assert null != item;
		
		return new EvComment<EvVoid, EntryClassIdentifier>(
				EvVoid.INST,
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getAuthor(),
				item.getAuthorPseudonym(),
				item.getComment()
				);
	}

	@Override
	protected EvComment<Ev, CommentIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvComment<Ev, CommentIdentifier>(
				constructEv(dataRow),
				new CommentIdentifier(dataRow.getLong(c_entryId.getName()), dataRow.getLong(c_commentId.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName())),
				dataRow.getString(c_authorPseudonym.getName()),
				new WoString(dataRow.getString(c_comment.getName()))
				);
	}

	@Override
	protected EvComment<Ev, CommentShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvComment<Ev, CommentShortIdentifier>(
				constructEv(dataRow),
				new CommentShortIdentifier(dataRow.getLong(c_commentId.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName())),
				dataRow.getString(c_authorPseudonym.getName()),
				new WoString(dataRow.getString(c_comment.getName()))
				);
	}

}
