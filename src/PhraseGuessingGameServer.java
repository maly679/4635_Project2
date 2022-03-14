import java.rmi.*;
public interface PhraseGuessingGameServer extends Remote{
	public String startGame(
			String player,
			int number_of_words,
			int failed_attempt_factor
			) throws RemoteException;
	public String guessLetter(String player,char letter) throws RemoteException;
	public String guessPhrase(String player, String phrase) throws RemoteException;
	public String endGame(String player) throws RemoteException;
	public String restartGame(String player) throws RemoteException;
//	public String addWord() throws RemoteException;
//	public String removeWord() throws RemoteException;
//	public String checkWord() throws RemoteException;
	public String getName(String client) throws RemoteException;
	public String getPhrase(String client) throws RemoteException;
	public int getNumWords(String client) throws RemoteException;
	public int getFailedAttempts(String client) throws RemoteException;
	public String getDisplay_phrase(String client) throws RemoteException;
	public void addWord(String client, String word) throws RemoteException;
	public void removeWord(String client,String word)throws RemoteException;
	public boolean checkWord(String client,String word) throws RemoteException;
}