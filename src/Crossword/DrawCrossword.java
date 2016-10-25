package crossword;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public class DrawCrossword extends JComponent implements ActionListener, AWTEventListener, MouseWheelListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize;
	private String[][] grid;
	private JTextField[][] boxes;
	public JTextField[][] tempBoxes;
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
	
	// Define color highlighting current word
	Color HIGHLIGHT_COLOUR = new Color(20,100,20,100) ;
	
	
	//tracking clicks
	int lastClick_x = 0;
	int lastClick_y = 0;
	int countClicks = 0;
	
	//tracking last text entry
	int currentDirection = 0;  // {0,1,2,3} = {right, down, left, up}
	boolean firstAutoMove = true;
	
	boolean firsteverclick = true; //stupid hack to fix a bug I couldn't find
	final static int initialSquareSize = 60;
	private double normalisedScale;
	private double scale;
	final double MAX_SCALE;
	private final static String SOME_ACTION = "control 1";
	final double MIN_SCALE;
	private double mouseX;
	private double mouseY;
	String[][] gridInit;
	
	
	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross,
			ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException {
                
		
		squareSize = 30;
		MIN_SCALE = 3.0;
		MAX_SCALE = 20.0;
		scale = 10.0;
		normalisedScale = scale/20;
		this.gridInit = gridInit;
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
		
		font = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale*squareSize / 5 * 3));
		font2 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 24));
		font3 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 15));
		font4 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 11));
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword", this);
		rand = new Random();
		
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
		Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
		double mouseX = mouseCoord.getX();
		double mouseY = mouseCoord.getY();
		
		
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
//		
//		/**
//		 * This is where all the crossword boxes are filled black or provide a
//		 * usable JTextfield. This is layered on top of the transparentLayer
//		 */
//		crosswordGrid = new JPanel(new GridLayout(x - 2, y - 2));
//		crosswordGrid.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
//		
//		crosswordGrid.setOpaque(false);  
// 		
//		boxes = new JTextField[x - 2][y - 2];
//		border = BorderFactory.createLineBorder(Color.BLACK);
//	
//        
//
//		for (int i = 0; i < x - 2; i++) {
//			for (int j = 0; j < y - 2; j++) {
//				
//				boxes[i][j] = new JTextField(); // need new layout to resize letters in boxes
//				
//	            mouseActionlabel(boxes[i][j]);
//				
//				//trying to stop 'dinging' sound when moving cursor between boxes
//				action = boxes[i][j].getActionMap().get(DefaultEditorKit.beepAction);
//				action.setEnabled(false);
//				//boxes[i][j].setFont(new Font("Times New Roman", Font.BOLD, 20));
//				boxes[i][j].setBorder(border);
//				boxes[i][j].setDocument(new JTextFieldLimit(1, true));
//				if (grid[j+1][i+1] == "_") {
//					boxes[i][j].setBackground(new Color(0, 0, 0, 255));
//					boxes[i][j].setEnabled(false);
//				} else {
//					boxes[i][j].setBackground(new Color(255, 255, 255, 105));
//										
//					keyActionTextField(boxes[i][j]);
//				}			
//				
//				boxes[i][j].setHorizontalAlignment(JTextField.CENTER);
//				boxes[i][j].setFont(font2);
//				crosswordGrid.add(boxes[i][j]);	
//
//				
//			}
//		}
//		
//		
//		
//		
//		
//		/**
//		 * This is where the transparentLayer to hold all the clue numbers is
//		 * created. It sets all the cells with question numbers with the correct
//		 * number in the top left corner of a GridLayout cell.
//		 */
//		clueNums = new JPanel(new GridLayout(x - 2, y - 2));
//		clueNums.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
//		clueNums.setOpaque(false);  //#### originally false
//		clueNumbers = new JLabel[x - 2][y - 2];
//
//		for (int i = 0; i < x - 2; i++) {
//			for (int j = 0; j < y - 2; j++) {
//				clueNumbers[i][j] = new JLabel();
//				clueNumbers[i][j].setBackground(new Color(255, 255, 255, 255));
//				clueNumbers[i][j].setForeground(Color.BLACK);
//				clueNumbers[i][j].setVisible(true);
//				clueNumbers[i][j].setFont(font4);
//				clueNumbers[i][j].setOpaque(false);//was false
//				if (!gridInit[j + 1][i + 1].equals("_")) {
//					clueNumbers[i][j].setText(gridInit[j + 1][i + 1]);
//				}
//				clueNumbers[i][j].setVerticalAlignment(JTextField.TOP);
//				clueNums.setOpaque(false);
//
//				
//				clueNums.add(clueNumbers[i][j]);
//			}
//		}

		tempBoxes = new JTextField[x-2][y-2];
		
		for(int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				tempBoxes[i][j] = new JTextField(); 
			}
		}
		
		
		drawGrid(normalisedScale);
		
		/**
		 * This is the JLayeredPane layer which holds the actual crossword. It
		 * is composed of two layers crosswordGrid and clueNums which are both
		 * GridLayout JPanels which are layered one on top of the other.
		 */
