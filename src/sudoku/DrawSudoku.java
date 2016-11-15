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
	private static int squareSize = 50;	
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
	JButton solution, hint, clue;
	DrawSudokuSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Integer> row, square, tempColumn, checks;
	ArrayList<ArrayList<Integer>> cols;
	ArrayList<ArrayList<Integer>> boxes;
	Font font, font2;
	Random rand;
	boolean solutionPushed, hintPushed, cluePushed;
	int difficulty;
	
	//@SuppressWarnings("unchecked")
	public DrawSudoku(int[][] grid, int x, int y, int difficulty) throws IOException{
		
		this.x = x;
		this.y = y;
		font = new Font("Century Gothic", Font.PLAIN, 30);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		
		this.difficulty = difficulty;
		sol = new DrawSudokuSolution( x, y);  // Create the whole object that will be revealed on "show solution"
		sol.generateSudoku(grid);             // Andy's algorithm to generate sudoku solution.
		sol.setVisible(false);
		
		
		// WE NOW HAVE GRID[][], THE BASE OF OUR SUDOKU. MODIFY THIS TO STARTING CONFIGURATION.
		
		//System.out.println( grid[0][0] + " " + grid[1][1] + " "+ grid[0][1]);   // CAREFUL: ANDY HAS FLIPPED [X][Y] TO GRID[Y][X]
		
		
		
		
		
		
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
		solutionPushed = false;
		
		solution = new JButton("Solution");
		solution.setFont(font2);
		//solution.setEnabled(true);
		solution.addActionListener(this);
		
		hint = new JButton("Hint");
		hint.setFont(font2);
		hint.addActionListener(this);
		
		
		clue = new JButton("Clue");
		clue.setFont(font2);
		clue.addActionListener(this);
		
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
		c.gridwidth = 3;
		panel.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.gridwidth = 1;
		panel.add(hint, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(clue, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(solution, c);
		
		frame = new JFrame("Auto Sudoku");
		frame.setPreferredSize(new Dimension(squareSize*(x)+squareSize/2,squareSize*(y+2)+10));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		//frame.setMinimumSize(new Dimension(550,400));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(solution);
		//generateSudoku();
		
		
		
		
		
		
		// Modify grid[][] to starting configuration
		int[][] initial_config = new int[9][9];
		
		if(difficulty == 0){
			initial_config = SudokuMethods.makeEasy( grid );	
		}
		else if(difficulty == 1){	
			initial_config = SudokuMethods.makeMedium( grid );  // Might be ridiculously hard...
		}	
		
		
		// Put starting config into nums[][] JTextFields
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if( initial_config[i][j] != 0 ){                      // TODO: CHANGE THIS 0 THING
					nums[i][j].setText( Integer.toString( initial_config[i][j])  );
					nums[i][j].setEditable(false);
					//nums[i][j].setForeground(new Color( 163 , 194,  163));
					nums[i][j].setForeground(Color.BLACK);
					nums[i][j].setHighlighter(null);
					nums[i][j].setBackground(Color.WHITE);
				}
			}
		}
		

		
		
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
		if(e.getSource()==solution){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				solution.setText("Hide Solution");
				
				//sol.setVisible(true);
				//sol.frame.setVisible(true);
//				for (int i = 0; i < x-2; i++){
//					for (int j = 0; j < y-2; j++){
//						if(numbers[i][j].getText().equals("")){
//							numbers[i][j].setForeground(new Color(0,0,0,255));
//						}
//						else if(numbers[i][j].getText().toLowerCase().equals(grid[j+1][i+1])){
//							numbers[i][j].setForeground(new Color(0,255,0,255));
//						}else{
//							numbers[i][j].setForeground(new Color(255,0,0,255));
//						}
					//}
			//	}
			}
				
			else{
				solution.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						nums[i][j].setForeground(new Color(0,0,0));
					}
				}
			}
		}
		if(e.getSource()==hint){
			System.out.println("hint");
			int a = rand.nextInt(9);
			int b = rand.nextInt(9);
			hintPushed = !hintPushed;
//			if(hintPushed){
//				nums[a][b].setBackground(Color.CYAN);
//			}
//			else{
//				nums[a][b].setBackground(Color.YELLOW);
//			}
		}
		if(e.getSource()==clue){
			System.out.println("clue");
			cluePushed = !cluePushed;
//			if(cluePushed){
//				int a = rand.nextInt(9);
//				int b = rand.nextInt(9);
//				int c = rand.nextInt(9);
//				nums[a][b].setText(""+c);
//			}
		}
	}
}
