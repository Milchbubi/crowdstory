package eWorld.database.impl.stories;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class StoryVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<SentenceIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		SentenceShortIdentifier,
		SentenceIdentifier,
		EntryClassIdentifier
	> {

	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_sentenceId = new Column("sentenceId", DataType.bigint());
	
	
	// constructors
	
	public StoryVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_sentenceId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_sentenceId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryId, c_sentenceId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryId.getName() + ", " + c_sentenceId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryId, c_sentenceId};
	}

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<SentenceIdentifier> identifier) {
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getSentenceId()};
	}

}
