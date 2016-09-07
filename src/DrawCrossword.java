import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Class to take String[][] grid and paint the crossword as it should look complete with 
 * all the required components: The clues, grid, clue numbers, solution button, hints etc
 */
public class DrawCrossword extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;
	private String[][] grid;	
	private JTextField [][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crosswordGrid, clue, clueNums, main;
	JLayeredPane layer;
	JScrollPane area;
	JTextArea text;
	JButton reveal;
	JLabel [][] clueNumbers;
	JLabel [] cluesDown, cluesAcross;
	GridBagConstraints c;
	ArrayList<JLabel> nums;
	ArrayList<JButton> hints;
	ArrayList<Entry> entries;
	DrawSolution sol;
	Font font, font2, font3;
	Random rand;
	Border border;
	
	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException{
		JFrame frame = new JFrame("Auto Crossword");
		frame.setSize(500, 400);
		frame.setPreferredSize(new Dimension(500,400));
		frame.setBackground(new Color(255,255,255,255));
		
		this.grid = grid;
		this.x = x;
		this.y = y;
		this.entries = entries;
		
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		font2 = new Font("Times New Roman", Font.PLAIN, 24);
		font3 = new Font("Times New Roman", Font.PLAIN, 20);
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword");
		frameSizeX = 2*(x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;
		rand = new Random();
		
		/*
		 * This is where the transparentLayer to hold all the clue numbers is created.
		 * It sets all the cells with question numbers with the correct number in the 
		 * top left corner of a GridLayout cell.
		 */
		clueNums = new JPanel(new GridLayout(x-2, y-2));
		clueNums.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		clueNums.setOpaque(false);
		clueNumbers = new JLabel [x-2][y-2];
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				clueNumbers[i][j] = new JLabel();
				clueNumbers[i][j].setBackground(new Color(255,255,255,255));
				clueNumbers[i][j].setForeground(Color.BLACK);
				clueNumbers[i][j].setVisible(true);
				clueNumbers[i][j].setOpaque(false);
				clueNumbers[i][j].setText(gridInit[j+1][i+1]);
				clueNumbers[i][j].setVerticalAlignment(JTextField.TOP);
				clueNums.add(clueNumbers[i][j]);
			}
		}
		
		/*
		 * This is where all the crossword boxes are filled black or provide
		 * a useable JTextfield.  This is layered on top of the transparentLayer
		 */
		crosswordGrid = new JPanel(new GridLayout(x-2,y-2));
		crosswordGrid.setBounds(squareSize, squareSize ,squareSize*(x-2),squareSize*(y-2));
		crosswordGrid.setOpaque(false);
		boxes = new JTextField [x-2][y-2];
		border = BorderFactory.createLineBorder(Color.BLACK);	
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				boxes[i][j] = new JTextField();	//need new layout to resize letters in boxes
				boxes[i][j].setFont(new Font("Times New Roman", Font.BOLD, 20));
				boxes[i][j].setBorder(border);
				boxes[i][j].setDocument(new JTextFieldLimit(1));
				if(grid[j+1][i+1] == "_"){
					boxes[i][j].setBackground(new Color(0, 0, 0, 255));
					boxes[i][j].setEnabled(false);
				}else{
					boxes[i][j].setBackground(new Color(255, 255, 255, 100));
					boxes[i][j].setOpaque(false);
				}
				boxes[i][j].setHorizontalAlignment(JTextField.CENTER);
				boxes[i][j].setFont(font3);

				
				crosswordGrid.add(boxes[i][j]);
				crosswordGrid.setMaximumSize(new Dimension(400,300));
				crosswordGrid.setMinimumSize(new Dimension(100,100));
			}
		}
		
		/*
		 * This is the JLayeredPane layer which holds the actual crossword.
		 * It is composed of two layers crosswordGrid and clueNums which are both 
		 * GridLayout JPanels which are layered one on top of the other.
		 */
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.add(clueNums, new Integer(0));
		layer.add(crosswordGrid, new Integer(1));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x-1),squareSize*(x-1)));
		//layer.setMaximumSize(frame.getSize());

		
		/*
		 * This is the GridBagLayout clue which holds all the clue components:
		 * The numbers and clues in a JTextArea and the hints in a JPanel
		 */
		clue = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		text = new JTextArea();
		text.append("\n\nACROSS: \n");
		text.append(getClues(cluesAcross));
		text.append("\nDOWN: \n" + getClues(cluesDown));
		//text.setVisible(true);
		//text.setOpaque(true);
		//text.setLineWrap(true);	//Not sure what to do about this - which looks better?
		text.setWrapStyleWord(true);
		hints = new ArrayList<JButton>();
		
		
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				if(!gridInit[j+1][i+1].equals("")){
					JButton hint = new JButton("Hint");  //try JLabels
		            hint.setOpaque(true);
		            hint.setVisible(true);
					hint.addActionListener(this);
					//hint.setBounds(0, 0, 100, 20);
//					hint.setPreferredSize(new Dimension(100,20));
//					hint.setMinimumSize(new Dimension(squareSize*4,squareSize));
//					hint.setMaximumSize(frame.getSize());
					hints.add(hint);
				}	
			}
		}
		for (JButton b: hints){
			//c.weightx = 1.0;
			//c.weighty = 1.0;
			c.gridx = 1;
			c.gridy = hints.indexOf(b);
			c.gridheight = 1/hints.size();
			clue.add(b, c);
		}
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.gridheight = hints.size();		//This is funny...
		clue.add(text, c);
		
		clue.setBackground(new Color(255, 255, 255, 255));
		//clue.add(area, new Integer(0));
		//clue.setVisible(true);
		//clue.setOpaque(true);
		//clue.setPreferredSize(new Dimension(200,200));
		//clue.setBounds(200, 0, 400, 400) ;
