import java.rmi.*;
public interface WordRepositoryServer extends Remote{
//	public boolean createWord(String word) throws RemoteException;
//	public boolean removeWord(String word) throws RemoteException;
//	public boolean checkWord(String word) throws RemoteException;
	public String getRandomWord(int length) throws RemoteException;
	public void populateWords() throws RemoteException;
	public void addWord(String word) throws RemoteException;
	public void removeWord(String word) throws RemoteException;
	public boolean checkWord(String word)throws RemoteException;
}