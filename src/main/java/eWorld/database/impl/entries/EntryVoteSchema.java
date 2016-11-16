package eWorld.database.impl.entries;

import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class EntryVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<EntryIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		EntryShortIdentifier,
		EntryIdentifier,
		EntryClassIdentifier
	> {

	// static finals
	
	protected static final Column c_classId = new Column("classId", IdGenerator.idType);
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	
	// constructors
	
	public EntryVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// overridden methods

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_classId, c_entryId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_classId.getName() + ", " + c_entryId.getName();
	}
	
	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_classId, c_entryId};
	}
	
	@Override
	protected Object[] getIdentifierValues(VoteIdentifier<EntryIdentifier> identifier) {
		assert null != identifier;
		
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getEntryId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_entryId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_classId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_entryId;
	}


	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

}
