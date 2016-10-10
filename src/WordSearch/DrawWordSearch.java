package WordSearch;
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
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import Crossword.DrawSolution;
import Crossword.Entry;

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
	String [] loopDirections = {"top", "topRight", "right", "bottomRight", "bottom", "bottomLeft", "left", "topLeft"};
	String operatingSystem;
	String imagePath = "";
	JButton reveal;
	boolean diagonal;
	JScrollPane area;
	DrawSolution sol;
	ArrayList<String> fullGrid, tempStrikethrough;
	ArrayList<JLabel> completed;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues;
	String randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
	Font font, font2, font3, font4;
	Random rand;
	boolean buttonPushed, clicked;
	Color grey;
	int wordLength, dir, startx, starty;
	Color clear;
	Dimension screenSize; 
	double width;
	double height;
	Border border;
	String tempWord = "";
	
	@SuppressWarnings("unchecked")
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		completed = new ArrayList<JLabel>();
		tempStrikethrough = new ArrayList<String>();
		
		font3 = new Font("Century Gothic", Font.PLAIN, 18);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		Map fontAttr = font3.getAttributes();
		fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font4 = new Font(fontAttr);
		
		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255,255,255,255));
		frame.setMinimumSize(new Dimension(550,400));
		
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
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "src\\resources\\";
		}
	    
//		for(String direction: loopDirections){
//				setImage(setPath(direction), squareSize, squareSize);
//		}
	
		
		border = BorderFactory.createLineBorder(Color.BLACK);
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
		
		transparentLayer.setBackground(clear);
		//transparentLayer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		transparentLayer.setOpaque(true);
		//transparentLayer.setVisible(true);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				letters[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters[i][j].setOpaque(true);
				
				letters[i][j].setFont(font);
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBackground(clear);
				letters[i][j].setBorder(null);
				//letters[i][j].setIcon(image);	//need to do this in another transparent layer.
				letters[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				if(grid[j+1][i+1] != "_"){
					letters[i][j].setText(grid[j+1][i+1].toUpperCase());
				}else{
					letters[i][j].setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
				}
				letters[i][j].setHorizontalAlignment(JTextField.CENTER);
				
				letters[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(letters[i][j]);
				
				transparentLayer.add(letters[i][j]);
				
			}
		}
		
		transparentLayer2 = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer2.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		//transparentLayer2.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		transparentLayer.setBackground(clear);
		transparentLayer2.setOpaque(false);
		
		
		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				backgrounds[i][j] = new JLabel();
				//mouseActionlabel(letters[i][j]);
				backgrounds[i][j].setBackground(clear);
				//backgrounds[i][j].setIcon(setImage("src\\Bottom.png", squareSize, squareSize));
				backgrounds[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));

				backgrounds[i][j].setOpaque(false);
				transparentLayer2.add(backgrounds[i][j]);
			}
		}
		
		layer.setBackground(clear);
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		layer.add(transparentLayer, new Integer(0));
		layer.add(transparentLayer2, new Integer(0));
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBackground(clear);
		
		clues.setVisible(true);
		clues.setOpaque(true);
		
		for(Entry entry: entries){
			JLabel temp = new JLabel(entry.getWord().toUpperCase());
			temp.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			allClues.add(temp);
		}
		
		for(JLabel temp: allClues){
			clues.add(temp);
		}
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);
		
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
		
		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);
		area.setVisible(true);
		area.setOpaque(true);
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		panel.setBackground(clear);
		panel.setBackground(clear);
		
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
		
		if(squareSize*(x+6) > width && squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
		}
		else if(squareSize*(x+6) > width){
			frame.setPreferredSize(new Dimension((int)width,squareSize*(y+2)));
		}else if(squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension(squareSize*(x+6), (int)height-30));
		}else{
			frame.setPreferredSize(new Dimension(squareSize*(x+6), squareSize*(y+2)));
		}
		frame.setContentPane(panel);
		frame.pack();
		frame.setBackground(clear);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.getRootPane().setDefaultButton(reveal);
	}
	
	/**
	 * This sets the imageIcon from a given name of image and creates the corresponding image, 
	 * from it.  The image is then scaled and placed into a temporary image
	 * and finally this is set back to the imageIcon itself.  
	 */
	public Icon setImage(String path, int scaleX, int scaleY){
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newimg = img.getScaledInstance(scaleX, scaleY, java.awt.Image.SCALE_SMOOTH ) ; 
		icon = new ImageIcon(newimg);
		return icon;	
	}

	/**
	 * Class which takes String name of image (eg top for the top facing loop image) and appends the 
	 * appropriate path according to operating system to the beginning and then appends ".png" to the end.
	 */
	public String setPath(String imageName){
		return (imagePath + imageName + ".png");	
	}
	
	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
		
			
			public void mouseClicked(MouseEvent e) {
				
				
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(letters[i][j])){
							//letters[i][j].setBackground(Color.GREEN);
							//for (String b: tempStrikethrough){
							for(Entry a : entries){
								if(a.end_x == j+1 && a.end_y == i+1){
									System.out.println("a.endx: " + a.end_x + " a.endy: " + a.end_y);
									System.out.println("now: " + tempWord);
									if(tempStrikethrough.contains(a.getWord())){
										for (JLabel temp: allClues){
											if(temp.getText().equals(a.getWord().toUpperCase())){
												temp.setFont(font4);
												String[] images = setImageDirections(a.direction);
												Icon ic0 = setImage(setPath(images[0]), squareSize, squareSize);
												Icon ic1 = setImage(setPath(images[1]), squareSize, squareSize);
												Icon ic2 = setImage(setPath(images[2]), squareSize, squareSize);
												Icon ic3 = setImage(setPath(images[3]), squareSize, squareSize);
												Icon ic4 = setImage(setPath(images[4]), squareSize, squareSize);
												letters[i][j].setIcon(ic2);
												letters[a.start_y-1][a.start_x-1].setIcon(ic0);
												int [] t = setIncrements(a.direction);
												for(int c = 0; c < a.getWordLength()-1; c++){
													if(!(c == 0)){
														letters[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic1);
													}													
													if(a.isDiagonal){
														letters[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic3);
														letters[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setIcon(ic4);
													}
												}
												tempStrikethrough.clear();
											}									
										}	
									}
								}
							}
							tempStrikethrough.clear();
							for(Entry b : entries){
								if(b.start_x == j+1 && b.start_y == i+1){
									tempWord = b.getWord();
									//tempStrikethrough.clear();
									tempStrikethrough.add(tempWord);
									System.out.println("Start clicked: "+ tempWord);
								}
							}
						}
					}
				}
			}
				
