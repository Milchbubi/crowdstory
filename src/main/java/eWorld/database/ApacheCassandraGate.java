package eWorld.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import eWorld.database.impl.DB;
import eWorld.database.impl.UserSchema;
import eWorld.database.impl.comments.CommentCounterSchema;
import eWorld.database.impl.comments.CommentDataSchema;
import eWorld.database.impl.comments.CommentSchemaGate;
import eWorld.database.impl.comments.CommentVoteSchema;
import eWorld.database.impl.entries.EntryCounterSchema;
import eWorld.database.impl.entries.EntryDataSchema;
import eWorld.database.impl.entries.EntryRegisterSchema;
import eWorld.database.impl.entries.EntrySchemaGate;
import eWorld.database.impl.entries.EntryVoteSchema;
import eWorld.database.impl.media.MediumCounterSchema;
import eWorld.database.impl.media.MediumDataSchema;
import eWorld.database.impl.media.MediumSchemaGate;
import eWorld.database.impl.media.MediumVoteSchema;
import eWorld.database.impl.stories.StoryCounterSchema;
import eWorld.database.impl.stories.StoryDataSchema;
import eWorld.database.impl.stories.StorySchemaGate;
import eWorld.database.impl.stories.StoryVoteSchema;
import eWorld.datatypes.EvPath;
import eWorld.datatypes.containers.EvCommentContainer;
import eWorld.datatypes.containers.EvEntryContainer;
import eWorld.datatypes.containers.EvMediumContainer;
import eWorld.datatypes.containers.EvStory;
import eWorld.datatypes.data.EvComment;
import eWorld.datatypes.data.EvEntry;
import eWorld.datatypes.data.EvMedium;
import eWorld.datatypes.data.EvSentence;
import eWorld.datatypes.evs.Ev;
import eWorld.datatypes.evs.EvVoid;
import eWorld.datatypes.exceptions.EvRequestException;
import eWorld.datatypes.identifiers.CommentIdentifier;
import eWorld.datatypes.identifiers.CommentShortIdentifier;
import eWorld.datatypes.identifiers.EntryClassIdentifier;
import eWorld.datatypes.identifiers.EntryIdentifier;
import eWorld.datatypes.identifiers.EntryShortIdentifier;
import eWorld.datatypes.identifiers.MediumIdentifier;
import eWorld.datatypes.identifiers.MediumShortIdentifier;
import eWorld.datatypes.identifiers.SentenceIdentifier;
import eWorld.datatypes.identifiers.SentenceShortIdentifier;
import eWorld.datatypes.identifiers.UserIdentifier;
import eWorld.datatypes.identifiers.VoteIdentifier;
import eWorld.datatypes.packages.EvElement;
import eWorld.datatypes.packages.EvEntryPackage;
import eWorld.datatypes.packages.RootPackage;
import eWorld.datatypes.user.AdvancedUser;
import eWorld.datatypes.user.RegisterUser;
import eWorld.datatypes.user.SecretUser;
import eWorld.datatypes.user.SignInUser;

public class ApacheCassandraGate implements Gate {

	/** the number of votes a user has by default after registration */
	private static final int USER_DEFAULT_REGISTER_VOTES = 1;
	
	private final DB db;
	
	private final UserSchema userSchema ;
	
	private final EntryRegisterSchema entryRegisterSchema;
	private final EntryDataSchema entryDataSchema;
	private final EntryCounterSchema entryCounterSchema;
	private final EntryVoteSchema entryVoteSchema;
	private final EntrySchemaGate entrySchemaGate;
	
	private final MediumDataSchema mediumDataSchema;
	private final MediumCounterSchema mediumCounterSchema;
	private final MediumVoteSchema mediumVoteSchema;
	private final MediumSchemaGate mediumSchemaGate;
	
	private final CommentDataSchema commentDataSchema;
	private final CommentCounterSchema commentCounterSchema;
	private final CommentVoteSchema commentVoteSchema;
	private final CommentSchemaGate commentSchemaGate;
	
	private final StoryDataSchema storyDataSchema;
	private final StoryCounterSchema storyCounterSchema;
	private final StoryVoteSchema storyVoteSchema;
	private final StorySchemaGate storySchemaGate;
	
