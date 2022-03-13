import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class game_stateImpl extends UnicastRemoteObject implements game_state {

	private String name;
	private int score;
	private String phrase;
	private int guessCount;
	private int numWords;
	private int failedAttempts;

	public game_stateImpl(String name) throws RemoteException {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setPhrase() throws RemoteException {
		WordRepositoryServer wrs = new WordRepositoryServerImpl();
		this.phrase = wrs.getRandomWord(this.numWords);
	}


	public void setNumWords(int numWords) {
		this.numWords = numWords;
			
	}   
	
	public void setFailedAttempts(int failedAttempts) {
		this.failedAttempts = failedAttempts;
		this.guessCount = this.numWords * this.failedAttempts;
	}

	public void setGuessCount(int guessCount) {

		this.guessCount = guessCount;
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


}
