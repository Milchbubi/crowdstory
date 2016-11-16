package eWorld.frontEnd.gwt.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvIllegalDataException;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.packages.RootPackage;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;
import eWorld.datatypes.user.User;

/**
 * The client-side part for the RPC service.
 */
@RemoteServiceRelativePath("general")
public interface GeneralService extends RemoteService {
	
	void register(RegisterUser user) throws EvRequestException;
	
	User<UserIdentifier> signIn(SignInUser user) throws EvRequestException;
	
	/**
	 * @return the user that is signed in or null if user is not signed in
	 */
	User<UserIdentifier> getSignedInUser();
	
	void signOut();
	
	/**
	 * @return the entry that is on the top of the hierarchy
	 */
	EvEntry<Ev, EntryIdentifier> getRootEntry();
	
	/**
	 * @return the entry that can be loaded as start-page
	 */
	EvEntry<Ev, EntryIdentifier> getStartEntry();
	
	/**
	 * @param identifier
	 * @return an entry that matches the given identifier
	 */
	EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier) throws EvRequestException;
	
	/**
	 * 
	 * @return the RootPackage that contains some stuff about the status of the site
	 */
	RootPackage getRootPackage();
	
	/**
	 * @return the entryPackage or directory that can be loaded as start-page
	 */
	@Deprecated
	EvEntryPackage getStartEntryPackage();
	
	/**
	 * requests entries of the given class from the server
	 * @param identifier specifies the entries that should be loaded
	 * @return package that contains container of the requested entries
	 */
	EvEntryPackage getEntryPackage(EntryClassIdentifier identifier);

	/**
	 * 
	 * @param entry
	 * @return the added entry
	 * @throws EvRequestException
	 */
	EvEntry<Ev, EntryIdentifier> addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry) throws EvRequestException, EvIllegalDataException;
	
	/**
	 * links or copies the entry specified by source to destination
	 * @param source
	 * @param destination
	 * @return
	 * @throws EvRequestException
	 */
	EvEntry<Ev, EntryIdentifier> linkEntry(EntryIdentifier source, EntryClassIdentifier destination) throws EvRequestException;
	
	/**
	 * TODO method to send a list of votes to the server
	 * @param entryIdent
	 * @param vote
	 * @throws IllegalArgumentException
	 */
	void voteEntry(EntryIdentifier entryIdent, boolean vote) throws EvRequestException;
	
	/**
	 * returns the path of an entry that is specified by the given identifier
	 * @param identifier specifies the entry
	 * @return the path that leads to the entry
	 */
	EvPath getPath(EntryShortIdentifier identifier);
	
	/**
	 * requests an EvElement from the server
	 * @param Classdentifier identifies i.a. the header and attributes that should be loaded
	 * @param valueIdentifier identifies i.a. the attributeValues that should be loaded
	 * @return the element
	 */
	EvElement getElement(EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier);

	/**
	 * 
	 * @param medium
	 * @return the added medium
	 * @throws EvRequestException
	 */
	EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium) throws EvRequestException, EvIllegalDataException;
	
	void voteMedium(MediumIdentifier mediumIdent, boolean vote) throws EvRequestException;
	
	/**
	 * 
	 * @param comment
	 * @return the added comment
	 * @throws EvRequestException
	 */
	EvComment<Ev, CommentIdentifier> addComment(EvComment<EvVoid, EntryClassIdentifier> comment) throws EvRequestException, EvIllegalDataException;
	
	void voteComment(CommentIdentifier commentIdent, boolean vote) throws EvRequestException;
	
	/**
	 * 
	 * @param sentences the sentences which are to add
	 * @param afterPosition the position of the sentence after that the given sentences should be inserted, null if there is no preceding sentence
	 * @param beforePosition the position of the sentence before that the given sentences should be inserted, null if there is no subsequent sentence
	 * @return the added sentence
	 * @throws EvRequestException
	 * @throws EvIllegalDataException
	 */
	ArrayList<EvSentence<Ev, SentenceShortIdentifier>> addSentences(ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences, Long afterPosition, Long beforePosition) throws EvRequestException, EvIllegalDataException;
	
	/**
	 * adds the given sentence as alternate to an other sentence to the database
	 * @param sentence the sentence which is to add, specifies the position
	 * @return
	 * @throws EvRequestException
	 * @throws EvIllegalDataException
	 */
	EvSentence<Ev, SentenceIdentifier> addSentenceAlternate(EvSentence<EvVoid, EntryClassIdentifier> sentence) throws EvRequestException, EvIllegalDataException;
	
	void voteSentence(SentenceIdentifier sentenceIdent, boolean vote) throws EvRequestException;
	
}
