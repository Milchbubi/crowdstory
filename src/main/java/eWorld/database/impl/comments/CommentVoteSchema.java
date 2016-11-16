package eWorld.database.impl.comments;

import com.datastax.driver.core.Session;

import eWorld.database.impl.BooleanVoteSchema;
import eWorld.database.impl.Column;
import eWorld.database.impl.DB;
import eWorld.database.impl.IdGenerator;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class CommentVoteSchema extends BooleanVoteSchema<
		VoteIdentifier<CommentIdentifier>,
		VoteIdentifier<EntryClassIdentifier>,
		CommentShortIdentifier,
		CommentIdentifier,
		EntryClassIdentifier
	> {

	// static finals
	
	protected static final Column c_entryId = new Column("entryId", IdGenerator.idType);
	
	protected static final Column c_commentId = new Column("commentId", IdGenerator.idType);
	
	
	// constructors
	
	public CommentVoteSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

	
	// methods
	
	
	// overridden methods

	@Override
	protected Column[] getListItemsSelectColumns() {
		return new Column[] {c_commentId, c_vote};
	}

	@Override
	protected Column[] getListItemsWhereColumns() {
		return new Column[] {c_userId, c_entryId};
	}

	@Override
	protected Column getShortIdentifierColumn() {
		return c_commentId;
	}

	@Override
	protected Object[] getSuperIdentifierValues(
			VoteIdentifier<EntryClassIdentifier> superIdentifier) {
		return new Long[] {superIdentifier.getUserIdent().getUserId(), superIdentifier.getEvIdent().getEntryClassId()};
	}

	@Override
	protected Column[] getColumns() {
		return new Column[] {c_userId, c_entryId, c_commentId, c_vote};
	}

	@Override
	protected String getPrimaryKey() {
		return c_userId.getName() + ", " + c_entryId.getName() + ", " + c_commentId.getName();
	}

	@Override
	protected Column[] getIdentifierColumns() {
		return new Column[] {c_userId, c_entryId, c_commentId};
	}

	@Override
	protected Object[] getIdentifierValues(
			VoteIdentifier<CommentIdentifier> identifier) {
		return new Long[] {identifier.getUserIdent().getUserId(),
				identifier.getEvIdent().getEntryClassId(), identifier.getEvIdent().getCommentId()};
	}
	
	@Override
	protected String getCreateSchemaWithPart() {
		return "WITH CLUSTERING ORDER BY ("  + c_entryId.getName() + " ASC, " + c_commentId.getName() + " DESC)";
	}

}
