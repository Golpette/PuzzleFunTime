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
public class DrawWordSearch extends JComponent implements ActionListener {
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
	String randomFill = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Font font, font2;
	Rectangle r;
	JLayeredPane layer;
	Random rand;
	
	public DrawWordSearch(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.grid = grid;
		this.x = x;
		this.y = y;
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		sol = new DrawSolution(grid, x, y, squareSize);
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
		layer = new JLayeredPane();
		clueNumbers = new JLabel [x-2][y-2];
		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);
		setOpaque(true);
		setBackground(Color.WHITE);
		JFrame frame = new JFrame("Auto-crossword!");
		frame.setPreferredSize(new Dimension(squareSize*(x+1),squareSize*(y+3)));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		area = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		rand = new Random();
		Border border = BorderFactory.createLineBorder(Color.BLACK);	
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				clueNumbers[i][j] = new JLabel();
				clueNumbers[i][j].setFont(font);
				clueNumbers[i][j].setBackground(new Color(255,255,255,255));
				clueNumbers[i][j].setForeground(Color.BLACK);
				clueNumbers[i][j].setBorder(border);
				clueNumbers[i][j].setVisible(true);
				clueNumbers[i][j].setOpaque(false);
				if(grid[j+1][i+1] != "_"){
					clueNumbers[i][j].setText(grid[j+1][i+1].toUpperCase());
				}else{
					clueNumbers[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(26))));
				}
				clueNumbers[i][j].setHorizontalAlignment(JTextField.CENTER);
				clueNumbers[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer.add(clueNumbers[i][j]);
			}
		}
		
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.add(transparentLayer, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(200,200));

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = (int)(frameSizeY*0.8);	//doesn't seem to do anything
		c.ipadx = 200;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.gridwidth = 3;
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
