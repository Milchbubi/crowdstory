package eWorld.datatypes.user;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RegisterUser extends SignInUser implements Serializable {

	/** default constructor for remote procedure call (RPC) */
	public RegisterUser() {
	}
	
	public RegisterUser(String pseudonym, String password) {
		super(pseudonym, password);
	}
}
