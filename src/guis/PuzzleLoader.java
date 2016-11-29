package guis;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import crossword.CrosswordGenerator;
import crossword.SetUpImages;
import sudoku.SudokuGenerator;
import wordsearch.WordSearchGenerator;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public crossword.CrosswordGenerator crossword;
	public wordsearch.WordSearchGenerator wordsearch;
	public sudoku.SudokuGenerator sudoku;
	public SetUpImages imageSetUp, imageSetUp2, imageSetUp3, imageSetUp4, imageSetUp5;
	public SignUp signUp;
	public LogIn logIn;
	JLayeredPane layer2;
	@SuppressWarnings("rawtypes")
	SpinnerNumberModel model1, model2, model3;
	JFrame frame;
	JLayeredPane layer;
	JPanel panel, panel2, grid, grid2, country, country2;
	JButton cwd, wds, sud, signup, login;
	JLabel icon;
	JLabel intro, intro2, pic, flag, flag2, arrow, clueLanguage, solutionLanguage;
	Font font, font2, font3, font4, font5;
	int cwdDiff=2; 
	int wdsDiff=2; 
	int sudDiff=2;
	String user;
	Icon [] icons;
	String [] images = {"crossword", "wordsearch", "sudoku", "Logo5"};
	Icon[] logo;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PuzzleLoader(String user) throws IOException {
		this.user = user;
		if(user == ""){
			frame = new JFrame("Auto Puzzle Generator");
		}else{
			frame = new JFrame("Welcome to PuzzleLoader "+ user);
		}

		frame.setMinimumSize(new Dimension(500,350));
		
		font = new Font("Century Gothic", Font.BOLD, 40);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		font3 = new Font("Century Gothic", Font.PLAIN, 16);
		font5 = new Font("Agency FB", Font.BOLD, 90);
		
		
		intro = new JLabel("SUD");
		intro.setFont(font5);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setVerticalAlignment(SwingConstants.CENTER);
		intro.setOpaque(true);
		intro.setBackground(Color.BLACK);
		intro.setForeground(Color.WHITE);
		
		intro2 = new JLabel(" WORDS");
		intro2.setFont(font5);
		intro2.setHorizontalAlignment(SwingConstants.CENTER);
		intro2.setVerticalAlignment(SwingConstants.CENTER);
		intro2.setOpaque(true);
		intro2.setBackground(Color.BLACK);
		intro2.setForeground(Color.WHITE);
		
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(220,220,250,255));
		panel.setBounds(0,0,frame.getWidth()-15,frame.getHeight()-37);
		panel.setOpaque(false); 
		
		grid = new JPanel(new GridLayout(1, 3));
		grid2 = new JPanel(new GridBagLayout());
		cwd = new JButton();
		cwd.setFont(font2);
		cwd.setHorizontalAlignment(SwingConstants.CENTER);
		cwd.addActionListener(this);
		wds = new JButton();
		wds.setFont(font2);
		wds.setHorizontalAlignment(SwingConstants.CENTER);
		wds.addActionListener(this);
		sud = new JButton();
		sud.setFont(font2);
		sud.setHorizontalAlignment(SwingConstants.CENTER);
		sud.addActionListener(this);
		icon = new JLabel();
		icon.setHorizontalAlignment(SwingConstants.CENTER);
		icon.setVerticalAlignment(SwingConstants.CENTER);
		icon.setBackground(Color.BLACK);
		
		icons = new Icon[4];
		logo = new Icon[4];
		imageSetUp = new SetUpImages(images, 100, 100, icons, 0);
		imageSetUp5 = new SetUpImages(images, 75, 50, logo, 0);
		
		cwd.setIcon(icons[0]);
		wds.setIcon(icons[1]);
		sud.setIcon(icons[2]);
		sud.setHorizontalAlignment(SwingConstants.CENTER);
		sud.setVerticalAlignment(SwingConstants.CENTER);
		wds.setHorizontalAlignment(SwingConstants.CENTER);
		wds.setVerticalAlignment(SwingConstants.CENTER);
		cwd.setHorizontalAlignment(SwingConstants.CENTER);
		cwd.setVerticalAlignment(SwingConstants.CENTER);
		icon.setIcon(logo[3]);
		icon.setVisible(true);
		icon.setOpaque(true);
		icon.setAlignmentX(BOTTOM_ALIGNMENT);
		
		frame.setPreferredSize(new Dimension(440,228));
		frame.setSize(440, 200);
				
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		grid.add(cwd);
		grid.add(wds);
		grid.add(sud);
		
		grid2.setBackground(Color.BLACK);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		grid2.add(intro, c);
		
		c.gridx = 1;
		c.gridy = 0;
		grid2.add(icon, c);
		
		c.gridx = 2;
		c.gridy = 0;
		grid2.add(intro2, c);		
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(grid2, c);	
		
		c.gridx = 0;
		c.gridy = 1;
		panel.add(grid, c);
		
		layer = new JLayeredPane();
		layer.setAlignmentX(frame.getWidth());
		layer.setBounds(0, 0, frame.getWidth(), frame.getHeight());
		layer.add(panel, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		frame.setContentPane(layer);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == cwd){
			try {
				crossword = new CrosswordGenerator(15, 2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
		if(e.getSource() == wds){
			try {
				wordsearch = new WordSearchGenerator(15, 2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(e.getSource() == sud){
			try {
				sudoku = new SudokuGenerator(9, 2);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == signup){
			try {
				signUp = new SignUp();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if (e.getSource() == login){
			try {
				logIn = new LogIn();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
		}
	}
	
	public static void main (String [] args){
		try {
			new PuzzleLoader("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

