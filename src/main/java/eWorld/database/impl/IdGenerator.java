package eWorld.database.impl;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Session;

/**
 * generates unique(should) ids for a certain schema
 * @author michael
 * FIXME the way of getting ids is stupid, can collide
 */
public class IdGenerator {

	// static finals
	
	/** cql-type of the generated ids, must fit to to returnType of method generateId() */
	public static final DataType idType = DataType.bigint();
	
	
	// attributes
	
	/** session to execute cql-statements, must fit to the keyspace of schemaName */
	private final Session session;
	
	/** cql-name of the schema for which ids should be generated */
	private final String schemaName;
	
	
	// constructors
	
	/**
	 * 
	 * @param session session to execute cql-statements, must fit to the keyspace of schemaName
	 * @param schemaName cql-name of the schema for which ids should be generated
	 */
	public IdGenerator(Session session, String schemaName) {
		assert null != session;
		assert null != schemaName;
		
		this.session = session;
		this.schemaName = schemaName;
	}
	
	
	// methods
	
	/**
	 * 
	 * @return generated id
	 * FIXME the way of getting ids is stupid, can collide
	 */
	public long generateId() {
		return System.currentTimeMillis();
	}
}
