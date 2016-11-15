package sudoku;
import java.util.ArrayList;


public class SudokuMethods {
	/**
	 * Class to generate sudoku starting configurations from a complete grid
	 * Different difficulty levels 
	 */

	
	//TODO: - tidy code
	//      - add arrow keys functionality for selecting grids
	//      - change colour of fixed entries? dark grey?
	//      - add check method to highlight correct/wrong numbers in green/red
	//      - general style: rounded corners, different colours, ...
	//
	//
	// - plot # starting entries for different methods. Any stat phys in there?
	// - (stat phys for crosswords - i.e. english language percolation! / word occurrences on bbc website / ... MAKE WEBSITE)
	
	
	public static int gridSize = 9;
	
	
	
	public static int[][] makeMedium(int[][] fullGrid){   // Maybe medium? Maybe hard?
		/**
		 *  Method just solves the full grid every time a number is removed.
		 *  To do this, it lists all possible numbers that can go at each site. If there is only 1, we add
		 *  it to grid (for all if multiple single choices), update the lists then repeat until no more can be added.
		 */
		
		int[][] startGrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				startGrid[i][j] = fullGrid[i][j];
			}
		}	
	
		
		// Remove numbers. Do this a set number of times / until no more can be removed 
		for( int tries=0; tries<1000; tries++ ){ //1000 is probably enough
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isSolved( int[][] grid ){   // TODO: THIS ONLY CHECKS GRID IS FULL... NOT ACTUALLY SOLVED CORRECTLY
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
	
	
	
	
	
	
	
	

	
	
	public static int[][] solver_improved( int[][] strtGrid ){
		/**
		 * Improved method now looks at all possibilities in rows, columns and 3x3s
		 * and checks if any appear in only 1 grid. i.e. there may be 2 or more options in
		 * every grid in a given row, but if only one of them contains a "4" option, this must be the 4.
		 */
		int[][] solvegrid = new int[9][9];
		for( int i=0; i<9; i++ ){
			for( int j=0; j<9; j++){
				solvegrid[i][j] = strtGrid[i][j];
			}
		}	
//		// Lists of all possible numbers for each grid point
//		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
//		for( int i=0; i<9; i++){
//			for( int j=0; j<9; j++ ){
//				all_lists[i][j] = new ArrayList<Integer>();
//			}
//		}
		
		
		
		boolean solving = true;
		
		int terminate = 0;
		
		while(solving){
			
			terminate++;
			
			
//			// Update all grid possibilities
//			for( int i=0; i<9; i++){
//				for( int j=0; j<9; j++ ){
//					if(solvegrid[i][j] == 0 ){   // Do not get possibilities from occupied sites
//						all_lists[i][j] = getGridPossibilities( solvegrid, i, j );
//					}
//				}
//			}
			
			// HOLD CURRENT GRID TO CHECK FOR DIFFERENCES
			int[][] prev_grid = new int[9][9];
			for( int i=0; i<9; i++ ){
				for( int j=0; j<9; j++){
					prev_grid[i][j] = solvegrid[i][j];
				}
			}	
			

			
			// add from checking rows
			add_definites_ROWS( solvegrid );
								
			// checking cols
		    add_definites_COLS( solvegrid );
						
			// TODO: check 3x3s
			add_definites_3x3s( solvegrid );
			
			
			
			
			// check we made at least 1 change
			boolean anything_added = false;
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if ( solvegrid[iii][jjj] != 0  &&  prev_grid[iii][jjj] == 0 ){
						anything_added = true;
					}
				}
			}
			if( !anything_added){
				solving=false;
			}
			
			//if( terminate>0 ){ solving=false;}
			
		}//end while
		
		
		
		
		
		
		return solvegrid;
	}
	
	

	
	

	
	
	
	
	
	
	public static void add_definites_ROWS( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each ROW
		 * Make a "row_required" list; check if any grids only have 1 of these
		 */
	

		
		
		//for each row
		for( int i=0; i<9; i++){
			
			
			// Lists of all possible numbers for each grid point
			@SuppressWarnings("unchecked")
			ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					all_lists[iii][jjj] = new ArrayList<Integer>();
				}
			}	
			// Update all grid possibilities
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}		
			
			 
			// make list of numbers this row needs
			ArrayList<Integer> required = new ArrayList<Integer>(); /// TODO: WE CNA MAKE THIS REQUIRED LIST MUCH EASIER!
			for( int j=0; j<9; j++ ){
				//ArrayList<Integer> grid_poss =  new ArrayList<Integer>( all_lists[i][j] );		
				
				//System.out.println( all_lists[i][j] );
				
				for( Integer poss: all_lists[i][j] ){
					if( !required.contains(poss) ){
						required.add( poss );
					}
				}
			}
			
			//System.out.println( "required = "+required );
			//System.exit(0);
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int j=0; j<9; j++ ){
					if( all_lists[i][j].contains( r ) ){  count_r++; }
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int j=0; j<9; j++ ){
						if( all_lists[i][j].contains( r ) ){  
							// add in the definite entry!
							
							grid[i][j] = r;   //STEVE CHANGE BACK
							//grid[j][i] = r;
							
							//System.out.println("put "+r +" in ("+ i + "," +j+")");

							//System.exit(0);

						}
					}
					
				}
				
			}
			
			
		
