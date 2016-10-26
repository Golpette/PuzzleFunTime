package wordsearch;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;

import crossword.Entry;
import resources.SetUpImages;

/**
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener, MouseWheelListener, AWTEventListener {
	SetUpImages setImages;
	JFrame frame;
	JLayeredPane layer, layer2, extra;
	JPanel panel, transparentLayer, transparentLayer2, transparentLayer3, transparentLayer4, transparentLayer5, transparentLayer6, transparentLayer7, transparentLayer8, main, clues;
	@SuppressWarnings({ "rawtypes" })
	JComboBox orderClues;
	JButton reveal;
	JScrollPane area;
	GridBagConstraints c;
	ArrayList<String> fullGrid, tempStrikethrough, struckThrough, solutions, clueText, sorted, cluesAcross, cluesDown, randomLetters;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues, completed;
	ArrayList<JLabel[][]> allLetters;
	ArrayList<JPanel> allLayers;
	ArrayList<Icon [][]> temporaryIcons;
	Font font, font2, font3, font4, font5;
	Random rand;
	Color grey, clear;
	Dimension screenSize; 
	Border border;
	String[][] grid;
	String [] ordering = {"RANDOM", "ALPHABETICAL", "BIGGEST", "SMALLEST"}, loopDirections = {"top", "topRight", "right", "bottomRight", "bottom", "bottomLeft", "left", "topLeft"};
	String tempWord, sortMethod, randomFill;
	private final static String SOME_ACTION = "control 1";
	private static final long serialVersionUID = 1L;
	double width, height, mouseX, mouseY, scale, normalisedScale;
	final double MAX_SCALE, MIN_SCALE;
	int x, y, x_pos, y_pos, counter, wordLength, dir, startx, starty, squareSize;
	final static int INITIAL_SQUARE_SIZE = 80, NUMBER_OF_LAYERS = 8;
	boolean buttonPushed, clicked, start, congratulations, reset, diagonal, notIn;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DrawWordSearch(String[][] grid, int x, int y, ArrayList<String> cluesAcross, ArrayList<String> cluesDown,  ArrayList<Entry> entries) throws IOException{
		System.out.println("Started again");
		this.x = x;
		this.y = y;
		this.grid = grid;
		this.entries = entries;
		this.cluesAcross = cluesAcross;
		this.cluesDown = cluesDown;
		this.normalisedScale = scale/20;
		squareSize = 40;
		fullGrid = new ArrayList<String>();
		allClues = new ArrayList<JLabel>();
		completed = new ArrayList<JLabel>();
		allLetters = new ArrayList<JLabel[][]>();
		randomLetters = new ArrayList<String>();
		tempStrikethrough = new ArrayList<String>();
		solutions = new ArrayList<String>();
		struckThrough = new ArrayList<String>();
		clueText = new ArrayList<String>();
		sorted = new ArrayList<String>();
		allLayers = new ArrayList<JPanel>();
		orderClues = new JComboBox(ordering);
		orderClues.addActionListener(this);
		orderClues.setFont(font5);
		extra = new JLayeredPane();
		notIn = true;
		scale = 10.0;
		counter = 0;
		MAX_SCALE = 20.0;
		MIN_SCALE = 3.0;
		tempWord = "";
		sortMethod = "random";
		randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
		temporaryIcons = new ArrayList<Icon [][]>();
		int test = (int)(3*INITIAL_SQUARE_SIZE*normalisedScale/5);
		System.out.println("Test: " + test);
		font3 = new Font("Century Gothic", Font.PLAIN, 18);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font = new Font("Century Gothic", Font.PLAIN, (int)(normalisedScale*INITIAL_SQUARE_SIZE / 5 * 3));
		Map fontAttr = font3.getAttributes();
		fontAttr.put (TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
		font4 = new Font(fontAttr);
		start = true;
		congratulations = false;
		reset = true;
		rand = new Random();
		for(int i = 0; i < x*y; i++){
			randomLetters.add(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
		}
		Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.FOCUS_EVENT_MASK);
		System.out.print(MouseInfo.getPointerInfo().getLocation() + " | ");
		Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
		double mouseX = mouseCoord.getX();
		double mouseY = mouseCoord.getY();
		System.out.println("mouseX: " + mouseX + " mouseY: " + mouseY);
		
		frame = new JFrame("Auto Word Search");
		frame.setBackground(new Color(255,255,255,255));
		frame.setMinimumSize(new Dimension(550,400));
		
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
		
		border = BorderFactory.createLineBorder(Color.BLACK);
		layer = new JLayeredPane();
		layer2 = new JLayeredPane();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		buttonPushed = false;
		
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		reveal.addMouseWheelListener(this);
		
		reveal.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		reveal.getActionMap().put(SOME_ACTION, someAction);
		
		transparentLayer = new JPanel(new GridLayout(x-2, y-2));
		for(int i = 0; i < NUMBER_OF_LAYERS; i++){
			JPanel panel = new JPanel();
			setUpIcons(temporaryIcons);
			setUpLetters(allLetters);
			allLayers.add(panel);
		}
		drawGrid(normalisedScale);
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.addMouseWheelListener(this);
		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		layer.getActionMap().put(SOME_ACTION, someAction);
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBounds(0,40,18*x,squareSize*(y-2));
		clues.setBackground(clear);
		clues.setVisible(true);
		clues.setOpaque(true);
		
		setUpClues(normalisedScale);
		
		extra.setBackground(clear);
		extra.setVisible(true);
		extra.setOpaque(true);
		extra.setBounds(squareSize*(x),0,squareSize*(x-2),squareSize*(y-2));
		extra.addMouseWheelListener(this);
		extra.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		extra.getActionMap().put(SOME_ACTION, someAction);
		
		orderClues.setBounds(0,0,18*x,30);
		orderClues.setBorder(border);
		orderClues.setBackground(clear);
		orderClues.setOpaque(false);
		orderClues.setVisible(false);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		extra.add(orderClues, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0, 0, 0, 0);
		extra.add(clues, c);
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 0, 0);
		main.add(layer, c);

		c.weightx = 0.1;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 10;
		c.anchor = GridBagConstraints.NORTHWEST;
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
		panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		panel.getActionMap().put(SOME_ACTION, someAction);
		
		
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
	
	private void setUpLetters(ArrayList<JLabel[][]> allLetters){
		JLabel[][] letters = new JLabel [x-2][y-2];
		allLetters.add(letters);
	}
	
	private void setUpIcons(ArrayList<Icon[][]> allLetters){
		Icon[][] icon = new Icon [x-2][y-2];
		temporaryIcons.add(icon);
	}
	
	private void setUpClues(double noralisedScale) {
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
				temp.setFont(font3);
			}
			
			allClues.add(temp);
		}
		for(JLabel temp: allClues){
			clues.add(temp);
		}
	}
	
	/**
	 * Class to set grids of labels for each potential layer
	 * @param labels
	 * @param layer
	 */
	public JPanel setUpLayers(JLabel[][] labels, JPanel layer, int level){
		
		layer = new JPanel(new GridLayout(x-2, y-2));
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		System.out.println("squareSize: "+ squareSize);
		layer.setBorder(border);
		layer.setBackground(clear);
		layer.setOpaque(false);

		for (int i = 0; i < x-2; i++){
			for (int j = 0; j < y-2; j++){
				ImageIcon temp;
				labels[i][j] = new JLabel();
				labels[i][j].setHorizontalTextPosition(SwingConstants.CENTER);
				labels[i][j].setOpaque(false);
				labels[i][j].setFont(font);
				labels[i][j].setForeground(Color.BLACK);
				labels[i][j].setBackground(clear);
				labels[i][j].setBorder(null);
				if(temporaryIcons.get(level)[i][j] != null){
					temp = (ImageIcon)temporaryIcons.get(level)[i][j];
					Image img = temp.getImage();
					Image newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH ) ; 
					temp = new ImageIcon(newimg);
					labels[i][j].setIcon(temp);
				}
				if(level == 0){
					if(grid[j+1][i+1] != "_"){
						if(buttonPushed){
							allLetters.get(0)[i][j].setOpaque(true);
							allLetters.get(0)[i][j].setBackground(Color.GREEN);
						}
						allLetters.get(0)[i][j].setText(grid[j+1][i+1].toUpperCase());
					}else{
						allLetters.get(0)[i][j].setText(randomLetters.get(i*x + j));
					}
				}
				labels[i][j].setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
				labels[i][j].setHorizontalAlignment(JTextField.CENTER);
				labels[i][j].setVerticalAlignment(JTextField.CENTER);
				mouseActionlabel(labels[i][j]);
				layer.add(labels[i][j]);				
			}
		}
		return layer;
	}

	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {
			
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						if (e.getSource().equals(allLetters.get(0)[i][j])){
							for(Entry a : entries){
								x_pos = a.end_x;
								y_pos = a.end_y;
								if(a.palindromic && !start){
									x_pos = a.start_x;
									y_pos = a.start_y;
								}
								if(x_pos == j+1 && y_pos == i+1){
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
												Icon[] icons = new Icon[5];
												setImages = new SetUpImages(images, squareSize, squareSize, icons);
												setDiagonalImages(a.end_y-1,a.end_x-1, 2, icons);
												setDiagonalImages(a.start_y-1, a.start_x-1, 0, icons);
												int [] t = setIncrements(a.direction);
												for(int c = 0; c < a.getWordLength()-1; c++){
													if(!(c == 0)){
														setDiagonalImages(a.start_y-1+c*t[1], a.start_x-1+c*t[0], 1, icons);
													}											
													if(a.isDiagonal){
														if(a.direction.equals("BLTRdiagonal")){
															setDiagonalImages(a.start_y-2+c*t[1], a.start_x-1+c*t[0], 3, icons);
															setDiagonalImages(a.start_y-1+c*t[1], a.start_x+c*t[0], 4, icons);
														}else if(a.direction.equals("backwardsBLTRdiagonal")){
															setDiagonalImages(a.start_y+c*t[1], a.start_x-2+(c-1)*t[0], 3, icons);
															setDiagonalImages(a.start_y-1+c*t[1], a.start_x-2+c*t[0], 4, icons);
														}else if(a.direction.equals("diagonal")){
															setDiagonalImages(a.start_y-1+c*t[1], a.start_x-1+(c+1)*t[0], 3, icons);
															setDiagonalImages(a.start_y+c*t[1], a.start_x-1+c*t[0], 4, icons);
														}else{
															setDiagonalImages(a.start_y-1+c*t[1], a.start_x-2+c*t[0], 3, icons);
															setDiagonalImages(a.start_y-2 + c*t[1], a.start_x-1+c*t[0], 4, icons);
														}
													}
												}
												tempStrikethrough.clear();
											}									
										}	
									}
								}
								if(counter == entries.size() && !congratulations){
									JOptionPane.showMessageDialog(frame, "Congratulations!");
									SwingWorker<Void, String> worker = new SwingWorker<Void, String>(){

									      /**   To implement threads
									       *   Allow GUI to run without pausing while congratulations comes up later
									       *   @Override(non-Javadoc)
									       * @see javax.swing.SwingWorker#doInBackground()
									       */
									        protected Void doInBackground() throws Exception {
									            this.publish("Everything");
									            Thread.sleep(3000);
									            return null;
									        }
									        @SuppressWarnings("unused")
											protected void process(ArrayList<String> res){
									            	 try {
														Thread.sleep(300);
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
									}else if(b.end_x == j+1 && b.end_y == i+1){
										tempWord = b.getWord();
										tempStrikethrough.add(tempWord);
										start = false;
									}
								}
								 if(b.start_x == j+1 && b.start_y == i+1){
									tempWord = b.getWord();
									tempStrikethrough.add(tempWord);
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
						clues.setBackground(new Color(240,240,240,255));
						orderClues.setVisible(true);
						notIn = false;
					}
				}
			for(int i = 0; i < x-2; i++){
				for (int j = 0; j < y-2; j++){
					if(e.getSource() == allLetters.get(0)[i][j]){
						orderClues.setVisible(false);
						reset = true;
					}
				}
			}
			}

			public void mouseExited(MouseEvent e) {
				for(JLabel lab: allClues){
					if(e.getSource() == lab){
						clues.setBackground(clear);
						notIn = true;
					}
				}
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	
	public void setDiagonalImages(int x, int y, int image, Icon[] icons){
		for(JLabel[][] lab: allLetters){
			if(!buttonPushed){
				lab[x][y].setOpaque(false);
			}
			if(temporaryIcons.get(allLetters.indexOf(lab))[x][y] == null && lab[x][y].getText().equals("")){
				lab[x][y].setIcon(icons[image]);
				temporaryIcons.get(allLetters.indexOf(lab))[x][y] = icons[image];
				lab[x][y].setText(" ");
				break;
			}
		}
	}
	
	public void drawGrid(double normalised){
		layer.removeAll();
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		for(int i = 0; i < 8; i++){
			JPanel transparentLayer = new JPanel(new GridLayout(x-2, y-2));
			allLayers.add(transparentLayer);
			layer.add(transparentLayer, new Integer(0));
		}
		
		

		
		transparentLayer = setUpLayers(allLetters.get(0), transparentLayer, 0);
		transparentLayer2 = setUpLayers(allLetters.get(1), transparentLayer2, 1);
		transparentLayer3 = setUpLayers(allLetters.get(2), transparentLayer3, 2);
		transparentLayer4 = setUpLayers(allLetters.get(3), transparentLayer4, 3);
		transparentLayer5 = setUpLayers(allLetters.get(4), transparentLayer5, 4);
		transparentLayer6 = setUpLayers(allLetters.get(5), transparentLayer6, 5);
		transparentLayer7 = setUpLayers(allLetters.get(6), transparentLayer7, 6);
		transparentLayer8 = setUpLayers(allLetters.get(7), transparentLayer8, 7);
		layer.removeAll();
		layer.setBounds(squareSize,100,squareSize*(x-2),squareSize*(y-2));
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
	}
	
	Action someAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

				public void actionPerformed(ActionEvent e) {
	                System.out.println("do some action");
	            }
	        };
	
	/**
	 * Method to Alphabetise arraylist of strings
	 * @param strings
	 * @return
	 */
	public ArrayList<String> sortedStrings(ArrayList<String> strings, String method){
		if(method.equals("alphabetical")){
		ArrayList<String> list = strings;
		Collections.sort(list);
		strings = (ArrayList<String>) list;
		}
		else if(method.equals("smallest")){
			strings = sortBySize(strings, true);
		}
		else if(method.equals("random")){
			ArrayList<String> list = strings;
			Collections.shuffle(list);
			strings = (ArrayList<String>) list;
		}
		else if(method.equals("biggest")){
			strings = sortBySize(strings, false);
			ArrayList<String> list = strings;
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
			ArrayList<String> list = strings;
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
		}else if(direction.equals("BLTRdiagonal")){
			start = "BottomLeft";
			middle = "DiagonalUpRight";
			end = "TopRight";
			corner1 = "BottomRightCorner";
			corner2 = "TopLeftCorner";
		}else if(direction.equals("backwardsBLTRdiagonal")){
			start = "TopRight";
			middle = "DiagonalUpRight";
			end = "BottomLeft";
			corner1 = "TopLeftCorner";
			corner2 = "BottomRightCorner";
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
					reveal.setText("Hide Solution");
					for (int i = 0; i < x-1; i++){
						for (int j = 0; j < y-1; j++){
							if(!grid[i][j].equals("_")){
								allLetters.get(0)[j-1][i-1].setIcon(null);
								allLetters.get(0)[j-1][i-1].setOpaque(true);
								allLetters.get(0)[j-1][i-1].setBackground(Color.GREEN);
							}
					}
				}
			}else{
				reveal.setText("Show Solution");
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						allLetters.get(0)[j][i].setOpaque(true);
						allLetters.get(0)[j][i].setBackground(clear);
					}
				}
				//Insert here a better way of checking the images that need to be loaded
				for (int i = 0; i < x-2; i++){
					for (int j = 0; j < y-2; j++){
						for(JLabel [][] labs: allLetters){
								if(temporaryIcons.get(allLetters.indexOf(labs))[i][j] != null){
									for(JLabel [][] labs2: allLetters){
										labs2[i][j].setOpaque(false);
									}
									ImageIcon temp = (ImageIcon)temporaryIcons.get(allLetters.indexOf(labs))[i][j];
									Image img = temp.getImage();
									Image newimg = img.getScaledInstance(squareSize, squareSize, java.awt.Image.SCALE_SMOOTH ) ; 
									temp = new ImageIcon(newimg);
									labs[i][j].setIcon(temp);
								}
							}
						//}
					}
				}
			}
		}
		if(e.getSource() == orderClues){
			@SuppressWarnings("rawtypes")
			JComboBox cb = (JComboBox)e.getSource();
			String msg = (String)cb.getSelectedItem();
			notIn = false;
			reset = false;
			if(msg.equals("ALPHABETICAL")){
				orderClues.setVisible(false);
				sortMethod = "alphabetical";
				setUpClues(normalisedScale);
			}else if(msg.equals("BIGGEST")){
				orderClues.setVisible(false);
				sortMethod = "biggest";
				setUpClues(normalisedScale);
			}else if(msg.equals("SMALLEST")){
				orderClues.setVisible(false);
				sortMethod = "smallest";
				setUpClues(normalisedScale);
			}else if(msg.equals("RANDOM")){
				orderClues.setVisible(false);
				sortMethod = "random";
				setUpClues(normalisedScale);
			}
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		 	
		 	Point mouseCoord = MouseInfo.getPointerInfo().getLocation();
			mouseX = mouseCoord.getX();
			mouseY = mouseCoord.getY();
	        if (e.isControlDown()) {
	            if (e.getWheelRotation() < 0) {
	                JComponent component = (JComponent)e.getComponent();
	                Action action = component.getActionMap().get(SOME_ACTION);
	                if(scale < MAX_SCALE){
	                	scale++;
	                }
	            	font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
	                normalisedScale = scale/20;
	    		 	squareSize = (int) (normalisedScale*INITIAL_SQUARE_SIZE);
	    		    font2 = new Font("Century Gothic", Font.PLAIN, (int)(3*INITIAL_SQUARE_SIZE*normalisedScale/5));
	    		    main.revalidate();
	    		    drawGrid( normalisedScale);
	                System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);	                    action.actionPerformed( null );
	            } else {
	                System.out.println("scrolled down");
	                if(scale > MIN_SCALE){
	                	scale--;
	                }
	            	font = new Font("Century Gothic", Font.PLAIN, squareSize / 5 * 3);
	                normalisedScale = scale/20;
	    		 	squareSize = (int) (normalisedScale*INITIAL_SQUARE_SIZE);
	    		    font2 = new Font("Century Gothic", Font.PLAIN, (int)(3*INITIAL_SQUARE_SIZE*normalisedScale/5));
	    		    main.revalidate();
	    		    drawGrid(normalisedScale);
	                System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);
	                System.out.println("mouseX: " + mouseX + " mouseY: "+ mouseY);
	               
	            }
	        }
	        if(e.getWheelRotation() < 0) {
	        	System.out.println("Scrolling Up...");
	        }else{
	        	System.out.println("Scrolling Down...");
	        }
	        //else scroll like normal
	    }

	@Override
	public void eventDispatched(AWTEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
