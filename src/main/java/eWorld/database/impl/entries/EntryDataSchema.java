package eWorld.database.impl.entries;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.EvDataSchema;
import eWorld.database.impl.Util;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.WoString;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;

public class EntryDataSchema extends EvDataSchema<
		EntryIdentifier,
		EntryClassIdentifier,
		EntryShortIdentifier,
		Long,
		EvEntry<Ev, EntryIdentifier>,
		EvEntry<?, EntryClassIdentifier>,
		EvEntry<Ev, EntryShortIdentifier>
	> {

	// static finals
	
	protected static final Column c_name = new Column("name", DataType.varchar());
	
	protected static final Column c_description = new Column("description", DataType.text());	
	
	protected static final Column c_isElement = new Column("isElement", DataType.cboolean());
	
	
	// preparedStatements
	
	private PreparedStatement insertStatement;
	
	
	// attributes
	
	
	// constructors
	
	public EntryDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		prepareStatements();
		
		insertRoot();	// TODO call only when schema becomes initialized
	}
	
	// methods
	
	private void prepareStatements() {
		
		insertStatement = session.prepare(
				"INSERT INTO " + getSchemaName()
				+ "("
				+ Util.composePreparedStatementSelectPart(new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_isElement, c_authorId})
				+ ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
				+ ";");	// TODO use if not exists
		
	}
	
	/**
	 * inserts the root
	 * FIXME only call/execute when schema is empty or root not exists
	 */
	private void insertRoot() {
		insert(
				new EvEntry<EvVoid, EntryClassIdentifier>(
						new EvVoid(), 
						new EntryClassIdentifier(EntryRegisterSchema.SUPER_ROOT_ID),
						new WoString("World"),
						null,
						false,
						new UserIdentifier(EntryRegisterSchema.ROOT_AUTHOR_ID)),
				new EntryShortIdentifier(EntryRegisterSchema.ROOT_ID),
				EntryRegisterSchema.ROOT_RATING_OR_VOTES,
				null
				);
	}
	
	
	// overridden methods
	
	@Override
	public void insert(EvEntry<?, EntryClassIdentifier> entry, EntryShortIdentifier entryShortIdent, float rating, Integer rank) {
		assert null != entry;
				
		BoundStatement boundInsertStatement = new BoundStatement(insertStatement);	// TODO create always new instance of BoundStatement?
		session.execute(boundInsertStatement.bind(
				entry.getIdentifier().getEntryClassId(),
				rating,
				entryShortIdent.getEntryId(),
				rank,
				entry.getName().getString(),
				entry.getDescriptionString(),
				entry.isElement(),
				entry.getAuthor().getUserId()
				));
		
		// TODO check if inserting was successful (via {@code selectOne(..)})
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_containerId, c_rating, c_id, c_rank, c_name, c_description, c_isElement, c_authorId};
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
	protected Object[] getIdentifierValues(EntryIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getEntryId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_rank, c_rating, c_name, c_description, c_isElement, c_authorId};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_containerId};
	}
	
	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

	@Override
	protected int getListItemsRankedLimit() {
//		return 30;
		return 100;
	}
	
	@Override
	protected Column getShortIdentifierColumn() {
		return c_id;
	}
	
	@Override
	protected EvEntry<EvVoid, EntryClassIdentifier> restrainEvDataTypeCompleteIdentifiedToSuperIdentified(
			EvEntry<Ev, EntryIdentifier> item) {
		assert null != item;
		
		return new EvEntry<EvVoid, EntryClassIdentifier>(
				new EvVoid(), 
				new EntryClassIdentifier(item.getIdentifier().getEntryClassId()), 
				item.getName(), 
				item.getDescription(),
				item.isElement(),
				item.getAuthor());
	}

	@Override
	protected EvEntry<Ev, EntryIdentifier> constructEvDataTypeCompleteIdentified(Row dataRow) {
		assert null != dataRow;
		
		return new EvEntry<Ev, EntryIdentifier>(
				constructEv(dataRow), 
				new EntryIdentifier(dataRow.getLong(c_containerId.getName()), dataRow.getLong(c_id.getName())), 
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				dataRow.getBool(c_isElement.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}

	@Override
	protected EvEntry<Ev, EntryShortIdentifier> constructEvDataTypeShortIdentified(Row dataRow) {
		assert null != dataRow;
		
		return new EvEntry<Ev, EntryShortIdentifier>(
				constructEv(dataRow), 
				new EntryShortIdentifier(dataRow.getLong(c_id.getName())), 
				new WoString(dataRow.getString(c_name.getName())),
				new WoString(dataRow.getString(c_description.getName())),
				dataRow.getBool(c_isElement.getName()),
				new UserIdentifier(dataRow.getLong(c_authorId.getName()))
				);
	}
	
}
