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
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 40;	
	int x, y;
	JFrame frame;
	JPanel panel, transparentLayer, main, clues;
	JLayeredPane layer;
	JLabel [][] letters;
	String[][] grid;
	GridBagConstraints c;
	JButton reveal;
	DrawSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues;
	String randomFill = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Font font;
	Random rand;
	boolean buttonPushed;
	
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		sol = new DrawSolution(grid, x, y, squareSize, "Word Search");
	
		layer = new JLayeredPane();
		letters = new JLabel [x-2][y-2];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				letters[i][j].setFont(font);
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBorder(border);
				if(grid[j+1][i+1] != "_"){
					letters[i][j].setText(grid[j+1][i+1].toUpperCase());
				}else{
					letters[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(26))));
				}
				letters[i][j].setHorizontalAlignment(JTextField.CENTER);
				letters[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer.add(letters[i][j]);
			}
		}
		
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(500,500));
//		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
//		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.add(transparentLayer);
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBackground(new Color(255, 255, 255, 255));
		clues.setVisible(true);
		clues.setOpaque(true);
		
		for(Entry entry: entries){
			JLabel temp = new JLabel(entry.getWord());
			clues.add(temp);
		}
		
		main = new JPanel(new GridBagLayout());
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		main.add(clues, c);
		
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(main, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);
		
		frame = new JFrame("Auto Word Search");
		//frame.setPreferredSize(new Dimension(squareSize*(x+1)+squareSize/2,squareSize*(y+2)));
		frame.setBackground(new Color(255,255,255,255));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								//letters[j-1][i-1].setForeground(new Color(255,0,0,255));
								letters[j-1][i-1].setOpaque(true);
								letters[j-1][i-1].setBackground(Color.GREEN);
							}
					}
				}
			}else{
				for (int i = 0; i < x-1; i++){
					for (int j = 0; j < y-1; j++){
						if(!grid[i][j].equals("_")){
							//letters[j-1][i-1].setForeground(Color.BLACK);
							letters[j-1][i-1].setBackground(new Color(255,255,255,255));
						}
					}
				}
			}
		}
	}
}
