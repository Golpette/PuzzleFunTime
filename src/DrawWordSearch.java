import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	JPanel panel, transparentLayer, transparentLayer2, main, clues;
	JLayeredPane layer;
	JLabel [][] letters, backgrounds;
	String[][] grid;
	GridBagConstraints c;
	JButton reveal;
	JScrollPane area;
	DrawSolution sol;
	ArrayList<String> fullGrid;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues;
	String randomFill = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	Font font, font2, font3;
	Random rand;
	boolean buttonPushed, clicked;
	Color grey;
	int wordLength, dir, startx, starty;
	ImageIcon image, image2;
	Image newimg, newimg2;
	Image img, img2;
	Color clear;
	Dimension screenSize; 
	double width;
	double height;
	Border border;
	
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		font3 = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		sol = new DrawSolution(grid, x, y, squareSize, "Word Search");
		grey = new Color(200,200,200,255);
		wordLength = 0;
		dir = 0;
		startx = 0;
		starty = 0;
		area = new JScrollPane();
		clear = new Color(255, 255, 255, 255);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
		//System.out.println(System.getProperty("os.name").toLowerCase());
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			path1 = "src/Circle.png";
			path2 = "src/CircleBlue.png";
		}
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			path1 = "src\\Circle.png";
			path2 = "src\\CircleBlue.png";
		}
	    
		image = new ImageIcon( path1 );
		img = image.getImage();
		newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);		
		image2 = new ImageIcon( path2 );

		border = BorderFactory.createLineBorder(Color.BLACK);

		img2 = image2.getImage();
		newimg2 = img2.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH ) ; 
		image2 = new ImageIcon(newimg2);
		
		layer = new JLayeredPane();
		//layer.setBorder(border);
		letters = new JLabel [x-2][y-2];
		backgrounds = new JLabel [x-2][y-2];
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		//Border border = BorderFactory.createLineBorder(Color.BLACK);	
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setBorder(border);
		transparentLayer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		transparentLayer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				mouseActionlabel(letters[i][j]);
				letters[i][j].setFont(font);
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBackground(new Color(255, 255, 255, 255));
				letters[i][j].setBorder(null);
				//letters[i][j].setIcon(image);	//need to do this in another transparent layer.
				letters[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));

				if(grid[j+1][i+1] != "_"){
					letters[i][j].setText(grid[j+1][i+1].toUpperCase());
				}else{
					letters[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(26))));
				}
				letters[i][j].setOpaque(false);
				mouseActionlabel(letters[i][j]);
				letters[i][j].setHorizontalAlignment(JTextField.CENTER);
				letters[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer.add(letters[i][j]);
				transparentLayer.add(letters[i][j]);
			}
		}
		
		transparentLayer2 = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer2.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer2.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		transparentLayer2.setOpaque(false);
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				backgrounds[i][j] = new JLabel();
				mouseActionlabel(letters[i][j]);
//				backgrounds[i][j].setFont(font);
//				backgrounds[i][j].setForeground(Color.BLACK);
				backgrounds[i][j].setBackground(new Color(255, 255, 255, 255));
//				backgrounds[i][j].setBorder(null);
				//backgrounds[i][j].setIcon(image);	//need to do this in another transparent layer.
//				if(grid[j+1][i+1] != "_"){
//					backgrounds[i][j].setText(grid[j+1][i+1].toUpperCase());
//				}else{
//					backgrounds[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(26))));
//				}
				backgrounds[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));

				backgrounds[i][j].setOpaque(false);
