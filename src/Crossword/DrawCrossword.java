package Crossword;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;
// Steve: need these to remove automatic arrow key scrolling in JScrollPane
import javax.swing.InputMap;
import javax.swing.UIManager;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;


/**
 * Class to take String[][] grid and paint the crossword as it should look
 * complete with all the required components: The clues, grid, clue numbers,
 * solution button, hints etc
 */
public class DrawCrossword extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;
	private String[][] grid;
	private JTextField[][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crosswordGrid, clue, clueNums, main;
	JLayeredPane layer;
	JScrollPane area;
	JButton reveal;
	JLabel[][] clueNumbers;
	ArrayList<JLabel> cluesDwn, cluesAcr, hints;
	GridBagConstraints c;
	ArrayList<JLabel> nums;
	ArrayList<Entry> entries;
	DrawSolution sol;
	Font font, font2, font3, font4;
	Random rand;
	Border border;
	Color clear, red, green, blue, black;
	JLabel hintD, hintA;
	ArrayList<KeyEvent> keys;
	Action action;
	Dimension screenSize;
	double width;
	double height;
	
	//tracking clicks
	int lastClick_x = 0;
	int lastClick_y = 0;
	int countClicks = 0;
	
	//tracking last text entry
	int currentDirection = 0;  // {0,1,2,3} = {right, down, left, up}
	boolean firstAutoMove = true;
	
	boolean firsteverclick = true; //stupid hack to fix a bug I couldn't find
	
	
	
	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross,
			ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException {
                
		
		frameSizeX = 2 * (x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;

		JFrame frame = new JFrame("Auto Crossword");
		frame.setSize(1000, 400);
		frame.setPreferredSize(new Dimension(550, 400));
		frame.setMinimumSize(new Dimension(550,400));
		frame.setBackground(new Color(255, 255, 255, 255));

		keys = new ArrayList<KeyEvent>();

		cluesDwn = new ArrayList<JLabel>();
		cluesAcr = new ArrayList<JLabel>();
		clear = new Color(255, 255, 255, 255);
		red = new Color(255, 0, 0, 255);
		green = new Color(0, 255, 0, 255);
		blue = new Color(0, 0, 255, 255);
		black = new Color(0, 0, 0, 255);

		this.grid = grid;
		this.x = x;
		this.y = y;
		this.entries = entries;

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 15);
		font4 = new Font("Century Gothic", Font.PLAIN, 11);
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword", this);
		rand = new Random();
		

		/**
		 * This is where all the crossword boxes are filled black or provide a
		 * usable JTextfield. This is layered on top of the transparentLayer
		 */
		crosswordGrid = new JPanel(new GridLayout(x - 2, y - 2));
		crosswordGrid.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		
		crosswordGrid.setOpaque(false);  
 		
		boxes = new JTextField[x - 2][y - 2];
		border = BorderFactory.createLineBorder(Color.BLACK);
	
        

		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				
				boxes[i][j] = new JTextField(); // need new layout to resize letters in boxes
				
	            mouseActionlabel(boxes[i][j]);
				
				//trying to stop 'dinging' sound when moving cursor between boxes
				action = boxes[i][j].getActionMap().get(DefaultEditorKit.beepAction);
				action.setEnabled(false);
				//boxes[i][j].setFont(new Font("Times New Roman", Font.BOLD, 20));
				boxes[i][j].setBorder(border);
				boxes[i][j].setDocument(new JTextFieldLimit(1, true));
				if (grid[j+1][i+1] == "_") {
					boxes[i][j].setBackground(new Color(0, 0, 0, 255));
					boxes[i][j].setEnabled(false);
				} else {
					boxes[i][j].setBackground(new Color(255, 255, 255, 105));
										
					keyActionTextField(boxes[i][j]);
				}			
				
				boxes[i][j].setHorizontalAlignment(JTextField.CENTER);
				boxes[i][j].setFont(font2);
				crosswordGrid.add(boxes[i][j]);	

				
			}
		}
		
		
		
		
		
		/**
		 * This is where the transparentLayer to hold all the clue numbers is
		 * created. It sets all the cells with question numbers with the correct
		 * number in the top left corner of a GridLayout cell.
		 */
		clueNums = new JPanel(new GridLayout(x - 2, y - 2));
		clueNums.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		clueNums.setOpaque(false);  //#### originally false
		clueNumbers = new JLabel[x - 2][y - 2];

		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				clueNumbers[i][j] = new JLabel();
				clueNumbers[i][j].setBackground(new Color(255, 255, 255, 255));
				clueNumbers[i][j].setForeground(Color.BLACK);
				clueNumbers[i][j].setVisible(true);
				clueNumbers[i][j].setFont(font4);
				clueNumbers[i][j].setOpaque(false);//was false
				if (!gridInit[j + 1][i + 1].equals("_")) {
					clueNumbers[i][j].setText(gridInit[j + 1][i + 1]);
				}
				clueNumbers[i][j].setVerticalAlignment(JTextField.TOP);
				clueNums.setOpaque(false);

				
				clueNums.add(clueNumbers[i][j]);
			}
		}

		/**
		 * This is the JLayeredPane layer which holds the actual crossword. It
		 * is composed of two layers crosswordGrid and clueNums which are both
		 * GridLayout JPanels which are layered one on top of the other.
		 */
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
		
		// STEVE: SWITCHED THESE
		layer.add(clueNums, new Integer(1));
		layer.add(crosswordGrid, new Integer(0));
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
		layer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));

		/**
		 * This is the GridBagLayout clue which holds all the clue components:
		 * The numbers and clues in a JTextArea and the hints in a JLabel
		 */
		clue = new JPanel(new GridBagLayout());
		clue.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		clue.setBackground(clear);
		clue.setAlignmentY(0);
		clue.setBounds(300, 300, 200, 200);
		hints = new ArrayList<JLabel>();

		JLabel first = new JLabel("Across");
		first.setFont(font2);
		cluesAcr.add(first);
		int len = 0;
		for (String s : cluesAcross) {
			for(Entry e: entries){
				if(e.definition.equals(s)){
					len = e.wordLength;
				}
			}
			
			String clue = s + " (" + len + ")";
			JLabel across = new JLabel(clue);
			//add word length here.
			
			across.setFont(font3);
			hintA = new JLabel(" ");
			hintA.setFont(font3);
			hintA.setForeground(Color.BLUE);
			hints.add(hintA);
			mouseActionlabel(across);
			mouseActionlabel(hintA);
			cluesAcr.add(across);
			cluesAcr.add(hintA);
		}



		
		JLabel second = new JLabel("Down\n");
		second.setFont(font2);
		cluesDwn.add(second);
		for (String s : cluesDown) {
			for(Entry e: entries){
				if(e.definition.equals(s)){
					len = e.wordLength;
				}
			}
			String clue = s + " (" + len + ")";
			JLabel down = new JLabel(clue);
			down.setFont(font3);
			hintD = new JLabel(" ");
			hintD.setFont(font3);
			hintD.setForeground(Color.BLUE);
			hints.add(hintD);
			mouseActionlabel(down);
			mouseActionlabel(hintD);
			cluesDwn.add(down);
			cluesDwn.add(hintD);
		}

		for (JLabel j : cluesAcr) {
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridx = 0;
			clue.add(j, c);
		}

		for (JLabel k : cluesDwn) {
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridx = 0;
			clue.add(k, c);
		}

		/**
		 * This is the layout of the GridBagLayout panel main which holds all
		 * the crossword components. There are two components inside it: A
		 * JLayeredPane and a GridBagLayout
		 */
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);

		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);

		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		main.add(clue, c);

		/**
		 * This is the largest area of the GUI which holds the crossword and
		 * clues pane and makes them scrollable.
		 */
		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		
		// Code to remove automatic arrow key scrolling in JScrollPane. Copy and pasted from: 
		// http://stackoverflow.com/questions/11533162/how-to-prevent-jscrollpane-from-scrolling-when-arrow-keys-are-pressed
		InputMap actionMap = (InputMap) UIManager.getDefaults().get("ScrollPane.ancestorInputMap");
		actionMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), new AbstractAction(){
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    }});

		actionMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), new AbstractAction(){
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    }});
		
		/**
		 * This is the button which generates a solution for the given crossword
		 * bringing up a new GUI instance with the filled in grid on being
		 * pressed.
		 */
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.addActionListener(this);

		/**
		 * This is the panel for the main area of the program. It holds two
		 * components: A JScrollPane and a JButton
		 */
		panel = new JPanel(new GridBagLayout());

		c.weighty = 1.0;
		c.ipadx = 1;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(area, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);

		/**
		 * Overall JFrame to hold all components This has the main panel
		 * assigned to it
		 */
		
		if(squareSize*(x+2)+squareSize/2 > width && squareSize*(y+2) > height-30){
			//frame.setPreferredSize(new Dimension((int)width,(int)height));
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
			//System.out.println("GOt here");
		}
		else if(squareSize*(x+2)+squareSize/2 > width){
			frame.setPreferredSize(new Dimension((int)width,squareSize*(y+4)));
		}else if(squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension(squareSize*(2*x+2)+squareSize/2, (int)height-30));
		}else{
			frame.setPreferredSize(new Dimension(squareSize*(2*x+2)+squareSize/2,squareSize*(y+4)));
		}

		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
		
		
		
		//Highlight first word to begin
		//highlightWord_fromClick(0,0);     ////// IS THIS THE ONLY BUG???
		//firstAutoMove=false;
		
	}



	void keyActionTextField(JTextField l) {
		
		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				

				if( e.getKeyCode()==KeyEvent.VK_UP  || e.getKeyCode()==KeyEvent.VK_DOWN ||
						e.getKeyCode()==KeyEvent.VK_RIGHT || e.getKeyCode()==KeyEvent.VK_LEFT ||
						e.getKeyCode() == KeyEvent.VK_BACK_SPACE  ){
					firstAutoMove = true; //reset for auto-cursor movements
					countClicks=0;
					firsteverclick=false;
				}
				if(firsteverclick){ // Stupid hack to fix the bug I couldn't find
					firstAutoMove = true;
					makeAllWhite();
					highlightWord_fromClick(lastClick_x,lastClick_y);
					firsteverclick=false;
				}

					
						
				
				
				for (int row = 0; row < x - 2; row++) {
					for (int col = 0; col < y - 2; col++) {	
						
						
						if (e.getSource() == boxes[row][col]) {
						

							
							if (e.getKeyCode() == KeyEvent.VK_UP) {			
								// get rid of all previous highlighting
								makeAllWhite();
							    // STEVE: jump black squares and implement periodic boundary conditions			   
								int newstart=row;
								for( int i=1; i<(x-2)*2; i++){										
									//Periodic BCs
									if( newstart-i < 0 ){
										i=1;  newstart=x-2;
									}
									// Jump black spaces to nearest white one
									if (boxes[ (newstart-i) ][col].isEnabled()) {
										boxes[ (newstart-i) ][col].requestFocus();
										// highlight any words that *start* from this square
										highlightWord( newstart-i, col);
										break;
									}
								}												
							}
							if (e.getKeyCode() == KeyEvent.VK_DOWN) {						
								makeAllWhite();
								int newstart=row;
								for( int i=1; i<x-2; i++ ){	
									if( newstart+i > x-3 ){
										i=1;  newstart=-1;
									}
									if (boxes[newstart+i][col].isEnabled()) {
										boxes[newstart+i][col].requestFocus();
										highlightWord(newstart+i,col);
										break;
									}						
								}																							
							}
							if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
								makeAllWhite();
								int newstart=col;
								for( int i=1; i<y-2; i++){
									if( newstart+i>y-3 ){
										i=1;  newstart=-1;
									}									
									if (boxes[row][newstart + i].isEnabled()) {
										boxes[row][newstart + i].requestFocus();
										highlightWord(row, newstart+i);
										break;
									}
								}								
							}
							if (e.getKeyCode() == KeyEvent.VK_LEFT) {
								makeAllWhite();
								int newstart=col;
								for(int i=1; i<y-2; i++){
									if( newstart-i<0 ){
										i=1;  newstart=y-2;
									}
									if (boxes[row][newstart - i].isEnabled()) {
										boxes[row][newstart - i].requestFocus();
										highlightWord(row,newstart-i);
										break;
									}
								}															
							}
							
							
							
							
							
							if (65 <= e.getKeyCode() && e.getKeyCode() <= 90) {
								
								countClicks=0;
								
								boxes[row][col].setForeground(black);
								boxes[row][col].setText(Character.toString(e.getKeyChar()));
								
								
								//determine initial direction, favouring across
								if( col<x-3 && boxes[row][col+1].isEnabled() && firstAutoMove ){    //NOTE: ANDY HAS FLIPPED X AND Y COORDS
									currentDirection = 0;
									firstAutoMove=false;
								}
								else if( row<y-3 && boxes[row+1][col].isEnabled() && firstAutoMove ){
									currentDirection = 1;
									firstAutoMove=false;
								}

								
					
								// highlighted squares take priority; i.e. can choose to go down if we want
								if( col<x-3 && boxes[row][col+1].isEnabled() && boxes[row][col+1].getBackground().getRGB() != -1 ){ //CARE: !=-1 OK??
									currentDirection = 0;
									firstAutoMove=false;
								}
								else if( row<y-3 && boxes[row+1][col].isEnabled() && boxes[row+1][col].getBackground().getRGB() != -1   ){
									currentDirection = 1;
									firstAutoMove=false;									
								}//////////////////////
								
								
								
								if(currentDirection == 0 ){ //across
									if( col<x-3 && boxes[row][col+1].isEnabled()  ){
										boxes[row][col+1].requestFocus();		
									}
									else{
										//check this square is start of a down word
										boolean isStart = startOfWord(row, col);
										if( isStart ){
											if( row<y-3 && boxes[row+1][col].isEnabled() ){ // this HAS to be true
												boxes[row+1][col].requestFocus();
												currentDirection = 1;  //i.e. going down
												//also make new highlight
												makeAllWhite();
												highlightWord( row, col);												
											}
										}
									}
								}
								else if(currentDirection ==1 ){ //going down		
									if( row<y-3 && boxes[row+1][col].isEnabled()  ){
										boxes[row+1][col].requestFocus();
									
									}
									else{
										//check this square is start of a down word
										boolean isStart = startOfWord(row, col);
										if( isStart ){
											if( col<x-3 && boxes[row][col+1].isEnabled() ){ // this HAS to be true
												boxes[row][col+1].requestFocus();
												currentDirection = 0;  //i.e. going across
												//also make new highlight
												makeAllWhite();
												highlightWord( row, col);	
											}
										}
									}									
								}
																
							}
							
												

							if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
								// STEVE: Changed this to only delete the highlighted word
								
								boxes[row][col].setText("");
								
								if(col>0 && boxes[row][col-1].isEnabled() && boxes[row][col-1].getBackground().getRGB() != -1){
									boxes[row][col - 1].requestFocus();
								}
								else if(row>0 && boxes[row-1][col].isEnabled() && boxes[row-1][col].getBackground().getRGB() != -1){
									boxes[row-1][col].requestFocus();
								}
								
					
							}
						}
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.equals(KeyEvent.VK_UP)) {

				}
				if (e.equals(KeyEvent.VK_DOWN)) {

				}
				if (e.equals(KeyEvent.VK_RIGHT)) {

				}
				if (e.equals(KeyEvent.VK_LEFT)) {

				}
			}

			public void keyTyped(KeyEvent e) {

			}
		});
	}
	
	
	// MOUSE ACTIONS on JTextArea (grid spaces) here ---------------------
	void mouseActionlabel(JTextField l) {
			
		l.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {	
			
				firstAutoMove = true; //reset for auto-cursor movements

				
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(boxes[i][j])){
							makeAllWhite();
							/////
							lastClick_x=i; lastClick_y=j;
						//////////////////////	
						
							highlightWord_fromClick(i,j);
						}
						for (JLabel lb : hints) {
							lb.setText(" ");
						}
					}
				}			
			}
			
			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}
			
			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {

			}
		});
	}
	
	
	
	

	
	// MOUSE ACTIONS on JLabel (hints) here ---------------------
	void mouseActionlabel(JLabel l) {
		
		
		l.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {				
				
				for (JLabel k : hints) {
					k.setText(" ");				//can remove this line if we want to keep showing all hints
					if (e.getSource() == k) {
						for (Entry ent : entries) {
							if (ent.isAcross()) {
								if (ent.getEntryAcross() == hints.indexOf(k)) {
									k.setText("      " + ent.getShuffledWord().toUpperCase());
									//String str = shuffleString(ent.word).toUpperCase();
									//k.setText("      " + shuffleString(ent.word).toUpperCase());
								}
							} else{
								if (ent.getEntryDown() == hints.indexOf(k) - (cluesAcr.size() / 2)) {
									k.setText("      " + ent.getShuffledWord().toUpperCase());
								}
							}
						}
					}
				}			
				
				
			}

					/////////////////CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
			public void mouseEntered(MouseEvent e) {
				for (JLabel i : hints) {
					if (e.getSource() == i) {
						if(i.getText().equals(" ")){
							i.setText("      HINT");
						}
					
//						for (Entry ent : entries) {
//							if (ent.isAcross()) {
//								if (ent.getEntryAcross() == hints.indexOf(i)) {
//									i.setText("      " + shuffleString(ent.word).toUpperCase());
//								}
//							} 
//						}
						//boxes[3][3].setBackground(Color.YELLOW);
					}
				}
//				for (JLabel j : cluesAcr) {
//					if (e.getSource() == j) {
//						if (!hints.contains(j)) {
//							cluesAcr.get(cluesAcr.indexOf(j) + 1).setText("      HINT");
//						}
//					}
//				}
//				for (JLabel k : cluesDwn) {
//					if (e.getSource() == k) {
//						if (!hints.contains(k)) {
//							cluesDwn.get(cluesDwn.indexOf(k) + 1).setText("      HINT");
//						}
//					}
//				}
			}

			public void mouseExited(MouseEvent e) {

				for (JLabel i : hints) {
					if (e.getSource() == i) {
						if(i.getText().equals("      HINT")){
							i.setText(" ");
						}
						
						//boxes[3][3].setBackground(Color.WHITE);
					}
				}
//				for (JLabel j : cluesAcr) {
//					if (e.getSource() == j) {
//						if (!hints.contains(j)) {
//							cluesAcr.get(cluesAcr.indexOf(j) + 1).setText(" ");
//						}
//					}
//				}
//				for (JLabel k : cluesDwn) {
//					if (e.getSource() == k) {
//						if (!hints.contains(k)) {
//							cluesDwn.get(cluesDwn.indexOf(k) + 1).setText(" ");
//						}
//					}
//				}
			}
			
			
			
			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {

			}
		});
	}
	
	
	
	
	// Highlight squares if on start of word
	public void highlightWord__OLD( int xstart, int ystart ){
		
		if( !clueNumbers[xstart][ystart].getText().equals("") ){
			
			for( Entry ent : entries ){
				String nomnom = Integer.toString( ent.getClueNumber()  );
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){
					
					if( ent.isAcross()  ) {
						int length = ent.getWord().length();
						for( int dbl=0; dbl<length; dbl++ ){
							boxes[xstart][ystart+dbl].setBackground(new Color(20,100,20,100) );
						}													
					} 
					else {
						int length = ent.getWord().length();
						for( int dbl=0; dbl<length; dbl++ ){
							boxes[xstart+dbl][ystart].setBackground(new Color(20,100,20,100) );
						}														
					}					
				}
			}
		}		
	}
	
	

	
	
	
	// Highlight squares of word from any position
	public void highlightWord( int xstart, int ystart ){
		
		
		if( !clueNumbers[xstart][ystart].getText().equals("") ){
			// i.e., if start of word
			
			boolean acrossExists=false; int across_l=0; 
			boolean downExists=false;   int down_l=0;
			
			for( Entry ent : entries ){
			// go through entries and check if we have across/down/both	
				String nomnom = Integer.toString( ent.getClueNumber()  );
				
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){			
					if( ent.isAcross()  ) {
						across_l = ent.getWord().length();
						acrossExists = true;
					}
					else{
						down_l = ent.getWord().length();
						downExists=true;
					}			
				}
			}
			// prioritise across over down
			if( acrossExists ){
				for( int dbl=0; dbl<across_l; dbl++ ){
					boxes[xstart][ystart+dbl].setBackground(new Color(20,100,20,100) );
				}						
			}
			else if( downExists ){
				for( int dbl=0; dbl<down_l; dbl++ ){
					boxes[xstart+dbl][ystart].setBackground(new Color(20,100,20,100) );
				}	
			}
			
		}

		//
		//
		// edit: allow word to be highlighted from any position, not just first letter
		else{
			int xx1=0; int yy1=0;
			if( ystart>0 && boxes[xstart][ystart-1].isEnabled() && ystart<y-3 &&  boxes[xstart][ystart+1].isEnabled() ){// EDITED 1546
				//find start point and highlight from there
				for( int mm=0; mm<y-2; mm++ ){
					if( !clueNumbers[xstart][ystart-mm].getText().equals("")  ){
						xx1=xstart;  yy1=ystart-mm;
						////// replace below with recursive method
						for( Entry ent : entries ){
							String nomnom = Integer.toString( ent.getClueNumber()  );
							if( clueNumbers[xx1][yy1].getText().equals( nomnom ) ){
								
								if( ent.isAcross()  ) {
									int length = ent.getWord().length();
									for( int dbl=0; dbl<length; dbl++ ){
										boxes[xx1][yy1+dbl].setBackground(new Color(20,100,20,100) );
									}	
									mm=y; //break
								} 
			
							}
						}////////replace this with recursive method
						
					}
				}
				
			}
			else if( xstart>0 && boxes[xstart-1][ystart].isEnabled()  ){    // WORKS. DOWN
			//else if( xstart>0 && boxes[xstart-1][ystart].isEnabled()  ||   
		///	/	( xstart>0 && boxes[xstart-1][ystart].isEnabled() &&   !boxes[xstart][ystart+1].isEnabled()   )	){   
	
			
			
				//find start point and highlight from there
				for( int mm=0; mm<x-2; mm++ ){
					if( !clueNumbers[xstart-mm][ystart].getText().equals("")  ){
						xx1=xstart-mm;  yy1=ystart;
						////// replace below with recursive method
						for( Entry ent : entries ){
							String nomnom = Integer.toString( ent.getClueNumber()  );
							if( clueNumbers[xx1][yy1].getText().equals( nomnom ) ){
								
								if( !ent.isAcross()  ) {
									int length = ent.getWord().length();
									for( int dbl=0; dbl<length; dbl++ ){
										boxes[xx1+dbl][yy1].setBackground(new Color(20,100,20,100) );
									}	
									mm=x; //break; 
								} 
								
							}
						}////////replace this with recursive method
						
					}
				}		
				
			
			}

			else{
				// in coord (0,0) so do nothing
			}
			
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	
	// Highlight across/down depending on number of clicks
	public void highlightWord_fromClick( int xstart, int ystart ){		
		
		
		// if clicking on start of a word
		if( !clueNumbers[xstart][ystart].getText().equals("") ){
		
			boolean acrossExists = false;
			boolean downExists = false;			
			int acrossLength=0;   
			int downLength = 0;
			
			for( Entry ent : entries){
				String nomnom = Integer.toString( ent.getClueNumber()  );
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){
					if( ent.isAcross()  ) {
						acrossExists = true;
						acrossLength = ent.getWord().length();
					} 
					else {
						downExists = true;
						downLength = ent.getWord().length();
					}
				}
			}
			
			
			countClicks++;		
			boolean highlightAcross = true;
			if( xstart==lastClick_x && ystart==lastClick_y && countClicks%2!=0 && acrossExists){
				highlightAcross = true;				
			}
			else if( xstart==lastClick_x && ystart==lastClick_y && countClicks%2==0 && downExists ){
				highlightAcross = false;
			}
			else if( !acrossExists && downExists ){
				highlightAcross =  false;	
				countClicks = 0;
			}
			else{
				lastClick_x = xstart;    lastClick_y = ystart;
				countClicks = 1;
			}
			
			
			for( Entry ent : entries ){
				String nomnom = Integer.toString( ent.getClueNumber()  );
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){
					
					if( highlightAcross  ) {
						for( int dbl=0; dbl<acrossLength; dbl++ ){
							boxes[xstart][ystart+dbl].setBackground(new Color(20,100,20,100) );
						}													
					} 
					else {
						for( int dbl=0; dbl<downLength; dbl++ ){
							boxes[xstart+dbl][ystart].setBackground(new Color(20,100,20,100) );
						}														
					}
				}
			}
		}
		//
		// or if clicking on random part of word
		else{
			highlightWord(xstart,ystart);
			countClicks=0;
		}
		
	}
	
	
	
	
	
	public void makeAllWhite(){
		//Reset all white 
		for (int rrrr = 0; rrrr < x - 2; rrrr++) {
			for (int cccc = 0; cccc < y - 2; cccc++) {
				if( boxes[rrrr][cccc].isEnabled() ){
					boxes[rrrr][cccc].setBackground(new Color(255,255,255,255) );
				}	
			}
		}	
	}

	
	
	
	public boolean startOfWord(int x, int y){
		//Determine if a square is the first letter of a word
		boolean isStart = false;
		for( Entry ent : entries){
			String nomnom = Integer.toString( ent.getClueNumber()  );
			if( clueNumbers[x][y].getText().equals( nomnom ) ){
				isStart = true;
			}
		}
		return isStart;
	}
	
	
	public void revealSolution(){
		reveal.setText("Hide Solution");
		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				if (boxes[i][j].getText().equals("")) {
					boxes[i][j].setForeground(black);
				} else if (boxes[i][j].getText().toLowerCase().equals(grid[j + 1][i + 1])) {
					boxes[i][j].setForeground(green);
				} else {
					boxes[i][j].setForeground(red);
				}
			}
		}
	}

	public void hideSolution(){
		reveal.setText("Show Solution");
		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				boxes[i][j].setForeground(new Color(0, 0, 0, 255));
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == reveal) {
			sol.frame.setVisible(!sol.frame.isVisible());
			if (sol.frame.isVisible()) {
				revealSolution();
			} else {
				hideSolution();
			}
		}
	}
}
