package eWorld.database.impl.entries;

import java.util.ArrayList;

import eWorld.database.impl.RatedSchemaGate;
import eWorld.datatypes.EvPath;
import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.elementars.UpDownVote;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;

public class EntrySchemaGate extends RatedSchemaGate<
		EntryIdentifier,
		EntryClassIdentifier,
		EntryShortIdentifier,
		Long,
		EvEntry<Ev, EntryIdentifier>,
		EvEntry<?, EntryClassIdentifier>,
		EvEntry<Ev, EntryShortIdentifier>,
		EvEntryContainer> {

	// attributes
	
	private final EntryRegisterSchema registerSchema;
	private final EntryDataSchema dataSchema;
	private final EntryCounterSchema counterSchema;
	private final EntryVoteSchema voteSchema;
	
	
	// constructors
	
	public EntrySchemaGate(EntryRegisterSchema registerSchema, EntryDataSchema dataSchema, EntryCounterSchema counterSchema, EntryVoteSchema voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		assert null != registerSchema;
		assert null != dataSchema;
		assert null != counterSchema;
		assert null != voteSchema;
		
		this.registerSchema = registerSchema;
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}
	
	
	// methods
	
	/**
	 * returns the path of an entry that is specified by the given identifier
	 * @param identifier specifies the entry
	 * @return the path that leads to the entry
	 */
	public EvPath getPath(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		EvPath path = new EvPath();
		
		int i = 0;
		EntryShortIdentifier shortIdentifier = identifier;
		
		for (; EntryRegisterSchema.SUPER_ROOT_ID != shortIdentifier.getEntryId() && i < 42; i++) {			
			EntryClassIdentifier classIdentifier = registerSchema.getClassIdentifier(shortIdentifier);
			EvEntry<Ev, EntryIdentifier> entry = dataSchema.selectOne(new EntryIdentifier(classIdentifier, shortIdentifier));
			
			path.addFront(EvEntry.voidCast(EvEntry.shortCast(entry)));	// TODO this kind of casting is stupid
			shortIdentifier = new EntryShortIdentifier(classIdentifier.getEntryClassId());
		}
		
		if (i >= 42) {
			System.out.println("42, warning very depth path (EntrySchemaGate.getPath())");	// TODO-log
		}
		
		return path;
	}
	
	/**
	 * 
	 * @param userIdentifier
	 * @param identifier
	 * @return entry that is filled with all existing information about it
	 */
	public EvEntry<Ev, EntryIdentifier> getFullEntry(UserIdentifier userIdentifier, EntryIdentifier identifier) {
		assert null != identifier;
		
		EvEntry<Ev, EntryIdentifier> entry = dataSchema.selectOne(identifier);
		
		entry.getEv().setSummarizedVotes(counterSchema.selectOne(entry.getIdentifier()));
		
		if (null != userIdentifier) {
			// load vote if user is given
			entry.getEv().setVote(voteSchema.getBooleanVote(new VoteIdentifier<EntryIdentifier>(userIdentifier, entry.getIdentifier())));
		} else {
			entry.getEv().setVote(UpDownVote.OPEN);
		}
		
		return entry;
	}
	
	
	// overridden methods

	@Override
	protected EvEntryContainer constructEvDataTypeContainer(
			EntryClassIdentifier classIdentifier,
			ArrayList<EvEntry<Ev, EntryShortIdentifier>> data) {
		
		return new EvEntryContainer(
				classIdentifier,
				data
				);
	}
}
