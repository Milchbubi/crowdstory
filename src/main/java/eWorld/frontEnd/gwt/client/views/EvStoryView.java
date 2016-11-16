package eWorld.frontEnd.gwt.client.views;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.datepicker.client.DateBox;

import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.settings.EvStoryPanelSettings;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddSentencesForm;
import eWorld.frontEnd.gwt.client.tables.EvSentenceTable;
import eWorld.frontEnd.gwt.client.util.EvImageButton;
import eWorld.frontEnd.gwt.client.util.EvUnfoldWidget;
import eWorld.frontEnd.gwt.client.util.EvToggleButton;
import eWorld.frontEnd.gwt.client.views.story.EvStoryPanel;

public class EvStoryView extends EvView {
	
	// attributes
	
	
	// components
	
	private FlowPanel flowPanel = new FlowPanel();
	
	/** header */
	private EvViewCaption eCaption = new EvViewCaption(new Image(Images.network_grey), "Story");	// TODO change image
	
	private EvToggleButton insertsVisibleToggle = new EvToggleButton("insert");
	private EvToggleButton statsVisibleToggle = new EvToggleButton("vote");
	private EvToggleButton textSizeVaryingToggle = new EvToggleButton("size");
	
	private EvUnfoldWidget filterOptionPanel;
	
	/** views the sentences that are contained by eStory */
	private EvStoryPanel eStoryPanel = new EvStoryPanel(EvApp.getStoryPanelSettings());
	
	
	// handlers
	
