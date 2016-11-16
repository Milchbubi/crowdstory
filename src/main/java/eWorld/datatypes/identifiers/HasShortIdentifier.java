package eWorld.datatypes.identifiers;

public interface HasShortIdentifier<
		TYPE,
		IDENT extends EvShortIdentifier<TYPE>
	> extends EvIdentifier {

	public IDENT getShortIdentifier();
}
