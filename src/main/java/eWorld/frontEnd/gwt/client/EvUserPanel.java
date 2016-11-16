package eWorld.frontEnd.gwt.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;
import eWorld.frontEnd.gwt.client.util.EvButton;
import eWorld.frontEnd.gwt.client.util.EvSubmitButton;

/**
 * widget where the user can register or sign in,
 * if the user is signed in this widgets displays the user who is signed in
 * @author michael
 *
 */
public class EvUserPanel extends FormPanel {

	// attributes
	
	/** called when user becomes signed in */
	private final EvObserver<Void> obsSignedIn;
	
	/** called when user becomes signed out */
	private final EvObserver<Void> obsSignedOut;
	
	
	// components
	
	/** displayed if user is not signed in  */
	private Grid form = new Grid();
	
	private Label pseudonymLabel = new Label ("Pseudonym:");
	private EvInputBox<TextBox> pseudonymBox = new EvInputBox<TextBox>(new TextBox());
	
	private Label passwordLabel = new Label("Password:");
	private EvInputBox<PasswordTextBox> passwordBox = new EvInputBox<PasswordTextBox>(new PasswordTextBox());
	
	private Label passwordConfLabel = new Label("Confirm Password:");
	private EvInputBox<PasswordTextBox> passwordConfBox = new EvInputBox<PasswordTextBox>(new PasswordTextBox());
	
//	private EvButton signInSubmit = new EvButton("Sign in");
	private EvSubmitButton signInSubmit = new EvSubmitButton("Sign in");
	
//	private EvButton registerSubmit = new EvButton("Register");
	private EvSubmitButton registerSubmit = new EvSubmitButton("Register");
	
	/** change to sign in from (has clickHandler) */
	private Label showSignInForm = new Label("Sign in");
	
	/** change to register form (has clickHandler) */
	private Label showRegisterForm = new Label("Register");
	
	
	/** displayed if user is signed in (instead of form) */
	private VerticalPanel signedInPanel = new VerticalPanel();
	
	/** displays the user who is signed in */
	private Label signedInLabel = new Label();
	
