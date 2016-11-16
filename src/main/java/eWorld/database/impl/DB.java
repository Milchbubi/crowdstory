package eWorld.database.impl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * DB for DataBase
 * initializes the database, checks if all tables are correct, creates them if they don't exist
 * closes the connection to the database cluster
 * @author michael
 *
 */
public class DB {

	// static finals
	
	/** keyspace in which users are stored */
	public static final String USER_KEYSPACE = "e_users";
	
	/** keyspace in which the rest is stored */
	public static final String CROWDSTORY_KEYSPACE = "crowdstory";
	
	/** table in which users are stored */
	public static final String USER_SCHEMA = "users";
	
	/** tables in which entries are stored */
	public static final String ENTRY_REGISTER_SCHEMA = "entries_register";
	public static final String ENTRY_DATA_SCHEMA = "entries_data";
	public static final String ENTRY_COUNTER_SCHEMA = "entries_counter";
	public static final String ENTRY_VOTE_SCHEMA = "entries_vote";
	
	/** tables in which media like images are stored */
	public static final String MEDIUM_DATA_SCHEMA = "media_data";
	public static final String MEDIUM_COUNTER_SCHEMA = "media_counter";
	public static final String MEDIUM_VOTE_SCHEMA = "media_vote";
	
	/** tables in which comments are stored */
	public static final String COMMENT_DATA_SCHEMA = "comments_data";
	public static final String COMMENT_COUNTER_SCHEMA = "comments_counter";
	public static final String COMMENT_VOTE_SCHEMA = "comments_vote";
	
	/** tables in which stories are stored */
	public static final String STORY_DATA_SCHEMA = "stories_data";
	public static final String STORY_COUNTER_SCHEMA = "stories_counter";
	public static final String STORY_VOTE_SCHEMA = "stories_vote";
	
	
	// final attributes
	
	/** IP-address of the connected node */
	private final String nodeAddress;
	
	/** connected cluster */
	private final Cluster cluster;
	
	/** session to access (only!) user_keyspace */
	private final Session user_keyspace_session;
	
	/** session to access (only!) crowdstory_keyspace */
	private final Session crowdstory_keyspace_session;
	
	// methods
	
	/**
	 * initializes the database, checks if keyspaces exist, creates them if not
	 * @param nodeAddress IP-address of the node to connect to
	 */
	public DB(final String nodeAddress) {
		assert null != nodeAddress;
		
		this.nodeAddress = nodeAddress;
		
		// connect & print out clusterMetadata
		cluster = Cluster.builder()
				.addContactPoint(nodeAddress)
				.build();
		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n", 
				metadata.getClusterName());
		for ( Host host : metadata.getAllHosts() ) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
		
		user_keyspace_session = cluster.connect();
		init_user_keyspace();
		
		crowdstory_keyspace_session = cluster.connect();
		init_crowdstory_keyspace();
	}
	
	/**
	 * initializes the user_keyspace
	 */
	private void init_user_keyspace() {
		assert null != user_keyspace_session;
		
		user_keyspace_session.execute("CREATE KEYSPACE IF NOT EXISTS " +
				USER_KEYSPACE +
				" WITH replication = {'class':'SimpleStrategy', 'replication_factor':5};");
		
		System.out.println("success: Keyspace '" + USER_KEYSPACE + "' initialized");	// TODO-note vary if created or already existed
		
		user_keyspace_session.execute("USE " + USER_KEYSPACE);	// keyspace of this session should never be changed
	}
	
	/**
	 * initializes the crowdstory_keyspace
	 */
	private void init_crowdstory_keyspace() {
		assert null != crowdstory_keyspace_session;
		
		crowdstory_keyspace_session.execute("CREATE KEYSPACE IF NOT EXISTS " +
				CROWDSTORY_KEYSPACE +
				" WITH replication = {'class':'SimpleStrategy', 'replication_factor':3};");
		
		System.out.println("success: Keyspace '" + CROWDSTORY_KEYSPACE + "' initialized");	// TODO-note vary if created or already existed
		
		crowdstory_keyspace_session.execute("USE " + CROWDSTORY_KEYSPACE);	// keyspace of this session should never be changed
	}
	
	/**
	 * closes the connection to the database cluster
	 */
	public void close() {
		assert null != cluster;
		
		cluster.close();
		
		System.out.println("success: connection to database cluster closed");
	}
	
	/**
	 * @return session to access (only!) user_keyspace
	 */
	public Session getUserKeyspaceSession() {
		assert null != user_keyspace_session;
		
		return user_keyspace_session;
	}
	
	/**
	 * @return session to access (only!) crowdstory_keyspace
	 */
	public Session getCrowdstoryKeyspaceSession() {
		assert null != crowdstory_keyspace_session;
		
		return crowdstory_keyspace_session;
	}
}
