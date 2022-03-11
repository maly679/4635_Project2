import java.rmi.Remote;
import java.rmi.RemoteException;

public interface game_state extends Remote {
    	
	public void setScore(int score) throws RemoteException;
	public void setPhrase(String phrase) throws RemoteException;
	public String getClient(String client) throws RemoteException;
	public int getScore() throws RemoteException;
	public String getPhrase() throws RemoteException;
	public int getGuessCount() throws RemoteException;
	
}