package eWorld.database.impl.media;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.EvDataSchema;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class MediumDataSchema extends EvDataSchema<
		MediumIdentifier,
		EntryClassIdentifier,
		MediumShortIdentifier,
		Long,
		EvMedium<Ev, MediumIdentifier>,
		EvMedium<?, EntryClassIdentifier>,
		EvMedium<Ev, MediumShortIdentifier>
	> {
	
	// static finals
	
	protected static final Column c_link = new Column("link", DataType.varchar());
	
	protected static final Column c_description = new Column("description", DataType.text());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// attributes
	
	private final IdGenerator idGenerator;
	
	
	// constructors
	
	public MediumDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		idGenerator = new IdGenerator(session, getSchemaName());
		
		prepareStatements();
	}
	
	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_link, c_description, c_authorId})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	/**
	 * generates a new id that can be used for the {@code insert} method
	 * @return the generated id
	 */
	public MediumShortIdentifier generateNewId() {
		return new MediumShortIdentifier(idGenerator.generateId());
	}
	
	
	// overridden methods
	
	@Override
	public void insert(EvMedium<?, EntryClassIdentifier> item,
			MediumShortIdentifier shortIdentifier, float rating, Integer rank) {
		assert null != item;
		
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				item.getIdentifier().getEntryClassId(),
				rating,
				shortIdentifier.getMediumId(),
				rank,
				item.getLink(),
				item.getDescriptionString(),
				item.getAuthor().getUserId()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)}
	}

	@Override
	protected int getListItemsRankedLimit() {
		return 20;
	}

	@Override
	protected EvMedium<EvVoid, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvMedium<Ev, MediumIdentifier> item) {
		assert null != item;
		
		return new EvMedium<EvVoid, EntryClassIdentifier>(
				new EvVoid(),
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()),
				item.getLink(),
				item.getDescription(),
				item.getAuthor()
				);
	}

	@Override
	protected EvMedium<Ev, MediumIdentifier> constructEvDataTypeCompleteIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvMedium<Ev, MediumIdentifier>(
				constructEv(dataRow),
				new MediumIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())),
				dataRow.getString(c_link.getName()),
				new WoString(dataRow.getString(c_description.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected EvMedium<Ev, MediumShortIdentifier> constructEvDataTypeShortIdentified(
			Row dataRow) {
		assert null != dataRow;
		
		return new EvMedium<Ev, MediumShortIdentifier>(
				constructEv(dataRow),
				new MediumShortIdentifier(dataRow.getLong(c_id.getName())),
				dataRow.getString(c_link.getName()),
				new WoString(dataRow.getString(c_description.getName())),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rank, c_rating, c_link, c_description, c_authorId};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_containerId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_id;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_link, c_description, c_authorId};
	}

	@Override
	protected String getPrimaryKey() {
		return c_containerId.getName() + ", " + c_rating.getName() + ", " + c_id.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_containerId, c_id};
	}

	@Override
	protected Object[] getIdentifierValues(MediumIdentifier identifier) {
		return new Long[] {identifier.getEntryClassId(), identifier.getMediumId()};
	}

}
