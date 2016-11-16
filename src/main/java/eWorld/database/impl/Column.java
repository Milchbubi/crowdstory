package eWorld.database.impl;

import com.datastax.driver.core.DataType;

public class Column {	
	
	// attributes
	
	private final String name;
	
	private final DataType type;
	
	
	// constructors
	
	public Column(String name, DataType type) {
		assert null != name;
		assert !name.trim().equals("");
		assert null != type;
		
		this.name = name;
		this.type = type;
	}
	
	
	// methods
	
	public String getName() {
		return name;
	}

	public DataType getType() {
		return type;
	}
}
