import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
	private String[][] gridInit, grid;	
	private JTextField [][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crossword, transparentLayer;
	JTextArea text2, text3, textS;
	JLabel text;
	JLabel [][] clueNumbers;
	JScrollBar vertical;
	JButton reveal;
	DrawSolution sol;
	JScrollPane area, area2;
	String clues;
	Font font, font2;
	Rectangle r;
	JLayeredPane layer;
	
	public DrawProblem(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.gridInit = gridInit;
		this.grid = grid;
		this.x = x;
		this.y = y;
		
		sol = new DrawSolution(grid, x, y);
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
		
		font = new Font("Times New Roman", Font.BOLD, 12);
		font2 = new Font("Times New Roman", Font.PLAIN, 12);
		textS = new JTextArea();
		text2 = new JTextArea(getClues(cluesAcross));
		text3 = new JTextArea(getClues(cluesDown));
		text = new JLabel("<html><b>ACROSS:</b><p>");
		text2.setFont(font);
		text3.setFont(font);
		text2.setEditable(false);
        text2.setLineWrap(true);
        text2.setWrapStyleWord(true);
		text3.setEditable(false);
        text3.setLineWrap(true);
        text3.setWrapStyleWord(true);
		textS.append("ACROSS: \n");
		text.setText(text.getText() + text2.getText());
		text.setFont(font2);
		text.setText(text.getText() + "<p><p><b>DOWN:</b></p><p>" + text3.getText());
		textS.append(getClues(cluesAcross));
		textS.append("\nDOWN: \n" + getClues(cluesDown));
		layer = new JLayeredPane();
		clueNumbers = new JLabel [x-2][y-2];
		crossword = new JPanel(new GridLayout(x-2,y-2));
		crossword.setBounds(0,0,squareSize*(x-2),squareSize*(y-2));
		crossword.setOpaque(false);
		//crossword.setBackground(new Color(255,255,255,255));
		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(0,0,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setOpaque(false);
		//transparentLayer.setForeground(new Color(255,255,255,255));
		setOpaque(true);
		setBackground(Color.WHITE);
		JFrame frame = new JFrame("Auto-crossword!");
		//frame.setPreferredSize(new Dimension(frameSizeX,frameSizeY));
		frame.setPreferredSize(new Dimension(1000,400));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(new Color(255,255,255,255));
		
		//textS.setPreferredSize(new Dimension(600, 800));
		area = new JScrollPane(textS, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area2 = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area2.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		
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
				boxes[i][j] = new JTextField();
				
				boxes[i][j].setBorder(border);
				boxes[i][j].setDocument(new JTextFieldLimit(1));
				//boxes[i][j].setBackground(new Color(255,255,255, 255));
				if(grid[j+1][i+1] == "_"){
					//boxes[i][j].setBackground(Color.BLACK);
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

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = (int)(frameSizeY*0.8);	//doesn't seem to do anything
		c.ipadx = 200;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(layer, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.ipady = (int)(frameSizeY*0.8);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(area, c);
		
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
	
	protected void paintComponent(Graphics g) {
		int width = getWidth(), height = getHeight();
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		int ox = 0, oy = 0;
		int sx = squareSize; int sy = squareSize;
		Font myFont = new Font("Times New Roman", Font.PLAIN, 11);
		g.setFont(myFont);
		for(int q =1; q<x-1; q++){
			for(int qq = 1; qq<y-1; qq++){
				if (grid[q][qq] == "_") {			
					g.setColor(Color.BLACK);
					new DrawRectangle(g, ox+q*sx, oy+qq*sy, squareSize, gridInit[q][qq]);	
				}
				if (grid[q][qq] != "_") {
					g.setColor(Color.WHITE);
					new DrawRectangle(g, ox+q*sx, oy+qq*sy, squareSize, gridInit[q][qq]);
				}	
			}					
		}
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
							//boxes[i][j].setBackground(new Color(255,255,255,255));
						}
						else if(boxes[i][j].getText().toLowerCase().equals(grid[j+1][i+1])){
							boxes[i][j].setForeground(new Color(0,255,0,255));
							//boxes[i][j].setBackground(new Color(255,255,255, 255));
						}else{
							boxes[i][j].setForeground(new Color(255,0,0,255));
							//boxes[i][j].setBackground(new Color(255,255,255, 255));
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
