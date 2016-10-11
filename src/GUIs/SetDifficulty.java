package GUIs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Crossword.CrosswordGenerator;
import WordSearch.WordSearchGenerator;

public class SetDifficulty extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public WordSearchGenerator wordsearch;
	public CrosswordGenerator crossword;
	public PuzzleLoader puzzleLoader;
	JFrame frame;
	JPanel panel, panel1, panel2, grid;
	JButton generate, easy, medium, hard, expert;
	JButton back;
	ButtonModel blah;
	JLayeredPane layer;
	JLabel chooseDifficulty, chooseDifficulty2, pic;
	SetSize size;
	SpinnerNumberModel model;
	Font font, font2, font3;
	String puzzle;
	ImageIcon image, image2, pic1, pic2;
	Image newimg;
	Image img;
	int difficulty = 4;
	
	public SetDifficulty(String puzzle) throws IOException {
		this.puzzle = puzzle;
		font = new Font("Century Gothic", Font.BOLD, 40);
		font3 = new Font("Century Gothic", Font.BOLD, 48);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		panel = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel1.setOpaque(false);
		panel2 = new JPanel(new GridBagLayout());
		panel2.setOpaque(false);
		grid = new JPanel(new GridLayout(1,4));
		frame = new JFrame("Set Puzzle Difficulty");
		frame.setSize(550, 400);
		panel1.setBounds(0, 0, frame.getWidth(), 320);
		panel2.setBounds(0, 0, frame.getWidth(), 320);
		generate = new JButton("OK");
		generate.setFont(font2);
		generate.setHorizontalAlignment(SwingConstants.CENTER);
		generate.addActionListener(this);
		easy = new JButton("Easy");
		easy.setFont(font2);
		easy.addActionListener(this);
		medium = new JButton("Normal");
		medium.setFont(font2);
		medium.addActionListener(this);
		hard = new JButton("Hard");
		hard.setFont(font2);
		hard.addActionListener(this);
		expert = new JButton("Expert");
		expert.setFont(font2);
		expert.addActionListener(this);
		
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
		String path3 = "";
		String path4 = "";
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			path1 = "src/resources/back.png";
			path2 = "src/resources/back1.png";
			path3 = "src/resources/crossword.png";
			path4 = "src/resources/wordsearch.png";
		}
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			path1 = "src\\resources\\back.png";
			path2 = "src\\resources\\back1.png";
			path3 = "src\\resources\\crossword.png";
			path4 = "src\\resources\\wordsearch.png";
		}
		
		pic = new JLabel("");
		pic.setOpaque(false);
		pic.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		pic.setBackground(new Color(255,255,255,255));
		pic.setBorder(null);
		
		grid.add(easy);
		grid.add(medium);
		grid.add(hard);
		grid.add(expert);
		
		image = new ImageIcon( path1 );  
		img = image.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);
		image2 = new ImageIcon( path2 );

		img = image2.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image2 = new ImageIcon(newimg);
		
		pic1 = new ImageIcon(path3);
		img = pic1.getImage();
		newimg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH ) ; 
		pic1 = new ImageIcon(newimg);
		
		pic2 = new ImageIcon(path4);
		img = pic2.getImage();
		newimg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH ) ; 
		pic2 = new ImageIcon(newimg);
		
		if(puzzle.equals("Crossword")){
			pic.setIcon(pic1);
		}else if(puzzle.equals("Word Search")){
			pic.setIcon(pic2);
		}
		
		back = new JButton("");
		back.setFont(font2);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		mouseActionlabel(back);
		keyActionTextField(back);
		back.addActionListener(this);
		back.setIcon(image);
		back.setOpaque(false);
		
		back.setBounds(0, 0, 100, 100);
		back.setBackground(new Color(255,255,255,255));
		back.setBorder(null);
		
		frame.setPreferredSize(new Dimension(550,400));
		model = new SpinnerNumberModel(12, 3, 30, 1);
		chooseDifficulty = new JLabel(puzzle + " Difficulty");
		chooseDifficulty.setFont(font);
		chooseDifficulty.setForeground(Color.BLACK);
		chooseDifficulty.setHorizontalAlignment(SwingConstants.CENTER);
//		chooseDifficulty2 = new JLabel(puzzle + " Difficulty");
//		chooseDifficulty2.setFont(font3);
//		chooseDifficulty2.setForeground(Color.BLACK);
//		chooseDifficulty2.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(0,0,0,0);
		panel1.add(chooseDifficulty, c);
		
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = 1.0;
//		c.weighty = 1.0;
//		c.gridx = 0;
//		c.gridy = 2;
//		c.gridwidth = 4;
//		c.insets = new Insets(0,0,0,0);
//		panel2.add(chooseDifficulty2, c);
		
		layer = new JLayeredPane();
		layer.add(panel1, new Integer(0));
		//layer.add(panel2, new Integer(0));		//adding this makes it text look a bit like shadowed text but it doesn't work really as required
		layer.add(back, new Integer(0));
		layer.add(pic, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		//c.ipady = (int)(frame.getHeight()*0.8);
		panel.add(layer, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		c.gridwidth = 4;
		panel.add(grid, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(550,400));
		frame.setVisible(true);		
		frame.getRootPane().setDefaultButton(generate);
		frame.setResizable(false);
	}

	void keyActionTextField(JButton l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if(e.getSource().equals(KeyEvent.VK_BACK_SPACE)){
					//actionPerformed();
					
					try {
						puzzleLoader = new PuzzleLoader("");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					frame.dispose();	
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {

			}
		});
	}

	void mouseActionlabel(JButton b) {
		b.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				back.setIcon(image2);
			}

			public void mouseExited(MouseEvent e) {
				back.setIcon(image);
			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}
		});
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == back){
			try {
				puzzleLoader = new PuzzleLoader("");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();			
		}
		if(e.getSource() == easy){
			difficulty = 2;		
			try {
				size = new SetSize(puzzle, difficulty);
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		frame.dispose();
		}
		if(e.getSource() == medium){
			difficulty = 4;
			try {
				size = new SetSize(puzzle, difficulty);
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		frame.dispose();
		}
		if(e.getSource() == hard){
			difficulty = 8;	
			try {
				size = new SetSize(puzzle, difficulty);
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		frame.dispose();
		}
		if(e.getSource() == expert){
			//difficulty = 9;				//set to 9 to include snakes
			difficulty = 8;
			try {
				size = new SetSize(puzzle, difficulty);
			}catch (IOException e1) {
				e1.printStackTrace();
			}
		frame.dispose();
		}
	}
}
