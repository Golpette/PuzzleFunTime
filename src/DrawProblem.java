import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class to take String[][] grid and paint the crossword as it should look
 */
public class DrawProblem extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 20;
	private String[][] gridInit, grid;
	private ArrayList<String> cluesAcross;
	private ArrayList<String> cluesDown;	
	int x,y, frameSizeX, frameSizeY;
	JPanel panel, crossword;
	JTextArea text;
	JScrollBar vertical;
	JButton reveal;
	DrawSolution sol;
	JScrollPane area;
	String clues;
	BufferedImage image;
	JLabel picLabel;
	
	public DrawProblem(String[][] grid_init, String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown) throws IOException{
		this.gridInit = grid_init;
		this.grid = grid;
		this.x = x;
		this.y = y;
		this.cluesAcross = cluesAcross;
		this.cluesDown = cluesDown;
		sol = new DrawSolution(grid, x, y);
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		frameSizeX = 2*(x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;

		//image = ImageIO.read(new File("Example_1.jpg"));
		picLabel = new JLabel(getImage("\\Example_1.jpg"));
		crossword.add(picLabel);
		
		reveal = new JButton("Show Solution");
		reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		text = new JTextArea("ACROSS: \n" + getClues(cluesAcross));
		text.append("\nDOWN: \n" + getClues(cluesDown));
		setOpaque(true);
		setBackground(Color.WHITE);
		JFrame frame = new JFrame("Auto-crossword!");
		frame.setPreferredSize(new Dimension(frameSizeX,frameSizeY));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
		text.setPreferredSize(new Dimension(600, 800));
		area = new JScrollPane(text, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//area = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		
		
		crossword = new JPanel();
		
		c.weightx = 0.5;
		c.weighty = 0.0;
		c.ipady = (int)(frameSizeY*0.8);
		c.gridx = 0;
		c.gridy = 0;
		panel.add(crossword, c);
		
		c.weightx = 0.5;
		c.weighty = 0.0;
		c.ipady = (int)(frameSizeY*0.8);
		c.gridx = 1;
		c.gridy = 0;
		panel.add(area, c);
		
		c.weightx = 0.25;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.gridwidth = 2;
		panel.add(reveal, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}

	private String getImage(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getClues(ArrayList<String> clues) {
		StringBuilder stringBuilder = new StringBuilder();
		for(String s: clues){
			System.out.println(s);
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
		myFont = new Font("Times New Roman", Font.BOLD, 12);
		g.setFont(myFont);
		g.drawString("ACROSS:", ox+(x*squareSize)+squareSize, oy+squareSize);
		int y_coord = 0;
		myFont = new Font("Times New Roman", Font.PLAIN , 12);
		g.setFont(myFont);
		int lineNumber = -1;
		for(int cl = 0; cl < cluesAcross.size(); cl++){
			lineNumber++;
			y_coord = oy+squareSize + (17*(lineNumber + 1));
			g.drawString(cluesAcross.get(cl), ox+(x*squareSize)+squareSize, y_coord);
		}
		myFont = new Font("Times New Roman", Font.BOLD , 12);
		g.setFont(myFont);
		int startHere = y_coord + 25;
		g.drawString("DOWN:", ox+(x*squareSize)+squareSize, startHere);
		myFont = new Font("Times New Roman", Font.PLAIN , 12);
		g.setFont(myFont);
		for(int cl = 0; cl < cluesDown.size(); cl++){
			y_coord = startHere + (17*(cl+1));
			g.drawString(cluesDown.get(cl), ox+(x*squareSize)+squareSize, y_coord);
		}		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
			}
			else{
				reveal.setText("Show Solution");
			}
		}
	}
}