//				for (int i = 0; i < x-2; i++){		//ie down, across or diagonally down
//					for (int j = 0; j < y-2; j++){
//						//if(e.getClickCount()<=1){
//						//letters[i][j].setBackground(grey);
////						int temp = e.getClickCount();
////						while (e.getClickCount() == temp ){
//						//System.out.println("clicked " + clicked);
//						if(e.getSource() == letters[i][j]){
//							if(!clicked){
//								//e.setSource(letters[0][0]);
//								System.out.println("not clicked");
//								for (int k = 0; k < x-2; k++){		//ie down, across or diagonally down
//									for (int l = 0; l < y-2; l++){
//										letters[k][l].setBackground(Color.WHITE);
//									}
//								}
//								letters[i][j].setBackground(grey);
//								startx = i;
//								starty = j;
//								clicked = true;
//								System.out.println("x = " + startx + " y = " + starty);
//								//break;
//							}else{
//								System.out.println("inside clicked");
//								if(i - startx == 0){
//									if(j - starty < 0){
//										for(int a = 0; a < starty - j; a++){
//											letters[i][starty+a].setBackground(grey);
//											System.out.println("along forward");
//										}
//									}else if(j - starty > 0){
//										for(int a = 0; a < j - starty; a++){
//											letters[i][j+a].setBackground(grey);
//										}
//									}
//								}else if (i - startx < 0){
//									if(j - starty == 0){
//										for(int a = 0; a < startx - i; a++){
//											letters[i+a][j].setBackground(grey);
//										}
//									}else if(j - starty < 0){
//										if(starty - j == startx - i){
//											for(int a = 0; a < startx - i; a++){
//												letters[i+a][j+a].setBackground(grey);
//											}
//										}
//									}else{
//										if(j - starty == startx - i){
//											for(int a = 0; a < startx - i; a++){
//												letters[i+a][starty+a].setBackground(grey);
//											}
//										}
//									}
//								}else{
//									if(j - starty == 0){
//										for(int a = 0; a < i - startx; a++){
//											letters[startx+a][j].setBackground(grey);
//										}
//									}else if(j - starty < 0){
//										if(starty - j == i - startx){
//											for(int a = 0; a < startx - i; a++){
//												letters[startx+a][j+a].setBackground(grey);
//											}
//										}
//									}else{
//										if(j - starty == i - startx){
//											for(int a = 0; a < startx - i; a++){
//												letters[startx+a][starty+a].setBackground(grey);
//											}
//								//letters[i][j].setBackground(grey);
//										}
//									}
//								}
//								clicked = false;
//							//clicked = false;
//								}
//							System.out.println("clicked " + clicked);
//							}
//							
//							//System.out.println(clicked);
//						}
//					}
//				//}
//			}

			private int[] setIncrements(String direction) {
				int [] inc = new int[2];
				if(direction.equals("across")){
					inc[0] = 1;
					inc[1] = 0;
				}
				else if(direction.equals("backwards")){
					inc[0] = -1;
					inc[1] = 0;				
				}
				else if(direction.equals("down")){
					inc[0] = 0;
					inc[1] = 1;
				}
				else if(direction.equals("up")){
					inc[0] = 0;
					inc[1] = -1;
				}
				else if(direction.equals("diagonal")){
					inc[0] = 1;
					inc[1] = 1;
				}
				else if(direction.equals("backwardsdiagonal")){
					inc[0] = -1;
					inc[1] = -1;
				}
				else if(direction.equals("BLTRdiagonal")){
					inc[0] = 1;
					inc[1] = -1;
				}
				else if(direction.equals("backwardsBLTRdiagonal")){
					inc[0] = -1;
					inc[1] = 1;
				}
				return inc;
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
	
	/**
	 * Get names of images for relevant direction of word
	 * @param direction
	 * @param diagonal
	 * @return
	 */
	public String [] setImageDirections(String direction){
		String middle = "";
		String start = "";
		String end = "";
		String corner1 = "";
		String corner2 = "";
		if(direction.equals("across")){
			start = "Left";
			middle = "Horizontal";
			end = "Right";
		}else if(direction.equals("down")){
			start = "Top";
			middle = "Vertical";
			end = "Bottom";
		}else if(direction.equals("diagonal")){
			start = "TopLeft";
			middle = "DiagonalDownRight";
			end = "BottomRight";
			corner1 = "BottomLeftCorner";
			corner2 = "TopRightCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("backwards")){
			start = "Right";
			middle = "Horizontal";
			end = "Left";
		}else if(direction.equals("up")){
			start = "Bottom";
			middle = "Vertical";
			end = "Top";
		}else if(direction.equals("backwardsdiagonal")){
			start = "BottomRight";
			middle = "DiagonalDownRight";
			end = "TopLeft";
			corner1 = "TopRightCorner";
			corner2 = "BottomLeftCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("BLTRdiagonal")){
			start = "BottomLeft";
			middle = "DiagonalUpRight";
			end = "TopRight";
			corner1 = "BottomRightCorner";
			corner2 = "TopLeftCorner";
			System.out.println("Changed Diagonal");
		}else if(direction.equals("backwardsBLTRdiagonal")){
			start = "TopRight";
			middle = "DiagonalUpRight";
			end = "BottomLeft";
			corner1 = "TopLeftCorner";
			corner2 = "BottomRightCorner";
			System.out.println("Changed Diagonal");
		}else{
			start = "Left";
			middle = "Horizontal";
			end = "Right";
			corner1 = "";
			corner2 = "";
		}
		String [] images = {start, middle, end, corner1, corner2};
		return images; 
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			//String direction = "Top";
			diagonal = false;
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								reveal.setText("Hide Solution");
								//letters[j-1][i-1].setForeground(new Color(255,0,0,255));
								letters[j-1][i-1].setOpaque(true);
								//letters[j-1][i-1].setBackground(Color.GREEN);
								//set the image around the word
								
								//System.out.println(setPath(direction));
								System.out.println("imagePath"+imagePath);
								//set conditions for each image
								String direction = "across";	//Set up direction of each letter
								String [] temps;				
								String upperSide = "";			//and possible side pieces 
								String lowerSide = "";
//								for(Entry a: entries){
//									if(a.getX()==j-1 && a.getY() == i-1){
//										direction = a.direction;
//										System.out.println("word: "+a.word);
//										System.out.println("direction: " +direction);
//										temps = setImageDirections(direction, diagonal);
//										if(i-1 == a.start_x && j-1 == a.start_y){//condition for first letter
//											direction = temps[0];
//											System.out.println("Start of Word: " + a.word);
//										}else if(i-1 == a.end_x && j-1 == a.end_y){//condition for last letter
//											direction = temps[1];
//											System.out.println("End of Word: " + a.word);
//										}else{//condition for middle letter
//											direction = temps[2];
//											upperSide = temps[3];
//											lowerSide = temps[4];
//										}
//									}
//								}
								Icon temp = setImage(setPath(direction), squareSize, squareSize);
								Icon corner1  = setImage(setPath(upperSide), squareSize, squareSize);
								Icon corner2 = setImage(setPath(lowerSide), squareSize, squareSize);
								if(diagonal){
									if(j >= 2 && i >=1){
										letters[j-2][i-1].setIcon(corner1);
									}
									if(i >= 1){
										letters[j][i-1].setIcon(corner2);
									}
									
								}
								letters[j-1][i-1].setIcon(temp);
								letters[j-1][i-1].setHorizontalTextPosition(SwingConstants.CENTER);
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
