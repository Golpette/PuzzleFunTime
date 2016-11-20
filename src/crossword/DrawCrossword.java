package crossword;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;


/**
 * Class to take String[][] grid and paint the crossword as it should look
 * complete with all the required components: The clues, grid, clue numbers,
 * solution button, hints etc
 */
public class DrawCrossword extends JComponent implements ActionListener, AWTEventListener, MouseWheelListener {

	
	private static final long serialVersionUID = 1L;
	private static int squareSize;
	private boolean buttonPushed;
	private String[][] grid;
	private JTextField[][] boxes;
	public JTextField[][] tempBoxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crosswordGrid, clue, clue2, clueNums, main, hintArea;
	JLayeredPane layer;
	JScrollPane area;
	JButton hint, reveal, showSolution;
	JLabel[][] clueNumbers;
	ArrayList<JTextArea> cluesDwn, cluesAcr, hints;
	GridBagConstraints c, c2, c3, c4, gbc_main;
	ArrayList<JLabel> nums;
	ArrayList<Entry> entries;
	DrawSolution sol;
	Font font, font2, font3, font4, fontLarge;
	Random rand;
	Border border;
	Color clear, red, green, blue, black;
	JTextArea hintD, hintA;
	ArrayList<KeyEvent> keys;
	Action action;
	Dimension screenSize;
	double width;
	double height;
	JPanel flow;
	int [] tempHighlighted;
	String tempDirection, currentClue;
	JLabel hintScrambled;
	
	// Define Color highlighting current word & clue
	Color HIGHLIGHT_COLOUR = new Color( 163 , 194,  163);  //( = faded forest green )
	
	
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
	
	boolean initialized; 
	
	
	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross,
			ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException {
                
		buttonPushed = false;
		
		tempHighlighted = new int[2];//just for zoom
		tempHighlighted[0]=0; tempHighlighted[1]=0; // dont initialize these as zero
		initialized=false; //dont color word on first draw
		tempDirection="across";       //just for zoom
		
		squareSize = 38;
		MIN_SCALE = 7.0;
		MAX_SCALE = 18.0;
		scale = 10.0;
		normalisedScale = scale/20;
		this.gridInit = gridInit;
		frameSizeX = 2 * (x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;

		currentClue = "";
		
		JFrame frame = new JFrame("Auto Crossword");
		frame.setSize(1000, 400);
		frame.setPreferredSize(new Dimension(550, 400));
		frame.setMinimumSize(new Dimension(550,400));
		frame.setBackground(new Color(255, 255, 255, 255));

		keys = new ArrayList<KeyEvent>();
		
		cluesDwn = new ArrayList<JTextArea>();
		cluesAcr = new ArrayList<JTextArea>();
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
		font2 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 26));
		font3 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 16));
		font4 = new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 12));  //clue numbers in grid
		fontLarge= new Font("Century Gothic", Font.PLAIN, (int) (2*normalisedScale* 50));

		sol = new DrawSolution(grid, x, y, squareSize, "Crossword", this);
		rand = new Random();
		
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
		Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
		double mouseX = mouseCoord.getX();
		double mouseY = mouseCoord.getY();
		
		
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
		
		tempBoxes = new JTextField[x-2][y-2];
		
		for(int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				tempBoxes[i][j] = new JTextField(); 
			}
		}
		drawGrid(normalisedScale);

		hintScrambled = new JLabel("Scrambled");
		hintScrambled.setFont(fontLarge);
		hintScrambled.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		
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

		hintArea = new JPanel(new GridLayout(1,1));
		hintArea.add(hintScrambled);
		
		//Muck around with this to get the grid positioned based on mouse position
		//crosswordGrid.setBounds((int)(squareSize - mouseX / 10), (int)(squareSize - mouseY /10), squareSize * (x - 2), squareSize * (y - 2));
		hintArea.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		hintArea.setBackground(Color.LIGHT_GRAY);
		hintArea.setOpaque(true);
		hintArea.setVisible(false);
		
		
		
		
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
		
		layer.add(clueNums, new Integer(1));
		layer.add(crosswordGrid, new Integer(0));
		layer.add(hintArea, new Integer(2));
		
		layer.setVisible(true);
		layer.setOpaque(true);
		
		
		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
		layer.setMinimumSize(new Dimension(squareSize * x, squareSize * x)  ); 

	
		//inline ifs!
		int acr = cluesAcross.size();
		int dow = cluesDown.size();
		
		int maxClues = (acr > dow) ? acr: dow ;

		
