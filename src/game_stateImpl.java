/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class game_stateImpl extends UnicastRemoteObject implements game_state {

	private String name;
	private int score;
	private String phrase;
	private int guessCount;
	private int numWords;
	private int failedAttempts;
	private String display_phrase;
	public String user_phrase;
	WordRepositoryServer wrs = new WordRepositoryServerImpl();

	//instantiate a new game_state
	public game_stateImpl(String name) throws RemoteException {
		this.name = name;
	}

	//get the actual words
	public String getDisplay_phrase()
	{
		return this.display_phrase;
	}

	//get the phrase displayed to the user
	public String getUserPhrase() { 
		return this.user_phrase;
	}

	//set/update the actual words
	public void setDisplay_phrase(String phrase, int guesscount)
	{
		this.display_phrase = phrase;
		this.guessCount = guesscount;
		//must simultaneously update the phrase displayed to user.
		this.user_phrase = phrase + " C" + guessCount;
	}

	//set the score
	public void setScore(int score) {
		this.score = score;
	}
	
	//set phrase INITIAL run of game.
	public void setPhrase() throws RemoteException {

		this.phrase = wrs.getRandomWord(this.numWords);

		String initialPlay ="";
		char [] phraseChar = this.phrase.trim().toCharArray();
		for (int i = 0; i < phraseChar.length; i ++) {
			if(Character.isWhitespace(phraseChar[i])) {
				initialPlay+= " ";
			} else {
				initialPlay+= "-";
			}
		}
		setDisplay_phrase(initialPlay, this.guessCount);
	}

	//set number of words
	public void setNumWords(int numWords) {
		this.numWords = numWords;

	}   

	//set failed attempts permitted
	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
		this.guessCount = this.numWords * this.failedAttempts;
	}

	//set the guess count
	public void setGuessCount(int guessCount) throws RemoteException {

		this.guessCount = guessCount;
		this.setDisplay_phrase(this.getDisplay_phrase(), this.guessCount);
	}

	//get the name of the user
	public String getName() {

		return this.name;

	}

	//get the number of words
	public int getNumWords() {

		return this.numWords;
	}

	//get score
	public int getScore() {
		return this.score;
	}

	//get phrase
	public String getPhrase() {
		return this.phrase;
	}

	//get guess count
	public int getGuessCount() {

		return this.guessCount;
	}

	//get failed attempts
	public int getFailedAttempts()  {
		// TODO Auto-generated method stub
		return this.failedAttempts;
	}

	// This method adds a word to the arrayList which is our wordRepo
	public void addWord(String word) throws RemoteException {
		wrs.addWord(word);

	}

	// This method removes a word from the arrayList which is our wordRepo
	public void removeWord(String word)throws RemoteException
	{
		wrs.removeWord(word);
	}

	// This method checks if a word is part of the arrayList which is our wordRepo
	public boolean checkWord(String word) throws RemoteException
	{
		return wrs.checkWord(word);
	}


}
