package eWorld.database.impl.stories;

import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DataSchema;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class StoryDataSchema extends DataSchema<
		SentenceIdentifier,
		EntryClassIdentifier,
		SentenceShortIdentifier,
		Long,
		EvSentence<Ev, SentenceIdentifier>,
		EvSentence<?, EntryClassIdentifier>,
		EvSentence<Ev, SentenceShortIdentifier>
	> {
	
	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	protected static final Column c_position = new Column("pos", DataType.bigint());
	protected static final Column c_sentenceId = new Column("sentenceId", IdGenerator.idType);
	protected static final Column c_authorId = new Column("authorId", IdGenerator.idType);
	protected static final Column c_sentence = new Column("sentence", DataType.varchar());
	protected static final Column c_rating = new Column("rating", DataType.cfloat());
	
	
	// prepared statements
	
	private PreparedStatement insertStatement;
	private PreparedStatement listSentencesStatement;
	private PreparedStatement listSentencesBoundedStatement;
	private PreparedStatement updateRatingStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public StoryDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}

	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_entryId, c_position, c_sentenceId, c_authorId, c_sentence, c_rating})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
		listSentencesStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ Util.composePreparedStatementWherePart(getListItemsWhereColumns())
//				+ " ORDER BY " + c_position.getName() + " ASC"	// optional, c_position is anyway cluster order
//				+ " LIMIT " + 10000
				+ ";");
		
		listSentencesBoundedStatement = session.prepare(
				"SELECT " 
				+ Util.composePreparedStatementSelectPart(getListItemsSelectColumns())
				+ " FROM " + getSchemaName()
				+ " WHERE "
				+ c_entryId.getName() + " = ?"
				+ " AND " + c_position.getName() + " >= ?"
				+ " AND " + c_position.getName() + " <= ?"
//				+ " ORDER BY " + c_position.getName() + " ASC"	// optional, c_position is anyway cluster order
				+ ";");
		
		updateRatingStatement = session.prepare(
				"UPDATE "
				+ getSchemaName()
				+ " SET "
				+ c_rating.getName() + " = ?"
				+ " WHERE "
				+ c_entryId.getName() + " = ? AND "
				+ c_position.getName() + " = ? AND "
				+ c_sentenceId.getName() + " = ?"
				+ ";");
	}
	
	/**
	 * generates a new id that can be used for the {@code insert} method
	 * @return the generated id
	 */
	public SentenceShortIdentifier generateNewId() {
		return new SentenceShortIdentifier(idGenerator.generateId());
	}
	
	public void insert(EvSentence<?, EntryClassIdentifier> item,
			SentenceShortIdentifier shortIdentifier, float rating) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				item.getPosition(),
				shortIdentifier.getShortId(),
				item.getAuthor().getUserId(),
				item.getSentence().getString(),
				rating
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}
	
	/**
	 * updates the rating of the specified row
	 * @param identifier specifies the row
	 * @param position specifies the row
	 * @param newRating the rating that should be set
	 */
	public void updateRating(SentenceIdentifier identifier, long position, float newRating) {
		assert null != identifier;
		
		BoundStatement boundStatement = new BoundStatement(updateRatingStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundStatement.bind(newRating, identifier.getEntryClassId(), position, identifier.getSentenceId()));
	}
	
	/**
	 * 
	 * @param identifier the identifier of the entry|story
	 * @param from position to start from
	 * @param to position where listing should be stopped
	 * @return
	 */
	public ResultSet listItemsBounded(EntryClassIdentifier identifier, long from, long to) {
		BoundStatement boundStatement = new BoundStatement(listSentencesBoundedStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundStatement.bind(identifier.getEntryClassId(), from, to));
	}
	
	/**
	 * constructs a new {@code Ev} object from the given dataRow
	 * @param dataRow contains the column-values
	 * @return an {@code Ev} object that is only filled with data that is stored in this schema (e.g. rating)
	 * TODO redundant to method in RatedDataSchema, move to common superclass DataSchema?
	 */
	protected Ev constructEv(Row dataRow) {
		assert null != dataRow;
		
		return new Ev(
				0, 
				dataRow.getFloat(c_rating.getName())
				);
	}
	
	
	// overridden methods
	
	@Override
	protected void createIndices() {
		super.createIndices();
		
		// index for id
		session.execute(
				"CREATE INDEX IF NOT EXISTS " + getSchemaName() + "_" + c_sentenceId.getName() + "_index"
				+ " ON " + getSchemaName()
				+ "(" + c_sentenceId.getName() + ")"
				+ ";");
		
	}
	
	@Override
	public ResultSet listItems(EntryClassIdentifier identifier) {
		BoundStatement boundStatement = new BoundStatement(listSentencesStatement);	// TODO create always new instance of BoundStatement?
		return session.execute(boundStatement.bind(getSuperIdentifierValues(identifier)));
	}

	@Override
	protected EvSentence<?, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvSentence<Ev, SentenceIdentifier> item) {
		assert null != item;
		
		return new EvSentence<EvVoid, EntryClassIdentifier>(
				EvVoid.INST,
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getPosition(),
				item.getAuthor(),
				new Date(item.getIdentifier().getEntryClassId()),
				item.getSentence()
				);
	}

	@Override
	protected EvSentence<Ev, SentenceIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvSentence<Ev, SentenceIdentifier>(
				constructEv(dataRow),
				new SentenceIdentifier(dataRow.getLong(c_entryId.getName()), dataRow.getLong(c_sentenceId.getName())),
				dataRow.getLong(c_position.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName())),
				new Date(dataRow.getLong(c_sentenceId.getName())),
				new WoString(dataRow.getString(c_sentence.getName()))
				);
	}

	@Override
	protected EvSentence<Ev, SentenceShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvSentence<Ev, SentenceShortIdentifier>(
				constructEv(dataRow),
				new SentenceShortIdentifier(dataRow.getLong(c_sentenceId.getName())),
				dataRow.getLong(c_position.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName())),
				new Date(dataRow.getLong(c_sentenceId.getName())),
				new WoString(dataRow.getString(c_sentence.getName()))
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_position, c_sentenceId, c_authorId, c_sentence, c_rating};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_sentenceId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryId, c_position, c_sentenceId, c_authorId, c_sentence, c_rating};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_position.getName() + ", " + c_sentenceId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryId, c_sentenceId};
	}

	@Override
	protected Object[] getIdentifierValues(SentenceIdentifier identifier) {
		return new Long[] {identifier.getEntryClassId(), identifier.getSentenceId()};
	}

}
