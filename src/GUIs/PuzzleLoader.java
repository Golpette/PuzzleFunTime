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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Sudolu.SudokuGenerator;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	//public CrosswordGenerator crossword;
	//public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetSize puz1;
	public SetDifficulty puz2;
	public SignUp signUp;
	public LogIn logIn;
	JLayeredPane layer;
	public static PuzzleLoader puzzle;
	JFrame frame;
	JPanel panel, grid;
	JButton cwd, wds, sud, signup, login;
	JLabel intro, pic;
	Font font, font2, font3;
	String user;
	ImageIcon image;
	Image newimg;
	Image img;
	
	public PuzzleLoader(String user) throws IOException {
		this.user = user;
		if(user == ""){
			frame = new JFrame("Auto Puzzle Generator");
		}else{
			frame = new JFrame("Welcome to PuzzleLoader "+ user);
		}

		frame.setMinimumSize(new Dimension(550,400));
		
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		
		intro = new JLabel("Welcome to Puzzle Solver!");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setOpaque(false);
		intro.setBounds(0, 0,frame.getWidth(),300);
		intro.setBackground(new Color(255,255,255,255));
		intro.setBorder(null);
		
		panel = new JPanel(new GridBagLayout());
		grid = new JPanel(new GridLayout(1, 3));
		cwd = new JButton("Crossword");
		cwd.setFont(font2);
		cwd.setHorizontalAlignment(SwingConstants.CENTER);
		cwd.addActionListener(this);
		wds = new JButton("WordSearch");
		wds.setFont(font2);
		wds.setHorizontalAlignment(SwingConstants.CENTER);
		wds.addActionListener(this);
		sud = new JButton("Sudoku");
		sud.setFont(font2);
		sud.setHorizontalAlignment(SwingConstants.CENTER);
		sud.addActionListener(this);
		signup = new JButton("Sign Up");
		signup.setFont(font3);
		signup.setHorizontalAlignment(SwingConstants.CENTER);
		signup.addActionListener(this);
		login = new JButton("Login");
		login.setFont(font3);
		login.setHorizontalAlignment(SwingConstants.CENTER);
		login.addActionListener(this);
		
		pic = new JLabel("");
		pic.setOpaque(false);
		pic.setBounds(0, 0, frame.getWidth(), 300);
		pic.setBackground(new Color(255,255,255,255));
		pic.setBorder(null);
		
		String path1 = "";
		
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			//Could try to allow the user to have a customisable background or backgrounds
			//they choose folder an the background is dynamically created from that location
			//could also create custom look and feels which can be selected by user
			path1 = "src/resources/1.jpg";			//lol
		}
			
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			path1 = "src\\resources\\1.jpg";
		}
	
		System.out.println(frame.getWidth());
		System.out.println(path1);
		image = new ImageIcon( path1 );  
		img = image.getImage();
		newimg = img.getScaledInstance(frame.getWidth(), frame.getHeight(), java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);
		
		pic.setIcon(image);
		//intro.setIcon(image);
		layer = new JLayeredPane();
		layer.setAlignmentX(frame.getWidth());
		layer.add(intro, new Integer(0));
		layer.add(pic, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		frame.setPreferredSize(new Dimension(550,400));
		frame.setSize(550, 400);
				
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		grid.add(cwd);
		grid.add(wds);
		grid.add(sud);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(signup, c);

		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 0;
		panel.add(login, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.ipady = (int)(frame.getHeight()*0.8);		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		panel.add(layer, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 0;
		c.gridwidth = 3;
		c.ipady = 10;
		panel.add(grid, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
		frame.setResizable(false);
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
		if(e.getSource() == cwd){
			try {
				puz1 = new SetSize("Crossword", 0);	//May add set difficulty to crosswords later
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if(e.getSource() == wds){
			try {
				puz2 = new SetDifficulty("Word Search");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if(e.getSource() == sud){
			try {
				sudoku = new SudokuGenerator(9);
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
			puzzle = new PuzzleLoader("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

