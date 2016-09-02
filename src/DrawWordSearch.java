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
	JPanel panel, transparentLayer;
	JLayeredPane layer;
	JLabel [][] letters;
	String[][] grid;
	GridBagConstraints c;
	JButton reveal;
	DrawSolution sol;
	ArrayList<String> fullGrid;
	String randomFill = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Font font;
	Random rand;
	
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		fullGrid = new ArrayList<String>();
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		sol = new DrawSolution(grid, x, y, squareSize, "Word Search");
		panel = new JPanel(new GridBagLayout());
		layer = new JLayeredPane();
		letters = new JLabel [x-2][y-2];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		
		reveal = new JButton("Show Solution");
		reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				letters[i][j].setFont(font);
				//letters[i][j].setBackground(new Color(255,255,255,255));
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBorder(border);
				letters[i][j].setVisible(true);
				letters[i][j].setOpaque(false);
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
		layer.add(transparentLayer);
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(200,200));

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);
		
		frame = new JFrame("Auto Word Search");
		frame.setPreferredSize(new Dimension(squareSize*(x+1),squareSize*(y+3)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			
			for (int i = 0; i < x-1; i++){
				for (int j = 0; j < y-1; j++){
					if(!grid[i][j].equals("_")){
						letters[j-1][i-1].setForeground(new Color(0,255,255,255));
					}
				}
			}
			
			
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
					
					}
				}
			}
			else{
				reveal.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						
					}
				}
			}
			
			
		}
	}
}