//		for (int i = 0; i < ((acr > dow) ? maxClues - dow: maxClues - acr) ; i++){
//			String jt = "\\.FILLER!!!";
//			if(acr > dow){
//				cluesAcross.add(jt);
//			}else{
//				cluesDown.add(jt);
//			}
//		}

		/**
		 * This is the GridBagLayout clue which holds all the clue components:
		 * The numbers and clues in a JTextArea and the hints in a JLabel
		 */	
		
		clue = new JPanel(new GridBagLayout());	
		clue.setMinimumSize(new Dimension( 200, 600)  );
		clue.setBackground(clear);
		
		clue2 = new JPanel(new GridBagLayout()     );
		clue2.setMinimumSize(new Dimension( 200 ,  600 )  );
		clue2.setBackground(clear);
		
		hints = new ArrayList<JTextArea>();

		//add space at top and "Across" title
		JTextArea blnk = new JTextArea("");
		blnk.setEditable(false);
		//cluesAcr.add(blnk);
		JTextArea first = new JTextArea("\nAcross"); 
		first.setEditable(false);
		first.setHighlighter(null);
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
			JTextArea across = new JTextArea(clue);
			
			across.setCursor(new Cursor(Cursor.HAND_CURSOR));   // MAKE HAND CURSOR
	
			across.setEditable(false);
			across.setHighlighter(null);  // WHY DOES THIS OD NOTHING?			
			across.setFont(font3);
			across.setLineWrap(true); 
		    across.setWrapStyleWord(true);
		    across.setColumns(25);   // CONTROLS HOW FAR TO GO BEFORE WRAPPING LINE
			mouseActionlabel(across);
			
			hintA = new JTextArea(" ");
			hintA.setFont(font3);
			hintA.setForeground(Color.BLUE);
			hints.add(hintA);
			mouseActionlabel(hintA);
		
			cluesAcr.add(across);
		}

		//add space at top and "Down" title
		JTextArea second = new JTextArea("\nDown");
		second.setEditable(false);
		second.setHighlighter(null);
		second.setFont(font2);
		cluesDwn.add(second);
		
		for (String s : cluesDown) {
			for(Entry e: entries){
				if(e.definition.equals(s)){
					len = e.wordLength;
				}
			}
			String clue = s + " (" + len + ")";
			JTextArea down = new JTextArea(clue);
			down.setCursor(new Cursor(Cursor.HAND_CURSOR));   // MAKE HAND CURSOR
			down.setFont(font3);
			down.setEditable(false);
			down.setHighlighter(null); 
			down.setLineWrap(true);
		    down.setWrapStyleWord(true);
		    down.setColumns(25); 

			hintD = new JTextArea(" ");
			hintD.setFont(font3);
			hintD.setForeground(Color.BLUE);
			hints.add(hintD);
			mouseActionlabel(down);
			mouseActionlabel(hintD);
					
			cluesDwn.add(down);
		}
				
		
		JTextArea temp = new JTextArea("FILLER");
		temp.setFont(font3);
		temp.setEditable(false);
		temp.setHighlighter(null); 
		temp.setLineWrap(true);
		temp.setWrapStyleWord(true);
	    temp.setColumns(25); 
	    
		// GridBagLayout for clue JPanels. This is how the clues will be arranged inside the JPanel.
		c3 = new GridBagConstraints();
		c3.fill = GridBagConstraints.NORTHWEST;
		c3.weighty = 0;
		c3.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		c3.gridx = 0;

		for (JTextArea j : cluesAcr) {
			clue.add(j, c3);
			JTextArea blankline = new JTextArea("");
			blankline.setEditable(false);
			clue.add(blankline, c3);//spacing between clues
		}
		c3.weighty=1;
		clue.add(new JLabel(" "), c3);
		
		c4 = new GridBagConstraints();
		c4.fill = GridBagConstraints.NORTHWEST;
		c4.weightx = 1.0;
		c4.weighty = 0;
		c4.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		c4.gridx = 1;
		
		for (JTextArea k : cluesDwn) {
			clue2.add(k, c4);
			JTextArea blankline = new JTextArea("");
			blankline.setEditable(false);
			clue2.add(blankline,c4);
		}
		c4.weighty=1;
		clue2.add(new JLabel(" "), c4);
		
	    GridBagConstraints zzz = new GridBagConstraints();
		zzz.fill = GridBagConstraints.VERTICAL;
		zzz.gridx=0;
		zzz.gridy=0;
		zzz.weightx=0.0;
		zzz.weighty=0.0;
		
		
		flow = new JPanel(new GridLayout(1,2));
		flow.setAlignmentX(SwingConstants.NORTH);
		flow.add(clue );
		flow.add(clue2 );		
		
		/**
		 * This is the layout of the GridBagLayout panel main which holds all
		 * the crossword components. There are two components inside it: A
		 * JLayeredPane and a GridBagLayout
		 */
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		
		gbc_main = new GridBagConstraints();
		gbc_main.weighty = 1.0;
		gbc_main.weightx = 1.0;
		gbc_main.gridx = 0;
		gbc_main.gridy = 0;
		
		gbc_main.fill = GridBagConstraints.BOTH;
		
		main.add(layer, gbc_main);
		gbc_main.gridx = 1; // NEED THIS
		
		main.add(flow, gbc_main);
		
		/**
		 * This is the largest area of the GUI which holds the crossword and
		 * clues pane and makes them scrollable.
		 */
		
		area = new JScrollPane(main);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
		   public void run() { 
		       area.getVerticalScrollBar().setValue(0);
		       area.getHorizontalScrollBar().setValue(0);
		   }
		});
		
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		
		// Scrolling as opposed to zooming. TODO: WILL NOT SCROLL WHEN MOUSE OVER GRID??
		area.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		area.getActionMap().put(SOME_ACTION, someAction);
		area.addMouseWheelListener(this);
		area.scrollRectToVisible(null);
		
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
		reveal = new JButton("Clue");
		reveal.setFont(font2); 
		reveal.addActionListener(this);
		reveal.addMouseWheelListener(this);
		
		/**
		 * This is the panel for the main area of the program. It holds two
		 * components: A JScrollPane and a JButton
		 */
		hint = new JButton("Hint");
		hint.setFont(font2);
		hint.addActionListener(this);
		hint.addMouseWheelListener(this);
		
		showSolution = new JButton("Solution");
		showSolution.setFont(font2);
		showSolution.addActionListener(this);
		showSolution.addMouseWheelListener(this);
		
		panel = new JPanel(new GridBagLayout());

		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;		
		c.weighty = 1.0;
		c.gridwidth = 3;
		
		panel.add(area, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.ipadx = 400;
		c.gridwidth = 1;
		panel.add(hint, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 10;
		
		panel.add(reveal, c);
		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 2;
//		c.gridy = 1;
//		c.ipady = 10;
//		
//		panel.add(showSolution, c);
		
		
		/**
		 * Overall JFrame to hold all components This has the main panel
		 * assigned to it
		 */
		
		if(squareSize*(x+2)+squareSize/2 > width && squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
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
							
							
							
							// Highlight appropriate  clue after every key event
							makeAllCluesWhite();
							colorAppropriateClue();

							
							
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
						if (e.getSource().equals(boxes[i][j]) && boxes[i][j].isEnabled()  ){
							makeAllWhite();
							highlightWord_fromClick(i,j);
//							tempHighlighted[0] = i;
//							tempHighlighted[1] = j;
						}
						//for (JLabel lb : hints) {
						//	lb.setText(" ");
						//}
					}
				}
				
				// highlight approp. clue
				makeAllCluesWhite();
				colorAppropriateClue();
				
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
	void mouseActionlabel(JTextArea l) {
		
		
		l.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
				firsteverclick = false;
				countClicks=0;
				
//				// GET FOCUS IF CLICKING ON CLUE   ///////////////////////////////////////////
//				//(no longer necessary - but reinstate for mobile phones)  -- STEVE
				// BETTER LEAVE IT IN: since trying to click on clues will defocus grid otherwise
				
				for( JTextArea cl : cluesAcr ){
					if( e.getSource()==cl ){
						makeAllWhite();
						makeAllCluesWhite();
						cl.setBackground(HIGHLIGHT_COLOUR);
						cl.setOpaque(true);	
						String c_n_string = ""+cl.getText().split("\\.")[0];
						int c_n = Integer.parseInt( c_n_string   );
						for( Entry ent : entries ){
							if( ent.getClueNumber() == c_n && ent.isAcross()   ){
								// then highlight this word
								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
								colourWord( ent.getY()-1, ent.getX()-1, "across");
							}
						}
						
					}
			
				}
				for( JTextArea cl : cluesDwn ){
					if( e.getSource()==cl ){
						makeAllWhite();
						makeAllCluesWhite();
						cl.setBackground(HIGHLIGHT_COLOUR);
						cl.setOpaque(true);	
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
					
				}    //////////////////////////////////////////////////////////////////////////
				
				
				
				
				
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


			
			public void mouseEntered(MouseEvent e) {
////				for (JLabel i : hints) {
////					if (e.getSource() == i) {
////						if(i.getText().equals(" ")){
////							i.setText("      HINT");
////						}
////					}
////				}    STEVE:  REMOVED FOR NOE, WASNT COMPATIBLE WITH HIGHLITING CLUES. DONT KNOW WHY			
//				
				
	// BELOW NEEDED FOR MOUSE HOVERING OVER CLUES
//				firsteverclick = false;
//				
//				makeAllCluesWhite();
//				
//				for( JTextArea cl : cluesAcr ){
//					if( e.getSource()==cl ){
//						cl.setBackground(HIGHLIGHT_COLOUR);
//						cl.setOpaque(true);	
//						
//						makeAllWhite();
//
//						String c_n_string = ""+cl.getText().split("\\.")[0];
//						int c_n = Integer.parseInt( c_n_string   );
//						for( Entry ent : entries ){
//							if( ent.getClueNumber() == c_n && ent.isAcross()   ){
//								// then highlight this word
//								boxes[ent.getY()-1][ent.getX()-1].requestFocus();
//								colourWord( ent.getY()-1, ent.getX()-1, "across");   // X AND Y COORDS ARE FUCKED UP. FIX THIS. BUG. TODO!!
//							}
//						}
//						
//						
//					}
//			
//				}
//				for( JTextArea cl : cluesDwn ){
//					if( e.getSource()==cl ){
//						cl.setBackground(HIGHLIGHT_COLOUR);
//						cl.setOpaque(true);	
//						
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
//						
//						
//					}					
//				}
		
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

				
//				
//				for( JTextArea cl : cluesAcr ){   // only need for hovering
//					if( e.getSource()==cl ){
//						cl.setBackground(Color.WHITE);
//						cl.setOpaque(true);						
//					}
//			
//				}
//				for( JTextArea cl : cluesDwn ){
//					cl.setBackground(Color.WHITE);
//					cl.setOpaque(true);							
//				}
//				
				
			}
			
			
			
			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {

			}
		});
	}
	
	
	public void showHintArea(){
		
				for (Entry ent : entries ) {
					System.out.println("currentClue: "+currentClue);
					//*******NEED CONDITION HERE FOR IF WORD IS HIGHLIGHTED
						if (ent.getWord().equals(currentClue)) {
							System.out.println("here!!!1");
							hintScrambled.setText(ent.getShuffledWord().toUpperCase());
					}
				}
		hintArea.setVisible(true);
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
		
		tempHighlighted[0] = xstart; //these store highlight&direc for zooming
		tempHighlighted[1] = ystart;
		tempDirection=direc;
		
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
						currentClue = ent.getWord();
							if( ent.isAcross()  ) {
								hintScrambled.setText(ent.getShuffledWord().toUpperCase());
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
							currentClue = ent.getWord();
							if( !ent.isAcross()  ) {
								hintScrambled.setText(ent.getShuffledWord().toUpperCase());
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
	
	
	
	
	
	
	
	
	public void colorAppropriateClue(){
		/** Highlight clue of any word that is highlighted in grid */
		
		
		// FIND ANY COLOURED SQUARE
		int coloredX=0;  int coloredY=0;
		for( int abc=0; abc<x-2; abc++ ){
			for( int xyz=0; xyz<y-2; xyz++ ){
				if( boxes[abc][xyz].getBackground().getRGB() == HIGHLIGHT_COLOUR.getRGB()  ){ 
					coloredY = abc;  coloredX = xyz;
					//break;
				}
			}
		}
				
		// GET DIRECITON
		String dd = "";
		if( (coloredX>0 && boxes[coloredY][coloredX-1].getBackground().getRGB() == HIGHLIGHT_COLOUR.getRGB() )  
				||   (coloredX<x-3 && boxes[coloredY][coloredX+1].getBackground().getRGB() == HIGHLIGHT_COLOUR.getRGB()  )  ){
			dd = "across";
		}
		else if( (coloredY>0 && boxes[coloredY-1][coloredX].getBackground().getRGB() == HIGHLIGHT_COLOUR.getRGB() ) 
				|| (coloredY<y-3 && boxes[coloredY+1][coloredX].getBackground().getRGB() == HIGHLIGHT_COLOUR.getRGB()  )   ){
			dd = "down"; 
		}
		
		// GET CLUE NUMBER AT START OF WORD 
		int cn_h = -1;
		
		if( dd.equalsIgnoreCase("across") ){

			for( int gg=0; gg<x-2; gg++ ){
				
				if( (!clueNumbers[coloredY][coloredX-gg].getText().equals("") && coloredX-gg==0)
						|| (!clueNumbers[coloredY][coloredX-gg].getText().equals("") && !boxes[coloredY][coloredX-gg-1].isEnabled() )  ){
					// conditional statements find first clue in word (since any word can contain multiple clue numbers)
					
					cn_h = Integer.parseInt( clueNumbers[coloredY][coloredX-gg].getText()  );
															
					// highlight appropriate clue
					for( JTextArea cl : cluesAcr ){		
						
						if( cl.getText().contains(".") ){
							String c_n_string = ""+cl.getText().split("\\.")[0];
							int c_n = Integer.parseInt( c_n_string   );
						
							if( c_n == cn_h ){
								cl.setBackground(HIGHLIGHT_COLOUR);
								cl.setOpaque(true);	
							}	
						}
					}
					break;										
				}
			}
		}
		else if( dd.equalsIgnoreCase("down") ){
						
			for( int gg=0; gg<y-2; gg++ ){
				
				if( (!clueNumbers[coloredY-gg][coloredX].getText().equals("")  && coloredY-gg==0 )     ||
					( !clueNumbers[coloredY-gg][coloredX].getText().equals("")  &&  !boxes[coloredY-gg-1][coloredX].isEnabled()   )    ){
					
					cn_h = Integer.parseInt( clueNumbers[coloredY-gg][coloredX].getText()  );
															
					// highlight appropriate clue
					for( JTextArea cl : cluesDwn ){		
						
						if( cl.getText().contains(".") ){
							String c_n_string = ""+cl.getText().split("\\.")[0];
							int c_n = Integer.parseInt( c_n_string   );
						
							if( c_n == cn_h ){
								cl.setBackground(HIGHLIGHT_COLOUR);
								cl.setOpaque(true);	
							}	
						}
					}
					break;										
				}
			}
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
	
	
	
	public void makeAllCluesWhite(){
		/** Make all clues white (de-highlight them)  **/
		for( JTextArea cl : cluesAcr ){
			cl.setBackground(Color.WHITE);
			cl.setOpaque(true);											
		}
		for( JTextArea cl : cluesDwn ){
			cl.setBackground(Color.WHITE);
			cl.setOpaque(true);											
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
	
	/**
	 * This does something or other
	 */
	public void revealSolution(){
		showSolution.setText("Hide Solution");
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
		showSolution.setText("Solution");
		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				boxes[i][j].setForeground(new Color(0, 0, 0, 255));
			}
		}
	}
	
	public void revealWord(){
		System.out.println("Word Revealed!");
		reveal.setText("Hide Clue");
	}
	
	public void hideWord(){
		System.out.println("Word Hidden!");
		reveal.setText("Clue");
	}
	
	public void showHint(){
		System.out.println("Hint revealed!");
		showHintArea();
		hint.setText("Hide Hint");
	}
	
	public void hideHint(){
		System.out.println("Hint gone!");
		hint.setText("Hint");
	}
	
	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == hint) {
			
			//sol.frame.setVisible(!sol.frame.isVisible());	//leave this for testing but remove for final product
//			if (sol.frame.isVisible()) {
			buttonPushed = !buttonPushed;
			if(buttonPushed){
				showHint();
			
				hintArea.setVisible(true);
				//revealSolution();
				//show anagram for random/currently highlighted word
			} else {
				//hideSolution();
				hintArea.setVisible(false);
				hideHint();
			}
		}
		
		if (e.getSource() == reveal) {
			//sol.frame.setVisible(!sol.frame.isVisible());	//leave this for testing but remove for final product
//			if (sol.frame.isVisible()) {
			buttonPushed = !buttonPushed;
			if(buttonPushed){
				revealWord();
				//reveal currently highlighted/random word
				//revealSolution();
			} else {
				hideWord();
				//hideSolution();
			}
		}
		//********************TECHNICAL NOTE**************************************
		//Change the line below to:  "    == hint)     " to enable full solution as well as hint
		//************************************************************************
		if (e.getSource() == showSolution) {
			sol.frame.setVisible(!sol.frame.isVisible());	//leave this for testing but remove for final product
//			if (sol.frame.isVisible()) {
			buttonPushed = !buttonPushed;
			if(buttonPushed){
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
	        	
	        	// no scrolling if ctrl is pressed
	        	area.setWheelScrollingEnabled(false);

	        	
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
	    		    //System.out.println("Got here \\n\n\n");
	    		    drawGrid( normalisedScale);
	                //System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);	                    action.actionPerformed( null );
	            } else {

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

	            }
	        }
	        else if(!e.isControlDown()){

	        	area.setWheelScrollingEnabled(true);

	        }
	    }


	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

	private void drawGrid(double normalisedScale) {	
		/**
		 * This is where all the crossword boxes are filled black or provide a
		 * usable JTextfield. This is layered on top of the transparentLayer
		 */
		crosswordGrid = new JPanel(new GridLayout(x - 2, y - 2));
		//Muck around with this to get the grid positioned based on mouse position
		//crosswordGrid.setBounds((int)(squareSize - mouseX / 10), (int)(squareSize - mouseY /10), squareSize * (x - 2), squareSize * (y - 2));
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
        layer.add(clueNums, new Integer(1));
		layer.add(crosswordGrid, new Integer(0));
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
		layer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
	
//		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
//		layer.getActionMap().put(SOME_ACTION, someAction);
		
		// RECOLOR WORD AFTER ZOOMING
		if( !initialized ){
			initialized=true;
		}else{
			colourWord( tempHighlighted[0], tempHighlighted[1], tempDirection );
		}
	}


	@Override
	public void eventDispatched(AWTEvent event) {
		// TODO Auto-generated method stub
		
	}
}
