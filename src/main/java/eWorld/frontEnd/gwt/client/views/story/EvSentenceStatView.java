package eWorld.frontEnd.gwt.client.views.story;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.WidgetCollection;

import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.util.EvDownWidget;
import eWorld.frontEnd.gwt.client.util.EvRatingWidget;
import eWorld.frontEnd.gwt.client.util.EvUpWidget;

public class EvSentenceStatView extends FlowPanel implements HasEvSentenceHandler {

	// attributes
	
	private EvSentenceHandler sentenceHandler;
	
	
	// components
	
	private EvDownWidget downWidget;
	
	private EvUpWidget upWidget;
	
	private EvRatingWidget ratingWidget;
	
	
	// constructors
	
	public EvSentenceStatView(EvSentenceHandler sentenceHandler) {
		assert null != sentenceHandler;
		
		this.sentenceHandler = sentenceHandler;
		
		downWidget = new EvDownWidget(sentenceHandler.getSentence().getEv().getVote());
		upWidget = new EvUpWidget(sentenceHandler.getSentence().getEv().getVote());
		ratingWidget = new EvRatingWidget(sentenceHandler.getSentence().getEv().getRating());
		
		// style
		downWidget.setStyle();
		downWidget.addStyleName(EvStyle.eSentenceStatViewVoteWidget);
		upWidget.setStyle();
		upWidget.addStyleName(EvStyle.eSentenceStatViewVoteWidget);
		ratingWidget.addStyleName(EvStyle.eSentenceStatViewRating);
		
		// compose
		add(downWidget);
		add(upWidget);
		add(ratingWidget);
		
	}
	
	
	// methods
	
	@Override
	public EvSentenceHandler getSentenceHandler() {
		return sentenceHandler;
	}
	
	/**
	 * @return
	 * TODO delete this, widgetCollection should not be public
	 */
	@Deprecated
	public WidgetCollection getWidgets() {
		return super.getChildren();
	}
	
	public void setVoteUpdate(boolean vote) {
		if (false == vote) {
			// downVoted
			downWidget.setVoted();
			upWidget.setDefault();
			
		} else {
			// upVoted
			downWidget.setDefault();
			upWidget.setVoted();
		}
		downWidget.setStyle();
		upWidget.setStyle();
	}
	
	public void setVoteRequested(boolean vote) {
		if (false == vote) {
			// downVoting
			downWidget.setRequested();
			
		} else {
			// upVoting
			upWidget.setRequested();
		}
	}
	
	/**
	 * 
	 * @param increment positive for increment, negative for decrement
	 */
	public void incrementRating(int increment) {
		ratingWidget.incrementRating(increment);
	}
	
}
