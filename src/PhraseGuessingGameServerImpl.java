/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */


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
	/*Initializes a new game with user input of the game name, number of words and attempts per word 
	Creates a new game state and puts it into the hashmap*/
	public String startGame(String player, int number_of_words, int failed_attempt_factor) throws RemoteException {
		
		game_state gs = new game_stateImpl(player);
		gs.setNumWords(number_of_words);
		gs.setFailedAttempts(failed_attempt_factor);
		gs.setPhrase();
		game_states.put(player, gs);
		return gs.getUserPhrase();
	}
	
	@Override
	/*Guesses a single letter of the phrase for a given game id*/ 
	public String guessLetter(String player, char letter) throws RemoteException {		
		String phrase;
		//Gets the phrase for the corresponding game id
		phrase = game_states.get(player).getPhrase();
		boolean found = false;
		String display_phrase = "";
		//Sets the display phrase as the phrase with blanks instead of letters
		display_phrase = game_states.get(player).getDisplay_phrase();
		char[] blankChar = display_phrase.toCharArray();
		int score ;
		//Check every charactrer in the character array
		for(int i=0;i<phrase.length();i++)
		{
		//If letter from phrase matches letter user entered
		if(phrase.charAt(i) == letter)
		{//Checks if letter was already previously guessed.
			if(game_states.get(player).getDisplay_phrase().indexOf(letter) == -1)
			{
				
			blankChar[i] = letter;
			display_phrase = String.valueOf(blankChar);
			
			found = true;
			//Update the display phrase replacing the blank with the correct letter
			game_states.get(player).setDisplay_phrase(display_phrase, game_states.get(player).getGuessCount());
			//Each correct letter gives 10 points
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
	String prompt = "Game ending, your score is: " + game_states.get(player).getScore() + "\n";
	prompt += "Phrase was: " + game_states.get(player).getPhrase();
	game_states.remove(player);
		return prompt;
	}

	@Override
	//Restarts the players game, with a new word keeping the same lives.
	public String restartGame(String player) throws RemoteException {
		
		int failedAttempts = game_states.get(player).getFailedAttempts();	
		int numWords = game_states.get(player).getNumWords();
		String prompt = "Phrase was: " + game_states.get(player).getPhrase() + "\n";
		game_states.get(player).setGuessCount(failedAttempts * numWords);
		game_states.get(player).setNumWords(numWords);
		game_states.get(player).setPhrase();
		prompt += "Score was: " + Integer.toString(game_states.get(player).getScore());
		prompt += "\nRestarting game with "+ failedAttempts + " guesses and " + numWords +" new words" + "\n" + game_states.get(player).getUserPhrase();
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
		
		return game_states.get(client).getFailedAttempts();
	}

	public int getGuessCount(String client) throws RemoteException {
		
		return game_states.get(client).getGuessCount();
	}

	public int getScore(String client) throws RemoteException {
		
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
