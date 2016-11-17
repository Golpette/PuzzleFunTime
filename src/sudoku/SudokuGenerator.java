package sudoku;
import java.io.*;
import java.util.*;

import javax.swing.JLabel;

public class SudokuGenerator{
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> boxes, cols, fullGrid, puzzleGrid;
	int size;
	int gridSize;
	int x;     
	int y;
	int[][] grid;
	Random rand;
	JLabel [][] numbers;
	
	

	public SudokuGenerator(int difficulty)throws IOException{
		

		row = new ArrayList<Integer>();
		cols = new ArrayList<ArrayList<Integer>>();
		boxes = new ArrayList<ArrayList<Integer>>();
		square = new ArrayList<Integer>();
		tempColumn = new ArrayList<Integer>();
		checks = new ArrayList<Integer>();
		fullGrid = new ArrayList<ArrayList<Integer>>();
		puzzleGrid = new ArrayList<ArrayList<Integer>>();
		
		
		

		
		
		// Solution and start grid made in here.
		new DrawSudoku(difficulty);
		
		
	}

	
	
	
	
	
	
	
//	public void removeNumbers(ArrayList<ArrayList<Integer>> fullGrid) {
//		for(ArrayList<Integer> arrayList: fullGrid){
//			for(Integer a: arrayList){
//				//this will contain algorithms to systematically remove numbers from grid
//				//will temporary remove even numbers
//				if(a%2 == 0){
//					numbers[fullGrid.indexOf(arrayList)][arrayList.indexOf(a)].setText("");
//				}
//			}
//		}
//		
//	}


	
	
}
