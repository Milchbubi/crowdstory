package eWorld.database.impl;

import com.datastax.driver.core.Session;

import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

public abstract class CounterSchema <
		COMPLETE_IDENT extends EvCompleteIdentifier,
		SUPER_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<?>
	> extends EvSchema<COMPLETE_IDENT, SUPER_IDENT, SHORT_IDENT> {

	public CounterSchema(Session session, String schemaName) {
		super(session, schemaName);
	}

}
