package sudoku;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import crossword.JTextFieldLimit;

/**
 * Class to draw a word search
 */
public class DrawSudoku extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize =60;	
	int x, y;
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	JFrame frame;
	JPanel panel, transparentLayer, largeGrid;
	JLayeredPane layer;
	JLabel [][] numbers, threeByThreeGrid;
	JTextField [][] nums;
	int[][] grid;
	String[][] grid2;
	GridBagConstraints c;
	JButton reveal;
	DrawSudokuSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> cols;
	ArrayList<ArrayList<Integer>> boxes;
	Font font, font2;
	Random rand;
	boolean buttonPushed;
	
	//@SuppressWarnings("unchecked")
	public DrawSudoku(int[][] grid, int x, int y, int difficulty) throws IOException{
		
		this.x = x;
		this.y = y;
		font = new Font("Century Gothic", Font.PLAIN, 30);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		
		
		sol = new DrawSudokuSolution( x, y);  // Create the whole object that will be revealed on "show solution"
		sol.generateSudoku(grid);             // Andy's algorithm to generate sudoku solution.
		sol.setVisible(false);
		
		
		// We now have a full grid[][] for sudoku base. Modify grid[][] to starting configuration
		int[][] initial_config = new int[9][9];
		
		
		
		
		
		
		
		// TEST SUDOKU READER METHOD.
		//initial_config = SudokuReader.readSudoku("sudoku_finland.dat");
		initial_config = SudokuReader.readSudoku("sudoku_medium2.dat");

		
		// Attempt to solve
		int[][] solved_grid = new int[9][9];
//		solved_grid = SudokuMethods.solver_basic( initial_config );
		solved_grid = SudokuMethods.solver_improved( initial_config );

		
		System.out.println("Is solved? -- "+ SudokuMethods.isSolved(solved_grid)  );
		

		
		
		
		
		
		
//		
//		if( difficulty == 2 ){
//			initial_config = SudokuMethods.makeEasy( grid );
//		}
//		else if( difficulty == 4 ){
//			initial_config = SudokuMethods.makeMedium( grid );  // Might be ridiculously hard...
//		}
//		else{
//			System.out.println("ONLY EASY AND MEDIUM HAVE BEEN IMPLEMENTED");
//			initial_config = SudokuMethods.makeMedium( grid );
//		}		
//		//System.out.println( grid[0][0] + " " + grid[1][1] + " "+ grid[0][1]);   // CAREFUL: ANDY HAS FLIPPED [X][Y] TO GRID[Y][X]
//		
//		
		
//		for( int ii=0; ii<9; ii++){
//			for( int jj=0; jj<9; jj++){
//				//if( initial_config[ii][jj] != 0 ){
//					ArrayList<Integer> poss = SudokuMethods.getGridPossibilities(initial_config, ii, jj);
//					System.out.print( poss + "  ##  ");
//				//}
//				//else{
//				//	System.out.print( "[]  xx  ");
//				//}
//			}
//			System.out.println();
//		}
//		
		
		
		
		
		
		
		
		
		row = new ArrayList<Integer>();
		cols = new ArrayList<ArrayList<Integer>>();
		boxes = new ArrayList<ArrayList<Integer>>();
		square = new ArrayList<Integer>();
		tempColumn = new ArrayList<Integer>();
		checks = new ArrayList<Integer>();
		fullGrid = new ArrayList<String>();
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		layer = new JLayeredPane();
		numbers = new JLabel [x-2][y-2];
		nums = new JTextField [x-2][y-2];
		threeByThreeGrid = new JLabel [3][3];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.GRAY);	  // this is border for single grids
		Border border2 = BorderFactory.createLineBorder(Color.BLACK);     //  border for 3x3 squares
		//Border border3 = BorderFactory.createLineBorder(Color.BLACK, 4, true);
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		//transparentLayer.setOpaque(true);
		transparentLayer.setOpaque(false); // STEVE
		transparentLayer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		largeGrid = new JPanel(new GridLayout(3, 3));
		largeGrid.setBounds(squareSize-1,squareSize-1,squareSize*(x-2),squareSize*(y-2));
		largeGrid.setOpaque(false);
		largeGrid.setAlignmentX(JComponent.CENTER_ALIGNMENT);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				nums[i][j] = new JTextField();
				nums[i][j].setFont(font);
				nums[i][j].setForeground(Color.BLACK);
				nums[i][j].setBorder(border);
				nums[i][j].setHorizontalAlignment(JTextField.CENTER);
				nums[i][j].setDocument(new JTextFieldLimit(1, false));
				transparentLayer.add(nums[i][j]);
			}
		}
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				threeByThreeGrid[i][j] = new JLabel();
				threeByThreeGrid[i][j].setBorder(BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
				//threeByThreeGrid[i][j].setBorder(border2);
				//threeByThreeGrid[i][j].setBorder(border3);
				largeGrid.add(threeByThreeGrid[i][j]);
			}
		}
		
		transparentLayer.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		
		layer.add(transparentLayer, new Integer(0));
		layer.add(largeGrid, new Integer(1));
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);
		
		frame = new JFrame("Auto Sudoku");
		frame.setPreferredSize(new Dimension(squareSize*(x)+squareSize/2,squareSize*(y+2)+10));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		//frame.setMinimumSize(new Dimension(550,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
		//generateSudoku();
		
		
		
	
		
		
		
		
		
		// Put starting config into nums[][] JTextFields
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if( solved_grid[i][j] != 0 ){                      // CHANGE X AND Y COORDS FFS
					nums[i][j].setText( Integer.toString( solved_grid[i][j])  );
					nums[i][j].setEditable(false);
					//nums[i][j].setForeground(new Color( 163 , 194,  163));
					nums[i][j].setForeground(Color.BLACK);
					nums[i][j].setHighlighter(null);
					nums[i][j].setBackground(Color.WHITE);;
				}
			}
		}		
		