//		clue.setMinimumSize(new Dimension(squareSize*(x-1),squareSize*(x-1)));
//		clue.setMaximumSize(frame.getSize());
		
		
		/*
		 * This is the layout of the GridBagLayout panel main which holds all the crossword
		 * components.  There are two components inside it: A JLayeredPane and a GridBagLayout
		 */
		main = new JPanel(new GridBagLayout());
//		main.setOpaque(true);
//		main.setVisible(true);
		
//		c.weightx = 0.5;		//frame.getWidth()/(frame.getWidth()-squareSize*x);
//		c.weighty = 0.2;
		//c.ipadx = squareSize*2;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);
		
//		c.weightx = 0.5;		//frame.getWidth()/(frame.getWidth()-squareSize*x);
//		c.weighty = 0.2;
		//c.ipadx = squareSize*2;
		c.gridx = 1;
		c.gridy = 0;
		main.add(clue, c);
		
		
		/*
		 * This is the largest area of the GUI which holds the 
		 * crossword and clues pane and makes them scrollable.
		 */
		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		//area.setBounds(0,0,frame.getWidth(), 500);
		///area.setOpaque(true);
		//area.setVisible(true);
		
		/*
		 * This is the button which generates a solution for the given crossword
		 * bringing up a new GUI instance with the filled in grid on being pressed.
		 */
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
//		reveal.setOpaque(true);
//		reveal.setEnabled(true);
		reveal.addActionListener(this);
		
		/*
		 * This is the panel for the main area of the program.
		 * It holds two components:  A JScrollPane and a JButton
		 */
		panel = new JPanel(new GridBagLayout());
//		panel.setOpaque(true);
//		panel.setVisible(true);
		
		c.weighty = 1.0;
		c.ipadx = 1;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(area, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		//c.gridwidth = 2;
		panel.add(reveal, c);
		
		/*Overall JFrame to hold all components
		 * This has the main panel assigned to it
		 */
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	private String getClues(ArrayList<String> clues) {
		StringBuilder stringBuilder = new StringBuilder();
		for(String s: clues){
			stringBuilder.append(s);
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	public String shuffleString(String string){
		ArrayList<Character> letters = new ArrayList<Character>();
		letters.clear();
		StringBuilder str = new StringBuilder(string.length());
		for (Character c: string.toCharArray()){
			letters.add(c);
		}
		while (letters.size() >= 1){
			char temp = letters.remove(rand.nextInt(letters.size()));
			str.append(temp);
			}
		return str.toString();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if(boxes[i][j].getText().equals("")){
							boxes[i][j].setForeground(new Color(0,0,0,255));
						}
						else if(boxes[i][j].getText().toLowerCase().equals(grid[j+1][i+1])){
							boxes[i][j].setForeground(new Color(0,255,0,255));
						}else{
							boxes[i][j].setForeground(new Color(255,0,0,255));
						}
					}
				}
			}
			else{
				reveal.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						boxes[i][j].setForeground(new Color(0,0,0,255));
					}
				}
			}
		}
		for(JButton h: hints){
			if(e.getSource()== h){
				h.setText(shuffleString(entries.get(hints.indexOf(h)).getWord()));		//need to set this to the corresponding word anagramised
				//h.setText(shuffleString(h.getText()));
			}else{
				h.setText("Hint");
			}
		}
		
	}
}
