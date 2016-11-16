package eWorld.frontEnd.gwt.client.util;

import com.google.gwt.user.client.ui.InlineLabel;

import eWorld.datatypes.evs.Ev;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvRatingWidget extends InlineLabel {
	
	private float rating;
	
	public EvRatingWidget(float rating) {
		this.rating = rating;
		
		set();
	}
	
	private void set() {
		if (rating > 0) {
			setText("+" + String.valueOf(rating));
			removeStyleName(EvStyle.eRatingWidgetNegative);
			addStyleName(EvStyle.eRatingWidgetPositive);
		} else if (rating < 0) {
			setText(String.valueOf(rating));
			removeStyleName(EvStyle.eRatingWidgetPositive);
			addStyleName(EvStyle.eRatingWidgetNegative);
		} else {
			setText(String.valueOf(rating));
			removeStyleName(EvStyle.eRatingWidgetPositive);
			removeStyleName(EvStyle.eRatingWidgetNegative);
		}
	}
	
	/**
	 * 
	 * @param increment positive for increment, negative for decrement
	 * TODO this is never used correctly, it is never considered if user votes first time changed his mind
	 */
	public void incrementRating(int increment) {
		rating += increment;
		
		set();
	}
}
