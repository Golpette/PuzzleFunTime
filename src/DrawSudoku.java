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
public class DrawSudoku extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;	
	int x, y;
	JFrame frame;
	JPanel panel, transparentLayer;
	JLayeredPane layer;
	JLabel [][] numbers;
	int[][] grid;
	GridBagConstraints c;
	JButton reveal;
	DrawSolution sol;
	ArrayList<String> fullGrid;
	Font font;
	Random rand;
	boolean buttonPushed;
	
	public DrawSudoku(int[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		fullGrid = new ArrayList<String>();
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		layer = new JLayeredPane();
		numbers = new JLabel [x-2][y-2];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		//reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);
		//transparentLayer.setBackground(Color.RED);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				numbers[i][j] = new JLabel();
				numbers[i][j].setFont(font);
				numbers[i][j].setForeground(Color.BLACK);
				numbers[i][j].setBorder(border);
				numbers[i][j].setHorizontalAlignment(JTextField.CENTER);
				numbers[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer.add(numbers[i][j]);
			}
		}

		layer.add(transparentLayer);
		
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
		frame.setPreferredSize(new Dimension(squareSize*(x)+squareSize/2,squareSize*(y+2)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!(grid[i][j] == 0)){
								//letters[j-1][i-1].setForeground(new Color(255,0,0,255));
								numbers[j-1][i-1].setOpaque(true);
								numbers[j-1][i-1].setBackground(Color.GREEN);
							}
					}
				}
			}else{
				for (int i = 0; i < x-1; i++){
					for (int j = 0; j < y-1; j++){
						if(!(grid[i][j] == 0)){
							//letters[j-1][i-1].setForeground(Color.BLACK);
							numbers[j-1][i-1].setBackground(new Color(255,255,255,255));
						}
					}
				}
			}
		}
	}
}
