package eWorld.database.impl.media;

import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;

public class MediumCounterSchema extends UpDownCounterSchema<
		MediumIdentifier,
		EntryClassIdentifier,
		MediumShortIdentifier
	> {
	
	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_mediumId = new Column("mediumId", IdGenerator.idType);
	
	
	// constructors
	
	public MediumCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}
	
	
	// methods
	
	
	// overridden methods

	@Override
	protected Object[] getIdentifierValues(MediumIdentifier identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getEntryClassId(), identifier.getMediumId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_mediumId, c_up, c_down};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_mediumId;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_entryId, c_mediumId, c_up, c_down};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_mediumId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_entryId, c_mediumId};
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			EntryClassIdentifier superIdentifier) {
		return new Long[] {superIdentifier.getEntryClassId()};
	}

}
