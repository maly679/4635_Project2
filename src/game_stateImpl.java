import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class game_stateImpl extends UnicastRemoteObject implements game_state {
    	

    	private String client;
    	private int score;
    	private String phrase;
    	private int guessCount;
    	
    	
    	public game_stateImpl(String client) throws RemoteException {
    		this.client = client;
    	}
    		
    	public void setScore(int score) {
    		this.score = score;
    	}
    	
    	public void setPhrase(String phrase) {
    		this.phrase = phrase;
    	}
    	
    	public void setGuessCount(int guessCount) {
    		
    		this.guessCount = guessCount;
    	}
    	
    	public String getClient(String client) {
    		
    		return this.client;
    		
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
    	
    }
