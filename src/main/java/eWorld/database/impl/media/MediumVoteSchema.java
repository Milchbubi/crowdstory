package eWorld.database.impl.media;

import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class MediumVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<MediumIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		MediumShortIdentifier,
		MediumIdentifier,
		EntryClassIdentifier
	> {
	
	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_mediumId = new Column("mediumId", IdGenerator.idType);
	
	
	// constructors
	
	public MediumVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<MediumIdentifier> identifier) {
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getMediumId()};
	}

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_mediumId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_mediumId;
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryId, c_mediumId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryId.getName() + ", " + c_mediumId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryId, c_mediumId};
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

}
