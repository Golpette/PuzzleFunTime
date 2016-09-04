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
public class DrawProblem extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;
	private String[][] grid;	
	private JTextField [][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crossword, transparentLayer;
	JTextArea text;
	JLabel [][] clueNumbers;
	JScrollBar vertical;
	JButton reveal;
	DrawSolution sol;
	JScrollPane area;
	String clues;
	Font font, font2;
	Rectangle r;
	JLayeredPane layer;
	
	public DrawProblem(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.grid = grid;
		this.x = x;
		this.y = y;
		//font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		font = new Font("Times New Roman", Font.BOLD, 8);
		font2 = new Font("Times New Roman", Font.PLAIN, 12);
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword");
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		frameSizeX = 2*(x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;
		reveal = new JButton("Show Solution");
		reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		boxes = new JTextField [x-2][y-2];
		r = new Rectangle(20,20);
		//need to put numbers into separate text field.
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
		frame.setPreferredSize(new Dimension(1000,400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		area = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
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
				boxes[i][j].setFont(font2);

				
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
		layer.setMinimumSize(new Dimension(squareSize*(x-1),600));
		layer.setMaximumSize(new Dimension(squareSize*(x+1),200));

		c.weightx = frame.getWidth()/(frame.getWidth()-squareSize*x);
		c.weighty = 0.0;
		c.ipady = (int)(frameSizeY*0.8);	//doesn't seem to do anything
		c.ipadx = squareSize*2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);
		
		if(frame.getWidth() > squareSize*x ){
			c.weightx = frame.getWidth()/(squareSize*x);
		}else{
			c.weightx = 1;
		}
		
		c.weighty = 0.0;
		//c.ipadx = 1;
		c.ipady = (int)(frameSizeY*0.8);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(area, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
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
	}
}
