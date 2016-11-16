package eWorld.database.impl.media;

import java.util.ArrayList;

import eWorld.database.impl.RatedSchemaGate;
import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;

public class MediumSchemaGate extends RatedSchemaGate<
		MediumIdentifier,
		EntryClassIdentifier,
		MediumShortIdentifier,
		Long,
		EvMedium<Ev, MediumIdentifier>,
		EvMedium<?, EntryClassIdentifier>,
		EvMedium<Ev, MediumShortIdentifier>,
		EvMediumContainer
	> {

	// attributes
	
	private final MediumDataSchema dataSchema;
	private final MediumCounterSchema counterSchema;
	private final MediumVoteSchema voteSchema;
	
	
	// constructors
	
	public MediumSchemaGate(MediumDataSchema dataSchema, MediumCounterSchema counterSchema, MediumVoteSchema voteSchema) {
		super(dataSchema, counterSchema, voteSchema);
		
		assert null != dataSchema;
		assert null != counterSchema;
		assert null != voteSchema;
		
		this.dataSchema = dataSchema;
		this.counterSchema = counterSchema;
		this.voteSchema = voteSchema;
	}

	
	// methods
	
	
	// overridden methods
	
	@Override
	protected EvMediumContainer constructEvDataTypeContainer(
			EntryClassIdentifier superIdentifier,
			ArrayList<EvMedium<Ev, MediumShortIdentifier>> data) {
		return new EvMediumContainer(superIdentifier, data);
	}

}
