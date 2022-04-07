/****
 * COMP 4635 Project 2
 * @author Mohamed A, Erik S, Chad K
 */


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class PhraseGuessingGameServerImpl extends UnicastRemoteObject  implements PhraseGuessingGameServer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HashMap<String, game_state> game_states = new HashMap<String, game_state>();
	private static final int TIMELIMIT_SECONDS = 10;
	String clientname;
	public PhraseGuessingGameServerImpl(String clientname) throws RemoteException {
		super();
		this.clientname = clientname;
		
	}

	public void main() throws RemoteException, InterruptedException {
		while (true) {
			
			TimeUnit.SECONDS.sleep(TIMELIMIT_SECONDS);
			System.out.print("Prunning the HashMap...");
		    @SuppressWarnings("unchecked")
			Iterator<Map.Entry<String, game_state>> it = (Iterator<Entry<String, game_state>>) game_states.entrySet();
		    if (it == null || it.hasNext() == false) {
		    	System.out.println(" nothing to do yet!");
		    	continue;
		    }
	    	int cntAlive = 0, cntDead = 0;
	    	ArrayList<String> removeList = new ArrayList<>();
		    while (it.hasNext()) {
		        Map.Entry<String, game_state> pair = it.next();
		        String name = pair.getKey();
		        game_state r = pair.getValue();
		        if (((game_stateImpl) r).getIsActive()) {
		        	((game_stateImpl) r).setIsActive(false);
		        	cntAlive++;
		        }		        
		        else {
		        	removeList.add(name);
		        	cntDead++;
		        }
		    }
		    for (String name:removeList)
		    	game_states.remove(name);
	        System.out.println("Removed " + cntDead + ", " + cntAlive + " still alive!");
		}
		
	}
	
	//To do
	//need to add synchronized to avoid race condition so they run once at a time for any method altering the HashMap or its data stored
	//for game states. need to understand who is accessing them simultaneously.
	
	@Override
	/*Initializes a new game with user input of the game name, number of words and attempts per word 
	Creates a new game state and puts it into the hashmap*/
	 public synchronized String startGame(String player, int number_of_words, int failed_attempt_factor) throws RemoteException {
		
		game_state gs = new game_stateImpl(player);
		gs.setNumWords(number_of_words);
		gs.setFailedAttempts(failed_attempt_factor);
		gs.setPhrase();
		game_states.put(player, gs);
		return gs.getUserPhrase();
	}
	
	public Iterator<Map.Entry<String, game_state>> getEntrySet() {
		return this.game_states.entrySet().iterator();
	}
	
	public synchronized void setGameStates(HashMap<String, game_state> game_states) {
		this.game_states = game_states;
	}
	
	public synchronized void removeEntry(String name) {
		game_states.remove(name);

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
			//Each correct letter guess gives 10 points
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
		//Lower remaining guess count by 1
		game_states.get(player).setGuessCount(game_states.get(player).getGuessCount() - 1);
		//Show how many lives are left next to the current display phrase
		game_states.get(player).setDisplay_phrase(game_states.get(player).getDisplay_phrase(), game_states.get(player).getGuessCount());
		if(game_states.get(player).getGuessCount() <= 0 )
			{
				return restartGame(player);
			}
		
		}
	
		return game_states.get(player).getUserPhrase();
	}

	@Override
	//Guess the phrase for a specific game
	public String guessPhrase(String player, String word) throws RemoteException {
		//Phrase needs to be trimmed for .equals to work with the phrase
		String phrase = game_states.get(player).getPhrase().trim(); 
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
	//remove game id from hashmap
	game_states.remove(player);
		return prompt;
	}

	@Override
	//Restarts the players game, with a new word keeping the same lives.
	public String restartGame(String player) throws RemoteException {
		//get failed attempts and games from the current game id
		int failedAttempts = game_states.get(player).getFailedAttempts();	
		int numWords = game_states.get(player).getNumWords();
		String prompt = "Phrase was: " + game_states.get(player).getPhrase() + "\n";
		//make new game with the same origonal inputs of the game id 
		game_states.get(player).setGuessCount(failedAttempts * numWords);
		game_states.get(player).setNumWords(numWords);
		game_states.get(player).setPhrase();
		prompt += "Score was: " + Integer.toString(game_states.get(player).getScore());
		prompt += "\nRestarting game with "+ failedAttempts + " guesses and " + numWords +" new words" + "\n" + game_states.get(player).getUserPhrase();
		return prompt;
	}
	
	
	public synchronized boolean heartBeat(String name) throws RemoteException {
		game_state r = game_states.get(name);
		if (r != null) {
			((game_stateImpl) r).setIsActive(true);
		}
		return false;
	}
	
	public synchronized void keepMyNameWhileAlive(String name) throws RemoteException {
		// If the record for this client does not exist, create and add record
		if (!game_states.containsKey(name)) {
			game_states.put(name, new game_stateImpl(name));
		}
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
