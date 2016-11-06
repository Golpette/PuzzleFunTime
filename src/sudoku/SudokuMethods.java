package sudoku;
import java.util.ArrayList;
import java.util.Arrays;

public class SudokuMethods {

	/**
	 * Class to generate sudoku starting configurations from a complete grid
	 * Different difficulty levels 
	 */

	
	public static int gridSize = 9;
	
	

	public static int[][] makeEasy(int[][] fullGrid){
		/**
		 * Easy sudoku makes sure that at every step (removal) the grid can still be solved
		 * by just looking at the row, column and square and checking if there is exactly
		 * one possible entry 
		 */
		
		
		int[][] startGrid = fullGrid.clone() ;  
		
		
		// Do this a set number of times / until no more can be removed / specific number of entries are left??
		
		for( int tries=0; tries<100000; tries++ ){
		
			// pick random space and remove            //TODO: do all similar numbers instead of random??
			int xp=(int)(Math.random()*gridSize);  
			int yp=(int)(Math.random()*gridSize); 
			
			int value_removed = startGrid[xp][yp];
			startGrid[xp][yp]=-1;                    /// TODO: CAREFUL, this is not printed since it is not in set [1,9] 

			// Get lists of possibilities according to row, column and 3x3 square		
			ArrayList<Integer> poss_row = getPossibilities_row( startGrid, xp, yp );
			ArrayList<Integer> poss_col = getPossibilities_col( startGrid, xp, yp);
			ArrayList<Integer> poss_sqr = getPossibilities_sqr( startGrid, xp, yp );			
			
			// Take the 3 lists above and check for common numbers.
			//ArrayList<Integer> common_nums = new ArrayList<Integer>();
			int count_cns=0;
			for( Integer i: poss_row ){
				if( poss_col.contains(i) && poss_sqr.contains(i) ){
					//common_nums.add(i);
					count_cns++;
				}
			}
			
			if( count_cns != 1 ){
				// cannot solve sudoku with "easy" method so put number back
				startGrid[xp][yp] = value_removed;
			}
			else if( count_cns ==0 ){
				System.out.println("SOMETHING ODD - NO SHARED NUMBERS AFTER REMOVAL. SudokuMethods.java ");
			}
			else{
				// We have exactly 1 possibility for this square. Leave it out and pick another!
			}
		
		}

		return startGrid;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static ArrayList<Integer> getPossibilities_sqr( int[][] strt_grid, int xx, int yy ){
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		//Get top left of current 3x3 square   // TODO: GENERALISE FOR NON-3X3 GRIDS
		int tl_x = (xx/3)*3;
		int tl_y = (yy/3)*3;		
		
		for( int i=0; i< 3 ; i++ ){    // TODO: GENERALISE FOR NON-3X3 GRIDS
			for( int j=0; j<3; j++ ){
				
				if( poss.contains( strt_grid[tl_x+i][tl_y+j] ) ){
					// then find it in poss and remove it
					int position = poss.indexOf( strt_grid[tl_x+i][tl_y+j]  );
					poss.remove( position );
				}
				
			}
		}
		
		return poss;
	}
	
	
	
	
	
	
	public static ArrayList<Integer> getPossibilities_row( int[][] strt_grid, int xx, int yy ){
		
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		for(int i=1; i<gridSize; i++){   //
			if( poss.contains( strt_grid[i][yy] ) ){
				// then find it in poss and remove it
				int position = poss.indexOf( strt_grid[i][yy]  );
				poss.remove( position );
			}
		}
		
		return poss;
	}
	
	
	
	
	
	public static ArrayList<Integer> getPossibilities_col( int[][] strt_grid, int xx, int yy ){
		
		ArrayList<Integer> poss = new ArrayList<Integer>();		
		for(int i=1; i<gridSize+1; i++){
			poss.add( i );
		}
		
		for(int i=1; i<gridSize; i++){
			if( poss.contains( strt_grid[xx][i] ) ){
				// then find it in poss and remove it
				int position = poss.indexOf( strt_grid[xx][i]  );
				poss.remove( position );
			}
		}
		
		return poss;
	}
	
	
	
	
	
}
