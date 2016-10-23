package wordsearch;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.SwingWorker;
import java.awt.font.TextAttribute;
import java.io.IOException;
import javax.swing.border.Border;


import crossword.Entry;
import resources.SetUpImages;

/**
 * Class to draw a word search
 */
public class DrawWordSearch extends JComponent implements ActionListener, MouseWheelListener, AWTEventListener {
	SetUpImages setImages;
	JFrame frame;
	ArrayList<JPanel> allLayers;
	JPanel panel, main, clues;
	JLayeredPane layer, layer2, extra;
	@SuppressWarnings({ "rawtypes" })
	JComboBox orderClues;
	JButton reveal;
	JScrollPane area;
	GridBagConstraints c;
	ArrayList<String> fullGrid, tempStrikethrough, struckThrough, solutions, clueText, sorted, cluesAcross, cluesDown, randomLetters;
	ArrayList<Entry> entries;
	ArrayList<JLabel> allClues, completed;
	ArrayList<JLabel[][]> allLetters;
	Font font, font2, font3, font4, font5;
	Random rand;
	Color grey, clear;
	Dimension screenSize; 
	Border border;
	ArrayList<Icon [][]> temporaryIcons;
	String[][] grid;
	String [] directions = {"backwardsdiagonal", "backwards", "backwardsBLTRdiagonal", "up", "", "down", "BLTRdiagonal", "across", "diagonal"};
	String [] ordering = {"RANDOM", "ALPHABETICAL", "BIGGEST", "SMALLEST"};
	String tempWord, sortMethod, randomFill;
	private final static String SOME_ACTION = "control 1";
	private static final long serialVersionUID = 1L;
	double width, height, mouseX, mouseY, scale, normalisedScale;
	final double MAX_SCALE, MIN_SCALE, NUMBER_OF_LAYERS;
	int x, y, x_pos, y_pos, counter, wordLength, dir, startx, starty, squareSize;
	final static int initialSquareSize = 80;
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
		NUMBER_OF_LAYERS = 8;
		tempWord = "";
		sortMethod = "random";
		randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
		temporaryIcons = new ArrayList<Icon [][]>();
		int test = (int)(3*initialSquareSize*normalisedScale/5);
		System.out.println("Test: " + test);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		font2 = new Font("Century Gothic", Font.PLAIN, (int)(3*initialSquareSize*normalisedScale/5));
		font = new Font("Century Gothic", Font.PLAIN, (int)(normalisedScale*initialSquareSize / 5 * 3));
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
		for(int i = 0; i < NUMBER_OF_LAYERS; i++){
			setUpIcons(temporaryIcons);
			setUpLetters(allLetters);
		}
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
		
		drawGrid(normalisedScale);
		
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.addMouseWheelListener(this);
		layer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		layer.getActionMap().put(SOME_ACTION, someAction);
		
		clues = new JPanel(new GridLayout(cluesAcross.size()+cluesDown.size(), 1));
		clues.setBounds(40,30,squareSize*(x-2),2*squareSize*(y-2));
		clues.setBackground(clear);
		clues.setVisible(true);
		clues.setOpaque(false);
		
		setUpClues(normalisedScale);
		
		extra.addMouseWheelListener(this);
		extra.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
		extra.getActionMap().put(SOME_ACTION, someAction);
		extra.setBackground(clear);
		extra.setVisible(true);
		extra.setOpaque(true);
		
		orderClues.setBounds(40,0,200,30);
		orderClues.setBorder(border);
		orderClues.setBackground(clear);
		orderClues.setOpaque(false);
		orderClues.setVisible(true);
		