//		layer = new JLayeredPane();
//		layer.setBackground(new Color(255, 255, 255, 255));
//		
//		// STEVE: SWITCHED THESE
//		layer.add(clueNums, new Integer(1));
//		layer.add(crosswordGrid, new Integer(0));
//		
//		layer.setVisible(true);
//		layer.setOpaque(true);
//		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
//		layer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
//		layer.addMouseWheelListener(this);
		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		layer.getActionMap().put(SOME_ACTION, someAction);
		layer.addMouseWheelListener(this);
	
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
			//cluesAcr.add(hintA);  //STEVE REMOVE
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
			//cluesDwn.add(hintD);  // STEVE REMOVE
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
		reveal.addMouseWheelListener(this);
//		reveal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
//		reveal.getActionMap().put(SOME_ACTION, someAction);
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
		
		
	}

	
	 Action someAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	                System.out.println("do some action");
	            }
	        };
	        

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
							
							
							
							
							// If key presses are letters:
							if (65 <= e.getKeyCode() && e.getKeyCode() <= 90) {
								
								countClicks=0;
								
								boxes[row][col].setForeground(black);
								boxes[row][col].setText(Character.toString(e.getKeyChar()));
								
								
								//determine initial direction, favouring across
								if( col<x-3 && boxes[row][col+1].isEnabled() && firstAutoMove ){    //NOTE: ANDY HAS FLIPPED X AND Y COORDS IN boxes[][]...
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
	
	
	// MOUSE ACTIONS on JTextField (grid spaces) here ---------------------
	void mouseActionlabel(JTextField l) {
			
		l.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {	
			
				firstAutoMove = true; //reset for auto-cursor movements

				
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(boxes[i][j])){
							makeAllWhite();
							highlightWord_fromClick(i,j);
						}
						//for (JLabel lb : hints) {
						//	lb.setText(" ");
						//}
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
				
				firsteverclick = false;
				countClicks=0;
				
				
//				// GET FOCUS IF CLICKING ON CLUE
//				//(no longer necessary - but reinstate for mobile phones)  -- STEVE
//				firsteverclick = false;
//				countClicks=0;
//
//				
//				for( JLabel cl : cluesAcr ){
//					if( e.getSource()==cl ){
//						makeAllWhite();
//						String c_n_string = ""+cl.getText().split("\\.")[0];
//						int c_n = Integer.parseInt( c_n_string   );
//						for( Entry ent : entries ){
//							if( ent.getClueNumber() == c_n && ent.isAcross()   ){
//								// then highlight this word
//								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
//								colourWord( ent.getY()-1, ent.getX()-1, "across");
//							}
//						}
//						
//					}
//			
//				}
//				for( JLabel cl : cluesDwn ){
//					if( e.getSource()==cl ){
//						makeAllWhite();
//						String c_n_string = ""+cl.getText().split("\\.")[0];
//						int c_n = Integer.parseInt( c_n_string   );
//						for( Entry ent : entries ){
//							if( ent.getClueNumber() == c_n && !ent.isAcross()   ){
//								// then highlight this word
//								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
//								colourWord( ent.getY()-1, ent.getX()-1, "down");
//							}
//						}
//						
//					}
//					
//				}
				
				
				
				
				
//				for (JLabel k : hints) {
//					k.setText(" ");				//can remove this line if we want to keep showing all hints
//					if (e.getSource() == k) {
//						for (Entry ent : entries ) {
//							if (ent.isAcross()) {
//								if (ent.getEntryAcross() == hints.indexOf(k)   ) {
//									k.setText("      " + ent.getShuffledWord().toUpperCase());
//									//String str = shuffleString(ent.word).toUpperCase();
//									//k.setText("      " + shuffleString(ent.word).toUpperCase());
//								}
//							} else{
//								if (ent.getEntryDown() == hints.indexOf(k) - (cluesAcr.size() / 2)  ) {
//									k.setText("      " + ent.getShuffledWord().toUpperCase());
//								}
//							}
//						}
//					}
//				}
				

				
				
				
				
			}

					/////////////////CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
			public void mouseEntered(MouseEvent e) {
//				for (JLabel i : hints) {
//					if (e.getSource() == i) {
//						if(i.getText().equals(" ")){
//							i.setText("      HINT");
//						}
//					}
//				}    STEVE REMOVE
				
				firsteverclick = false;
				
				for( JLabel cl : cluesAcr ){
					if( e.getSource()==cl ){
						cl.setBackground(HIGHLIGHT_COLOUR);
						cl.setOpaque(true);	
						
						makeAllWhite();
						//cl.setBackground(HIGHLIGHT_COLOUR);
						//cl.setOpaque(true);
						String c_n_string = ""+cl.getText().split("\\.")[0];
						int c_n = Integer.parseInt( c_n_string   );
						//System.out.println(c_n);
						for( Entry ent : entries ){
							if( ent.getClueNumber() == c_n && ent.isAcross()   ){
								// then highlight this word
								//System.out.println( "x="+ent.getX()+ " y="+ent.getY());
								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
								colourWord( ent.getY()-1, ent.getX()-1, "across");   // X AND Y COORDS ARE FUCKED UP. FIX THIS. BUG. TODO!!
							}
						}
						
						
					}
			
				}
				for( JLabel cl : cluesDwn ){
					if( e.getSource()==cl ){
						cl.setBackground(HIGHLIGHT_COLOUR);
						cl.setOpaque(true);	
						
						makeAllWhite();
						String c_n_string = ""+cl.getText().split("\\.")[0];
						int c_n = Integer.parseInt( c_n_string   );
						for( Entry ent : entries ){
							if( ent.getClueNumber() == c_n && !ent.isAcross()   ){
								// then highlight this word
								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
								colourWord( ent.getY()-1, ent.getX()-1, "down");
							}
						}
						
						
						
					}					
				}
				
				
				

				
				
			}

			public void mouseExited(MouseEvent e) {

//				for (JLabel i : hints) {
//					if (e.getSource() == i) {
//						if(i.getText().equals("      HINT")){
//							i.setText(" ");
//						}
//						
//						//boxes[3][3].setBackground(Color.WHITE);
//					}
//				}   // STEVE REMOVE

				for( JLabel cl : cluesAcr ){
					if( e.getSource()==cl ){
						cl.setBackground(Color.WHITE);
						cl.setOpaque(true);						
					}
			
				}
				for( JLabel cl : cluesDwn ){
					cl.setBackground(Color.WHITE);
					cl.setOpaque(true);							
				}
				
				
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
							boxes[xstart][ystart+dbl].setBackground(HIGHLIGHT_COLOUR );
						}													
					} 
					else {
						int length = ent.getWord().length();
						for( int dbl=0; dbl<length; dbl++ ){
							boxes[xstart+dbl][ystart].setBackground(HIGHLIGHT_COLOUR );
						}														
					}					
				}
			}
		}		
	}
	
	

	
	
	
	public void highlightWord( int xstart, int ystart ){
		/** Highlight word from any letter **/
		
		if( !clueNumbers[xstart][ystart].getText().equals("") ){
			// i.e., if start of word
			
			boolean acrossExists=false; 
			boolean downExists=false;   
			
			for( Entry ent : entries ){
			// go through entries and check if we have across/down/both	
				String nomnom = Integer.toString( ent.getClueNumber()  );
				
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){			
					if( ent.isAcross()  ) {
						acrossExists = true;
					}
					else{
						downExists=true;
					}			
				}
			}
			// prioritise across over down
			if( acrossExists ){
				colourWord( xstart, ystart, "across" );
			}
			else if( downExists ){
				colourWord( xstart, ystart, "down" );
			}
			
		}
		//
		//
		// edit: allow word to be highlighted from any position, not just first letter
		else{
								
			if( ystart>0 && boxes[xstart][ystart-1].isEnabled() && ystart<y-3 &&  boxes[xstart][ystart+1].isEnabled() ){//across
				
				colourWord(xstart, ystart, "across");		
			}
			else if( xstart>0 && boxes[xstart-1][ystart].isEnabled()  ){//down
	
				colourWord(xstart, ystart, "down");
			}
			// Across words were not highlighting from final position if cell was isolated. QUICK FIX:
			else if( (ystart<y-3 &&  !boxes[xstart][ystart+1].isEnabled()  && xstart<x-3 &&  !boxes[xstart+1][ystart].isEnabled() && 	ystart>0 && boxes[xstart][ystart-1].isEnabled() )
					||   ( ystart==y-3 && xstart<x-3 &&  !boxes[xstart+1][ystart].isEnabled() 
					||   ( xstart==x-3 && ystart<y-3 &&  ystart>0 && boxes[xstart][ystart-1].isEnabled() )
					||   ( ystart==y-3 && xstart==x-3 )    )      
					){   
					
				colourWord(xstart, ystart, "across");		
			}

			
		}
		
		
	}
	
	
	
	
	
	public void colourWord(int xstart, int ystart, String direc){
		/** Colour word given one grid point in it  **/
		
		int xx1=xstart;  int yy1=ystart;
		
		if(direc.equals("across")){		
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
									boxes[xx1][yy1+dbl].setBackground( HIGHLIGHT_COLOUR );
							}	
								mm=y; //break
							} 
						}
					}				
				}
			}
			
		}
		else if(direc.equals("down")){
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
									boxes[xx1+dbl][yy1].setBackground( HIGHLIGHT_COLOUR );
								}	
								mm=x; //break; 
							} 
							
						}
					}
					
				}
			}				
		}
		else{ System.out.println("direction not defined in colourWord()" );  }
	
	}
	
	
	
	
	
	
	
	
	public void highlightWord_fromClick( int xstart, int ystart ){		
		/** Highlight from mouse clicks. Across/down depends on number of clicks **/
		
		// if clicking on start of a word
		if( !clueNumbers[xstart][ystart].getText().equals("") ){
		
			boolean acrossExists = false;
			boolean downExists = false;			
			
			for( Entry ent : entries){
				String nomnom = Integer.toString( ent.getClueNumber()  );
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){
					if( ent.isAcross()  ) {
						acrossExists = true;
					} 
					else {
						downExists = true;
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
			else if( acrossExists  ){
				highlightAcross = true;	
				countClicks = 1;
				lastClick_x = xstart;    lastClick_y = ystart;
			}			
			else if( !acrossExists && downExists ){
				highlightAcross =  false;	
				countClicks = 0;
				lastClick_x = xstart;    lastClick_y = ystart;
			}		
			
			for( Entry ent : entries ){
				String nomnom = Integer.toString( ent.getClueNumber()  );
				if( clueNumbers[xstart][ystart].getText().equals( nomnom ) ){
					
					if( highlightAcross  ) {	
						colourWord(xstart, ystart, "across");
					} 
					else {														
						colourWord(xstart, ystart, "down");
					}
				}
			}
		}
		
		
		// else use highlightWord() method if clicking anywhere else in word other than first letter
		else{
			highlightWord(xstart,ystart);
			countClicks=0;
		}
	
	}
	
	
	
	
	
	public void makeAllWhite(){
		/** Reset entire grid to white **/ 
		for (int rrrr = 0; rrrr < x - 2; rrrr++) {
			for (int cccc = 0; cccc < y - 2; cccc++) {
				if( boxes[rrrr][cccc].isEnabled() ){
					boxes[rrrr][cccc].setBackground(new Color(255,255,255,255) );
				}	
			}
		}	
	}

	
	
	
	public boolean startOfWord(int x, int y){
		/**Determine if a square is the first letter of a word**/
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
	
	 public void mouseWheelMoved(MouseWheelEvent e) {
		 	
		 	Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
			mouseX = mouseCoord.getX();
			mouseY = mouseCoord.getY();
	        if (e.isControlDown()) {
	            if (e.getWheelRotation() < 0) {
	                if(scale < MAX_SCALE){
	                	scale++;
	                }
	                for(int i = 0; i < x-2; i++){
	                	for (int j = 0; j < y-2; j++){
	                		tempBoxes[i][j].setText(boxes[i][j].getText());
	                	}
	                }
	                normalisedScale = scale/20;
	    		 	squareSize = (int) (normalisedScale*initialSquareSize);
	    			font = new Font("Century Gothic", Font.PLAIN, (int) (normalisedScale*initialSquareSize / 5 * 3));
	    			font2 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 24));
	    			font3 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 15));
	    			font4 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 11));
	    			
	    		    main.revalidate();
	    		    System.out.println("Got here \\n\n\n");
	    		    drawGrid( normalisedScale);
	                System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);	                    action.actionPerformed( null );
	            } else {
	                System.out.println("scrolled down");
	                if(scale > MIN_SCALE){
	                	scale--;
	                }
	                for(int i = 0; i < x-2; i++){
	                	for (int j = 0; j < y-2; j++){
	                		tempBoxes[i][j].setText(boxes[i][j].getText());
	                	}
	                }
	                normalisedScale = scale/20;
	    		 	squareSize = (int) (normalisedScale*initialSquareSize);
	    			font = new Font("Century Gothic", Font.PLAIN, (int) (normalisedScale*initialSquareSize / 5 * 3));
	    			font2 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 24));
	    			font3 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 15));
	    			font4 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 11));
	    		    main.revalidate();
	    		    drawGrid(normalisedScale);
	                System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);
	                System.out.println("mouseX: " + mouseX + " mouseY: "+ mouseY);
	            }
	        }
//	        if(e.getWheelRotation() < 0){
//	    	area.scrollRectToVisible(area.getBounds());
//	        //else scroll like normal
//	        }
	    }



	private void drawGrid(double normalisedScale) {
		// TODO Auto-generated method stub

		
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

		for(int i = 0; i < x-2; i++){
        	for (int j = 0; j < y-2; j++){
        		String str = tempBoxes[i][j].getText();
        		boxes[i][j].setText(str);
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
		
	
		layer.removeAll();
		// STEVE: SWITCHED THESE
		layer.add(clueNums, new Integer(1));
		layer.add(crosswordGrid, new Integer(0));
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
		layer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
	
//		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
//		layer.getActionMap().put(SOME_ACTION, someAction);
		
	}


	@Override
	public void eventDispatched(AWTEvent event) {
		// TODO Auto-generated method stub
		
	}
}
