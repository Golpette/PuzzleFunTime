import java.util.*;
import java.lang.Math;

/**
 * Fitting diagonal words in wordsearch
 * 
 */
public class FitWords_wordsearch {

	//'Random fit approach' to try and fit words ACROSS into grid. Updates ArrayList<Entry> and grid all at once
	public FitWords_wordsearch(String[][] grid, int xLength, int yLength, ArrayList<Word> wordlist, ArrayList<Entry> entries, String dir) {
		
		
		boolean biggestFirst = true;  
		//TODO: we don't want lots of little (3-letter words) but we don't always want to fit the biggest possible.
		//   Make some appropriate word-length weighting here.
		
		
		int init_x = (int) (Math.random() * (xLength - 2) + 1);
	    int init_y = (int) (Math.random() * (yLength - 2) + 1);
			
	    ArrayList<Integer[]> squares = new ArrayList<Integer[]> ();

		//Make sure space on LHS is empty, otherwise do nothing
		String toWorkWith = "";
		if ( dir.equals("across") ) {
			// Max length of word due to grid size 
			int maxPossLength = (xLength - 1) - init_x;
			// Get the configuration of grid sites 'across' from the random initial site
			// Will end looking like:  "_____" or "_B__A___" etc...
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y];			
			}
		} else if( dir.equals("down") ) { 
			int maxPossLength = (yLength - 1) - init_y;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x][init_y + i];					
			}
		} else if( dir.equals("diagonal") ){
			int maxX = (xLength - 1) - init_x;
			int maxY = (yLength - 1) - init_y;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y + i];					
			}
		} else if( dir.equals("backwards") ){
			int maxPossLength = init_x - 1 ;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y];					
			}
		} else if( dir.equals("up") ){
			int maxPossLength = init_y - 1 ;
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x ][init_y - i ];					
			}
		}else if( dir.equals("backwardsdiagonal") ){
			int maxX = init_x - 1;
			int maxY = init_y - 1;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y - i];					
			}
		}else if( dir.equals("backwardsBLTRdiagonal") ){
			int maxX = init_x - 1;
			int maxY = (yLength - 1) - init_y;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x - i][init_y + i];					
			}
		}else if( dir.equals("BLTRdiagonal") ){
			int maxX= (xLength - 1) - init_x;
			int maxY = init_y - 1;
			int maxPossLength = 0;
			if( maxX < maxY ){ maxPossLength = maxX; }
			else{ maxPossLength = maxY; }
			for (int i = 0; i < maxPossLength; i++) {
				toWorkWith = toWorkWith + grid[init_x + i][init_y - i];					
			}
		}else if( dir.equals("snaking") ){		//Attempt at snaking words
			System.out.println("Here");
			int maxPossLength = 10;		// -- could vary this
			squares.clear();
			Integer[] current = {init_x, init_y};
			squares.add(current);
			
			for (int i = 0; i < maxPossLength; i++) {
				int nextAcross = 0;
				int nextDown = 0;
				
				// BCs//
				//while (!(nextAcross == 0 && nextDown == 0)){
					if(squares.get(i)[0] == 0 && squares.get(i)[1] == 0){			//topleft
						nextAcross = (int)(Math.random()*2);
						nextDown = (int)(Math.random()*2);
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here1");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2);
							nextDown = (int)(Math.random()*2);	
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
						//would this work:  if(nextAcross == 0 && nextDown == 0){i--; continue;} 
						//ie repeat the loop ----- there is probably a much simpler way of doing it
						//need to prevent both from being 0 so not putting in same place as first letter *in all methods
					}else if(squares.get(i)[0] == 0 && squares.get(i)[1] == yLength-1){		//top
						nextAcross = (int)(Math.random()*2);
						nextDown = (int)(Math.random()*2) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here2");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2);
							nextDown = (int)(Math.random()*2)-1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[0] == xLength - 1 && squares.get(i)[1] == 0){		//bottom
						nextAcross = (int)(Math.random()*2) - 1;
						nextDown = (int)(Math.random()*2);
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here3");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2)-1;
							nextDown = (int)(Math.random()*2);
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[0] == xLength - 1 &&  squares.get(i)[1] == yLength-1){		//left
						nextAcross = (int)(Math.random()*2) - 1;
						nextDown = (int)(Math.random()*2) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here4");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2)-1;
							nextDown = (int)(Math.random()*2)-1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[0] == 0){
						nextAcross = (int)(Math.random()*2);
						nextDown = (int)(Math.random()*3) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here4");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2);
							nextDown = (int)(Math.random()*3) - 1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[0] == xLength){
						System.out.println("!!!");
						nextAcross = (int)(Math.random()*2) - 1;
						nextDown = (int)(Math.random()*3) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here5");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*2) - 1;
							nextDown = (int)(Math.random()*3) - 1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[1] == 0){
						nextAcross = (int)(Math.random()*3) - 1;
						nextDown = (int)(Math.random()*2);
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here6");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*3) - 1;
							nextDown = (int)(Math.random()*2);
							System.out.println("!!");
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else if(squares.get(i)[1] == yLength-1){
						nextAcross = (int)(Math.random()*3) - 1;
						nextDown = (int)(Math.random()*2) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						System.out.println("Here7");
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*3) - 1;
							nextDown = (int)(Math.random()*2) - 1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}else{	
						System.out.println("Here8");
						//normal condition ie in the middle of the grid
						nextAcross = (int)(Math.random()*3) - 1;
						nextDown = (int)(Math.random()*3) - 1;
						Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
						while(nextAcross == 0 && nextDown == 0 && !squares.contains(newone)){
							nextAcross = (int)(Math.random()*3) - 1;
							nextDown = (int)(Math.random()*3) - 1;
						}
						toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
						squares.add(newone);
					}	
			}
			for(Integer [] a: squares){
				System.out.println(squares.indexOf(a) + ": (" + a[0].toString() + ", "+ a[1].toString() +")" );
				}
		}
		
		
		//////********TO IMPLEMENT:  SNAKING WORDS!!!!********/////////
		
		
		else{
			System.out.println("CROSSWORD DIRECTION NOT DEFINED");  System.exit(1);
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

				// Then fit has been found. Need to modify grid
				for (int g = 0; g < word.length(); g++) {
					if ( dir.equals("across") ) {
						grid[init_x + g][init_y] = "" + word.charAt(g);
					} else if( dir.equals("down") ) {
						grid[init_x][init_y + g] = "" + word.charAt(g);
					} else if( dir.equals( "diagonal" ) ){
						grid[init_x + g][init_y + g] = "" + word.charAt(g);						
					} else if( dir.equals( "backwards" ) ){
						grid[init_x - g][init_y ] = "" + word.charAt(g);						
					} else if( dir.equals( "up" ) ){
						grid[init_x ][init_y - g ] = "" + word.charAt(g);						
					}else if( dir.equals("backwardsdiagonal")  ){
						grid[init_x - g][init_y - g] = "" + word.charAt(g);						
					}else if( dir.equals("BLTRdiagonal")  ){
						grid[init_x + g][init_y - g] = "" + word.charAt(g);	
					}else if( dir.equals("backwardsBLTRdiagonal")  ){
						grid[init_x - g][init_y + g] = "" + word.charAt(g);	
					}else if( dir.equals("snaking")  ){
						//somehow need to put in the actual word grid; 
						//could use the arraylist of numbers: 'squares' 
						//grid[init_x - g][init_y + g] = "" + word.charAt(g);	
						for(Integer [] a: squares){
							grid[a[0]][a[1]] = "" + word.charAt(g);
							System.out.println(word.charAt(g));
						}
					}
					
				}

				// Update Entries list
				Entry entry = new Entry(init_x, init_y, true, word, definition); //across boolean not sensible for wordsearches
				entries.add(entry);
			}
		}
		///}Andy bug
	}
}
