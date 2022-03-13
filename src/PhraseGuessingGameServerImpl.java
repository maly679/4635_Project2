import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PhraseGuessingGameServerImpl extends UnicastRemoteObject  implements PhraseGuessingGameServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, game_state> game_states = new HashMap<>();
	//String phrase;
	String clientname;
	public PhraseGuessingGameServerImpl(String clientname) throws RemoteException {
		super();
		this.clientname = clientname;
		
	}

	@Override
	public String startGame(String player, int number_of_words, int failed_attempt_factor) throws RemoteException {
		
		game_state gs = new game_stateImpl(player);
		gs.setNumWords(number_of_words);
		gs.setFailedAttempts(failed_attempt_factor);
		gs.setPhrase();
		game_states.put(player, gs);
		return player;
	}
	
	@Override
	public String guessLetter(String player, char letter) throws RemoteException {
		String phrase;
		phrase = game_states.get(player).getPhrase();
		
		for(int i=0;i<phrase.length();i++)
		{
		if(phrase.charAt(i) == letter)
		{
			System.out.println("letter correct");
			//displayString[i] = letter  //update display string
			
			//game_states.update(player,game_state.setScore(10)); //- however to update.

		}
		else{
			
			int guessCount = game_states.get(player).getGuessCount();
			int failedAttempts = game_states.get(player).getFailedAttempts();
			System.out.println("letter incorrect");
			guessCount -= 1;
			failedAttempts -= 1;
			//update count/failed attempts
		}
	}

		return player;
	}

	@Override
	public String guessPhrase(String player, String word) throws RemoteException {
		
		String phrase = game_states.get(player).getPhrase(); //not sure if it works way i think it does.
		if(phrase == word ){
			//update score.
			System.out.println("Word correct");
		}
		else
		{
			//reduce failed attempts , increase guessCount.
			System.out.println("Word incorrect");
		}

		return player;
	}

	@Override
	public String endGame(String player) throws RemoteException {

	//	System.out.println("Game ending, your score is: ")   //how to access score 
	//remove from hashmap
		return null;
	}

	@Override
	public String restartGame(String player) throws RemoteException {
		//	System.out.println("Game ending, your score is: ")   //how to access score 
		//Set fields of player to 0. call start game
		return null;
	}

	//	@Override
	//	public String addWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	@Override
	//	public String removeWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	@Override
	//	public String checkWord() throws RemoteException {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//	
	@Override
	public synchronized String getName(String client) throws RemoteException  {
		return game_states.get(client).getName();
	}

	public synchronized String getPhrase(String client) throws RemoteException  {
		return game_states.get(client).getPhrase();
	}

	public synchronized int getNumWords (String client) throws RemoteException {

		return game_states.get(client).getNumWords();

	}

	@Override
	public int getFailedAttempts(String client) throws RemoteException {
		// TODO Auto-generated method stub
		return game_states.get(client).getFailedAttempts();
	}

	public int getGuessCount(String client) throws RemoteException {
		// TODO Auto-generated method stub
		return game_states.get(client).getGuessCount();
	}

	public int getScore(String client) throws RemoteException {
		// TODO Auto-generated method stub
		return game_states.get(client).getScore();
	}

}
