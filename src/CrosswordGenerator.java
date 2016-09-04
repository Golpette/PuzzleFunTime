import java.io.*;
import java.util.*;

public class CrosswordGenerator{

	public static void main(String args[])throws IOException{
		
	//public CrosswordGenerator() throws IOException{
		int crosswordSize = 10;
		int gridSize = crosswordSize + 2;
		int x = gridSize;     
		int y = gridSize;
		String[][] grid = new String[x][y];
		String[][] gridInit = new String[x][y];
		ArrayList<String> acrossClues = new ArrayList<String>();
		ArrayList<String> downClues = new ArrayList<String>();
		ArrayList<Word> words = new ArrayList<Word>();
		words = ReadWords.getWordsandDefs("words_cambridge.txt");
		boolean connected = false;			// Create grids until you find a connected one!  
		while(!connected){					// Actual 'active' grid is 2 shorter in each direction 
			grid = new String[x][y];		// (edges kept blank for simplicity in later methods)
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = "_";
				}
			}	
			ArrayList<Entry> entries = new ArrayList<Entry>();
			for( int trys=0; trys < 10000; trys++){
				Random r = new Random();
				boolean direction = r.nextBoolean();
				if(!direction){
					new FitWords(grid, x, y, words, entries, true);
				}
				else{
					new FitWords(grid, x, y, words, entries, false);
				}
			}
			gridInit = new String[x][y];			//Generate  2 lists of ACROSS and DOWN clues from 'entries' and generate
			for(int i=0; i<y; i++){					//a 'grid_init' to hold the clue indices.  REFRESH grid_init.
				for( int j=0; j<x; j++){
					gridInit[i][j] = "";
				}
			}
			acrossClues.clear();
			downClues.clear();
			int problemNumber = 0;
			for(int i = 1; i < y-1; i++){
				for(int j = 1; j < x-1; j++){
					boolean twoOnSameSite = false;
					for(int current=0; current <entries.size(); current++){
						if(entries.get(current).getX() == j && entries.get(current).getY() == i){
							if(!twoOnSameSite){problemNumber++;}
							twoOnSameSite=true;
							gridInit[j][i] = Integer.toString(problemNumber);
							if(entries.get(current).isAcross()){
								acrossClues.add(Integer.toString(problemNumber) + ". " + entries.get(current).getDefinition());
							}
							else{
								downClues.add(Integer.toString(problemNumber) + ". " + entries.get(current).getDefinition());
							}						
						}
					}				
				}
			}	
			connected = HoshenKopelman.isConnected(grid, x, y);
		}
		new DrawProblem(gridInit, grid, x, y, acrossClues, downClues);
	}
}