	/**
	 * initializes the database, checks if all tables are correct, creates them if they don't exist
	 * @param nodeAddress IP-address of the node to connect to
	 */
	public ApacheCassandraGate(final String nodeAddress) {
		assert null != nodeAddress;
		
		db = new DB(nodeAddress);
		
		userSchema = new UserSchema(db.getUserKeyspaceSession(), DB.USER_SCHEMA);
		
		entryRegisterSchema = new EntryRegisterSchema(db.getCrowdstoryKeyspaceSession(), DB.ENTRY_REGISTER_SCHEMA);
		entryDataSchema = new EntryDataSchema(db.getCrowdstoryKeyspaceSession(), DB.ENTRY_DATA_SCHEMA);
		entryCounterSchema = new EntryCounterSchema(db.getCrowdstoryKeyspaceSession(), DB.ENTRY_COUNTER_SCHEMA);
		entryVoteSchema = new EntryVoteSchema(db.getCrowdstoryKeyspaceSession(), DB.ENTRY_VOTE_SCHEMA);
		entrySchemaGate = new EntrySchemaGate(entryRegisterSchema, entryDataSchema, entryCounterSchema, entryVoteSchema);
		
		mediumDataSchema = new MediumDataSchema(db.getCrowdstoryKeyspaceSession(), DB.MEDIUM_DATA_SCHEMA);
		mediumCounterSchema = new MediumCounterSchema(db.getCrowdstoryKeyspaceSession(), DB.MEDIUM_COUNTER_SCHEMA);
		mediumVoteSchema = new MediumVoteSchema(db.getCrowdstoryKeyspaceSession(), DB.MEDIUM_VOTE_SCHEMA);
		mediumSchemaGate = new MediumSchemaGate(mediumDataSchema, mediumCounterSchema, mediumVoteSchema);
		
		commentDataSchema = new CommentDataSchema(db.getCrowdstoryKeyspaceSession(), DB.COMMENT_DATA_SCHEMA);
		commentCounterSchema = new CommentCounterSchema(db.getCrowdstoryKeyspaceSession(), DB.COMMENT_COUNTER_SCHEMA);
		commentVoteSchema = new CommentVoteSchema(db.getCrowdstoryKeyspaceSession(), DB.COMMENT_VOTE_SCHEMA);
		commentSchemaGate = new CommentSchemaGate(commentDataSchema, commentCounterSchema, commentVoteSchema);
		
		storyDataSchema = new StoryDataSchema(db.getCrowdstoryKeyspaceSession(), DB.STORY_DATA_SCHEMA);
		storyCounterSchema = new StoryCounterSchema(db.getCrowdstoryKeyspaceSession(), DB.STORY_COUNTER_SCHEMA);
		storyVoteSchema = new StoryVoteSchema(db.getCrowdstoryKeyspaceSession(), DB.STORY_VOTE_SCHEMA);
		storySchemaGate = new StorySchemaGate(storyDataSchema, storyCounterSchema, storyVoteSchema);
		
	}
	
	@Override
	public void closeDatabase() {
		db.close();
	}
	
