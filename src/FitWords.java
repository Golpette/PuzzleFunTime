import java.util.*;
import java.lang.Math;

// Class holds two methods: for fitting *across* and *down* in the crossword

public class FitWords{

	//'Random fit approach' to try and fit words ACROSS into grid. Updates ArrayList<Entry> and grid all at once
	public static void fitEntryAcross(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries){

		// Choose initial point. Recall edge sites are not 'active'
		int init_x = (int)(Math.random()*(xLength-2) + 1);
		int init_y = (int)(Math.random()*(yLength-2) + 1);

		//Make sure space on LHS is empty, otherwise do nothing
		if( grid[init_x - 1][init_y].equals("_") ){		

			// Max length of word due to grid size 
			int maxPossLength = (xLength-1) - init_x;

			// Get the configuration of grid sites 'across' from the random initial site
			// Will end looking like:  "_____" or "_B__A___" etc...
			String toWorkWith = ""; 
			for( int i=0; i < maxPossLength; i++){

				if( grid[init_x+i][init_y] == "_"){
					if( grid[init_x+i][init_y-1].equals("_") && grid[init_x+i][init_y+1].equals("_") ){		

						toWorkWith = toWorkWith + grid[init_x+i][init_y];	
	
					}
					else{  i = (maxPossLength * 2); }	
				}
				else{
					toWorkWith = toWorkWith + grid[init_x+i][init_y];	
				}

			}

			// Now need to fit something to the "toWorkWith" String.
			// Uses **SearchWords.java**
			String[] word_and_def = new String[2];
			String word="";   String definition = "";
			// Get max possible word length
			int maxWordLength = toWorkWith.length();
						
			if( maxWordLength > 2 && toWorkWith.contains("_") ){

				word_and_def = SearchWords.findWord(wordlist, toWorkWith);
				word = word_and_def[0];
				definition = word_and_def[1];
			}


			if( !word.equals("") ){
				
				// Only add entry if it's not been seen before.
				boolean seenBefore = false;
				for( int fedup = 0; fedup<entries.size(); fedup++){
					if( entries.get( fedup ).getWord().equals( word ) ){
						seenBefore = true;
					}
				}
				
				if( !seenBefore ){
					// Then fit has been found
					// Need to modify grid
					for( int g=0; g< word.length(); g++){
						grid[init_x + g][init_y] = "" + word.charAt(g);
					}

					// Update Entries list
					Entry entry = new Entry(init_x, init_y, true, word, definition);
					entries.add ( entry );
				}
			}
			else{
				//System.out.println("No word found");
			}


			// Updates to be passed back to main program:
			// 1. Put word into grid             - DONE
			// 2. Store 'Entry' object in list   - DONE


		}
		else{  // if( grid[init_x-1][y] != "_" )
			//System.out.println("LHS occupied");   // Could do this better by having a list of 'active' start sites.
			}
	}
	//#####################################################################
	//#####################################################################
	
	
	
	
	
	
	//Try and fit words DOWN into grid; updates ArrayList<Entry> and grid.  EXACT SAME PROCESS AS ABOVE!
	public static void fitEntryDown(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries){

		// Choose initial point. Recall edge sites are not 'active'
		int init_x = (int)(Math.random()*(xLength-2) + 1);
		int init_y = (int)(Math.random()*(yLength-2) + 1);

		//Make sure space UP is empty, otherwise do nothing
		if( grid[init_x][init_y - 1].equals("_") ){		

			// Max length of word due to grid size 
			int maxPossLength = (yLength-1) - init_y;

			String toWorkWith = ""; // Might look like:  "_____" or "_B__A___" etc...
			for( int i=0; i < maxPossLength; i++){

				if( grid[init_x][init_y+i] == "_"){
					if( grid[init_x-1][init_y+i].equals("_") && grid[init_x+1][init_y+i].equals("_") ){		

						toWorkWith = toWorkWith + grid[init_x][init_y+i];	
					}
					else{  i = (maxPossLength * 2); }	
				}
				else{
					toWorkWith = toWorkWith + grid[init_x][init_y+i];	
				}

			}


			// Now need to fit something to "toWorkWith"
			String[] word_and_def = new String[2];
			String word="";   String definition = "";
			int maxWordLength = toWorkWith.length();
			
			if( maxWordLength > 2 && toWorkWith.contains("_") ){   ////  is this toWorkWith.contains() OK!?!?!?!? '''''####'####

				word_and_def = SearchWords.findWord( wordlist, toWorkWith);
				word = word_and_def[0];
				definition = word_and_def[1];
			}


			if( !word.equals("") ){
				
				//only add entry if it's not been seen before.
				boolean seenBefore = false;
				for( int fedup = 0; fedup<entries.size(); fedup++){
					if( entries.get( fedup ).getWord().equals( word ) ){
						seenBefore = true;
					}
				}
				
				
				if( !seenBefore ){
					// then fit has been found
					// Need to modify grid
					for( int g=0; g< word.length(); g++){
						grid[init_x][init_y + g] = "" + word.charAt(g);
					}
					
					Entry entry = new Entry(init_x, init_y, false, word, definition);
					entries.add ( entry );
				}
				
			}
			else{
			}
		}
		else{  // if( grid[init_x-1][y] != "_" )
			//System.out.println("LHS occupied");   // Could do this better by having a list of 'active' start sites.
			}
	}
	
	


}
