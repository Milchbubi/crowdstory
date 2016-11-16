package eWorld.frontEnd.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.settings.EvStoryPanelSettings;
import eWorld.datatypes.user.User;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EvApp implements EntryPoint {

	/** globally used for server requests */
	public static final DataCache REQ = new DataCache();
	
	/** globally used to show some info */
	public static final EvInfoPanel INFO = new EvInfoPanel();
	
	/** the user that is currently signed in, null if user is not signed in, set by EvUserPanel */
	private static User<UserIdentifier> user = null;
	
	/** settings for EvStoryPanel */
	private static EvStoryPanelSettings storyPanelSettings = new EvStoryPanelSettings();
	
	private EvUserInterface eWorld = new EvUserInterface();
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    
	    // Associate the EvWorld widget with the HTML host page.
	    RootPanel.get("eWorld").add(eWorld);
	    
	}
	
	/**
	 * @return the user that is currently signed in, null if user is not signed in
	 */
	public static User<UserIdentifier> getUser() {
		return user;
	}
	
	/**
	 * @return the user that is currently signed in, null if user is not signed in
	 */
	public static UserIdentifier getUserIdentifier() {
		if (null == user) {
			return null;
		} else {
			return user.getIdentifier();
		}
	}
	
	/**
	 * ONLY TO USE BY EvUserPanel
	 * @param user the user that is currently signed in, null if user is not signed in
	 */
	public static void setUser(User<UserIdentifier> user) {
		EvApp.user = user;
		storyPanelSettings = new EvStoryPanelSettings();	// refresh storyPanelSettings because of userDependent stuff
	}
	
	/**
	 * @return settings for EvStoryPanel
	 */
	public static EvStoryPanelSettings getStoryPanelSettings() {
		return storyPanelSettings;
	}
	
}
