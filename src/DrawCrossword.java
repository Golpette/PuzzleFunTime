import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;

/**
 * Class to take String[][] grid and paint the crossword as it should look
 * complete with all the required components: The clues, grid, clue numbers,
 * solution button, hints etc
 */
public class DrawCrossword extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static int squareSize = 30;
	private String[][] grid;
	private JTextField[][] boxes;
	int x, y, frameSizeX, frameSizeY;
	JPanel panel, crosswordGrid, clue, clueNums, main;
	JLayeredPane layer;
	JScrollPane area;
	JTextArea text;
	JButton reveal;
	JLabel[][] clueNumbers;
	ArrayList<JLabel> cluesDwn, cluesAcr, hints;
	GridBagConstraints c;
	ArrayList<JLabel> nums;
	ArrayList<Entry> entries;
	DrawSolution sol;
	Font font, font2, font3;
	Random rand;
	Border border;
	Color clear, red, green, blue, black;
	JLabel hintD, hintA;
	ArrayList<KeyEvent> keys;
	Action action;

	public DrawCrossword(String[][] gridInit, String[][] grid, int x, int y, ArrayList<String> cluesAcross,
			ArrayList<String> cluesDown, ArrayList<Entry> entries) throws IOException {
		frameSizeX = 2 * (x + 1) * squareSize;
		frameSizeY = (y + 4) * squareSize;

		JFrame frame = new JFrame("Auto Crossword");
		frame.setSize(1000, 400);
		frame.setPreferredSize(new Dimension(frameSizeX, frameSizeY));
		frame.setBackground(new Color(255, 255, 255, 255));

		keys = new ArrayList<KeyEvent>();

		cluesDwn = new ArrayList<JLabel>();
		cluesAcr = new ArrayList<JLabel>();
		clear = new Color(255, 255, 255, 255);
		red = new Color(255, 0, 0, 255);
		green = new Color(0, 255, 0, 255);
		blue = new Color(0, 0, 255, 255);
		black = new Color(0, 0, 0, 255);

		this.grid = grid;
		this.x = x;
		this.y = y;
		this.entries = entries;

		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		font2 = new Font("Times New Roman", Font.PLAIN, 24);
		font3 = new Font("Times New Roman", Font.PLAIN, 20);
		sol = new DrawSolution(grid, x, y, squareSize, "Crossword");
		rand = new Random();

		/**
		 * This is where the transparentLayer to hold all the clue numbers is
		 * created. It sets all the cells with question numbers with the correct
		 * number in the top left corner of a GridLayout cell.
		 */
		clueNums = new JPanel(new GridLayout(x - 2, y - 2));
		clueNums.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		clueNums.setOpaque(false);
		clueNumbers = new JLabel[x - 2][y - 2];

		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				clueNumbers[i][j] = new JLabel();
				clueNumbers[i][j].setBackground(new Color(255, 255, 255, 255));
				clueNumbers[i][j].setForeground(Color.BLACK);
				clueNumbers[i][j].setVisible(true);
				clueNumbers[i][j].setOpaque(false);
				if (!gridInit[j + 1][i + 1].equals("_")) {
					clueNumbers[i][j].setText(gridInit[j + 1][i + 1]);
				}
				clueNumbers[i][j].setVerticalAlignment(JTextField.TOP);
				clueNums.add(clueNumbers[i][j]);
			}
		}

		/**
		 * This is where all the crossword boxes are filled black or provide a
		 * useable JTextfield. This is layered on top of the transparentLayer
		 */
		crosswordGrid = new JPanel(new GridLayout(x - 2, y - 2));
		crosswordGrid.setBounds(squareSize, squareSize, squareSize * (x - 2), squareSize * (y - 2));
		crosswordGrid.setOpaque(false);
		boxes = new JTextField[x - 2][y - 2];
		border = BorderFactory.createLineBorder(Color.BLACK);

		for (int i = 0; i < x - 2; i++) {
			for (int j = 0; j < y - 2; j++) {
				boxes[i][j] = new JTextField(); // need new layout to resize
												// letters in boxes
				action = boxes[i][j].getActionMap().get(DefaultEditorKit.beepAction);
				action.setEnabled(false);
				boxes[i][j].setFont(new Font("Times New Roman", Font.BOLD, 20));
				boxes[i][j].setBorder(border);
				boxes[i][j].setDocument(new JTextFieldLimit(1));
				if (grid[j + 1][i + 1] == "_") {
					boxes[i][j].setBackground(new Color(0, 0, 0, 255));
					boxes[i][j].setEnabled(false);
				} else {
					boxes[i][j].setBackground(new Color(255, 255, 255, 100));
					boxes[i][j].setOpaque(false);
					keyActionTextField(boxes[i][j]);
				}
				boxes[i][j].setHorizontalAlignment(JTextField.CENTER);
				boxes[i][j].setFont(font3);

				crosswordGrid.add(boxes[i][j]);
				crosswordGrid.setMaximumSize(new Dimension(400, 300));
				crosswordGrid.setMinimumSize(new Dimension(100, 100));
			}
		}

		/**
		 * This is the JLayeredPane layer which holds the actual crossword. It
		 * is composed of two layers crosswordGrid and clueNums which are both
		 * GridLayout JPanels which are layered one on top of the other.
		 */
		layer = new JLayeredPane();
		layer.setBackground(new Color(255, 255, 255, 255));
		layer.add(clueNums, new Integer(0));
		layer.add(crosswordGrid, new Integer(1));
		layer.setVisible(true);
		layer.setOpaque(true);
		layer.setPreferredSize(new Dimension(squareSize * (x), squareSize * (y)));
		layer.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));

		/**
		 * This is the GridBagLayout clue which holds all the clue components:
		 * The numbers and clues in a JTextArea and the hints in a JLabel
		 */
		clue = new JPanel(new GridBagLayout());
		clue.setMinimumSize(new Dimension(squareSize * (x - 1), squareSize * (x - 1)));
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		clue.setBackground(clear);
		clue.setAlignmentY(0);
		clue.setBounds(300, 300, 200, 200);
		hints = new ArrayList<JLabel>();

		JLabel first = new JLabel("Across");
		first.setFont(font2);
		cluesAcr.add(first);
		for (String s : cluesAcross) {
			JLabel across = new JLabel(s);
			hintA = new JLabel(" ");
			hintA.setForeground(Color.GREEN);
			hints.add(hintA);
			mouseActionlabel(across);
			mouseActionlabel(hintA);
			cluesAcr.add(across);
			cluesAcr.add(hintA);
		}

		JLabel second = new JLabel("Down\n");
		second.setFont(font2);
		cluesDwn.add(second);
		for (String s : cluesDown) {
			JLabel down = new JLabel(s);
			hintD = new JLabel(" ");
			hintD.setForeground(Color.GREEN);
			hints.add(hintD);
			mouseActionlabel(down);
			mouseActionlabel(hintD);
			cluesDwn.add(down);
			cluesDwn.add(hintD);
		}

		for (JLabel j : cluesAcr) {
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridx = 0;
			clue.add(j, c);
		}

		for (JLabel k : cluesDwn) {
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.gridx = 0;
			clue.add(k, c);
		}

		/**
		 * This is the layout of the GridBagLayout panel main which holds all
		 * the crossword components. There are two components inside it: A
		 * JLayeredPane and a GridBagLayout
		 */
		main = new JPanel(new GridBagLayout());
		main.setBackground(clear);

		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		main.add(layer, c);

		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 1;
		c.gridy = 0;
		main.add(clue, c);

		/**
		 * This is the largest area of the GUI which holds the crossword and
		 * clues pane and makes them scrollable.
		 */
		area = new JScrollPane(main, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.getVerticalScrollBar().setUnitIncrement(8);
		area.getHorizontalScrollBar().setUnitIncrement(8);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(clear);

		/**
		 * This is the button which generates a solution for the given crossword
		 * bringing up a new GUI instance with the filled in grid on being
		 * pressed.
		 */
		reveal = new JButton("Show Solution");
		reveal.setFont(font2);
		reveal.addActionListener(this);

		/**
		 * This is the panel for the main area of the program. It holds two
		 * components: A JScrollPane and a JButton
		 */
		panel = new JPanel(new GridBagLayout());

		c.weighty = 1.0;
		c.ipadx = 1;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(area, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(reveal, c);

		/**
		 * Overall JFrame to hold all components This has the main panel
		 * assigned to it
		 */

		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	void keyActionTextField(JTextField l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				for (int row = 0; row < x - 2; row++) {
					for (int col = 0; col < y - 2; col++) {
						if (e.getSource() == boxes[row][col]) {
							if (e.getKeyCode() == KeyEvent.VK_UP) {
								System.out.println("Pressed Up");
								if (row > 0) {
									if (boxes[row - 1][col].isEnabled()) {
										boxes[row - 1][col].requestFocus();
									}
								}
							}
							if (e.getKeyCode() == KeyEvent.VK_DOWN) {
								System.out.println("Pressed Down");
								if (row < x - 3) {
									if (boxes[row + 1][col].isEnabled()) {
										boxes[row + 1][col].requestFocus();
									}
								}
							}
							if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
								System.out.println("Pressed Right");
								if (col < y - 3) {
									if (boxes[row][col + 1].isEnabled()) {
										boxes[row][col + 1].requestFocus();
									}
								}
							}
							if (e.getKeyCode() == KeyEvent.VK_LEFT) {
								System.out.println("Pressed Left");
								if (col > 0) {
									if (boxes[row][col - 1].isEnabled()) {
										boxes[row][col - 1].requestFocus();
									}
								}
							}
							if (65 <= e.getKeyCode() && e.getKeyCode() <= 90) {
								boxes[row][col].setForeground(black);
								boxes[row][col].setText(Character.toString(e.getKeyChar()));
								System.out.println("Keycode: " + Character.toString(e.getKeyChar()));
								if(row > 0){
									
								}
								if (col < x - 3 && row < y - 3) {
									if (boxes[row][col + 1].isEnabled()) {
										if (boxes[row][col + 1].getText().equals("")) {
											boxes[row][col + 1].setText("");
										}
										boxes[row][col + 1].requestFocus();
									} else if (boxes[row + 1][col].isEnabled()) {
										if (boxes[row + 1][col].getText().equals("")) {
											boxes[row + 1][col].setText("");
										}
										boxes[row + 1][col].requestFocus();
									}
								} else if (col < x - 3) {
									if (boxes[row][col + 1].isEnabled()) {
										if (boxes[row][col + 1].getText().equals("")) {
											boxes[row][col + 1].setText("");
										}
										boxes[row][col + 1].requestFocus();
									}
								} else if (row < y - 3) {
									if (boxes[row + 1][col].isEnabled()) {
										if (boxes[row + 1][col].getText().equals("")) {
											boxes[row + 1][col].setText("");
										}
										boxes[row + 1][col].requestFocus();
									}
								}else{
									for (int steps = 1; steps <= row * (x - 2) + col; steps++) {
										if (boxes[((row*(x-2) + col)+steps) / (x-2)][((row*(x-2) + col)+steps) % (x-2)].isEnabled()) {
											boxes[((row*(x-2) + col)+steps) / (x-2)][((row*(x-2) + col)+steps) % (x-2)].requestFocus();
											break;
										}
									}
								}
							}

							if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
								System.out.println("Pressed Backspace");
								boolean stuck = true;
								if (row > 0) {
									if (boxes[row - 1][col].isEnabled()) {
										stuck = false;
									}
								}
								if (col > 0) {
									if (boxes[row][col - 1].isEnabled()) {
										stuck = false;
									}
								}
								boxes[row][col].setText("");
								if (col > 0 && row > 0 && !stuck) {
									if (boxes[row][col - 1].isEnabled()
											&& !boxes[row][col - 1].getText().equals("")) {
										boxes[row][col - 1].requestFocus();
									} else if (boxes[row - 1][col].isEnabled()
											&& !boxes[row - 1][col].getText().equals("")) {
										boxes[row - 1][col].requestFocus();
									} else if (boxes[row][col - 1].isEnabled()) {
										boxes[row][col - 1].requestFocus();
									} else if (boxes[row - 1][col].isEnabled()) {
										boxes[row - 1][col].requestFocus();
									}
								} else if (col > 0 && !stuck) {
									if (boxes[row][col - 1].isEnabled()
											&& !boxes[row][col - 1].getText().equals("")) {
										boxes[row][col - 1].requestFocus();
									} else if (boxes[row][col - 1].isEnabled()) {
										boxes[row][col - 1].requestFocus();
									}
								} else if (row > 0 && !stuck) {
									if (boxes[row - 1][col].isEnabled()
											&& !boxes[row - 1][col].getText().equals("")) {
										boxes[row - 1][col].requestFocus();
									} else if (boxes[row - 1][col].isEnabled()) {
										boxes[row - 1][col].requestFocus();
									}
								} else {
									if (!(row == 0 && col == 0) && stuck) {
										System.out.println("Stuck");
										System.out.println("i = " + row + " j = " + col);

										for (int stepsBack = 1; stepsBack <= row * (x - 2) + col; stepsBack++) {
											if (boxes[((row * (x - 2) + col) - stepsBack) / (x - 2)][((row * (x - 2) + col) - stepsBack) % (x - 2)].isEnabled()) {
												//if (!boxes[((row * (rowWidth - 2) + column) - stepsBack) / (rowWidth - 2)][((row * (rowWidth - 2) + column) - stepsBack) % (rowWidth - 2)].getText().equals("")) {
													boxes[((row * (x - 2) + col) - stepsBack) / (x - 2)][((row * (x - 2) + col) - stepsBack) % (x - 2)].requestFocus();
												//}
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.equals(KeyEvent.VK_UP)) {

				}
				if (e.equals(KeyEvent.VK_DOWN)) {

				}
				if (e.equals(KeyEvent.VK_RIGHT)) {

				}
				if (e.equals(KeyEvent.VK_LEFT)) {

				}
			}

			public void keyTyped(KeyEvent e) {

			}
		});
	}

	void mouseActionlabel(JLabel l) {
		l.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				for (JLabel k : hints) {
					if (e.getSource() == k) {
						for (Entry ent : entries) {
							if (ent.isAcross()) {
								if (ent.getEntryAcross() == hints.indexOf(k)) {
									String str = shuffleString(ent.word).toUpperCase();
									k.setText("     " + shuffleString(ent.word).toUpperCase());
								}
							} else {
								if (ent.getEntryDown() == hints.indexOf(k) - (cluesAcr.size() / 2)) {
									k.setText("     " + shuffleString(ent.word).toUpperCase());
								}
							}
						}
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				for (JLabel i : hints) {
					if (e.getSource() == i) {
						i.setText("     HINT");
					}
				}
				for (JLabel j : cluesAcr) {
					if (e.getSource() == j) {
						if (!hints.contains(j)) {
							cluesAcr.get(cluesAcr.indexOf(j) + 1).setText("     HINT");
						}
					}
				}
				for (JLabel k : cluesDwn) {
					if (e.getSource() == k) {
						if (!hints.contains(k)) {
							cluesDwn.get(cluesDwn.indexOf(k) + 1).setText("     HINT");
						}
					}
				}
			}

			public void mouseExited(MouseEvent e) {

				for (JLabel i : hints) {
					if (e.getSource() == i) {
						i.setText(" ");
					}
				}
				for (JLabel j : cluesAcr) {
					if (e.getSource() == j) {
						if (!hints.contains(j)) {
							cluesAcr.get(cluesAcr.indexOf(j) + 1).setText(" ");
						}
					}
				}
				for (JLabel k : cluesDwn) {
					if (e.getSource() == k) {
						if (!hints.contains(k)) {
							cluesDwn.get(cluesDwn.indexOf(k) + 1).setText(" ");
						}
					}
				}
			}

			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {

			}
		});
	}

	public String shuffleString(String string) {
		ArrayList<Character> letters = new ArrayList<Character>();
		letters.clear();
		StringBuilder str = new StringBuilder(string.length());
		for (Character c : string.toCharArray()) {
			letters.add(c);
		}
		while (letters.size() >= 1) {
			char temp = letters.remove(rand.nextInt(letters.size()));
			str.append(temp);
		}
		return str.toString();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reveal) {
			sol.frame.setVisible(!sol.frame.isVisible());
			if (sol.frame.isVisible()) {
				reveal.setText("Hide Solution");
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						if (boxes[i][j].getText().equals("")) {
							boxes[i][j].setForeground(black);
						} else if (boxes[i][j].getText().toLowerCase().equals(grid[j + 1][i + 1])) {
							boxes[i][j].setForeground(green);
						} else {
							boxes[i][j].setForeground(red);
						}
					}
				}
			} else {
				reveal.setText("Show Solution");
				for (int i = 0; i < x - 2; i++) {
					for (int j = 0; j < y - 2; j++) {
						boxes[i][j].setForeground(new Color(0, 0, 0, 255));
					}
				}
			}
		}
	}
}
