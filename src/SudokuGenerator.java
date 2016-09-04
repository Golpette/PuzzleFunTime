import java.io.*;
import java.util.*;

public class SudokuGenerator{

	int size;
	int gridSize;
	int x;     
	int y;
	int[][] grid;
	
	public SudokuGenerator(int size)throws IOException{
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
		new DrawSudoku(grid, x, y);
	}
}
