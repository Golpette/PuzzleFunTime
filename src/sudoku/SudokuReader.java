package sudoku;

import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

import crossword.Word;

public class SudokuReader {
	/**
	 * Class for reading in sudoku puzzles to test solve methods 
	 */
	
	
	
	public static int[][] readSudoku(String filename)throws IOException{
		/**
		 * Method to read in sudoku from text file
		 */
		int[][] grid = new int[9][9];
		for(int i=0;i<9;i++){
			for(int j=0;j<9;j++){
				grid[i][j]=0;
			}
		}
		
		
		BufferedReader in = new BufferedReader(new FileReader(filename));
		String s="";
		Scanner scan = new Scanner(s);
		
		int count_lines=-1;
		
		while(true){
			try{
				
				s = in.readLine();
				scan = new Scanner(s);
				
				count_lines++;
				
				//read each grid from line:
				int count_cols=-1;
				while(true){
					try{
						
						int num = Integer.parseInt( scan.next()  );
						System.out.println( num );
						count_cols++;
						if(count_cols>8){
							System.out.println("SUDOKU FILE DOES NOT CONTAIN VALID SUDOKU GRID. MUST BE 9x9");
						}
					
						grid[count_lines][count_cols] = num;  //NB: flipped these since Andy changed x and y coords.
						
					}
					catch(NoSuchElementException e){
						break;
					}
				}
				
			}
			catch(NullPointerException e){
				break;
			}
			
		}
		in.close();
		scan.close();		
		
	
		
		
		
		return grid;
	}
	
	
}
