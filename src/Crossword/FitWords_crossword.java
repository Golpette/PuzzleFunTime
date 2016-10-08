package Crossword;
import java.util.*;
import java.lang.Math;

/**
 * Class holds two methods: for fitting *across* and *down* in the crossword
 * 
 * Have played with the fitting procedure, trying to make prettier crosswords by forcing the corner
 * cells to be occupied. I let these be random lengths, then after a certain number of words are
 * fitted, I start forcing the biggest possible words. This helps ensure connectivity and looks nicer.
 * 
 * 
 */
public class FitWords_crossword {

	//'Random fit approach' to try and fit words ACROSS into grid. Updates ArrayList<Entry> and grid all at once
	public FitWords_crossword(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries, boolean across) {
		// Steve added "type" since fitting is different for crosswords and wordsearches
		
		boolean biggestFirst;
		int init_x;
	    int init_y;
		

	    // Steve:  choose to fit biggest first or random sizes (in any case, we need to fit biggest word
	    //         when starting from bottom right or we'll miss the final square -------------------------
	    biggestFirst = false;   // This is fed into Search.findWord()

	    if( entries.size() >= 6 ){  //make this depend on size of crossword
	    	biggestFirst = true;
	    }

	    // Make sure corner cells are always occupied//	        
	    //	        if(  grid[xLength-2][yLength-2].equals("_")  ){
	    //	           int rndm_length = (int) (Math.random() * (xLength - 2 - 4) );     // CAREFUL WITH SMALL CROSSWORDS
	    //	           init_x = xLength - 2 - rndm_length ;   init_y = yLength-2;  
	    //	            // force exact size on word; a random choice can leave corner blank
	    //	            biggestFirst = true ;
	    //	        }
	    //	        else if( grid[xLength-2][1].equals("_")  ){      // BUG HERE. SOMETIMES MISSES THE BOTTOM RIGHT SQUARE. WHY!?!
	    if( grid[xLength-2][1].equals("_")  ){
	    	init_x = xLength-2;   init_y = 1;
	    	biggestFirst = false;
	    }
	    else if( grid[1][yLength-2].equals("_")  ){
	    	init_x = 1;  init_y=yLength-2;
	    	biggestFirst = false;
	    }
	    else if( grid[1][1].equals("_") ){
	    	//if(false){
	    	init_x = 1;  init_y=1;
	    	biggestFirst = false;
	    }else{
	    	init_x = (int) (Math.random() * (xLength - 2) + 1);
	    	init_y = (int) (Math.random() * (yLength - 2) + 1);
	    } //-------------------------------------------------------------------------------------------






		//Make sure space on LHS is empty, otherwise do nothing
		//if (grid[init_x][init_y - 1].equals("_") && grid[init_x - 1][init_y].equals("_")) {   //Andy's bug
		String toWorkWith = "";
		if (across && grid[init_x - 1][init_y].equals("_")) {

			// Max length of word due to grid size 
			int maxPossLength = (xLength - 1) - init_x;

			// Get the configuration of grid sites 'across' from the random initial site
			// Will end looking like:  "_____" or "_B__A___" etc...
			for (int i = 0; i < maxPossLength; i++) {
				if (grid[init_x + i][init_y] == "_") {
					if (grid[init_x + i][init_y - 1].equals("_") && grid[init_x + i][init_y + 1].equals("_")) { //steve fix andy change
						toWorkWith = toWorkWith + grid[init_x + i][init_y];
					} else {
						break;
					}
				}else{
					toWorkWith = toWorkWith + grid[init_x + i][init_y];
				}
			}
		} else if(!across && grid[init_x][init_y - 1].equals("_")) {  // steve fix andy change
			int maxPossLength = (yLength - 1) - init_y;

			for (int i = 0; i < maxPossLength; i++) {
				if (grid[init_x][init_y + i] == "_") {
					if (grid[init_x - 1][init_y + i].equals("_") && grid[init_x + 1][init_y + i].equals("_")) {
						toWorkWith = toWorkWith + grid[init_x][init_y + i];
					} else {
						break;
					}
				} else {
					toWorkWith = toWorkWith + grid[init_x][init_y + i];
				}
			}
		}

		// Now need to fit something to the "toWorkWith" String.
		// Uses **SearchWords.java**
		String[] word_and_def = new String[2];
		String word = "";
		String definition = "";

		// Get max possible word length
		int maxWordLength = toWorkWith.length();
		if (maxWordLength > 2 && toWorkWith.contains("_")) {
			
			word_and_def = Search.findWord(wordlist, toWorkWith, biggestFirst);   //new parameter here
			
			word = word_and_def[0];
			definition = word_and_def[1];
		}
		if (!word.equals("")) {

			// Only add entry if it's not been seen before.
			boolean seenBefore = false;
			for (int j = 0; j < entries.size(); j++) {
				if(word.toLowerCase().contains(entries.get(j).getWord().toLowerCase())){
					seenBefore = true;
				}
			}
			if (!seenBefore) {

				// Then fit has been found
				// Need to modify grid
				for (int g = 0; g < word.length(); g++) {
					if (across) {
						grid[init_x + g][init_y] = "" + word.charAt(g);
					} else {
						grid[init_x][init_y + g] = "" + word.charAt(g);
					}
				}

				// Update Entries list
				Entry entry = new Entry(init_x, init_y, across, word, definition);
				entries.add(entry);
			}
		}
		///}Andy bug
	}
}
