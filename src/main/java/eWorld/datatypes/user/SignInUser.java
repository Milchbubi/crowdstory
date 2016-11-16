package eWorld.datatypes.user;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SignInUser implements Serializable {

	public static final int MIN_PSEUDONYM_LENGTH = 4;
	public static final int MAX_PSEUDONYM_LENGTH = 42;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_PASSWORD_LENGTH = 256;
	
	private String pseudonym;
	
	private String password;
	
	
	/** default constructor for remote procedure call (RPC) */
	public SignInUser() {
	}
	
	public SignInUser(String pseudonym, String password) {
		assert null == checkPseudonym(pseudonym): checkPseudonym(pseudonym);
		assert null == checkPassword(password): checkPassword(password);
		
		this.pseudonym = pseudonym;
		this.password = password;
	}
	
	public String getPseudonym() {
		return pseudonym;
	}
	
	public String getPassword() {
		return password;
	}
	
	/**
	 * checks if the given String can be used as pseudonym
	 * @param pseudonym the String that is to check
	 * @return null if the given String can be used as pseudonym otherwise a message why not
	 */
	public static String checkPseudonym(String pseudonym) {
		if (null == pseudonym) {
			return "pseudonym is null";
		} else if (MIN_PSEUDONYM_LENGTH > pseudonym.trim().length()) {
			return "pseudoym should have at least " + MIN_PSEUDONYM_LENGTH + " characters";
		} else if (MAX_PSEUDONYM_LENGTH < pseudonym.trim().length()) {
			return "pseudoym should have a maximum of " + MAX_PSEUDONYM_LENGTH + " characters";
		} else {
			return null;
		}
	}
	
	/**
	 * checks if the given String can be used as password
	 * @param password the String that is to check
	 * @return null if the given String can be used as password otherwise a message why not
	 */
	public static String checkPassword(String password) {
		if (null == password) {
			return "password is null";
		} else if (MIN_PASSWORD_LENGTH > password.length()) {
			return "password should have at least " + MIN_PASSWORD_LENGTH + " characters";
		} else if (MAX_PASSWORD_LENGTH < password.length()) {
			return "password should have a maximum of " + MAX_PASSWORD_LENGTH +" characters";
		} else {
			return null;
		}
	}
}
