package eWorld.frontEnd.gwt.client.views.story;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.Images;

public class EvInsertSentenceButton extends Image {
	
	private final Long afterPosition;
	
	private final Long beforePosition;
	
	
	/**
	 * 
	 * @param afterPosition null if there is no sentence after
	 * @param beforePosition null if there is no sentence before
	 */
	public EvInsertSentenceButton(Long afterPosition, Long beforePosition) {
		super(Images.insert);
		
		this.afterPosition = afterPosition;
		this.beforePosition = beforePosition;
	}
	
	
	public Long getAfterPosition() {
		return afterPosition;
	}
	
	public Long getBeforePosition() {
		return beforePosition;
	}
	
}
