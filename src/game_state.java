import java.rmi.Remote;
import java.rmi.RemoteException;

public interface game_state extends Remote {
    	
	public void setScore(int score) throws RemoteException;
	public void setPhrase() throws RemoteException;
	public void setNumWords(int numWords) throws RemoteException;
	public void setFailedAttempts(int failedAttempts) throws RemoteException;
	public void setGuessCount(int guessCount) throws RemoteException;
	public String getName() throws RemoteException;
	public int getFailedAttempts() throws RemoteException;
	public int getNumWords() throws RemoteException;
	public int getScore() throws RemoteException;
	public String getPhrase() throws RemoteException;
	public int getGuessCount() throws RemoteException;
	public void setDisplay_phrase(String phrase) throws RemoteException;
	public String getDisplay_phrase() throws RemoteException;
}