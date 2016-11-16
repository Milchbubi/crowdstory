package eWorld.datatypes.containers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import eWorld.datatypes.data.IdDataType;
import eWorld.datatypes.identifiers.EvCompleteIdentifier;
import eWorld.datatypes.identifiers.EvShortIdentifier;

@SuppressWarnings("serial")
public class IdDataTypeContainer<
		CLASS_IDENT extends EvCompleteIdentifier,
		SHORT_IDENT extends EvShortIdentifier<SHORT_IDENT_TYPE>,
		SHORT_IDENT_TYPE,
		DATA_TYPE extends IdDataType<SHORT_IDENT>
	>
	implements Serializable {
	
	// attributes
	
	private CLASS_IDENT classIdentifier;
	
	/** time when data was last time updated, null for not specified */
	private Date timestamp = null;
	
	/** the data the container stores */
	private ArrayList<DATA_TYPE> data;
	
	
	// constructors
	
	/** default constructor for remote procedure call (RPC) */
	public IdDataTypeContainer() {
	}
	
	public IdDataTypeContainer(CLASS_IDENT classIdentifier, ArrayList<DATA_TYPE> data) {
		assert null != classIdentifier;
		assert null != data;
		
		this.classIdentifier = classIdentifier;
		this.data = data;
	}
	
	
	// methods
	
	public CLASS_IDENT getClassIdentifier() {
		assert null != classIdentifier;
		
		return classIdentifier;
	}
	
	public ArrayList<DATA_TYPE> getData() {
		assert null != data;
		
		return data;
	}
	
	public void addItem(DATA_TYPE item) {
		data.add(item);
	}

}
