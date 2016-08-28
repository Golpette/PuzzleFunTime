import java.util.*;

// Class to hold HoshenKopelman method which can tell us if grid is connected / how many
// disconnected clusters are there / how big are they(?)... etc...
// Is there anything interesting in this? Percolation threshold of the Cambridge dictionary? LOLZ.


public class HoshenKopelman {
	
	
	
	public static void replaceAll( int[][] HK_grid, int a, int b, int xLength, int yLength ){
        /**  Method to replace all 'a's with 'b's in an array, int[][].  **/
		
		for( int i=1; i<yLength-1; i++){
			for( int j=1; j<xLength; j++){
				if( HK_grid[j][i] == a ){
					//Then make it b
					HK_grid[j][i] = b;
				}
			}
		}	
	}
	
	
	
	

	public static boolean isConnected( String[][] grid, int xLength, int yLength ){
		/** Hoshen-Kopelman (ish?) algorithm to check if crossword is fully connected  -- 
		    This method implicitly counts the number of clusters too...  **/		
		
		// Array to hold temporary labels. All entries initially 0
		int[][] HK_grid = new int[xLength][yLength];
		
		int HK_label = 1;
		for( int i=1; i<yLength-1; i++){
			for( int j=1; j<xLength; j++){
				
				if( grid[j][i] != "_"){ //This would indicate black square in crossword
					
					if( HK_grid[j][i-1] != 0){
						HK_grid[j][i] = HK_grid[j][i-1];
						//Now if left square is different, do union
						if( HK_grid[j-1][i]!=0  &&  HK_grid[j-1][i] != HK_grid[j][i] ){
							//Then go through replacing all occurrences of one with the other
							replaceAll(HK_grid, HK_grid[j-1][i], HK_grid[j][i], xLength, yLength );
						}				
					}
					else if( HK_grid[j-1][i] != 0 ){
						HK_grid[j][i] = HK_grid[j-1][i];
						//Since we are here, the up square must be a zero so no union needed.						
					}
					else{
						HK_grid[j][i] = HK_label;
						HK_label++;
					}
				}
			}
		}
		
		//Count the number of disconnected domains (i.e. number of distinct labels in HK_grid)
		ArrayList<Integer> labels = new ArrayList<Integer>();
		for( int i=1; i<yLength-1; i++){
			for( int j=1; j<xLength; j++){		
				if( HK_grid[j][i] != 0){
					// Check if this label is already in the labels list, add if not
					if( !labels.contains( HK_grid[j][i] ) ){
						labels.add( HK_grid[j][i] );
					}			
				}		
			}
		}
		
		boolean connected=true;
		if( labels.size() == 1 ){
			connected=true;
		}
		else{  connected = false; }
		
		//System.out.println(  labels.size() );  This is the number of clusters!
		
		return connected;
	}
	
	
	

	
	
}