	private EvButton signOutButton = new EvButton("Sign out");
	
	
	public EvUserPanel(EvObserver<Void> obsSignedIn, EvObserver<Void> obsSignedOut) {
		update();
		
		assert null != obsSignedIn;
		assert null != obsSignedOut;
		
		this.obsSignedIn = obsSignedIn;
		this.obsSignedOut = obsSignedOut;
		
		// style
		this.addStyleName(EvStyle.eUserPanel);
		pseudonymLabel.addStyleName(EvStyle.eUserPanelInputLabel);
		passwordLabel.addStyleName(EvStyle.eUserPanelInputLabel);
		passwordConfLabel.addStyleName(EvStyle.eUserPanelInputLabel);
		pseudonymBox.addStyleName(EvStyle.eUserPanelInputBox);
		passwordBox.addStyleName(EvStyle.eUserPanelInputBox);
		passwordConfBox.addStyleName(EvStyle.eUserPanelInputBox);
		showSignInForm.addStyleName(EvStyle.eUserPanelShowFormLabel);
		showRegisterForm.addStyleName(EvStyle.eUserPanelShowFormLabel);
		signedInPanel.addStyleName(EvStyle.eUserPanelSignedInPanel);
		signedInLabel.addStyleName(EvStyle.eUserPanelSignedInLabel);
		signOutButton.addStyleName(EvStyle.eUserPanelSignOutButton);
		
		// handlers
		
		signInSubmit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				signIn();
			}
		});
		
		registerSubmit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				register();
			}
		});
		
		showSignInForm.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				initForm(false);
				setWidget(form);
			}
		});
		
		showRegisterForm.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				initForm(true);
				setWidget(form);
			}
		});
		
		signOutButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				signOut();
			}
		});
	}
	
	/**
	 * updates the widget
	 */
	private void update() {
		EvApp.REQ.getSignedInUser(new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				
				EvApp.setUser(result);
				
				if (null == result) {
					initForm(false);
					setWidget(form);
				} else {
					showSignedIn(result);
				}
			}
		});
	}
	
	/**
	 * called when user has signed in
	 * @param user
	 */
	private void showSignedIn(User<UserIdentifier> user) {
		assert null != user;
		
		signedInLabel.setText(/*"signed in as " + */user.getPseudonym());
		
		initSignedInPanel();
		
		setWidget(signedInPanel);
	}
	
	/**
	 * called after user has registered
	 */
	private void showRegistered() {
		clearTotalForm();
		
		initForm(false);
		
		setWidget(form);
	}
	
	private void initSignedInPanel() {
		signedInPanel.clear();
		signedInPanel.add(signedInLabel);
		signedInPanel.add(signOutButton);
	}
	
	/**
	 * @param register false for login form, true for register form
	 */
	private void initForm(boolean register) {
		if (false == register) {
			form.resize(4, 2);
		} else {
			form.resize(5, 2);
		}
		
		int i = 0;
		
		form.setWidget(i, 0, pseudonymLabel);
		form.setWidget(i++, 1, pseudonymBox);
		
		form.setWidget(i, 0, passwordLabel);
		form.setWidget(i++, 1, passwordBox);
		
		if (true == register) {
			form.setWidget(i, 0, passwordConfLabel);
			form.setWidget(i++, 1, passwordConfBox);
		} else {
			form.clearCell(i, 0);
		}
		
		if (false == register) {
			form.setWidget(i++, 1, signInSubmit);
			form.setWidget(i++, 1, showRegisterForm);
		} else {
			form.setWidget(i++, 1, registerSubmit);
			form.setWidget(i++, 1, showSignInForm);
		}
		
		// focus pseudonymBox
//		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//			@Override
//			public void execute() {
//				pseudonymBox.focusInputBox();
//			}
//		});
	}
	
	/**
	 * @return value or null if input is not correct
	 */
	private String checkAndGetPseudonymBoxValue() {
		String pseudonym = pseudonymBox.getInputBox().getValue();
		String problem = SignInUser.checkPseudonym(pseudonym);
		
		if (null == problem) {
			pseudonymBox.clearLabel();
			return pseudonym;
		} else {
			pseudonymBox.setLabel(problem);
			return null;
		}
	}
	
	/**
	 * @return value or null if input is not correct
	 */
	private String checkAndGetPasswordBoxValue() {
		String password = passwordBox.getInputBox().getValue();
		String problem = SignInUser.checkPassword(password);
		
		if (null == problem) {
			passwordBox.clearLabel();
			return password;
		} else {
			passwordBox.setLabel(problem);
			return null;
		}
	}
	
	/**
	 * @return value or null if input is not correct
	 */
	private String checkAndGetPasswordConfBoxValue() {
		String passwordConf = passwordConfBox.getInputBox().getValue();
		
		if (passwordConf.equals(passwordBox.getInputBox().getValue())) {
			passwordConfBox.clearLabel();
			return passwordConf;
		} else {
			passwordConfBox.setLabel("confirm password should be equal to password");
			return null;
		}
	}
	
	private void clearTotalForm() {
		pseudonymBox.getInputBox().setValue("");
		clearPasswords();
	}
	
	private void clearPasswords() {
		passwordBox.getInputBox().setValue("");
		passwordConfBox.getInputBox().setValue("");
	}
	
	/**
	 * @return user or null if input is not correct
	 */
	private SignInUser constructSignInUser() {
		String pseudonym = checkAndGetPseudonymBoxValue();
		String password = checkAndGetPasswordBoxValue();
		
		if (null == pseudonym || null == password) {
			return null;
		} else {
			return new SignInUser(pseudonym, password);
		}
	}
	
	/**
	 * @return user or null if input is not correct
	 */
	private RegisterUser constructRegisterUser() {
		String pseudonym = checkAndGetPseudonymBoxValue();
		String password = checkAndGetPasswordBoxValue();
		String passwordConf = checkAndGetPasswordConfBoxValue();
		
		if (null == pseudonym || null == password || null == passwordConf) {
			return null;
		} else {
			return new RegisterUser(pseudonym, password);
		}
	}
	
	/**
	 * signs the user in if input is valid otherwise does nothing
	 * showing notes is done by other methods
	 */
	private void signIn() {
		SignInUser user = constructSignInUser();
		if (null != user) {
			signInReq(user);
			clearPasswords();
		}
	}
	
	private void signInReq(SignInUser user) {
		assert null != user;
		
		EvApp.REQ.signIn(user, new EvAsyncCallback<User<UserIdentifier>>() {
			@Override
			public void onSuccess(User<UserIdentifier> result) {
				assert null != result;
				
				EvApp.setUser(result);
				
				obsSignedIn.call(null);
				showSignedIn(result);
				EvApp.INFO.addSuccess("Successfully signed in, you can now evaluate the world");
			}
		});
	}
	
	/**
	 * registers the user if input is valid otherwise does nothing
	 * showing notes is done by other methods
	 */
	private void register() {
		final RegisterUser user = constructRegisterUser();
		if (null != user) {
			EvApp.REQ.register(user, new EvAsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					showRegistered();
					EvApp.INFO.addSuccess("Successfully registered, you can now sign in");
					signInReq(user);
				}
			});
			
			clearPasswords();
		}
	}
	
	/**
	 * signs the user out
	 */
	private void signOut() {
		EvApp.REQ.signOut(new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				
				obsSignedOut.call(null);
				update();
				EvApp.INFO.addSuccess("Successfully signed out");
			}
		});
	}
}