//			// Before choosing next row, makes sense to update possibilities
//			// but maybe this is unnecessary computational effort. MAYBE JUST REMOVE THIS.
//			for( int iii=0; iii<9; iii++){
//				for( int jjj=0; jjj<9; jjj++ ){
//					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
//						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
//					}
//				}
//			}
			
			// Update all grid possibilities
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					all_lists[iii][jjj].clear();
				}
			}	
			
			
			//System.exit(0);

			
			
		}//end row
		

			
	}
	
	
	
	
	
	public static void add_definites_COLS( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each COLUMN
		 * Make a "col_required" list; check if any grids only have 1 of these
		 */
		// Lists of all possible numbers for each grid point
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int i=0; i<9; i++){
			for( int j=0; j<9; j++ ){
				all_lists[i][j] = new ArrayList<Integer>();
			}
		}		
		// Update all grid possibilities
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
					all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
				}
			}
		}
		
		
		//for each COLUMN
		for( int j=0; j<9; j++){
			
			// make list of number this row needs
			ArrayList<Integer> required = new ArrayList<Integer>();
			for( int i=0; i<9; i++ ){
				//ArrayList<Integer> grid_poss = all_lists[i][j];			
				//for( Integer poss: grid_poss ){
				for( Integer poss: all_lists[i][j] ){
					if( !required.contains(poss) ){
						required.add( poss );
					}
				}
			}
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int i=0; i<9; i++ ){
					if( all_lists[i][j].contains( r ) ){  count_r++; }
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int i=0; i<9; i++ ){
						if( all_lists[i][j].contains( r ) ){  
							// add in the definite entry!
							grid[i][j] = r;
						}
					}
					
				}
				
			}
			
		
			// Before choosing next row, makes sense to update possibilities
			// but maybe this is unnecessary computational effort. MAYBE JUST REMOVE THIS.
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}
			
		}
		
	}
	
	
	

	
	

	
	
	
	
	
	
	public static void add_definites_3x3s( int[][] grid ){
		/**
		 * Use all grid possibilities for all grids for each 3X3 GRID
		 * Make a "3x3_required" list; check if any grids only have 1 of these
		 */
		// Lists of all possible numbers for each grid point
		ArrayList<Integer>[][] all_lists =  (ArrayList<Integer>[][])new ArrayList[9][9];
		for( int i=0; i<9; i++){
			for( int j=0; j<9; j++ ){
				all_lists[i][j] = new ArrayList<Integer>();
			}
		}		
		// Update all grid possibilities
		for( int iii=0; iii<9; iii++){
			for( int jjj=0; jjj<9; jjj++ ){
				if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
					all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
				}
			}
		}
		
		
		//for each 3x3 grid!  Layout is: top left 3x3 is called 0, top middle, 1 and so on.
		for( int sq=0; sq<9; sq++){
			
			int x_strt = (sq%3)*3;
			int y_strt = (sq/3)*3;
			
						
			// make list of number this 3x3 grid needs
			ArrayList<Integer> required = new ArrayList<Integer>();
			for( int i=x_strt; i<3; i++ ){
				for( int j=y_strt; j<3; j++){
				
					//ArrayList<Integer> grid_poss = all_lists[i][j];			
//					for( Integer poss: grid_poss ){
					for( Integer poss : all_lists[i][j] ){
						if( !required.contains(poss) ){
							required.add( poss );
						}
					}
				
				}
			}
			
			
			
			// now check how many squares each possibility appears in
			for( Integer r: required){
				
				int count_r=0;
				for( int i=x_strt; i<3; i++ ){
					for( int j=y_strt; j<3; j++ ){
						if( all_lists[i][j].contains( r ) ){  count_r++; }
					}
				}
				if( count_r==1 ){
					// then we know we can add this number. find out where:
					for( int i=x_strt; i<3; i++ ){
						for( int j=y_strt; j<3; j++){
							if( all_lists[i][j].contains( r ) ){  
								// add in the definite entry!
								grid[i][j] = r;
							}
						}
					}
					
				}
				
			}
			
		
			// Before choosing next row, makes sense to update possibilities
			// but maybe this is unnecessary computational effort. MAYBE JUST REMOVE THIS.
			for( int iii=0; iii<9; iii++){
				for( int jjj=0; jjj<9; jjj++ ){
					if( grid[iii][jjj] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[iii][jjj] = getGridPossibilities( grid, iii, jjj );
					}
				}
			}
			
			
		}
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unchecked") // WTF IS THIS!?
	public static int[][] solver_basic( int[][] strtGrid ){
		/**
		 * Method to solve (??) sudokus by writing out every possible number for each
		 * grid space and checking if there is exactly 1 possibility for any of them.
		 * If so, add them, update grid lists, look again. 
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
					if(solvegrid[i][j] == 0 ){   // Do not get possibilities from occupied sites
						all_lists[i][j] = getGridPossibilities( solvegrid, i, j );
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
						// clear the corresponding list
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
			if( poss_col.contains(i) && poss_sqr.contains(i) ){     // STEVE TRYING TO DEBUG
			//if( poss_col.contains(i) && poss_sqr.contains(i) ){

				ents.add(i);
			}
		}		
		
		//if( strt_grid[xxx][yyy]==0 ){  ents.clear(); }
		
		
		
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
		
		for(int i=0; i<gridSize; i++){   //
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
		
		for(int i=0; i<gridSize; i++){
			if( poss.contains( strt_grid[xx][i] ) ){
				// then find it in poss and remove it
				int position = poss.indexOf( strt_grid[xx][i]  );
				poss.remove( position );
			}
		}
		
		return poss;
	}
	
	
	
	
	
}
