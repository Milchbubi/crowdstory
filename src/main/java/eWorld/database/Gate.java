package eWorld.database;

import java.util.ArrayList;

import eWorld.datatypes.EvPath;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
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
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SignInUser;

/**
 * describes a facade to interact with the database
 * @author michael
 *
 */
public interface Gate {
	
	/**
	 * closes the connection to the database cluster
	 */
	public void closeDatabase();
	
	/**
	 * @param user specifies the user that should be added
	 * @throws EvRequestException if the user couldn't be added for some reason
	 */
	public void registerUser(final RegisterUser user) throws EvRequestException;
	
	/**
	 * @param user specifies the user that should be signed in
	 * @return the user that signs in (contains also the id of the user)
	 * @throws EvRequestException if the user cannot be signed in for some reason
	 */
	public AdvancedUser signInUser(final SignInUser user) throws EvRequestException;
	
	/**
	 * @return the entry that is on the top of the hierarchy
	 */
	public EvEntry<Ev, EntryIdentifier> getRootEntry();
	
	/**
	 * @param 
	 * @return the top rated entry of the specified class|directory or null if class|directory contains no items (or does not exist)
	 */
	public EvEntry<Ev, EntryIdentifier> getTopRatedEntry(EntryClassIdentifier directory);
	
	/**
	 * TODO? UserIdentifier as argument and load votes?
	 * @param identifier
	 * @return an entry that matches the given identifier
	 * @throws EvRequestException if specified entry does not exist
	 */
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier) throws EvRequestException;
	
	/**
	 * adds the given entry to the database
	 * ensures that entryClass|directory does exist and that entryClass describes not an element|story
	 * executes also a upVote for the given user for the added entry
	 * @param entry the entry which is to add
	 * @param userIdentifier the user who adds the entry	TODO this information is now already in param entry given (author)
	 * @param votes the initial-value for votes
	 * @return the added entry
	 * @throws EvRequestException if entryClass where entry should be added does not exist or if entryClass describes an element
	 */
	public EvEntry<Ev,EntryIdentifier> addEntry(final EvEntry<EvVoid, EntryClassIdentifier> entry, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * copies the specified entry from source to destination, the name and description are retained
	 * this creates only a linkage, the super|parent-entry of the copied entry remains the one of source
	 * ensures that entryClass does exist
	 * ensures that entryClass describes not an element
	 * ensures that entryClass does not contain the entry already
	 * executes also a upVote for the given user for the added entry
	 * @param source specified the entry that is to copy
	 * @param destination the entryClass where the specified entry should be copied to
	 * @param userIdentifier specifies the user who copies the entry
	 * @param votes the initial-value for votes
	 * @return the copied entry
	 * @throws EvRequestException if entryClass where entry should be copied to does not exist or if it describes an element or if it contains the entry already
	 */
	public EvEntry<Ev, EntryIdentifier> copyEntry(final EntryIdentifier source, final EntryClassIdentifier destination, final UserIdentifier userIdentifier, final int votes) throws EvRequestException;
	
	/**
	 * executes a vote for an entry
	 * ensures that specified entry does exists
	 * manages if specified user has already voted for the specified entry
	 * @param userIdent specifies the user who votes
	 * @param entryIdent specifies the entry to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the entry to vote for does not exist
	 */
	public void voteEntry(final UserIdentifier userIdent, final EntryIdentifier entryIdent, final boolean vote, final int votes) throws EvRequestException;
	
	/**
	 * returns the path of an entry that is specified by the given identifier
	 * @param identifier specifies the entry
	 * @return the path that leads to the entry
	 */
	public EvPath getPath(EntryShortIdentifier identifier);
	
	/**
	 * 
	 * @param userIdentifier null if not specified
	 * @return
	 */
	public RootPackage getRootPackage(UserIdentifier userIdentifier);
	
	/**
	 * 
	 * @param userIdentifier
	 * @return the entryPackage or directory that can be loaded as start-page
	 */
	public EvEntryPackage getStartEntryPackage(UserIdentifier userIdentifier);
	
	public EvEntryPackage getEntryPackage(UserIdentifier userIdentifier, EntryClassIdentifier superIdentifier);
	
	/**
	 * 
	 * @param userIdentifier
	 * @param classdentifier identifies i.a. the header and attributes that should be loaded
	 * @param valueIdentifier identifies i.a. the attributeValues that should be loaded
	 * @return
	 */
	public EvElement getElement(UserIdentifier userIdentifier, EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier);
	
	/**
	 * adds the given medium to the database
	 * ensures that entry where medium should be added does exist
	 * executes also a upVote for the given user for the added medium
	 * @param medium the medium which is to add
	 * @param userIdentifier the user who adds the medium
	 * @param votes the initial-value for votes
	 * @return the added medium
	 * @throws EvRequestException if entry where medium should be added does not exist
	 */
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for a medium
	 * ensures that specified medium does exists
	 * manages if specified user has already voted for the specified medium
	 * @param userIdent specifies the user who votes
	 * @param mediumIdent specifies the medium to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the medium to vote for does not exist
	 */
	public void voteMedium(UserIdentifier userIdent, MediumIdentifier mediumIdent, boolean vote, int votes) throws EvRequestException;
	
	/**
	 * adds the given comment to the database
	 * ensures that entry where comment should be added does exist
	 * executes also a upVote for the given user for the added comment
	 * @param comment the comment which is to add
	 * @param userIdentifier the user who adds the comment
	 * @param votes the initial-value for votes
	 * @return the added comment
	 * @throws EvRequestException if entry where comment should be added does not exist
	 */
	public EvComment<Ev, CommentIdentifier> addComment(EvComment<EvVoid, EntryClassIdentifier> comment, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for a comment
	 * ensures that specified comment does exists
	 * manages if specified user has already voted for the specified comment
	 * @param userIdent specifies the user who votes
	 * @param commentIdent specifies the comment to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the comment to vote for does not exist
	 */
	public void voteComment(UserIdentifier userIdent, CommentIdentifier commentIdent, boolean vote, int votes) throws EvRequestException;
	
	/**
	 * adds the given sentences to the database
	 * ensures that identifiers of sentences are equal (that they describe the same entry)
	 * ensures that entry where sentences should be added does exist
	 * ensures that afterPosition and beforePosition are valid, assigns MIN_VALUE to afterPosition and MAX_VALUE to beforePosition if they are null
	 * executes also upVotes for the given user for the added sentences
	 * @param sentences the sentences which are to add
	 * @param afterPosition the position of the sentence after that the given sentences should be inserted, null if there is no preceding sentence
	 * @param beforePosition the position of the sentence before that the given sentences should be inserted, null if there is no subsequent sentence
	 * @param userIdentifier the user who adds the sentences
	 * @param votes the initial-value for votes
	 * @return the added sentences
	 * @throws EvRequestException if entry where sentences should be added does not exist, or if afterPosition and beforePosition are not valid
	 */
	public ArrayList<EvSentence<Ev, SentenceShortIdentifier>> addSentences(ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences, Long afterPosition, Long beforePosition, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * adds the given sentence as alternate to an other sentence to the database
	 * ensures that position given by sentence.getPosition() is not null
	 * ensures that entry where sentence should be added does exist
	 * ensures that on the specified position an other sentence already exists (the position is specified by sentence)
	 * executes also a upVote for the given user for the added sentence
	 * @param sentence the sentence which is to add, specifies the position
	 * @param userIdentifier the user who adds the sentence
	 * @param votes the initial-value for votes
	 * @return the added sentence
	 * @throws EvRequestException if sentence.getPosition() is null or if entry where sentence should be added does not exist, or if there is no other sentence on the specified position
	 */
	public EvSentence<Ev, SentenceIdentifier> addSentenceAlternate(EvSentence<EvVoid, EntryClassIdentifier> sentence, UserIdentifier userIdentifier, int votes) throws EvRequestException;
	
	/**
	 * executes a vote for a sentence
	 * ensures that specified sentence does exit
	 * manages if specified user has already voted for the the specified sentence
	 * @param userIdent specifies the user who votes
	 * @param sentenceIdent specifies the sentence to vote for
	 * @param vote true for upVote, false for downVote
	 * @param votes the number of votes the specified user has
	 * @throws EvRequestException if the sentence to vote for does not exist
	 */
	public void voteSentence(UserIdentifier userIdent, SentenceIdentifier sentenceIdent, boolean vote, int votes) throws EvRequestException;
	
}
