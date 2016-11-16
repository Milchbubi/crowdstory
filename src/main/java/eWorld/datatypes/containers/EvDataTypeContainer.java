package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;

import eWorld.datatypes.data.EvDataType;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

@SuppressWarnings("serial")
public abstract class EvDataTypeContainer<
		CLASS_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_TYPE>,
		SHORT_IDENT_TYPE,
		DATA_TYPE extends EvDataType<Ev, SHORT_IDENT>
	>
	extends IdDataTypeContainer<CLASS_IDENT, SHORT_IDENT, SHORT_IDENT_TYPE, DATA_TYPE>
	implements Serializable {
	
	// attributes
	
	
	// constructors

	/** default constructor for remote procedure call (RPC) */
	public EvDataTypeContainer() {
	}
	
	public EvDataTypeContainer(CLASS_IDENT classIdentifier, ArrayList<DATA_TYPE> data) {
		super(classIdentifier, data);
	}
	
	
	// methods
	
}