//				mouseActionlabel(letters[i][j]);
//				backgrounds[i][j].setHorizontalAlignment(JTextField.CENTER);
//				backgrounds[i][j].setVerticalAlignment(JTextField.CENTER);
				transparentLayer2.add(backgrounds[i][j]);
				transparentLayer2.add(backgrounds[i][j]);
			}
		}
		
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(500,500));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		layer.add(transparentLayer, new Integer(1));
		layer.add(transparentLayer2, new Integer(0));
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBackground(new Color(255, 255, 255, 255));
		
		clues.setVisible(true);
		clues.setOpaque(true);
		
		for(Entry entry: entries){
			JLabel temp = new JLabel(entry.getWord().toUpperCase());
			temp.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			clues.add(temp);
		}
		
		main = new JPanel(new GridBagLayout());
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		main.add(clues, c);
		
		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(area, c);

		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);
		
		
		System.out.println("Screen size " + width + "x" + height);
		frame = new JFrame("Auto Word Search");
		if(squareSize*(x+2)+squareSize/2 > width && squareSize*(y+2) > height){
			//frame.setPreferredSize(new Dimension((int)width,(int)height));
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
			System.out.println("GOt here");
		}
		else if(squareSize*(x+2)+squareSize/2 > width){
			frame.setPreferredSize(new Dimension((int)width,squareSize*(y+2)));
		}else if(squareSize*(y+2) > height){
			frame.setPreferredSize(new Dimension(squareSize*(x+2)+squareSize/2, (int)height-30));
		}else{
			frame.setPreferredSize(new Dimension(squareSize*(x+2)+squareSize/2,squareSize*(y+2)));
		}
		//frame.setPreferredSize(new Dimension(700,500));
		frame.setBackground(new Color(255,255,255,255));
		frame.setMinimumSize(new Dimension(500,400));
		//frame.setMaximumSize(new Dimension(1000,800));
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
		
	}

	
	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
				
				
				for (int i = 0; i < x-2; i++){		//ie down, across or diagonally down
					for (int j = 0; j < y-2; j++){
						//if(e.getClickCount()<=1){
						//letters[i][j].setBackground(grey);
//						int temp = e.getClickCount();
//						while (e.getClickCount() == temp ){
						//System.out.println("clicked " + clicked);
						if(e.getSource() == letters[i][j]){
							if(!clicked){
								//e.setSource(letters[0][0]);
								System.out.println("not clicked");
								for (int k = 0; k < x-2; k++){		//ie down, across or diagonally down
									for (int l = 0; l < y-2; l++){
										letters[k][l].setBackground(Color.WHITE);
									}
								}
								letters[i][j].setBackground(grey);
								startx = i;
								starty = j;
								clicked = true;
								System.out.println("x = " + startx + " y = " + starty);
								//break;
							}else{
								System.out.println("inside clicked");
								if(i - startx == 0){
									if(j - starty < 0){
										for(int a = 0; a < starty - j; a++){
											letters[i][starty+a].setBackground(grey);
											System.out.println("along forward");
										}
									}else if(j - starty > 0){
										for(int a = 0; a < j - starty; a++){
											letters[i][j+a].setBackground(grey);
										}
									}
								}else if (i - startx < 0){
									if(j - starty == 0){
										for(int a = 0; a < startx - i; a++){
											letters[i+a][j].setBackground(grey);
										}
									}else if(j - starty < 0){
										if(starty - j == startx - i){
											for(int a = 0; a < startx - i; a++){
												letters[i+a][j+a].setBackground(grey);
											}
										}
									}else{
										if(j - starty == startx - i){
											for(int a = 0; a < startx - i; a++){
												letters[i+a][starty+a].setBackground(grey);
											}
										}
									}
								}else{
									if(j - starty == 0){
										for(int a = 0; a < i - startx; a++){
											letters[startx+a][j].setBackground(grey);
										}
									}else if(j - starty < 0){
										if(starty - j == i - startx){
											for(int a = 0; a < startx - i; a++){
												letters[startx+a][j+a].setBackground(grey);
											}
										}
									}else{
										if(j - starty == i - startx){
											for(int a = 0; a < startx - i; a++){
												letters[startx+a][starty+a].setBackground(grey);
											}
								//letters[i][j].setBackground(grey);
										}
									}
								}
								clicked = false;
							//clicked = false;
								}
							System.out.println("clicked " + clicked);
							}
							
							//System.out.println(clicked);
						}
					}
				//}
			}

			public void mouseEntered(MouseEvent e) {//try to get to highlight letters in one direction
//				for (int i = 0; i < x-2; i++){		//ie down, across or diagonally down
//					for (int j = 0; j < y-2; j++){
//						if(e.getSource() == letters[i][j]){
//							if(sameDirection(i, j, dir)){
//								letters[i][j].setBackground(grey);
//								wordLength++;
//							}else{
//								wordLength = 0;
//							}
//						}
//					}
//				}
			}

			private boolean sameDirection(int i, int j, int dir) {
				//dir is int for direction of word:   567
				//									  801
				//									  234
				if(wordLength == 0){
					dir = 0;
					return true;
				}else if(wordLength == 1){
					for(int a = 0; a < 2; a++){
						for(int b = 0; b < 2; b++){
							if(!(a == 1 && b == 1)){
								//check boundary conditions - or is this included in the extra grid squares?
								if(letters[a][b].getBackground().equals(grey)){
									dir = (a*b+a)%8;
									return true;
							}
							//if(i > 0 && j > 0 && )
								
								
							}
						}
					}
				}
				return false;
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								reveal.setText("Hide Solution");
								//letters[j-1][i-1].setForeground(new Color(255,0,0,255));
								letters[j-1][i-1].setOpaque(true);
								letters[j-1][i-1].setBackground(Color.GREEN);
								//backgrounds[j-1][i-1].setIcon(image2);
							}
					}
				}
			}else{
				for (int i = 0; i < x-1; i++){
					for (int j = 0; j < y-1; j++){
						if(!grid[i][j].equals("_")){
							reveal.setText("Show Solution");
							//letters[j-1][i-1].setForeground(Color.BLACK);
							letters[j-1][i-1].setBackground(new Color(255,255,255,255));
							//backgrounds[j-1][i-1].setIcon(null);
						}
					}
				}
			}
		}
	}
}
