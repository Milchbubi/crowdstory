package eWorld.frontEnd.gwt.client.tables;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;

public class EvMediumTable extends EvTable<
		EntryClassIdentifier,
		MediumShortIdentifier,
		Long,
		EvMedium<Ev, MediumShortIdentifier>,
		EvMediumContainer
	> {

	// attributes
	
	private final EvObserver<EvMedium<Ev, MediumShortIdentifier>> obsRowSelected;
	
	
	// constructors
	
	/**
	 * 
	 * @param obsRowSelected called when an item becomes selected, parameter is the selected item
	 */
	public EvMediumTable(EvObserver<EvMedium<Ev, MediumShortIdentifier>> obsRowSelected) {
		assert null != obsRowSelected;
		
		this.obsRowSelected = obsRowSelected;
		
		// style
		this.addStyleName(EvStyle.eMediumTable);
	}
	
	
	// overridden methods
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eMediumTableRow;
	}
	
	@Override
	protected int extendCaptionRow(int row, int startCol) {
		
		int col = startCol;
		
		setText(row, col++, "Image");
		
		return col;
	}

	@Override
	protected int extendDataRow(int row, int startCol,
			EvMedium<Ev, MediumShortIdentifier> item) {

		int col = startCol;
		
		Image image = new Image(item.getLink());
//		HTML image = new HTML("<img src=\"" + item.getLink() + "\" alt=\"failed to load image: " + item.getLink() + "\">");
//		HTML image = new HTML("<img src=\"" + item.getLink() + "\">");	// problematically because of style
		image.setStyleName(EvStyle.eMediumTableImage);
		setWidget(row, col++, image);
		
		return col;
	}

	@Override
	protected void vote(MediumShortIdentifier shortIdentifier, final boolean vote,
			final Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		MediumIdentifier completeIdentifier = new MediumIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteMedium(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(EvMedium<Ev, MediumShortIdentifier> item, HTMLTable.Cell cell, ClickEvent event) {
		obsRowSelected.call(item);
	}

}
