import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PhraseGuessingGameServerImpl extends UnicastRemoteObject  implements PhraseGuessingGameServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, game_state> game_states = new HashMap<>();
	
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
		boolean found = false;
		String display_phrase = "";
		display_phrase = game_states.get(player).getDisplay_phrase();
		char[] blankChar = display_phrase.toCharArray();
		
		
		System.out.println(phrase);
		for(int i=0;i<phrase.length();i++)
		{
		if(phrase.charAt(i) == letter)
		{

			blankChar[i] = letter;
			display_phrase = String.valueOf(blankChar);
			game_states.get(player).setDisplay_phrase(display_phrase);
			
			int score = game_states.get(player).getScore();
			game_states.get(player).setScore(score + 10);
			
			found = true;
			
		}
	}
		
		if (found == false)
		{
				int failedAttempts = game_states.get(player).getFailedAttempts();
		game_states.get(player).setFailedAttempts(failedAttempts - 1);

		}
		
		System.out.println(display_phrase);
		return display_phrase;
	}

	@Override
	public String guessPhrase(String player, String word) throws RemoteException {
		
		String phrase = game_states.get(player).getPhrase(); //not sure if it works way i think it does.
		String phrase_fixed = phrase.substring(0,word.length());
		
		if(word.equals(phrase_fixed) ){
			int score = game_states.get(player).getScore();
			game_states.get(player).setScore(score + 100);
			
			return phrase;
		}
		else
		{
			int failedAttempts = game_states.get(player).getFailedAttempts();
			game_states.get(player).setFailedAttempts(failedAttempts - 1);
	
			
			return player;	
		}

		
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

	public synchronized String getDisplay_phrase(String client) throws RemoteException  {
		return game_states.get(client).getDisplay_phrase();
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

	@Override
	public void addWord(String client, String word) throws RemoteException {
		
		
		game_states.get(client).addWord(word);
	}
	
	public void removeWord(String client, String word) throws RemoteException
	{
		game_states.get(client).removeWord(word);
	}

	@Override
	public boolean checkWord(String client, String word) throws RemoteException {
		return game_states.get(client).checkWord(word);
	}

}
