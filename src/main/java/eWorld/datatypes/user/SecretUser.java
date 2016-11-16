package eWorld.datatypes.user;

/**
 * don't implement Serializable TODO? move to other package?
 * @author michael
 * 
 */
public class SecretUser {

	private AdvancedUser user;
	
	private String passwordSalt;
	
	private String passwordHash;
	
	
	/**
	 * 
	 * @param user
	 * @param passwordSalt the salt of the password
	 * @param passwordHash the with the given salt hashed password
	 */
	public SecretUser(AdvancedUser user, String passwordSalt, String passwordHash) {
		assert null != user;
		assert null != passwordSalt;
		assert null != passwordHash;
		
		this.user = user;
		this.passwordSalt = passwordSalt;
		this.passwordHash = passwordHash;
	}
	
	
	public AdvancedUser getUser() {
		return user;
	}
	
	public String getPasswordSalt() {
		return passwordSalt;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
}
