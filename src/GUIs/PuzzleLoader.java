package GUIs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.Icon;
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

import Crossword.CrosswordGenerator;
import Sudoku.SudokuGenerator;
import WordSearch.WordSearchGenerator;
import resources.SetUpImages;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SudokuGenerator sudo;
	public SetUpImages imageSetUp;
	public SignUp signUp;
	public LogIn logIn;
	JLayeredPane layer;
	@SuppressWarnings("rawtypes")
	JComboBox cwdDifficulty, wdsDifficulty, sudDifficulty;
	String [] difficulty = {"EASY", "NORMAL", "HARD", "EXPERT"};
	JSpinner spinner1, spinner2;
	SpinnerNumberModel model1;
	SpinnerNumberModel model2;
	JFrame frame;
	JPanel panel, grid;
	JButton cwd, wds, sud, signup, login;
	JLabel intro, pic;
	Font font, font2, font3;
	int cwdDiff, wdsDiff, sudDiff;
	String user;
	Icon [] icons;
	String [] images = {"crossword", "wordsearch", "sudoku"};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PuzzleLoader(String user) throws IOException {
		this.user = user;
		if(user == ""){
			frame = new JFrame("Auto Puzzle Generator");
		}else{
			frame = new JFrame("Welcome to PuzzleLoader "+ user);
		}

		frame.setMinimumSize(new Dimension(550,400));
		
		font = new Font("Century Gothic", Font.BOLD, 40);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		
		intro = new JLabel();
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setOpaque(false);
		intro.setBounds(0, 0,frame.getWidth(),300);
		intro.setBackground(new Color(255,255,255,255));
		intro.setBorder(null);
		
		cwdDifficulty = new JComboBox(difficulty);
		cwdDifficulty.addActionListener(this);
		cwdDifficulty.setFont(font3);
		
		wdsDifficulty = new JComboBox(difficulty);
		wdsDifficulty.addActionListener(this);
		wdsDifficulty.setFont(font3);
		
		sudDifficulty = new JComboBox(difficulty);
		sudDifficulty.addActionListener(this);
		sudDifficulty.setFont(font3);
				
		model1 = new SpinnerNumberModel(12, 4, 30, 1);
		spinner1 = new JSpinner(model1);
		spinner1.setForeground(Color.WHITE);
		spinner1.setEditor(new JSpinner.DefaultEditor(spinner1));
		spinner1.setFont(font2);
		
		model2 = new SpinnerNumberModel(10, 3, 50, 1);
		spinner2 = new JSpinner(model2);
		spinner2.setForeground(Color.WHITE);
		spinner2.setEditor(new JSpinner.DefaultEditor(spinner2));
		spinner2.setFont(font2);
		
		panel = new JPanel(new GridBagLayout());
		panel.setBackground(new Color(220,220,250,255));
		grid = new JPanel(new GridLayout(3, 1));
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
//		signup = new JButton("Sign Up");
//		signup.setFont(font3);
//		signup.setHorizontalAlignment(SwingConstants.CENTER);
//		signup.addActionListener(this);
//		login = new JButton("Login");
//		login.setFont(font3);
//		login.setHorizontalAlignment(SwingConstants.CENTER);
//		login.addActionListener(this);
		
		pic = new JLabel("");
		pic.setOpaque(false);
		pic.setBounds(0, 0, frame.getWidth(), 300);
		pic.setBackground(new Color(255,255,255,255));
		pic.setBorder(null);
		
		icons = new Icon[3];
		imageSetUp = new SetUpImages(images, 100, 100, icons);
		cwd.setIcon(icons[0]);
		wds.setIcon(icons[1]);
		sud.setIcon(icons[2]);
		layer = new JLayeredPane();
		layer.setAlignmentX(frame.getWidth());
		//layer.add(intro, new Integer(0));
		//layer.add(pic, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		frame.setPreferredSize(new Dimension(550,400));
		frame.setSize(550, 400);
				
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		grid.add(cwd);
		grid.add(wds);
		grid.add(sud);
		
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 3;
		panel.add(grid, c);
		
		c.fill = GridBagConstraints.BOTH;	
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.ipady = 5;
		c.insets = new Insets(40,30,40,15);
		panel.add(spinner1, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.gridheight = 1;
		c.insets = new Insets(45,30,40,15);
		panel.add(spinner2, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(40,15,40,30);
		panel.add(cwdDifficulty, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(45,15,40,30);
		panel.add(wdsDifficulty, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 2;
		c.insets = new Insets(42,30,40,30);
		panel.add(sudDifficulty, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		frame.setResizable(false);
	}

	private int getDifficulty(int index) {
		return (int) Math.pow(2, index+1);
	}

	void mouseActionlabel(JButton b) {
		b.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				
			}

			public void mouseEntered(MouseEvent e) {
				
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
		if(e.getSource() == cwdDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox cwdDifficulty = (JComboBox)e.getSource();
			String msg = (String)cwdDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				cwdDiff = 2;
			}
			if(msg.equals("NORMAL")){
				cwdDiff = 4;
			}
			if(msg.equals("HARD")){
				cwdDiff = 8;
			}
			if(msg.equals("EXPERT")){
				cwdDiff = 16;
			}
		}
		if(e.getSource() == wdsDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox wdsDifficulty = (JComboBox)e.getSource();
			String msg = (String)wdsDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				wdsDiff = 2;
			}
			if(msg.equals("NORMAL")){
				wdsDiff = 4;
			}
			if(msg.equals("HARD")){
				wdsDiff = 8;
			}
			if(msg.equals("EXPERT")){
				wdsDiff = 16;
			}
		}
		if(e.getSource() == sudDifficulty){
			@SuppressWarnings("rawtypes")
			JComboBox sudDifficulty = (JComboBox)e.getSource();
			String msg = (String)sudDifficulty.getSelectedItem();
			if(msg.equals("EASY")){
				sudDiff = 2;
			}
			if(msg.equals("NORMAL")){
				sudDiff = 4;
			}
			if(msg.equals("HARD")){
				sudDiff = 8;
			}
			if(msg.equals("EXPERT")){
				sudDiff = 16;
			}
		}
		
		if(e.getSource() == cwd){
			try {
				cwdDiff = getDifficulty(cwdDifficulty.getSelectedIndex());
				crossword = new CrosswordGenerator((Integer)spinner1.getValue());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
		}
		if(e.getSource() == wds){
			try {
				//System.out.println("wdsDiff: " + wdsDiff);
				wordsearch = new WordSearchGenerator((Integer)spinner2.getValue(), wdsDiff);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
		}
		if(e.getSource() == sud){
			try {
				sudDiff = getDifficulty(cwdDifficulty.getSelectedIndex());
				sudo = new SudokuGenerator(9);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//frame.dispose();
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

