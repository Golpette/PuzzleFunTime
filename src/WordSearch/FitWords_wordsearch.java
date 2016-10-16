package WordSearch;
import java.util.*;

import Crossword.Entry;
import Crossword.Search;
import Crossword.Word;

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
		}
		//}else if( dir.equals("snaking") ){		//Attempt at snaking words
//			System.out.println("Here");
//			int maxPossLength = 5;		// -- could vary this
//			squares.clear();
//			Integer[] current = {init_x, init_y};
//			squares.add(current);
//			
//			for (int i = 0; i < maxPossLength; i++) {
//				int nextAcross;
//				int nextDown;
//				int dirX = 0;
//				int dirY = 0;
//				// BCs//
//				
////				if(squares.get(i)[0] == 1 && squares.get(i)[1] == 1){			//topleft
////					nextAcross = (int)(Math.random()*2);
////					nextDown = (int)(Math.random()*2);
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2);
////						nextDown = (int)(Math.random()*2);
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////					//would this work:  if(n){i--; continue;} 
////					//ie repeat the loop ----- there is probably a much simpler way of doing it
////					//need to prevent both from being 0 so not putting in same place as first letter *in all methods
////				}else if(squares.get(i)[0] == 1 && squares.get(i)[1] == yLength-2){		//top
////					nextAcross = (int)(Math.random()*2);
////					nextDown = (int)(Math.random()*2) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2);
////						nextDown = (int)(Math.random()*2)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[0] == xLength - 2 && squares.get(i)[1] == 1){		//bottom
////					nextAcross = (int)(Math.random()*2) - 1;
////					nextDown = (int)(Math.random()*2);
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2)-1;
////						nextDown = (int)(Math.random()*2);
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[0] == xLength - 2 &&  squares.get(i)[1] == yLength-2){		//left
////					nextAcross = (int)(Math.random()*2) - 1;
////					nextDown = (int)(Math.random()*2) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2)-1;
////						nextDown = (int)(Math.random()*2)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[0] == 1){
////					nextAcross = (int)(Math.random()*2);
////					nextDown = (int)(Math.random()*3) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2);
////						nextDown = (int)(Math.random()*3)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[0] == xLength-2){
////					nextAcross = (int)(Math.random()*2) - 1;
////					nextDown = (int)(Math.random()*3) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*2)-1;
////						nextDown = (int)(Math.random()*3)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[1] == 1){
////					nextAcross = (int)(Math.random()*3) - 1;
////					nextDown = (int)(Math.random()*2);
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*3)-1;
////						nextDown = (int)(Math.random()*2);
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else if(squares.get(i)[1] == yLength-2){
////					nextAcross = (int)(Math.random()*3) - 1;
////					nextDown = (int)(Math.random()*2) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*3)-1;
////						nextDown = (int)(Math.random()*2)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}else{	
////					//normal condition ie in the middle of the grid
////					nextAcross = (int)(Math.random()*3) - 1;
////					nextDown = (int)(Math.random()*3) - 1;
////					Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
////					while(squaresNotFilled(newone[0], newone[1], squares)){
////						nextAcross = (int)(Math.random()*3)-1;
////						nextDown = (int)(Math.random()*3)-1;
////					}
////					toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
////					squares.add(newone);
////				}
//				//
//				
//				if(squares.get(i)[0] == 1 && squares.get(i)[1] == 1){
//					
//				}
//				else if(squares.get(i)[0] == 1 && squares.get(i)[1] == yLength-2){
//					
//				}
//				else if(squares.get(i)[0] == xLength - 2 && squares.get(i)[1] == 1){	
//					
//				}				
//				else if(squares.get(i)[0] == xLength - 2 &&  squares.get(i)[1] == yLength-2){	
//					
//				}				
//				else if(squares.get(i)[0] == 1){
//					
//				}
//				else if(squares.get(i)[0] == xLength-2){
//					
//				}
//				else if(squares.get(i)[1] == 1){
//					
//				}
//				else if(squares.get(i)[1] == yLength-2){
//					
//				}
//				
//				nextAcross = (int)(Math.random()*dirX) - 1;
//				nextDown = (int)(Math.random()*dirY) - 1;
//				Integer[] newone = {squares.get(i)[0]+nextAcross,squares.get(i)[1]+nextDown};
//				while(squaresNotFilled(newone[0], newone[1], squares)){
//					nextAcross = (int)(Math.random()*3)-1;
//					nextDown = (int)(Math.random()*3)-1;
//				}
//				toWorkWith = toWorkWith + grid[newone[0]][newone[1]];	
//				squares.add(newone);
//			}
//			for(Integer [] a: squares){
//				System.out.println(squares.indexOf(a) + ": (" + a[0].toString() + ", "+ a[1].toString() +")" );
//				}
//		}
		
		
		//////********TO IMPLEMENT:  SNAKING WORDS!!!!********/////////
		
		
		else{
			System.out.println("WORDSEARCH DIRECTION NOT DEFINED");  System.exit(1);
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

				if( !dir.equals("snaking" ) ){
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
						}
					}
				}
				else if( dir.equals("snaking")  ){
					//somehow need to put in the actual word grid; 
					//could use the arraylist of numbers: 'squares' 
					//grid[init_x - g][init_y + g] = "" + word.charAt(g);	
					int letter=0;
					for(Integer [] a: squares){
						if( letter>=word.length() ){ break;}
						grid[a[0]][a[1]] = "" + word.charAt( letter );
						letter++;
						//break;
					}
				}
					
				

				// Update Entries list
				Entry entry = new Entry(init_x, init_y, true, word, definition, dir); //across boolean not sensible for wordsearches
//				System.out.println(entry.getWord());
//				System.out.println(entry.palindromic);
				entries.add(entry);
			}
		}
	}
}