//		// Put starting config into nums[][] JTextFields
//		for (int i = 0; i < x-2; i++){
//			for (int j = 0; j < y-2; j++){
//				if( initial_config[i][j] != 0 ){                      // TODO: CHANGE THIS 0 THING
//					nums[i][j].setText( Integer.toString( initial_config[i][j])  );
//					nums[i][j].setEditable(false);
//					//nums[i][j].setForeground(new Color( 163 , 194,  163));
//					nums[i][j].setForeground(Color.BLACK);
//					nums[i][j].setHighlighter(null);
//					nums[i][j].setBackground(Color.WHITE);;
//				}
//			}
//		}
		

		
		// Count number of starting entries (just to gauge difference between methods)
		int cnt_nums=0;
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if( initial_config[i][j] != 0 ){                      // TODO: CHANGE THIS 0 THING
					cnt_nums++;
				}
			}
		}
		
		System.out.println("Number of initial entries = " + cnt_nums);		
		
		
		
		// CARE. HERE INITIAL_CONFIG HAS LOTS OF -1 ENTRIES
		
		
//		for (int i = 0; i < x-2; i++){
//			for (int j = 0; j < y-2; j++){
//				System.out.print( initial_config[i][j] + " ");
//			}
//		}
		
		
		
//		int[][] solved_config = new int[9][9];
//		solved_config = SudokuMethods.solver_easy(  initial_config );
//				
//		
//		
//		// UPDATE AFTER SOLVING
//		for (int i = 0; i < x-2; i++){
//			for (int j = 0; j < y-2; j++){
//				if( solved_config[i][j] != 0 ){       
//					nums[i][j].setText( Integer.toString( solved_config[i][j])  );
//				}
//			}
//		}
		
		
		
		
		
	}

	
	
	

	
	
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
				
			}
				
			else{
				reveal.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						nums[i][j].setForeground(new Color(0,0,0));
					}
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
