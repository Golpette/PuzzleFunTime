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

public class SetDifficulty extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public WordSearchGenerator wordsearch;
	public CrosswordGenerator crossword;
	public PuzzleLoader puzzleLoader;
	JFrame frame;
	JPanel panel, panel1, grid;
	JButton generate, easy, medium, hard, expert;
	JButton back;
	ButtonModel blah;
	JLayeredPane layer;
	JLabel chooseDifficulty;
	SetSize size;
	SpinnerNumberModel model;
	Font font, font2;
	String puzzle;
	ImageIcon image, image2;
	Image newimg;
	Image img;
	int difficulty = 4;
	
	public SetDifficulty(String puzzle) throws IOException {
		this.puzzle = puzzle;
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		panel = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel1.setOpaque(false);
		grid = new JPanel(new GridLayout(1,4));
		frame = new JFrame("Set Puzzle Difficulty");
		frame.setSize(550, 400);
		panel1.setBounds(0, 0, frame.getWidth(), 320);
		//panel1.setAlignmentX(SwingConstants.CENTER);
		generate = new JButton("OK");
		generate.setFont(font2);
		generate.setHorizontalAlignment(SwingConstants.CENTER);
		generate.addActionListener(this);
		easy = new JButton("Easy");
		easy.setFont(font2);
		//easy.setHorizontalAlignment(SwingConstants.CENTER);
		easy.addActionListener(this);
		medium = new JButton("Normal");
		medium.setFont(font2);
		//medium.setHorizontalAlignment(SwingConstants.CENTER);
		medium.addActionListener(this);
		hard = new JButton("Hard");
		hard.setFont(font2);
		//hard.setHorizontalAlignment(SwingConstants.CENTER);
		hard.addActionListener(this);
		expert = new JButton("Expert");
		expert.setFont(font2);
		//expert.setHorizontalAlignment(SwingConstants.CENTER);
		expert.addActionListener(this);
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
		//System.out.println(System.getProperty("os.name").toLowerCase());
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			
			path1 = "src/back.png";
			path2 = "src/back1.png";
		}
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			path1 = "src\\back.png";
			path2 = "src\\back1.png";
		}
		
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
		back = new JButton("");
		back.setFont(font2);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		mouseActionlabel(back);
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
		chooseDifficulty.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 4;
		c.insets = new Insets(0,0,0,0);
		panel1.add(chooseDifficulty, c);
		
		layer = new JLayeredPane();
		layer.add(panel1, new Integer(1));
		layer.add(back, new Integer(0));
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
		
//		c.fill = GridBagConstraints.BOTH;
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 0;
//		c.gridy = 1;
//		c.ipady = 10;
//		c.gridwidth = 1;
//		panel.add(easy, c);
//		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 1;
//		c.gridy = 1;
//		c.ipady = 10;
//		c.gridwidth = 1;
//		panel.add(medium, c);
//		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 2;
//		c.gridy = 1;
//		c.ipady = 10;
//		c.gridwidth = 1;
//		panel.add(hard, c);
//		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 3;
//		c.gridy = 1;
//		c.ipady = 10;
//		c.gridwidth = 1;
//		panel.add(expert, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(550,400));
		frame.setVisible(true);		
		frame.getRootPane().setDefaultButton(generate);
	}

	void keyActionTextField(JTextField l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
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
