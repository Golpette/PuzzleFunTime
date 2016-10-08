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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	//public CrosswordGenerator crossword;
	//public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetSize puz1;
	public SetDifficulty puz2;
	public SignUp signUp;
	public LogIn logIn;
	public static PuzzleLoader puzzle;
	JFrame frame;
	JPanel panel, grid;
	JButton cwd, wds, sud, signup, login;
	JLabel intro;
	Font font, font2, font3;
	String user;
	
	public PuzzleLoader(String user) throws IOException {
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		intro = new JLabel("Welcome to Puzzle Solver!");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
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
		this.user = user;
		
		if(user == ""){
			frame = new JFrame("Auto Puzzle Generator");
		}else{
			frame = new JFrame("Welcome to PuzzleLoader "+ user);
		}
		
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
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		//c.ipady = (int)(frame.getHeight()*0.8);
		c.gridwidth = 3;
		panel.add(intro, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 0;
		c.gridwidth = 3;
		c.ipady = 10;
		panel.add(grid, c);
		
		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 0;
//		c.gridy = 3;
//		c.ipady = 0;
//		c.gridwidth = 1;
//		c.ipady = 10;
//		panel.add(cwd, c);
//		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 1;
//		c.gridy = 3;
//		c.ipady = 0;
//		c.gridwidth = 1;
//		c.ipady = 10;
//		panel.add(wds, c);
//		
//		c.weightx = 1.0;
//		c.weighty = 0.0;
//		c.gridx = 2;
//		c.gridy = 3;
//		c.ipady = 0;
//		c.gridwidth = 1;
//		c.ipady = 10;
//		panel.add(sud, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(550,400));
		frame.setVisible(true);		
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

