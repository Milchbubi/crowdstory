package eWorld.frontEnd.gwt.client.tables;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;

import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.frontEnd.gwt.client.EvApp;
import eWorld.frontEnd.gwt.client.EvAsyncCallback;
import eWorld.frontEnd.gwt.client.EvNameWidget;
import eWorld.frontEnd.gwt.client.EvObserver;
import eWorld.frontEnd.gwt.client.EvStyle;
import eWorld.frontEnd.gwt.client.Images;

public class EvEntryTable extends EvTable<
		EntryClassIdentifier, 
		EntryShortIdentifier,
		Long,
		EvEntry<Ev, EntryShortIdentifier>, 
		EvEntryContainer
	> {

	// column-indices:
	private final int colIndexEntry = getColIndexExtensionStart();
	private final int colIndexIsElement = getColIndexExtensionStart()+1;
	
	
	// attributes
	
	/** called when a row becomes selected */
	private EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected;
	
	/** the index of the row that is currently selected or -1 if no row is selected */
	private int selectedRow = -1;
	
	
	// constructors
	
	public EvEntryTable(EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected) {
		assert null != obsEntrySelected;
		
		this.obsEntrySelected = obsEntrySelected;
		
		// style
		getColumnFormatter().addStyleName(colIndexIsElement, EvStyle.eTableSlimColumn);
		addStyleName(EvStyle.eEntryTable);
		
	}
	
	
	// methods
	
	public void setObservers(EvObserver<EvEntry<EvVoid, EntryShortIdentifier>> obsEntrySelected) {
		assert null != obsEntrySelected;
		
		this.obsEntrySelected = obsEntrySelected;
	}
	
	/**
	 * marks the row of the given entry as selected, 
	 * it should be already ensured, that the item container is already set (getItemContainer() should not be null)
	 * @param entry
	 */
	public void setSelectedRow(EvEntry<Ev, EntryShortIdentifier> entry) {
		assert null != entry;
		
		int index = getItemContainer().getData().indexOf(entry);
		if (-1 < index) {
			setSelectedRow(index +1);
		}
	}
	
	public void deselectSelectedRow() {
		if (-1 != selectedRow) {
			getRowFormatter().removeStyleName(selectedRow, EvStyle.eEntryTableSelectedRow);
			selectedRow = -1;
		}
	}
	
	private void setSelectedRow(int row) {
		
		deselectSelectedRow();
		
		selectedRow = row;
		getRowFormatter().addStyleName(row, EvStyle.eEntryTableSelectedRow);
	}
	
	
	// overridden methods
	
	@Override
	protected String getAdditionalRowStyle() {
		return EvStyle.eEntryTableRow;
	}
	
	@Override
	protected int extendCaptionRow(int row, int startCol) {
		
		setText(row, colIndexEntry, "Entry");
		setText(row, colIndexIsElement, "IsElement");
		
		return startCol+2;
	}

	@Override
	protected int extendDataRow(int row, int startCol, EvEntry<Ev, EntryShortIdentifier> item) {
		
		setWidget(row, colIndexEntry, new EvNameWidget(item.getName(), item.getDescription()));
		
		if (item.isElement()) {
			setWidget(row, colIndexIsElement, new Image(Images.element));
		} else {
			setWidget(row, colIndexIsElement, new Image(Images.directory));
		}
		
		return startCol+2;
	}
	
	@Override
	protected void vote(final EntryShortIdentifier shortIdentifier, final boolean vote, final HTMLTable.Cell cell) {
		assert null != shortIdentifier;
		assert null != cell;
		
		EntryIdentifier completeIdentifier = new EntryIdentifier(getItemContainer().getClassIdentifier(), shortIdentifier);
		
		EvApp.REQ.voteEntry(completeIdentifier, vote, new EvAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				setRowVoteUpdate(vote, cell);
			}
		});
		
		setRowVoteRequested(vote, cell);
	}

	@Override
	protected void rowSelected(EvEntry<Ev, EntryShortIdentifier> item, HTMLTable.Cell cell, ClickEvent event) {
		setSelectedRow(cell.getRowIndex());
		obsEntrySelected.call(EvEntry.voidCast(item));
	}
	
}
