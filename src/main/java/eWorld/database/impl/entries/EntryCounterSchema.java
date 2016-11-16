package eWorld.database.impl.entries;

import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;

public class EntryCounterSchema extends UpDownCounterSchema<
		EntryIdentifier,
		EntryClassIdentifier,
		EntryShortIdentifier
	> {

	// static finals
	
	protected static final Column c_classId = new Column("classId", IdGenerator.idType);
	
	protected static final Column c_id = new Column("id", IdGenerator.idType);
	
	
	// prepared statements
	
	
	
	// constructors
	
	public EntryCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		insertRoot();
	}
	
	
	// methods
	
	/**
	 * inserts the root
	 * FIXME only call/execute when schema is empty or root not exists
	 */
	private void insertRoot() {
		upVote(new EntryIdentifier(EntryRegisterSchema.SUPER_ROOT_ID, EntryRegisterSchema.ROOT_ID), EntryRegisterSchema.ROOT_RATING_OR_VOTES);
	}
	
	
	// overridden methods

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_classId, c_id, c_up, c_down};
	}
	
	@Override
	protected String getPrimaryKey() {
		return c_classId.getName() + ", " + c_id.getName();
	}
	
	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_classId, c_id};
	}

	@Override
	protected Long[] getIdentifierValues(EntryIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getEntryId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_id, c_up, c_down};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_classId};
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
	
}
