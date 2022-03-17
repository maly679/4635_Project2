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

	//private setDisplay_Phrase(String)



	public game_stateImpl(String name) throws RemoteException {
		this.name = name;
	}

	public String getDisplay_phrase()
	{
		return this.display_phrase;
	}
	public String getUserPhrase() { 

		return this.user_phrase;
	}
	public void setDisplay_phrase(String phrase, int guesscount)
	{
		System.out.println(phrase + guessCount);
		this.display_phrase = phrase;
		this.guessCount = guesscount;
		this.user_phrase = phrase + " C" + guessCount;
	}

	public void setScore(int score) {
		this.score = score;
	}


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



	public void setNumWords(int numWords) {
		this.numWords = numWords;

	}   

	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
		this.guessCount = this.numWords * this.failedAttempts;
	}

	public void setGuessCount(int guessCount) throws RemoteException {

		this.guessCount = guessCount;
		this.setDisplay_phrase(this.getDisplay_phrase(), this.guessCount);
	}

	public String getName() {

		return this.name;

	}

	public int getNumWords() {

		return this.numWords;
	}

	public int getScore() {
		return this.score;
	}

	public String getPhrase() {
		return this.phrase;
	}

	public int getGuessCount() {

		return this.guessCount;
	}

	public int getFailedAttempts()  {
		// TODO Auto-generated method stub
		return this.failedAttempts;
	}

	@Override
	public void addWord(String word) throws RemoteException {
		wrs.addWord(word);

	}

	public void removeWord(String word)throws RemoteException
	{
		wrs.removeWord(word);
	}

	public boolean checkWord(String word) throws RemoteException
	{
		return wrs.checkWord(word);
	}


}
