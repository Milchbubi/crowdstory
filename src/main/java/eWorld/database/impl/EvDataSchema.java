package eWorld.database.impl;

import com.datastax.driver.core.Session;

import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class EvDataSchema <
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_COLUMN_TYPE>,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED extends EvDataType<Ev, COMPLETE_IDENT>,
		DATA_SUPER_IDENTIFIED extends EvDataType<?, SUPER_IDENT>,
		DATA_SHORT_IDENTIFIED extends EvDataType<Ev, SHORT_IDENT>
	> extends RatedDataSchema<
		COMPLETE_IDENT, 
		SUPER_IDENT, 
		SHORT_IDENT,
		SHORT_IDENT_COLUMN_TYPE,
		DATA_COMPLETE_IDENTIFIED,
		DATA_SUPER_IDENTIFIED,
		DATA_SHORT_IDENTIFIED
	> {

	// static finals
	
	protected static final Column c_authorId = new Column("authorId", IdGenerator.idType);
	
	
	// constructors
	
	public EvDataSchema(Session session, String schemaName) {
		super(session, schemaName);
		
		assert Util.contains(getColumns(), c_authorId);
	}

	
	// overridden methods

	@Override
	protected void createIndices() {
		super.createIndices();
		
		// index for authorId
		session.execute(
				"CREATE INDEX IF NOT EXISTS " + getSchemaName() + "_" + c_authorId.getName() + "_index"
				+ " ON " + getSchemaName()
				+ "(" + c_authorId.getName() + ")"
				+ ";");
		
	}
}
