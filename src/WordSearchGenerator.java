import java.io.*;
import java.util.*;

public class WordSearchGenerator{

	int wordsearchSize;
	int gridSize;
	int x;     
	int y;
	String[][] grid;
	ArrayList<String> acrossClues;
	ArrayList<String> downClues;
	ArrayList<Word> words;	
	
	public WordSearchGenerator(int wordsearchSize) throws IOException{
		this.wordsearchSize = wordsearchSize;
		gridSize = wordsearchSize + 2;
		x = gridSize;     
		y = gridSize;
		grid = new String[x][y];
		acrossClues = new ArrayList<String>();
		downClues = new ArrayList<String>();
		ArrayList<Word> words = new ArrayList<Word>();
		words = ReadWords.getWordsandDefs("words_cambridge.txt");		
			grid = new String[x][y];		
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = "_";
				}
			}	
			
			ArrayList<Entry> entries = new ArrayList<Entry>();
			
			
			
			int num_words_to_fit = 12;  // TODO: CHOOSE THIS DEPENDING ON GRID SIZE
			int count_tries = 0;
			
			//for( int trys=0; trys < 20; trys++){
			while( entries.size() < num_words_to_fit   && count_tries<10000 ){  //stop infinite loop

				count_tries++;	
				int direc = (int)(Math.random()*3);
				
				if( direc==0 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "across" );
				}
				else if( direc==1 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "down" );
				}
				else if( direc==2 ){
					new FitWords_wordsearch(grid, x, y, words, entries, "diagonal" );
				}
				
			}
			
			acrossClues.clear();
			downClues.clear();
			int c=1;
			
			for(int i = 1; i < y-1; i++){   //STEVE: modified from crossword. Don't care about direction or clue numbers
				for(int j = 1; j < x-1; j++){
					for(int current=0; current <entries.size(); current++){
						if(entries.get(current).getX() == j && entries.get(current).getY() == i){
							acrossClues.add(Integer.toString(c)+ ". " + entries.get(current).getDefinition());	
							c++;
						}
					}				
				}
			}	
			
			
			
		new DrawWordSearch(grid, x, y, acrossClues, downClues, entries);
	}
}