	private ValueChangeHandler<Boolean> haInsertsVisibleToggled = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			eStoryPanel.getSettings().setInsertsVisible(event.getValue());
			eStoryPanel.setInsertButtonsVisible();
		}
	};
	
	private ValueChangeHandler<Boolean> haVotesVisibleToggled = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			eStoryPanel.getSettings().setStatsVisible(event.getValue());
			eStoryPanel.setStatViewsVisible();
		}
	};
	
	private ValueChangeHandler<Boolean> haTextSizeVaryingToggled = new ValueChangeHandler<Boolean>() {
		@Override
		public void onValueChange(ValueChangeEvent<Boolean> event) {
			eStoryPanel.getSettings().setTextSizeVarying(event.getValue());
			eStoryPanel.setTextSizeVarying();
		}
	};
	
	
	// constructors
	
	public EvStoryView() {
		
		// init setting-widgets
		insertsVisibleToggle.setValue(eStoryPanel.getSettings().isInsertsVisible());
		statsVisibleToggle.setValue(eStoryPanel.getSettings().isStatsVisible());
		textSizeVaryingToggle.setValue(eStoryPanel.getSettings().isTextSizeVarying());
		filterOptionPanel = new EvUnfoldWidget("LSD | filter", new EvStoryPanelSettingsWidget());
		
		// assemble
		eCaption.add(insertsVisibleToggle);
		eCaption.add(statsVisibleToggle);
		eCaption.add(textSizeVaryingToggle);
		eCaption.add(filterOptionPanel);
		
		flowPanel.add(eCaption);
		flowPanel.add(eStoryPanel);
		setWidget(flowPanel);
		
		// style
		
		
		// handlers
		insertsVisibleToggle.addValueChangeHandler(haInsertsVisibleToggled);
		statsVisibleToggle.addValueChangeHandler(haVotesVisibleToggled);
		textSizeVaryingToggle.addValueChangeHandler(haTextSizeVaryingToggled);
		
	}
	
	
	// methods
	
	public void setContainer(EvStory eStory, String storyName) {
		assert null != eStory;
		assert null != storyName;
		
		eStoryPanel.setStory(eStory);
		eCaption.setCaption(storyName);
	}
	
	/**
	 * settingsWidget for more settings
	 * @author michael
	 *
	 */
	public class EvStoryPanelSettingsWidget extends FlexTable {

		private FlowPanel filterOptionMinRatingPanel = new FlowPanel();
		private InlineLabel filterOptionMinRatingLabel = new InlineLabel("minimum rating ");
		private DoubleBox filterOptionMinRatingInput = new DoubleBox();
		
		private CheckBox highlightMyUpvoted = new CheckBox("highlight");
		private CheckBox filterOptionOnlyMyUpvoted = new CheckBox("only my upvoted");
		
		private CheckBox highlightMyDownvoted = new CheckBox("highlight");
		private CheckBox filterOptionNotMyDownvoted = new CheckBox("not my downvoted");
		
		private CheckBox highlightMyOwn = new CheckBox("highlight");
		private CheckBox filterOptionOnlyMyOwn = new CheckBox("only my own");
		
		private CheckBox highlightNew = new CheckBox("highlight");
		private FlowPanel filterOptionNewSincePanel = new FlowPanel();
		private CheckBox filterOptionNewSinceCheckBox = new CheckBox("new since ");
		private DateBox filterOptionNewSinceInput = new DateBox();
		
		private ValueChangeHandler<Double> haMinRating = new ValueChangeHandler<Double>() {
			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				eStoryPanel.getSettings().setMinRating(event.getValue().floatValue());
				eStoryPanel.setAll();
			}
		};
		
		private ValueChangeHandler<Boolean> haHighlightMyUpvoted = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setHighlightMyUpvoted(event.getValue());
				eStoryPanel.setAll();
			}
		};
		private ValueChangeHandler<Boolean> haOnlyMyUpvoted = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setOnlyMyUpvoted(event.getValue());
				eStoryPanel.setAll();
			}
		};
		
		private ValueChangeHandler<Boolean> haHighlightMyDownvoted = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setHighlightMyDownvoted(event.getValue());
				eStoryPanel.setAll();
			}
		};
		private ValueChangeHandler<Boolean> haNotMyDownvoted = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setNotMyDownvoted(event.getValue());
				eStoryPanel.setAll();
			}
		};
		
		private ValueChangeHandler<Boolean> haHighlightMyOwn = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setHighlightAuthor(event.getValue());
				eStoryPanel.setAll();
			}
		};
		private ValueChangeHandler<Boolean> haOnlyMyOwn = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (true == event.getValue()) {
					eStoryPanel.getSettings().setOnlyAuthor(EvApp.getUserIdentifier());
				} else {
					eStoryPanel.getSettings().setOnlyAuthor(null);
				}
				eStoryPanel.setAll();
			}
		};
		
		private ValueChangeHandler<Boolean> haHighlightNew = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				eStoryPanel.getSettings().setHighlightNew(event.getValue());
				eStoryPanel.setAll();
			}
		};
		private ValueChangeHandler<Boolean> haNewSinceCheckBox = new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (true == event.getValue()) {
					eStoryPanel.getSettings().setNewSince(filterOptionNewSinceInput.getValue().getTime());
				} else {
					eStoryPanel.getSettings().setNewSince(null);
				}
				filterOptionNewSinceInput.setEnabled(event.getValue());
				eStoryPanel.setAll();
			}
		};
		private ValueChangeHandler<Date> haNewSinceInput = new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				eStoryPanel.getSettings().setNewSince(event.getValue().getTime());
				eStoryPanel.setAll();
			}
		};
		
		
		public EvStoryPanelSettingsWidget() {
			
			// init setting-widgets
			filterOptionMinRatingInput.setValue((double)eStoryPanel.getSettings().getMinRating());
			
			highlightMyUpvoted.setValue(eStoryPanel.getSettings().isHighlightMyUpvoted());
			filterOptionOnlyMyUpvoted.setValue(eStoryPanel.getSettings().isOnlyMyUpvoted());
			
			highlightMyDownvoted.setValue(eStoryPanel.getSettings().isHighlightMyDownvoted());
			filterOptionNotMyDownvoted.setValue(eStoryPanel.getSettings().isNotMyDownvoted());
			
			highlightMyOwn.setValue(eStoryPanel.getSettings().isHighlightAuthor());
			if (null != EvApp.getUserIdentifier() && 
					EvApp.getUserIdentifier().equals(eStoryPanel.getSettings().getOnlyAuthor())) {
				filterOptionOnlyMyOwn.setValue(true);
			} else {
				filterOptionOnlyMyOwn.setValue(false);
			}
			
			highlightNew.setValue(eStoryPanel.getSettings().isHighlightNew());
			if (null != eStoryPanel.getSettings().getNewSince()) {
				filterOptionNewSinceCheckBox.setValue(true);
				filterOptionNewSinceInput.setEnabled(true);
				filterOptionNewSinceInput.setValue(new Date(eStoryPanel.getSettings().getNewSince()));
			} else {
				filterOptionNewSinceCheckBox.setValue(false);
				filterOptionNewSinceInput.setEnabled(false);
				Date date = new Date();
				date.setTime((date.getTime() / (1000*60*60*24)) * (1000*60*60*24));
				filterOptionNewSinceInput.setValue(date);
			}
			
			// assemble
			filterOptionMinRatingPanel.add(filterOptionMinRatingLabel);
			filterOptionMinRatingPanel.add(filterOptionMinRatingInput);
			
			filterOptionNewSincePanel.add(filterOptionNewSinceCheckBox);
			filterOptionNewSincePanel.add(filterOptionNewSinceInput);
			
			setWidget(0, 1, filterOptionMinRatingPanel);
			
			setWidget(1, 0, highlightMyUpvoted);
			setWidget(1, 1, filterOptionOnlyMyUpvoted);
			
			setWidget(2, 0, highlightMyDownvoted);
			setWidget(2, 1, filterOptionNotMyDownvoted);
			
			setWidget(3, 0, highlightMyOwn);
			setWidget(3, 1, filterOptionOnlyMyOwn);
			
			setWidget(4, 0, highlightNew);
			setWidget(4, 1, filterOptionNewSincePanel);
			
			// style
			highlightMyUpvoted.addStyleName(EvStyle.eTableHighlightUpvoted);
			highlightMyDownvoted.addStyleName(EvStyle.eTableHighlightDownvoted);
			highlightMyOwn.addStyleName(EvStyle.eTableHighlightOwn);
			highlightNew.addStyleName(EvStyle.eTableHighlightNew);
			
			// handlers
			filterOptionMinRatingInput.addValueChangeHandler(haMinRating);
			
			highlightMyUpvoted.addValueChangeHandler(haHighlightMyUpvoted);
			filterOptionOnlyMyUpvoted.addValueChangeHandler(haOnlyMyUpvoted);
			
			highlightMyDownvoted.addValueChangeHandler(haHighlightMyDownvoted);
			filterOptionNotMyDownvoted.addValueChangeHandler(haNotMyDownvoted);
			
			highlightMyOwn.addValueChangeHandler(haHighlightMyOwn);
			filterOptionOnlyMyOwn.addValueChangeHandler(haOnlyMyOwn);
			
			highlightNew.addValueChangeHandler(haHighlightNew);
			filterOptionNewSinceCheckBox.addValueChangeHandler(haNewSinceCheckBox);
			filterOptionNewSinceInput.addValueChangeHandler(haNewSinceInput);
			
		}
		
	}
	
}
