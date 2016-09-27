import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SignUp extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetPuzzleSize setSize;
	public static PuzzleLoader puzzle;
	JFrame frame;
	JPanel panel;
	JButton  signup, login, back;
	JLabel intro, emailLabel, passwordLabel, passwordRetypeLabel;
	Font font, font2, font3;
	JTextField email, password, passwordRetype;
	ImageIcon image, image2;
	Image newimg;
	Image img;
	
	public SignUp() throws IOException {
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		intro = new JLabel("Sign Up to Puzzle Solver");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		panel = new JPanel(new GridBagLayout());
		signup = new JButton("Sign Up");
		signup.setFont(font3);
		signup.setHorizontalAlignment(SwingConstants.CENTER);
		signup.addActionListener(this);
		login = new JButton("Login");
		login.setFont(font3);
		login.setHorizontalAlignment(SwingConstants.CENTER);
		login.addActionListener(this);
		
		image = new ImageIcon("src\\back.png");
		img = image.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);
		image2 = new ImageIcon("src\\back1.png");

		img = image2.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image2 = new ImageIcon(newimg);
		
		back = new JButton("");
		back.setHorizontalAlignment(SwingConstants.CENTER);
		mouseActionlabel(back);
		back.addActionListener(this);
		back.setIcon(image);

		emailLabel = new JLabel("Email: ");
		emailLabel.setFont(font);
		emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel = new JLabel("Create Password: ");
		passwordLabel.setFont(font);
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordRetypeLabel = new JLabel("Retype Password: ");
//		passwordRetypeLabel.setFont(font);
//		passwordRetypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		email = new JTextField("Email:");
		password = new JTextField("Password:");
		passwordRetype = new JTextField("Retype Password");
		frame = new JFrame("Auto Puzzle Generator");
		frame.setPreferredSize(new Dimension(500,400));
		frame.setSize(500, 400);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		panel.add(back, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		panel.add(login, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		panel.add(intro, c);
		
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.ipadx = 3;
		panel.add(emailLabel, c);
		
		c.weightx = 0.5;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.ipadx = 1;
		panel.add(email, c);
		
		c.weightx = 0.2;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.ipadx = 3;
		panel.add(passwordLabel, c);
		
		c.weightx = 0.8;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.ipadx = 1;
		panel.add(password, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.ipadx = 3;
		panel.add(passwordRetypeLabel, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 1;
		c.gridy = 5;
		c.gridwidth = 2;
		c.ipadx = 1;
		panel.add(passwordRetype, c);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 2;
		panel.add(signup, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
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
	
	
	}
	
	public static void main (String [] args){
		try {
			puzzle = new PuzzleLoader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