		extra.add(orderClues, new Integer(0));
		extra.add(clues, new Integer(0));
		
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);
		main.setVisible(true);
		main.setOpaque(false);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
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
												for(JLabel[][] lab: allLetters){
													if(!buttonPushed){
														lab[a.end_y-1][a.end_x-1].setOpaque(false);
													}
													if(temporaryIcons.get(allLetters.indexOf(lab))[a.end_y-1][a.end_x-1] == null && lab[a.end_y-1][a.end_x-1].getText().equals("")){
														lab[a.end_y-1][a.end_x-1].setIcon(icons[2]);
														temporaryIcons.get(allLetters.indexOf(lab))[a.end_y-1][a.end_x-1] = icons[2];
														lab[a.end_y-1][a.end_x-1].setText(" ");
														break;
													}
												}		
												for(JLabel[][] lab: allLetters){
													if(!buttonPushed){
														lab[a.start_y-1][a.start_x-1].setOpaque(false);
													}
													if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1][a.start_x-1] == null && lab[a.start_y-1][a.start_x-1].getText().equals("")){
														lab[a.start_y-1][a.start_x-1].setIcon(icons[0]);
														temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1][a.start_x-1] = icons[0];
														lab[a.start_y-1][a.start_x-1].setText(" ");
														break;
													}
												}
												int [] t = setIncrements(a.direction);
												for(int c = 0; c < a.getWordLength()-1; c++){
													if(!(c == 0)){
														for(JLabel[][] lab: allLetters){
															if(!buttonPushed){
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setOpaque(false);
															}
															if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]] == null && lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setIcon(icons[1]);
																temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]] = icons[1];
																lab[a.start_y-1+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																break;
															}
														}
													}			
													//For all diagonal images which require images in two cells on either side of main diagonal
													if(a.isDiagonal){
														if(a.direction.equals("BLTRdiagonal")){
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]] == null && lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setIcon(icons[3]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]] = icons[3];
																	lab[a.start_y-2+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	break;
																}
															}
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x+c*t[0]] == null && lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setIcon(icons[4]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x+c*t[0]] = icons[4];
																	lab[a.start_y-1+c*t[1]][a.start_x+c*t[0]].setText(" ");
																	break;
																}
															}
														}else if(a.direction.equals("backwardsBLTRdiagonal")){
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]] == null && lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(icons[4]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]] = icons[4];
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	break;
																}
															}
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]] == null && lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setIcon(icons[3]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]] = icons[3];
																	lab[a.start_y+c*t[1]][a.start_x-2+(c-1)*t[0]].setText(" ");
																	break;
																}
															}
														}else if(a.direction.equals("diagonal")){
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]] == null && lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setIcon(icons[3]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]] = icons[3];
																	lab[a.start_y-1+c*t[1]][a.start_x-1+(c+1)*t[0]].setText(" ");
																	break;
																}
															}
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y+c*t[1]][a.start_x-1+c*t[0]] == null && lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setIcon(icons[4]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y+c*t[1]][a.start_x-1+c*t[0]] = icons[4];
																	lab[a.start_y+c*t[1]][a.start_x-1+c*t[0]].setText(" ");
																	break;
																}
															}
														}else{
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]] == null && lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].getText().equals("")){
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setIcon(icons[3]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]] = icons[3];
																	lab[a.start_y-1+c*t[1]][a.start_x-2+c*t[0]].setText(" ");
																	break;
																}
															}
															for(JLabel[][] lab: allLetters){
																if(!buttonPushed){
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setOpaque(false);
																}
																if(temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]] == null && lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].getText().equals("")){
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setIcon(icons[4]);
																	temporaryIcons.get(allLetters.indexOf(lab))[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]] = icons[4];
																	lab[a.start_y-2 + c*t[1]][a.start_x-1+c*t[0]].setText(" ");
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
//								System.out.println("counter: " + counter);
//								System.out.println("entries.size: " + entries.size());
//								System.out.println("congrat: "+congratulations);
								if(counter == entries.size() && !congratulations){
									//Broke the proper function
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
				for(int i = 0; i < 3; i++){
					for (int j = 0; j < 3; j ++){
						if(!(i == 1 && j == 1) && direction.equals(directions[i*3+j])){
							inc[0] = i-1;
							inc[1] = j-1;
						}
					}
				}
				return inc;
			}

			public void mouseEntered(MouseEvent e) {
			for(JLabel lab: allClues){
					if(e.getSource() == lab && notIn && reset){
						clues.setBackground(new Color(250,250,250,255));
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
	
	public void drawGrid(double normalised){
		layer.removeAll();
		allLayers.clear();
		layer.setBounds(squareSize,squareSize,squareSize*(x-2),squareSize*(y-2));
		layer.setPreferredSize(new Dimension(squareSize*(x),squareSize*(y)));
		layer.setMinimumSize(new Dimension(squareSize*(x),squareSize*(y+2)));
		for(int i = 0; i < NUMBER_OF_LAYERS; i++){
			JPanel transparentLayer = new JPanel(new GridLayout(x-2, y-2));
			transparentLayer = setUpLayers(allLetters.get(i), transparentLayer, i);
			allLayers.add(transparentLayer);
			layer.add(transparentLayer, new Integer(0));
		}	
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
	    		 	squareSize = (int) (normalisedScale*initialSquareSize);
	    		    font2 = new Font("Century Gothic", Font.PLAIN, (int)(3*initialSquareSize*normalisedScale/5));
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
	    		 	squareSize = (int) (normalisedScale*initialSquareSize);
	    		    font2 = new Font("Century Gothic", Font.PLAIN, (int)(3*initialSquareSize*normalisedScale/5));
	    		    main.revalidate();
	    		    drawGrid(normalisedScale);
	                System.out.println("Scale: "+scale + " Normalised: " + normalisedScale + " squareSize: " + squareSize);
	                System.out.println("mouseX: " + mouseX + " mouseY: "+ mouseY);
	               
	            }
	        }
	        
	        //else scroll like normal
	    }

	@Override
	public void eventDispatched(AWTEvent arg0) {
	}
	
}
