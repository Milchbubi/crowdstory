package eWorld.database.impl.comments;

import com.datastax.driver.core.Session;

import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.database.impl.UpDownCounterSchema;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;

public class CommentCounterSchema extends UpDownCounterSchema<
		CommentIdentifier,
		EntryClassIdentifier,
		CommentShortIdentifier
	> {

	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_commentId = new Column("commentId", IdGenerator.idType);
	
	
	// constructors
	
	public CommentCounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}


	// methods
	
	
	// overridden methods
	
	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_commentId, c_up, c_down};
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
		return new Column[] {c_entryId, c_commentId, c_up, c_down};
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

}
