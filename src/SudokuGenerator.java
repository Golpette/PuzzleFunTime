import java.io.*;
import java.util.*;

public class SudokuGenerator{

	public static void main(String args[])throws IOException{
		int crosswordSize = 9;
		int gridSize = crosswordSize + 2;
		int x = gridSize;     
		int y = gridSize;
		int[][] grid = new int[x][y];
		ArrayList<String> acrossClues = new ArrayList<String>();
		ArrayList<String> downClues = new ArrayList<String>();
		ArrayList<Word> words = new ArrayList<Word>();
		words = ReadWords.getWordsandDefs("words_cambridge.txt");		
			grid = new int[x][y];		
			for(int i = 0; i < y; i++){
				for( int j = 0; j < x; j++){
					grid[i][j] = 0;
				}
			}	
			ArrayList<Entry> entries = new ArrayList<Entry>();
			for( int trys=0; trys < 10000; trys++){
				Random r = new Random();
				boolean direction = r.nextBoolean();
				
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
		new DrawSudoku(grid, x, y, acrossClues, downClues);
	}
}
