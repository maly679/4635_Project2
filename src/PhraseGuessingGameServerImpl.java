import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PhraseGuessingGameServerImpl extends UnicastRemoteObject  implements PhraseGuessingGameServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, game_state> game_states = new HashMap<String, game_state>();
	
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
		return gs.getUserPhrase();
	}
	
	@Override
	public String guessLetter(String player, char letter) throws RemoteException {
		System.out.println(game_states.get(player));
		
		String phrase;
		
		phrase = game_states.get(player).getPhrase();
		System.out.println(phrase);
		boolean found = false;
		String display_phrase = "";
		display_phrase = game_states.get(player).getDisplay_phrase();
		char[] blankChar = display_phrase.toCharArray();
		int score ;
		System.out.println(phrase);
		for(int i=0;i<phrase.length();i++)
		{
		if(phrase.charAt(i) == letter)
		{//Checks if letter was already previously guessed.
			if(game_states.get(player).getDisplay_phrase().indexOf(letter) == -1)
			{
				
			blankChar[i] = letter;
			display_phrase = String.valueOf(blankChar);
			
			found = true;

			game_states.get(player).setDisplay_phrase(display_phrase, game_states.get(player).getGuessCount());
		
			score = game_states.get(player).getScore();
			game_states.get(player).setScore(score + 10);
			
			return game_states.get(player).getUserPhrase();
			}
			else
			{
				String item_error = "Letter already guessed";
				return item_error;
			}
			
		}
	}
		//System.out.println("Score for this game is: " + game_states.get(player).getScore());
		if (!found)
		{
			
		game_states.get(player).setGuessCount(game_states.get(player).getGuessCount() - 1);
		game_states.get(player).setDisplay_phrase(game_states.get(player).getDisplay_phrase(), game_states.get(player).getGuessCount());
		if(game_states.get(player).getGuessCount() <= 0 )
			{
				return restartGame(player);
			}
		
		}
	
		return game_states.get(player).getUserPhrase();
	}

	@Override
	public String guessPhrase(String player, String word) throws RemoteException {
		
		String phrase = game_states.get(player).getPhrase().trim(); //not sure if it works way i think it does.
		System.out.println(word);
		if(word.trim().equals(phrase) ){
			
			int score = game_states.get(player).getScore();
			game_states.get(player).setScore(score + 100);
			
			String victory = "You won! Score for this game is: " + game_states.get(player).getScore();
			return victory;
		}
		else
		{
			int guessCount = game_states.get(player).getGuessCount();
			game_states.get(player).setGuessCount(guessCount - 1);
			
			if(game_states.get(player).getGuessCount() <= 0 )
			{
				restartGame(player);
			}
			
			return "Incorrect guess! " +  game_states.get(player).getUserPhrase();	
		}
		
	}

	@Override
	//Ends the game removing the player game id from the hashmap pernamently
	public String endGame(String player) throws RemoteException {

	System.out.println("Game ending, your score is: " + game_states.get(player).getScore());   //how to access score 
	game_states.remove(player);
		return null;
	}

	@Override
	//Restarts the players game, with a new word keeping the same lives.
	public String restartGame(String player) throws RemoteException {
		
		int failedAttempts = game_states.get(player).getFailedAttempts();	
		int numWords = game_states.get(player).getNumWords();
		
		game_states.get(player).setGuessCount(failedAttempts * numWords);
		game_states.get(player).setNumWords(numWords);
		game_states.get(player).setPhrase();
		game_states.get(player).setDisplay_phrase(game_states.get(player).getPhrase(), game_states.get(player).getGuessCount());
		System.out.println(game_states.get(player).getDisplay_phrase());
		game_states.get(player).setScore(0);
		String prompt = "Restarting game with "+ failedAttempts + " guesses and " + numWords +" new words";
		return prompt;
	}

	
	@Override
	public synchronized game_state getName(String client) throws RemoteException  {
		return game_states.get(client);
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

	// This method calls the method in game_states that is responsible for adding a word to the wordRepo
	public void addWord(String client, String word) throws RemoteException {
		

		game_states.get(client).addWord(word);
	}
	
	// This method calls the method in game_states that is responsible for removing a word from the wordRepo
	public void removeWord(String client, String word) throws RemoteException
	{
		game_states.get(client).removeWord(word);
	}

	// This method calls the method in game_states that is responsible for checking if a word is in the wordRepo
	public boolean checkWord(String client, String word) throws RemoteException {
		return game_states.get(client).checkWord(word);
	}

}
