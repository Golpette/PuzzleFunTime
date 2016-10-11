package Sudoku;
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

	public SudokuGenerator(int size)throws IOException{
		row = new ArrayList<Integer>();
		cols = new ArrayList<ArrayList<Integer>>();
		boxes = new ArrayList<ArrayList<Integer>>();
		square = new ArrayList<Integer>();
		tempColumn = new ArrayList<Integer>();
		checks = new ArrayList<Integer>();
		fullGrid = new ArrayList<ArrayList<Integer>>();
		puzzleGrid = new ArrayList<ArrayList<Integer>>();
		
		this.size = size;
		gridSize = size + 2;
		x = gridSize;     
		y = gridSize;
		grid = new int[x][y];		
		for(int i = 0; i < y; i++){
			for( int j = 0; j < x; j++){
				grid[i][j] = 0;
			}
		}	
		generateSudoku();
		removeNumbers(fullGrid);
		System.out.println(fullGrid.toString());
		new DrawSudoku();
	}
	
	public void removeNumbers(ArrayList<ArrayList<Integer>> fullGrid) {
		for(ArrayList<Integer> arrayList: fullGrid){
			for(Integer a: arrayList){
				//this will contain algorithms to systematically remove numbers from grid
				//will temporary remove even numbers
				if(a%2 == 0){
					numbers[fullGrid.indexOf(arrayList)][arrayList.indexOf(a)].setText("");
				}
			}
		}
		
	}

	public void generateSudoku(){
		boxes.clear();
		cols.clear();
		row.clear();
		checks.clear();
		boolean carryOn = false;
		int bound = 9;
		for (int i = 0; i < x-2; i++){	
			ArrayList<Integer> tempRow = new ArrayList<Integer>();
			ArrayList<Integer> tempBox = new ArrayList<Integer>();
			for (int j = 0; j < y-2; j++){
				if(j == 0){
					for(int k = 1; k < 10; k++){
						row.add(k);
						if(i == 0){
							ArrayList<Integer> box = new ArrayList<Integer>();
							boxes.add(box);
							ArrayList<Integer> temps = new ArrayList<Integer>();
							cols.add(temps);
						}
					}
				}
				tempRow.clear();
				tempBox.clear();
				for(Integer a : cols.get(j)){
					if(row.contains(a) && bound > 0){
						tempRow.add(a);
						row.remove(a);
						bound--;
					}
				}
				for(Integer b : boxes.get((i/3)*3+(j/3))){
					if(row.contains(b) && bound > 0){
						tempBox.add(b);
						row.remove(b);
						bound--;
					}
				}
				if(row.isEmpty()){
					if(i != 8 || j != 8){
						System.out.println("got here first");
						generateSudoku();
					}else{
						carryOn = true;
						System.out.println("got here");
						bound = 100;
					}
					System.out.println("Got here");
				}
				if(carryOn){
					int temp = (rand.nextInt(bound));
					int insertion = row.get(temp);
					numbers[i][j].setText(""+insertion);
					checks.add(row.get(temp));
					cols.get(j).add(row.get(temp));
					boxes.get(((i/3)*3)+(j/3)).add(row.get(temp));
					row.remove(row.get(temp));
					bound--;
					for(Integer a: tempRow){
						if(!row.contains(a)){
							row.add(a);
						bound++;
						}
					}
				}
				for(Integer b: tempBox){
					if(!row.contains(b)){
						row.add(b);
						System.out.println("B: "+b);
						fullGrid.get(i).add(b);
						bound++;
					}
				}
			}
		}		
	}

	
	
}
