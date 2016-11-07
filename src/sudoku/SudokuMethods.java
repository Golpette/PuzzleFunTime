package sudoku;
import java.util.ArrayList;
import java.util.Arrays;

public class SudokuMethods {
	/**
	 * Class to generate sudoku starting configurations from a complete grid
	 * Different difficulty levels 
	 */

	
	public static int gridSize = 9;
	
	
	
	public static int[][] makeUnknownDiff(int[][] fullGrid){   // NEW METHOD. DO IDEA HOW HARD
		/**
		 *  Method now just solves the full grid every time a number is removed.
		 *  To dot his, it lists all possible numbers at each site. If there is only 1, we add
		 *  it to grid (for all if multiple), update the lists then repeat until no more can be added.
		 */
		
		int[][] startGrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				startGrid[i][j] = fullGrid[i][j];
			}
		}	
	
		
		// Do this a set number of times / until no more can be removed / specific number of entries are left??
		
		for( int tries=0; tries<2000; tries++ ){
		
			// pick random space and remove      
			int xp=(int)(Math.random()*gridSize);  
			int yp=(int)(Math.random()*gridSize); 
			
			int value_removed = startGrid[xp][yp];
			startGrid[xp][yp]=0;                    /// TODO: CAREFUL!!!

			// Try to solve
			int[][] solved_config = new int[9][9];
			solved_config = SudokuMethods.solver_basic(  startGrid  );
			
			// Check if solved
			boolean is_solved = isSolved( solved_config );
			
			if( is_solved ){
				// remove number and re-enter loop to pick another
			}
			else{
				// put number back and try again
				startGrid[xp][yp] = value_removed;
			}
		
		
		}

		return startGrid;
	}
	
	
	
	
	
	
	
	public static int[][] makeEasy(int[][] fullGrid){   //  MAYBE "VERRRRY" EASY
		/**
		 * Easy sudoku makes sure that at every step (removal) the grid can still be solved
		 * by just looking at the row, column and square and checking if there is exactly
		 * one possible entry 
		 */
		
		int[][] startGrid = new int[9][9];
		
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				startGrid[i][j] = fullGrid[i][j];
			}
		}	
	
		
		// Do this a set number of times / until no more can be removed / specific number of entries are left??
		
		for( int tries=0; tries<100000; tries++ ){
		
			// pick random space and remove            //TODO: do all similar numbers instead of random??
			int xp=(int)(Math.random()*gridSize);  
			int yp=(int)(Math.random()*gridSize); 
			
			int value_removed = startGrid[xp][yp];
			startGrid[xp][yp]=0;                    /// TODO: CAREFUL, this is not printed since it is not in set [1,9] 

			// Get lists of possibilities according to row, column and 3x3 square		
			ArrayList<Integer> poss_row = possibilities_from_row( startGrid, xp, yp );
			ArrayList<Integer> poss_col = possibilities_from_col( startGrid, xp, yp );
			ArrayList<Integer> poss_sqr = possibilities_from_sqr( startGrid, xp, yp );			
			
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// BELOW ARE METHODS AND SOLVERS
	
	
	
	public static boolean isSolved( int[][] grid ){
		/**
		 * Check if grid is full (i.e. solved)
		 */
		boolean solved = true;
		
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				if( grid[i][j] == 0 ){  // 0 entries signify no number //TODO
					solved = false;
				}
			}
		}
		
		return solved;
	}
	
	
	
	
	
	
	@SuppressWarnings("unchecked") // WTF IS THIS!?
	public static int[][] solver_basic( int[][] strtGrid ){
		/**
		 * Method to solve (??) sudokus by writing out every possible number for each
		 * grid space and checking if there is exactly 1 possibility for any one.
		 * If so, add it, update grid lists, look again. 
		 */
		int[][] solvegrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}	
		
		

		// Lists of all possible numbers for each grid point
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int i=0; i<9; i++){
			for( int j=0; j<9; j++ ){
				all_lists[i][j] = new ArrayList<Integer>();
			}
		}
		
		
		
		
		boolean solving = true;
		while( solving ){
		
			// Update all_lists
			for( int i=0; i<9; i++){
				for( int j=0; j<9; j++ ){
					if(solvegrid[i][j] == 0 ){   // MAKE SURE NOT TO GET POSSIBILITIES FOR OCCUPIED SITES!
						all_lists[i][j] = getGridPossibilities( solvegrid, i, j );
					}
					else{
						//all_lists[i][j].clear();  //reset in case this was previously empty
					}
				}
			}
			
			// Check we can actually add something
			boolean anything_to_add = false;
			for( int i=0; i<9; i++){
				for( int j=0; j<9; j++ ){
					if(  all_lists[i][j].size() == 1  ){
						anything_to_add = true;
					}					
				}
			}
			// Stop if we can't add any more
			if( !anything_to_add ){ solving = false; }
			
			
			// Add any definite numbers
			for( int i=0; i<9; i++){
				for( int j=0; j<9; j++ ){
					if(  all_lists[i][j].size() == 1  ){
						int n = all_lists[i][j].get(0);
						solvegrid[i][j] = n;
						all_lists[i][j].clear();
					}
				}
			}

		}
        
		
		return solvegrid;
	}
	
	
	
	
	
	
	
	
	
	
	public static ArrayList<Integer> getGridPossibilities( int[][] strt_grid, int xxx, int yyy ){
		/**
		 * Method to produce list of possible numbers for single grid space
		 */
		ArrayList<Integer> ents = new ArrayList<Integer>();
		
		// Get lists of possibilities according to row, column and 3x3 square		
		ArrayList<Integer> poss_row = possibilities_from_row( strt_grid, xxx, yyy );
		ArrayList<Integer> poss_col = possibilities_from_col( strt_grid, xxx, yyy);
		ArrayList<Integer> poss_sqr = possibilities_from_sqr( strt_grid, xxx, yyy );			
		
		// Take the 3 lists above and check for common numbers.
		for( Integer i: poss_row ){
			if( poss_col.contains(i) && poss_sqr.contains(i) ){
				ents.add(i);
			}
		}		
		return ents;
	}
	
	
	
	
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_sqr( int[][] strt_grid, int xx, int yy ){
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
	
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_row( int[][] strt_grid, int xx, int yy ){
		
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
	
	
	
	
	
	public static ArrayList<Integer> possibilities_from_col( int[][] strt_grid, int xx, int yy ){
		
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
