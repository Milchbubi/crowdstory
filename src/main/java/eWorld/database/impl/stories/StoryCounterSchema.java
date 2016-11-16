package eWorld.database.impl.stories;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;

public class StoryCounterSchema extends UpDownCounterSchema<
		SentenceIdentifier,
		EntryClassIdentifier,
		SentenceShortIdentifier
	> {

	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_sentenceId = new Column("sentenceId", DataType.bigint());
	
	
	// constructors
	
	public StoryCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_sentenceId, c_up, c_down};
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
		return new Column[] {c_entryId, c_sentenceId, c_up, c_down};
	}

	@Override
	protected String getPrimaryKey() {
		return c_entryId.getName() + ", " + c_sentenceId.getName();
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