	/**
	 * 
	 * @param salt
	 * @param password
	 * @return the hash of password with salt as prefix
	 * @throws EvRequestException when calculating of hash failed
	 */
	private String calculateHash(String salt, String password) throws EvRequestException {
		assert null != salt;
		assert null != password;
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			
			String saltedPassword = salt + password;
			
			return new String(messageDigest.digest(saltedPassword.getBytes("UTF-8")), "UTF-8");
			
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();	// TODO log
			throw new EvRequestException("epic fail");
		}
	}

	@Override
	public void registerUser(RegisterUser user) throws EvRequestException {
		assert null != user;
		
		// check pseudonym TODO? check this in SericeImpl instead of here?
		if (null != SignInUser.checkPseudonym(user.getPseudonym())) {
			throw new EvRequestException(SignInUser.checkPseudonym(user.getPseudonym()));
		}
		
		// check password TODO? check this in SericeImpl instead of here?
		if (null != SignInUser.checkPassword(user.getPassword())) {
			throw new EvRequestException(SignInUser.checkPassword(user.getPassword()));
		}
		
		// check if a user with this pseudonym already exists
		long count = userSchema.countPseudonyms(user.getPseudonym());
		if (1 <= count) {
			if (1 < count) {
				System.out.println("there are " + count + " users with pseudonym '" + user.getPseudonym() + "'");	// TODO log
			}
			throw new EvRequestException("User with this pseudonym already exists.");
		}
		
		// insert new user
		String passwordSalt = String.valueOf(System.currentTimeMillis());	// FIXME? is this a good salt? I think yes
		UserIdentifier identifier = userSchema.insert(user, passwordSalt, calculateHash(passwordSalt, user.getPassword()), USER_DEFAULT_REGISTER_VOTES);
		
		// check if other user with same pseudonym was added at same time
		if (2 <= userSchema.countPseudonyms(user.getPseudonym())) {
			userSchema.delete(identifier);
			throw new EvRequestException("User with this pseudonym already exists.");
		}
	}

	@Override
	public AdvancedUser signInUser(SignInUser user) throws EvRequestException {
		assert null != user;
		
		// TODO? call check-methods of SignInUser, check this in SericeImpl instead of here?
		
		SecretUser secretUser = userSchema.selectSecret(user.getPseudonym());
		
		if (null == secretUser) {
			throw new EvRequestException("User with this pseudonym does not exist");
		}
		
		// check if password is correct
		if (!calculateHash(secretUser.getPasswordSalt(), user.getPassword()).equals(secretUser.getPasswordHash())) {
			throw new EvRequestException("password is not correct");
		}
		
		return secretUser.getUser();
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getRootEntry() {
		return entryDataSchema.selectOne(new EntryIdentifier(EntryRegisterSchema.SUPER_ROOT_ID, EntryRegisterSchema.ROOT_ID));
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getTopRatedEntry(
			EntryClassIdentifier directory) {
		return entryDataSchema.getTopRatedItem(directory);
	}
	
	@Override
	public EvEntry<Ev, EntryIdentifier> getEntry(EntryShortIdentifier identifier) throws EvRequestException {
		assert null != identifier;
		
		// get entryClass
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(identifier);
		if (null == entryClass) {
			throw new EvRequestException("entry does not exist");
		}
		
		// get entry
		EvEntry<Ev, EntryIdentifier> entry = entryDataSchema.selectOne(new EntryIdentifier(entryClass, identifier));
		if (null == entry) {
			throw new EvRequestException("epic fail, entry does not exist but should");
		}
		
		return entry;
	}

	@Override
	public EvEntry<Ev,EntryIdentifier> addEntry(EvEntry<EvVoid, EntryClassIdentifier> entry,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != entry;
		assert null != userIdentifier;
		
		// ensure that class in which entry is to add does exist
		EntryClassIdentifier entryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(entry.getIdentifier().getEntryClassId()));
		if (null == entryClass) {
			throw new EvRequestException("entryClass does not exist");
		}
		
		// ensure that entry(Class) where entry should be added is not an element
		EvEntry<Ev, EntryIdentifier> superEntry = entryDataSchema.selectOne(new EntryIdentifier(entryClass.getEntryClassId(), entry.getIdentifier().getEntryClassId()));
		if (superEntry.isElement()) {
			throw new EvRequestException("entryClass specifies an element");
		}
		
		// add entry
		EntryShortIdentifier entryShortIdent = entryRegisterSchema.insert(entry.getIdentifier());
		EntryIdentifier entryIdent = new EntryIdentifier(entry.getIdentifier(), entryShortIdent);
		VoteIdentifier<EntryIdentifier> voteIdent = new VoteIdentifier<EntryIdentifier>(userIdentifier, entryIdent);
		entryDataSchema.insert(entry, entryShortIdent, (float)votes, null);
		entryVoteSchema.vote(voteIdent, true);
		entryCounterSchema.upVote(entryIdent, votes);
		
		// return added entry
		EvEntry<Ev, EntryIdentifier> re = entryDataSchema.selectOne(entryIdent);
		re.getEv().setVote(entryVoteSchema.getBooleanVote(voteIdent));
		return re;
	}
	
	public EvEntry<Ev, EntryIdentifier> copyEntry(
			final EntryIdentifier source, final EntryClassIdentifier destination, final UserIdentifier userIdentifier, final int votes)
					throws EvRequestException {
		assert null != source;
		assert null != destination;
		assert null != userIdentifier;
		
		// ensure that class in which entry is to copy does exist
		EntryClassIdentifier destEntryClass = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(destination.getEntryClassId()));
		if (null == destEntryClass) {
			throw new EvRequestException("destination entryClass does not exist");
		}
		
		// ensure that entry(Class) where entry should be copied to is not an element
		EvEntry<Ev, EntryIdentifier> destSuperEntry = entryDataSchema.selectOne(new EntryIdentifier(destEntryClass.getEntryClassId(), destination.getEntryClassId()));
		if (destSuperEntry.isElement()) {
			throw new EvRequestException("destination entryClass specifies an element");
		}
		
		// ensure that entry(Class) where entry should be copied to does not already contain the entry
		if (null != entryDataSchema.selectOne(new EntryIdentifier(destination, source.getShortIdentifier()))) {
			throw new EvRequestException("destination entryClass already contains the specified entry");
		}
		
		// get entry that is specified by source
		EvEntry<Ev, EntryIdentifier> sourceEntry = entryDataSchema.selectOne(source);
		if (null == sourceEntry) {
			throw new EvRequestException("source entry does not exist");
		}
		
		// construct destinationEntry
		EvEntry<Ev, EntryClassIdentifier> destEntry = new EvEntry<Ev, EntryClassIdentifier>(
				sourceEntry.getEv(),
				destination,
				sourceEntry.getName(),
				sourceEntry.getDescription(),
				sourceEntry.isElement(),
				sourceEntry.getAuthor()
				);
		
		// add|copy entry
		EntryIdentifier entryIdent = new EntryIdentifier(destination, source.getShortIdentifier());
		VoteIdentifier<EntryIdentifier> voteIdent = new VoteIdentifier<EntryIdentifier>(userIdentifier, entryIdent);
		entryDataSchema.insert(destEntry, source.getShortIdentifier(), (float)votes, null);
		entryVoteSchema.vote(voteIdent, true);
		entryCounterSchema.upVote(entryIdent, votes);
		
		// return added|copied entry
		EvEntry<Ev, EntryIdentifier> re = entryDataSchema.selectOne(entryIdent);
		re.getEv().setVote(entryVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteEntry(UserIdentifier userIdent, EntryIdentifier entryIdent, boolean vote, int votes) throws EvRequestException {
		assert null != userIdent;
		assert null != entryIdent;
		
		entrySchemaGate.vote(new VoteIdentifier<EntryIdentifier>(userIdent, entryIdent), vote, votes);
		
		// update rating of the entry
		entrySchemaGate.updateRating(entryIdent, entryIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
	}

	@Override
	public EvPath getPath(EntryShortIdentifier identifier) {
		assert null != identifier;
		
		return entrySchemaGate.getPath(identifier);
	}
	
	@Override
	public RootPackage getRootPackage(UserIdentifier userIdentifier) {
		return new RootPackage(
				userSchema.countRows(), 
				entryRegisterSchema.countRows(), 
				storySchemaGate.listItems(userIdentifier, new EntryClassIdentifier(EntryRegisterSchema.ROOT_ID)),
				commentSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(EntryRegisterSchema.ROOT_ID))
				);
	}

	@Override
	public EvEntryPackage getStartEntryPackage(UserIdentifier userIdentifier) {
		return getEntryPackage(userIdentifier, new EntryClassIdentifier(EntryRegisterSchema.ROOT_ID));
	}
	
	@Override
	public EvEntryPackage getEntryPackage(UserIdentifier userIdentifier,
			EntryClassIdentifier superIdentifier) {
		assert null != superIdentifier;
		
		EntryClassIdentifier superClassIdentifier = entryRegisterSchema.getClassIdentifier(new EntryShortIdentifier(superIdentifier.getEntryClassId()));
		
		EvEntry<Ev, EntryIdentifier> entry = entrySchemaGate.getFullEntry(
				userIdentifier, 
				new EntryIdentifier(superClassIdentifier.getEntryClassId(), superIdentifier.getEntryClassId())
				);
		
		EvEntryContainer eEntryContainer = entrySchemaGate.listItems(userIdentifier, superIdentifier);
		
		return new EvEntryPackage(entry, eEntryContainer);
	}
	
	@Override
	public EvElement getElement(UserIdentifier userIdentifier, EntryClassIdentifier classIdentifier, EntryShortIdentifier valueIdentifier) {
		assert null != classIdentifier;
		assert null != valueIdentifier;
		
		EntryClassIdentifier entryClassIdentifier = entryRegisterSchema.getClassIdentifier(valueIdentifier);
		
		EvEntry<Ev, EntryIdentifier> entry = entrySchemaGate.getFullEntry(
				userIdentifier, 
				new EntryIdentifier(entryClassIdentifier, valueIdentifier)
				);
		
		// media i.a. images of entry
		EvMediumContainer eMediumContainer = mediumSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId()));
		
		// story of entry
		EvStory eStory = storySchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId()));
		
		// comments of entry
		EvCommentContainer eCommentContainer = commentSchemaGate.listItems(userIdentifier, new EntryClassIdentifier(valueIdentifier.getEntryId()));
		
		// construct element
		EvElement eElement = new EvElement(entry, eMediumContainer, eStory, eCommentContainer);
		
		return eElement;
	}

	@Override
	public EvMedium<Ev, MediumIdentifier> addMedium(EvMedium<EvVoid, EntryClassIdentifier> medium,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != medium;
		assert null != userIdentifier;
		
		// ensure that entry where medium is to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(medium.getIdentifier().getEntryClassId()))) {
			throw new EvRequestException("entry where medium should be added does not exist");
		}
		
		MediumShortIdentifier mediumShortIdent = mediumDataSchema.generateNewId();
		MediumIdentifier mediumIdent = new MediumIdentifier(medium.getIdentifier(), mediumShortIdent);
		VoteIdentifier<MediumIdentifier> voteIdent = new VoteIdentifier<MediumIdentifier>(userIdentifier, mediumIdent);
		
		mediumDataSchema.insert(medium, mediumShortIdent, (float)votes, null);
		mediumVoteSchema.vote(voteIdent, true);
		mediumCounterSchema.upVote(mediumIdent, votes);
		
		// return added medium
		EvMedium<Ev, MediumIdentifier> re = mediumDataSchema.selectOne(mediumIdent);
		re.getEv().setVote(mediumVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteMedium(UserIdentifier userIdent,
			MediumIdentifier mediumIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != mediumIdent;
		
		mediumSchemaGate.vote(new VoteIdentifier<MediumIdentifier>(userIdent, mediumIdent), vote, votes);
		
		// update rating of the medium
		mediumSchemaGate.updateRating(mediumIdent, mediumIdent.getShortIdentifier());	// TODO don't do this all the time, delete this line from here
		
	}

	@Override
	public EvComment<Ev, CommentIdentifier> addComment(
			EvComment<EvVoid, EntryClassIdentifier> comment,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != comment;
		assert null != userIdentifier;
		
		// ensure that entry where comment is to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(comment.getIdentifier().getEntryClassId()))) {
			throw new EvRequestException("entry where comment should be added does not exist");
		}
		
		CommentShortIdentifier commentShortIdent = commentDataSchema.generateNewId();
		CommentIdentifier commentIdent = new CommentIdentifier(comment.getIdentifier(), commentShortIdent);
		VoteIdentifier<CommentIdentifier> voteIdent = new VoteIdentifier<CommentIdentifier>(userIdentifier, commentIdent);
		
		commentDataSchema.insert(comment, commentShortIdent, (float)votes, null);
		commentVoteSchema.vote(voteIdent, true);
		commentCounterSchema.upVote(commentIdent, votes);
		
		// return added comment
		EvComment<Ev, CommentIdentifier> re = commentDataSchema.selectOne(commentIdent);
		re.getEv().setVote(commentVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteComment(UserIdentifier userIdent,
			CommentIdentifier commentIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != commentIdent;
		
		commentSchemaGate.vote(new VoteIdentifier<CommentIdentifier>(userIdent, commentIdent), vote, votes);
		
		// update rating of the comment
		commentSchemaGate.updateRating(commentIdent);	// TODO don't do this all the time, delete this line from here
	}

	@Override
	public ArrayList<EvSentence<Ev, SentenceShortIdentifier>> addSentences(
			ArrayList<EvSentence<EvVoid, EntryClassIdentifier>> sentences,
			Long afterPosition, Long beforePosition,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != sentences;
		assert null != userIdentifier;
		
		// ensure that identifiers of sentences are equal
		EntryClassIdentifier identifier = sentences.get(0).getIdentifier();
		for (EvSentence<EvVoid, EntryClassIdentifier> sentence : sentences) {
			if (sentence.getIdentifier().getEntryClassId() != identifier.getEntryClassId()) {
				throw new EvRequestException("identifiers of sentences are not equal");
			}
		}
		
		// ensure that entry where sentences are to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(identifier.getEntryClassId()))) {
			throw new EvRequestException("entry where sentences should be added does not exist");
		}
		
		// ensure that afterPosition and beforePosition are valid
		if (null == afterPosition) {
			afterPosition = Long.MIN_VALUE/2 +1;
		} else {
			// TODO ensure that on afterPosition is a sentence
		}
		if (null == beforePosition) {
			beforePosition = Long.MAX_VALUE/2;
		} else {
			// TODO ensure that on beforePosition is a sentence
		}
		if (afterPosition >= beforePosition) {
			throw new EvRequestException("afterPosition should be smaller than beforePosition");
		}
		
		// ensure that there are no sentences between afterPosition and beforePosition (aPos and bPos)
		ArrayList<EvSentence<Ev, SentenceShortIdentifier>> shouldBeEmptyList = storySchemaGate.listItemsBounded(null, identifier, afterPosition+1, beforePosition-1).getData();
		if (!shouldBeEmptyList.isEmpty()) {
			// adjust aPosition if there would be sentences..
			afterPosition = shouldBeEmptyList.get(shouldBeEmptyList.size()-1).getPosition();
			// ..and avoid exception
//			throw new EvRequestException("given positions are invalid to add sentences, this could be because the clients data are out of time, refresh could help");
		}
		
		// ensure that list of sentences is not empty
		if (sentences.isEmpty()) {
			throw new EvRequestException("list of sentences is empty");
		}
		
		// ensure that there is enough space to add sentences TODO move other sentences to create free space if there is none
		float distance = (float)(beforePosition - afterPosition - 1) / sentences.size();
		if (1 > distance) {
			throw new EvRequestException("not enough space to add sentences on given position");
		}
		
		// add sentences and execute upVotes for the added sentences
		long startPosition = afterPosition + (long)distance - (long)(distance/2);
		for (int i = 0; i < sentences.size(); i++) {
			
			EvSentence<EvVoid, EntryClassIdentifier> sentence = sentences.get(i);
			sentence.setPosition(startPosition + (long)(distance*i));
			
			SentenceShortIdentifier sentenceShortIdent = storyDataSchema.generateNewId();
			SentenceIdentifier sentenceIdent = new SentenceIdentifier(sentence.getIdentifier(), sentenceShortIdent);
			VoteIdentifier<SentenceIdentifier> voteIdent = new VoteIdentifier<SentenceIdentifier>(userIdentifier, sentenceIdent);
			
			storyDataSchema.insert(sentence, sentenceShortIdent, (float)votes);
			storyVoteSchema.vote(voteIdent, true);
			storyCounterSchema.upVote(sentenceIdent, votes);
		}
		
		// return added sentences
		return storySchemaGate.listItemsBounded(userIdentifier, identifier, afterPosition+1, beforePosition-1).getData();
	}

	@Override
	public EvSentence<Ev, SentenceIdentifier> addSentenceAlternate(
			EvSentence<EvVoid, EntryClassIdentifier> sentence,
			UserIdentifier userIdentifier, int votes) throws EvRequestException {
		assert null != sentence;
		assert null != userIdentifier;
		
		// ensure that position given is not null
		if (null == sentence.getPosition()) {
			throw new EvRequestException("position of sentence is not set");
		}
		
		// ensure that entry where sentences are to add does exist
		if (!entryRegisterSchema.exists(new EntryShortIdentifier(sentence.getIdentifier().getEntryClassId()))) {
			throw new EvRequestException("entry where sentence should be added does not exist");
		}
		
		// ensure that on the specified position an other sentence already exists
		if (storySchemaGate.listItemsBounded(null, sentence.getIdentifier(), sentence.getPosition(), sentence.getPosition()).getData().isEmpty()) {
			throw new EvRequestException("there is no sentence on the specified position");
		}
		
		// add sentence and execute upVote for it
		SentenceShortIdentifier sentenceShortIdent = storyDataSchema.generateNewId();
		SentenceIdentifier sentenceIdent = new SentenceIdentifier(sentence.getIdentifier(), sentenceShortIdent);
		VoteIdentifier<SentenceIdentifier> voteIdent = new VoteIdentifier<SentenceIdentifier>(userIdentifier, sentenceIdent);
		
		storyDataSchema.insert(sentence, sentenceShortIdent, (float)votes);
		storyVoteSchema.vote(voteIdent, true);
		storyCounterSchema.upVote(sentenceIdent, votes);
		
		// return added sentence
		EvSentence<Ev, SentenceIdentifier> re = storyDataSchema.selectOne(sentenceIdent);
		re.getEv().setVote(storyVoteSchema.getBooleanVote(voteIdent));
		return re;
	}

	@Override
	public void voteSentence(UserIdentifier userIdent,
			SentenceIdentifier sentenceIdent, boolean vote, int votes)
			throws EvRequestException {
		assert null != userIdent;
		assert null != sentenceIdent;
		
		storySchemaGate.vote(new VoteIdentifier<SentenceIdentifier>(userIdent, sentenceIdent), vote, votes);
		
		// update rating of the comment
		storySchemaGate.updateRating(sentenceIdent);	// TODO don't do this all the time, delete this line from here
	}
	
}
