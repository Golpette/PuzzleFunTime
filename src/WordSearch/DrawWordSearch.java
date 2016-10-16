package WordSearch;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
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
	JPanel extra, panel, transparentLayer, transparentLayer2, transparentLayer3, transparentLayer4, transparentLayer5, transparentLayer6,
	transparentLayer7, transparentLayer8, main, clues;
	JLayeredPane layer, layer2;
	JLabel [][] letters, letters2, letters3, letters4, letters5, letters6, letters7, letters8;
	ArrayList<JLabel[][]> allLayers;
	String[][] grid;
	String [] ordering = {"RANDOM", "ALPHABETICAL", "BIGGEST", "SMALLEST"};
	@SuppressWarnings({ "rawtypes" })
	JComboBox orderClues;
	GridBagConstraints c;
	String [] loopDirections = {"top", "topRight", "right", "bottomRight", "bottom", "bottomLeft", "left", "topLeft"};
	String operatingSystem;
	String imagePath = "";
	JButton reveal;
	boolean diagonal, notIn;
	JScrollPane area;
	DrawSolution sol;
	ArrayList<String> fullGrid, tempStrikethrough, struckThrough, solutions, clueText, sorted;
	ArrayList<JLabel> completed;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues;
	String randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
	Font font, font2, font3, font4, font5;
	Random rand;
	boolean buttonPushed, clicked, start;
	Color grey;
	int wordLength, dir, startx, starty;
	Color clear;
	Dimension screenSize; 
	double width;
	double height;
	Border border;
	String tempWord = "";
	String sortMethod = "random";
	public int counter = 0;
	boolean congratulations = false;
	boolean reset = true;
	int x_pos, y_pos;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		completed = new ArrayList<JLabel>();
		allLayers = new ArrayList<JLabel[][]>();
		tempStrikethrough = new ArrayList<String>();
		solutions = new ArrayList<String>();
		struckThrough = new ArrayList<String>();
		clueText = new ArrayList<String>();
		sorted = new ArrayList<String>();
		orderClues = new JComboBox(ordering);
		orderClues.addActionListener(this);
		orderClues.setFont(font5);
		extra = new JPanel(new GridBagLayout());
		notIn = true;
		font3 = new Font("Century Gothic", Font.PLAIN, 18);
		font3 = new Font("Century Gothic", Font.PLAIN, 16);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
		Map fontAttr = font3.getAttributes();
		fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font4 = new Font(fontAttr);
		start = true;
		
		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255,255,255,255));
		frame.setMinimumSize(new Dimension(550,400));
		
	//	sol = new DrawSolution(grid, x, y, squareSize, "Word Search");
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
		
		/**
		 *  Set image path depending on OS
		 */
		operatingSystem = System.getProperty("os.name").toLowerCase();
		if(operatingSystem.equals("linux")){
			imagePath = "src/resources/";
		}
		else if(operatingSystem.contains("windows")){
			imagePath = "src\\resources\\";
		}
	
		border = BorderFactory.createLineBorder(Color.BLACK);
		layer = new JLayeredPane();
		layer2 = new JLayeredPane();
		letters = new JLabel [x-2][y-2];
		letters2 = new JLabel [x-2][y-2];
		letters3 = new JLabel [x-2][y-2];
		letters4 = new JLabel [x-2][y-2];
		letters5 = new JLabel [x-2][y-2];
		letters6 = new JLabel [x-2][y-2];
		letters7 = new JLabel [x-2][y-2];
		letters8 = new JLabel [x-2][y-2];
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		rand = new Random();
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);

		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		transparentLayer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		transparentLayer.setBorder(border);
		transparentLayer.setBackground(clear);
		transparentLayer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				letters[i][j] = new JLabel();
				letters[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				letters[i][j].setOpaque(false);
				letters[i][j].setFont(font);
				letters[i][j].setForeground(Color.BLACK);
				letters[i][j].setBackground(clear);
				letters[i][j].setBorder(null);
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
	
		transparentLayer2 = setUpLayers(letters2, transparentLayer2);
		transparentLayer3 = setUpLayers(letters3, transparentLayer3);
		transparentLayer4 = setUpLayers(letters4, transparentLayer4);
		transparentLayer5 = setUpLayers(letters5, transparentLayer5);
		transparentLayer6 = setUpLayers(letters6, transparentLayer6);
		transparentLayer7 = setUpLayers(letters7, transparentLayer7);
		transparentLayer8 = setUpLayers(letters8, transparentLayer8);
		
		allLayers.add(letters);
		allLayers.add(letters2);
		allLayers.add(letters3);
		allLayers.add(letters4);
		allLayers.add(letters5);
		allLayers.add(letters6);
		allLayers.add(letters7);
		allLayers.add(letters8);
		
		layer.setBackground(clear);
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		layer.add(transparentLayer, new Integer(0));
		layer.add(transparentLayer2, new Integer(0));
		layer.add(transparentLayer3, new Integer(0));
		layer.add(transparentLayer4, new Integer(0));
		layer.add(transparentLayer5, new Integer(0));
		layer.add(transparentLayer6, new Integer(0));
		layer.add(transparentLayer7, new Integer(0));
		layer.add(transparentLayer8, new Integer(0));
		
		
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBounds(0,0,squareSize*(x-2),squareSize*(y-2));
		clues.setBackground(clear);
		clues.setVisible(true);
		clues.setOpaque(true);
		
		setUpClues();
		
		extra.setBackground(clear);
		extra.setVisible(true);
		extra.setOpaque(true);
		
		orderClues.setBounds(0,0,100,40);
		orderClues.setBorder(border);
		orderClues.setBackground(clear);
		orderClues.setOpaque(false);
		orderClues.setVisible(false);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		extra.add(orderClues, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 50, 0, 0);
		extra.add(clues, c);
		
//		layer2.setBackground(clear);
//		layer2.add(clues, new Integer(0));
//		layer2.add(orderClues, new Integer(1));
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		main.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		main.add(extra, c);
		
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
	
	private void setUpClues() {
		clues.removeAll();
		clueText.clear();
		sorted.clear();
		allClues.clear();
		
		
		for(Entry entry: entries){
		clueText.add(entry.getWord().toUpperCase());
		}
		
		sorted = sortedStrings(clueText, sortMethod);
		
		for(String a: sorted){
			JLabel temp = new JLabel(a);
			mouseActionlabel(temp);
			if(struckThrough.contains(a)){
				temp.setFont(font4);
			}else{
				temp.setFont(new Font("Century Gothic", Font.PLAIN, 18));
			}
			
			allClues.add(temp);
		}
		
		
		for(JLabel temp: allClues){
			clues.add(temp);
		}
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
	 * Class to set grids of labels for each potential layer
	 * @param labels
	 * @param layer
	 */
	public JPanel setUpLayers(JLabel[][] labels, JPanel layer){
		
		layer = new JPanel(new GridLayout(x-2, y-2));
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setBorder(border);
		layer.setBackground(clear);
		layer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				labels[i][j] = new JLabel();
				labels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				labels[i][j].setOpaque(false);
				labels[i][j].setFont(font);
				labels[i][j].setForeground(Color.BLACK);
				labels[i][j].setBackground(clear);
				labels[i][j].setBorder(null);
				labels[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				labels[i][j].setHorizontalAlignment(JTextField.CENTER);
				labels[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(labels[i][j]);
				layer.add(labels[i][j]);				
			}
		}
		return layer;
	}
	
	/**
	 * Class which takes String name of image (eg top for the top facing loop image) and appends the 
	 * appropriate path according to operating system to the beginning and then appends ".png" to the end.
	 */
	public String setPath(String imageName){
		return (imagePath + imageName + "2.png");	
	}
	
	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(letters[i][j])){
							for(Entry a : entries){
								x_pos = a.end_x;
								y_pos = a.end_y;
								if(a.palindromic && !start){
									//System.out.println("Palindromic start_x: " + a.start_x + " start_y: " + a.start_y +" end_x: " + a.end_x + " end_y: " + a.end_y);
									x_pos = a.start_x;
									y_pos = a.start_y;
								}
								if(x_pos == j+1 && y_pos == i+1){
//									System.out.println("a.endx: " + a.end_x + " a.endy: " + a.end_y);
//									System.out.println("now: " + tempWord);
									if(tempStrikethrough.contains(a.getWord())){
										for (JLabel temp: allClues){
											if(temp.getText().equals(a.getWord().toUpperCase()) && !(struckThrough.contains(temp.getText()))){
												if(!solutions.contains(temp.getText())){
													solutions.add(temp.getText());
													counter++;
													temp.setFont(font4);
													struckThrough.add(temp.getText());
												}
												String[] images = setImageDirections(a.direction);
												Icon ic0 = setImage(setPath(images[0]), squareSize, squareSize);
												Icon ic1 = setImage(setPath(images[1]), squareSize, squareSize);
												Icon ic2 = setImage(setPath(images[2]), squareSize, squareSize);
												Icon ic3 = setImage(setPath(images[3]), squareSize, squareSize);
												Icon ic4 = setImage(setPath(images[4]), squareSize, squareSize);
												for(JLabel[][] lab: allLayers){
													if(lab[a.end_y-1][a.end_x-1].getText().equals("")){
														lab[a.end_y-1][a.end_x-1].setIcon(ic2);
														lab[a.end_y-1][a.end_x-1].setText(" ");
														//System.out.println("Direction: "+ a.direction+ " \nlayer: " + allLayers.indexOf(lab));
														break;
													}
												}
												for(JLabel[][] lab: allLayers){
													if(lab[a.start_y-1][a.start_x-1].getText().equals("")){
														lab[a.start_y-1][a.start_x-1].setIcon(ic0);
														lab[a.start_y-1][a.start_x-1].setText(" ");
														//System.out.println("layer: " + allLayers.indexOf(lab));
														break;
													}
												}
												int [] t = setIncrements(a.direction);
												for(int c = 0; c < a.getWordLength()-1; c++){
													if(!(c == 0)){
														for(JLabel[][] lab: allLayers){
															if(lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic1);
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																//System.out.println("layer: " + allLayers.indexOf(lab));
																break;
															}
														}
													}											
													if(a.isDiagonal){
														if(a.direction.equals("BLTRdiagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic3);
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setIcon(ic4);
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else if(a.direction.equals("backwardsBLTRdiagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic4);
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setIcon(ic3);
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else if(a.direction.equals("diagonal")){
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setIcon(ic3);
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}else{
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(ic3);
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
															for(JLabel[][] lab: allLayers){
																if(lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setIcon(ic4);
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	//System.out.println("layer: " + allLayers.indexOf(lab));
																	break;
																}
															}
														}
													}
												}
												tempStrikethrough.clear();
											}									
										}	
									}
								}
								if(counter == entries.size() && !congratulations){
									
									SwingWorker<Void, String> worker = new SwingWorker<Void, String>(){

									      /*
									       * 
									       *   Trying to implement threads
									       *   Allow GUI to run without pausing while congratulations comes up later
									       *   @Override(non-Javadoc)
									       * @see javax.swing.SwingWorker#doInBackground()
									       */
									        protected Void doInBackground() throws Exception {
									            this.publish("Everything");
									            Thread.sleep(3000);
									            return null;
									        }

									        @Override
									        protected void process(List<String> res){
									            	 try {
														Thread.sleep(500);
													} catch (InterruptedException e) {
														e.printStackTrace();
													}
									            	JOptionPane.showMessageDialog(frame, "Congratulations!");
									        }

									    };

									    worker.execute();
									congratulations = true;
								}
							}
							tempStrikethrough.clear();
							for(Entry b : entries){
								if(b.palindromic){
									if(b.start_x == j+1 && b.start_y == i+1){
										tempWord = b.getWord();
										tempStrikethrough.add(tempWord);
										start = true;
										//System.out.println("Palindromic start clicked: "+ tempWord);
									}else if(b.end_x == j+1 && b.end_y == i+1){
										tempWord = b.getWord();
										tempStrikethrough.add(tempWord);
										//System.out.println("Palindromic end clicked: "+ tempWord);
										start = false;
									}
								}
								 if(b.start_x == j+1 && b.start_y == i+1){
									tempWord = b.getWord();
									tempStrikethrough.add(tempWord);
									//System.out.println("Start clicked: "+ tempWord);
								}
							}
						}
					}
				}
			}
			
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

			public void mouseEntered(MouseEvent e) {
			for(JLabel lab: allClues){
					if(e.getSource() == lab && notIn && reset){
						//display ordering dropdown
						clues.setBackground(new Color(240,240,240,255));
						orderClues.setVisible(true);
						notIn = false;
					}
				}
			for(int i = 0; i < x-2; i++){
				for (int j = 0; j < y-2; j++){
					if(e.getSource() == letters[i][j]){
						orderClues.setVisible(false);
						reset = true;
					}
				}
			}
			}

			public void mouseExited(MouseEvent e) {
				for(JLabel lab: allClues){
					if(e.getSource() == lab){
						//display ordering dropdown
						clues.setBackground(clear);
						notIn = true;
						//orderClues.setVisible(false);
					}
				}
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	/**
	 * Method to Alphabetise arraylist of strings
	 * @param strings
	 * @return
	 */
	public ArrayList<String> sortedStrings(ArrayList<String> strings, String method){
		if(method.equals("alphabetical")){
		List<String> list = strings;
		Collections.sort(list);
		strings = (ArrayList<String>) list;
		}
		else if(method.equals("smallest")){
			strings = sortBySize(strings, true);
		}
		else if(method.equals("random")){
			List<String> list = strings;
			Collections.shuffle(list);
			strings = (ArrayList<String>) list;
		}
		else if(method.equals("biggest")){
			strings = sortBySize(strings, false);
			List<String> list = strings;
			Collections.reverse(list);
			strings = (ArrayList<String>) list;
		}
		return strings;
	}
	
	/**
	 * Sort arraylist of strings by size and alphabetically or reverse of both.
	 * Involves sorting list, then running through and putting any smaller words in front of larger ones.
	 * Knowing they are ordered means only need to check one is smaller or equal than the other.
	 * The boolean is to determine if it's ordering each size group in alphabetical or reverse alphabetical order.
	 * (The latter list can then be reverse sorted to give the alphabetical by size largest first ordering).
	 * @param strings
	 * @param filter
	 * @return
	 */
	private ArrayList<String> sortBySize(ArrayList<String> strings, boolean filter) {
			List<String> list = strings;
			Collections.sort(list);
			strings = (ArrayList<String>) list;
			ArrayList<String> temp = new ArrayList<String>();
			for(String a: strings){
				if(temp.isEmpty()){
					temp.add(a);
				}
				else if(temp.get(temp.size()-1).length() <= a.length() && filter){
					temp.add(temp.size(), a);
				}else if(temp.get(temp.size()-1).length() < a.length()){
					temp.add(temp.size(), a);
				}else{
					for(int b = temp.size()-1; b >= 0; b--){
						if(b == 0){
								if(temp.get(b).length() <= a.length() && filter){
									temp.add(1, a);
									break;
								}else if(temp.get(b).length() < a.length()){
									temp.add(1, a);
									break;
								}
								else{
									temp.add(0,a);
									break;
								}
							}
							else if(temp.get(b).length() <= a.length() && filter){
								temp.add(b+1, a);
								break;
							}else if(temp.get(b).length() < a.length()){
								temp.add(b+1, a);
								break;
							}
					}
				}
			}
		return temp;
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
			//System.out.println("Changed Diagonal");
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
			//System.out.println("Changed Diagonal");
		}else if(direction.equals("BLTRdiagonal")){
			start = "BottomLeft";
			middle = "DiagonalUpRight";
			end = "TopRight";
			corner1 = "BottomRightCorner";
			corner2 = "TopLeftCorner";
			//System.out.println("Changed Diagonal");
		}else if(direction.equals("backwardsBLTRdiagonal")){
			start = "TopRight";
			middle = "DiagonalUpRight";
			end = "BottomLeft";
			corner1 = "TopLeftCorner";
			corner2 = "BottomRightCorner";
			//System.out.println("Changed Diagonal");
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
			diagonal = false;
			buttonPushed = !buttonPushed;
				if(buttonPushed){
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								reveal.setText("Hide Solution");
								letters[j-1][i-1].setOpaque(true);
								letters[j-1][i-1].setBackground(Color.GREEN);
							}
					}
				}
			}else{
				for (int i = 0; i < x-1; i++){
					for (int j = 0; j < y-1; j++){
						if(!grid[i][j].equals("_")){
							reveal.setText("Show Solution");
							letters[j-1][i-1].setOpaque(false);
							letters[j-1][i-1].setBackground(new Color(255,255,255,255));
						}
					}
				}
			}
		}
		if(e.getSource() == orderClues){
			JComboBox cb = (JComboBox)e.getSource();
			String msg = (String)cb.getSelectedItem();
			notIn = false;
			reset = false;
			if(msg.equals("ALPHABETICAL")){
				orderClues.setVisible(false);
				sortMethod = "alphabetical";
				setUpClues();
			}else if(msg.equals("BIGGEST")){
				orderClues.setVisible(false);
				sortMethod = "biggest";
				setUpClues();
			}else if(msg.equals("SMALLEST")){
				orderClues.setVisible(false);
				sortMethod = "smallest";
				setUpClues();
			}else if(msg.equals("RANDOM")){
				orderClues.setVisible(false);
				sortMethod = "random";
				setUpClues();
			}
		}
	}
}
