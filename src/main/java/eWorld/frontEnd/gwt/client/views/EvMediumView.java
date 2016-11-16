package eWorld.frontEnd.gwt.client.views;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.frontEnd.gwt.client.EvDialog;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;
import eWorld.frontEnd.gwt.client.forms.EvAddMediumForm;
import eWorld.frontEnd.gwt.client.tables.EvMediumTable;
import eWorld.frontEnd.gwt.client.util.EvImageButton;

public class EvMediumView extends EvView {

	// attributes
	
	/** contains the media that are viewed by eMediumTable TODO delete*/
	private EvMediumContainer eMediumContainer = null;
	
	
	// handlers
	
	private ClickHandler haAddMedium = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			
			// dialog that pops up
			final EvDialog dialog = new EvDialog("Add Image");
			
			// the content of the pop up, observer is called when pop up becomes closed (cancel(false) and add(true))
			EvAddMediumForm addForm = new EvAddMediumForm(
					eMediumContainer.getClassIdentifier(), 
					new EvObserver<EvMedium<Ev, MediumIdentifier>>() {
				@Override
				public void call(EvMedium<Ev, MediumIdentifier> value) {
					dialog.hide();
					
					if (null != value) {
						eMediumTable.addItem(EvMedium.shortCast(value));
					}
				}
			});
			
			// initialize dialog
			dialog.setWidget(addForm);
		}
	};
	
	private ClickHandler haMediaBoxClose = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			mediaBox.setVisible(false);
		}
	};
	
	
	// callbacks
	
	
	// observers
	
	EvObserver<EvMedium<Ev, MediumShortIdentifier>> obsMediumSelected = new EvObserver<EvMedium<Ev, MediumShortIdentifier>>() {
		@Override
		public void call(EvMedium<Ev, MediumShortIdentifier> value) {
			setMediaBox(value);
		}
	};
	
	
	// components
	
	private FlowPanel flowPanel = new FlowPanel();
	
	private HorizontalPanel horizontalPanel = new HorizontalPanel();
	
	/** button to add media */
	private EvImageButton addMediumButton = new EvImageButton(new Image(Images.add));
	
	private EvViewCaption eCaption = new EvViewCaption(new Image(Images.network_grey), "Media", addMediumButton);	// TODO change image
	
	/** views the media that are contained by eMediumContainer */
	private EvMediumTable eMediumTable;
	
	/** displays the selected medium */
	private FlowPanel mediaBox = new FlowPanel();	// TODO set style "display: absolute" and only display onHover
	
	/** button to close mediaBox */
	private EvImageButton mediaBoxClose = new EvImageButton(new Image(Images.close));
	
	
	// constructors
	
	public EvMediumView() {
		
		eMediumTable = new EvMediumTable(obsMediumSelected);
		
		// compose
		horizontalPanel.add(eMediumTable);
		horizontalPanel.add(mediaBox);
		flowPanel.add(eCaption);
		flowPanel.add(horizontalPanel);
		setWidget(flowPanel);
		
		// style
		mediaBox.setVisible(false);
		mediaBox.addStyleName(EvStyle.eMediumViewMediaBox);
		mediaBoxClose.addStyleName(EvStyle.eMediumViewMediaBoxClose);
		
		// handlers
		addMediumButton.addClickHandler(haAddMedium);
		mediaBoxClose.addClickHandler(haMediaBoxClose);
	}
	
	
	// methods
	
	public void setContainer(EvMediumContainer eMediumContainer) {
		assert null != eMediumContainer;
		
		this.eMediumContainer = eMediumContainer;
		
		eMediumTable.setItemContainer(eMediumContainer, true);
	}
	
	private void setMediaBox(EvMedium<Ev, MediumShortIdentifier> medium) {
		assert null != medium;
		
		mediaBox.clear();
		
		Image image = new Image(medium.getLink());
//		HTML image = new HTML("<img src=\"" + medium.getLink() + "\" alt=\"failed to load image\">");
//		HTML image = new HTML("<img src=\"" + medium.getLink() + "\">");	// problematically because of style
		image.setStyleName(EvStyle.eMediumViewImage);
		
		mediaBox.add(mediaBoxClose);
		mediaBox.add(image);
		mediaBox.add(new Label("Link: " + medium.getLink()));
		mediaBox.add(new Label("Description: " + medium.getDescriptionString()));
		
		mediaBox.setVisible(true);
	}

}
