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
 * Class to take String[][] grid and paint the crossword as it should look
 */
public class DrawCrossword extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;
	private String[][] grid;	
	private JTextField [][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crossword, clue, transparentLayer;
	GridBagConstraints c;
	JTextArea text;
	JLabel [][] clueNumbers;
	ArrayList<JLabel> nums;
	ArrayList<JButton> hints;
	JScrollBar vertical;
	JButton reveal;
	DrawSolution sol;
	JScrollPane area;
	String clues;
	Font font, font2, font3;
	Rectangle r;
	JLayeredPane layer;
	Random rand;
	ArrayList<Entry> entries;
	
	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException{
		this.grid = grid;
		this.x = x;
		this.y = y;
		this.entries = entries;
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		font2 = new Font("Times New Roman", Font.PLAIN, 24);
		font3 = new Font("Times New Roman", Font.PLAIN, 20);
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword");
		panel = new JPanel(new GridBagLayout());
		clue = new JPanel(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		frameSizeX = 2*(x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		boxes = new JTextField [x-2][y-2];
		hints = new ArrayList<JButton>();
		r = new Rectangle(20,20);
		text = new JTextArea();
		text.append("\n\nACROSS: \n");
		text.append(getClues(cluesAcross));
		text.append("\nDOWN: \n" + getClues(cluesDown));
		//text.setLineWrap(true);	//Not sure what to do about this - which looks better?
		text.setWrapStyleWord(true);
		layer = new JLayeredPane();
		clueNumbers = new JLabel [x-2][y-2];
		crossword = new JPanel(new GridLayout(x-2,y-2));
		crossword.setBounds(squareSize, squareSize ,squareSize*(x-2),squareSize*(y-2));
		crossword.setOpaque(false);
		//crossword.setPreferredSize(new Dimension(300,200));
		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);
		setOpaque(true);
		setBackground(Color.WHITE);
		JFrame frame = new JFrame("Auto Crossword");
		frame.setSize(1000, 400);
		frame.setPreferredSize(new Dimension(600,400));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		area = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		area.setOpaque(false);
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				clueNumbers[i][j] = new JLabel();
				clueNumbers[i][j].setBackground(new Color(255,255,255,255));
				clueNumbers[i][j].setForeground(Color.BLACK);
				clueNumbers[i][j].setVisible(true);
				clueNumbers[i][j].setOpaque(false);
				clueNumbers[i][j].setText(gridInit[j+1][i+1]);
				clueNumbers[i][j].setVerticalAlignment(JTextField.TOP);
				
				if(!gridInit[j+1][i+1].equals("")){
					JButton hint = new JButton("Hint");
					JLabel num = new JLabel(gridInit[j+1][i+1]);
					
					hint.addActionListener(this);
					hints.add(hint);
					
					c.weightx = 0.0;
					c.weighty = 0.0;
					c.gridx = 0;
					c.gridy = i;
					clue.add(num, c);
					
					c.weightx = 0.0;
					c.weighty = 0.0;
					c.gridx = 1;
					c.gridy = i;
					clue.add(hint, c);
				}
				transparentLayer.add(clueNumbers[i][j]);
			}
		}
		
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

				
				crossword.add(boxes[i][j]);
				crossword.setMaximumSize(new Dimension(400,300));
				crossword.setMinimumSize(new Dimension(100,100));
			}
		}
		
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.add(transparentLayer, new Integer(0));
		layer.add(crossword, new Integer(1));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(200,200));
		layer.setMinimumSize(new Dimension(squareSize*(x-1),squareSize*(x-1)));
		layer.setMaximumSize(frame.getSize());

//		clue.setBackground(new Color(255, 255, 255, 255));
//		clue.add(area, new Integer(0));
//		clue.setVisible(true);
//		clue.setOpaque(true);
		//clue.setPreferredSize(new Dimension(200,200));
		//clue.setMinimumSize(new Dimension(squareSize*(x-1),squareSize*(x-1)));
		//clue.setMaximumSize(frame.getSize());
		
		c.weightx = frame.getWidth()/(frame.getWidth()-squareSize*x);
		c.weighty = 1.0;
		c.ipadx = squareSize*2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);
		
		if(frame.getWidth() > squareSize*x ){
			//c.weightx = frame.getWidth()/(squareSize*x);
		}else{
			//c.weightx = 1;
		}
		
		c.weighty = 1.0;
		//c.ipadx = 1;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(clue, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		//c.ipady = 10;
		c.gridwidth = 2;
		panel.add(reveal, c);
		
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
