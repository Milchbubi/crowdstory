package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import eWorld.frontEnd.gwt.client.EvStyle;

/**
 * 
 * @author michael
 * TODO move implementation to EvView, unscramble EvView in 2 subclasses?
 */
public class EvViewCaption extends FlowPanel {

	Image image;
	InlineLabel captionLabel;
	InlineLabel aboutLabel = new InlineLabel();
	Widget additionalWidget = null;
	
	public EvViewCaption(Image image, String caption) {
		assert null != image;
		assert null != caption;
		
		captionLabel = new InlineLabel(caption);
		
		// compose
		add(image);
		add(captionLabel);
		add(aboutLabel);
		
		// style
		setStyleName(EvStyle.eViewCaption);
		captionLabel.setStyleName(EvStyle.eViewCaptionCaption);
	}
	
	public EvViewCaption(Image image, String caption, Widget additionalWidget) {
		this(image, caption);
		
		assert null != additionalWidget;
		
		// compose
		add(additionalWidget);
		
		// style
		additionalWidget.addStyleName(EvStyle.eViewCaptionAdditionalWidget);
	}
	
	public void setCaption(String caption) {
		assert null != caption;
		
		captionLabel.setText(caption);
	}
	
	public void setAbout(String about) {
		assert null != about;
		
		aboutLabel.setText(" about " + about);
	}
	
	/***
	 * TODO delete?
	 */
	@Override
	public void add(Widget widget) {
		super.add(widget);
	}
	
	/***
	 * TODO delete?
	 */
	@Override
	public void add(IsWidget child) {
		super.add(child);
	}
	
}
