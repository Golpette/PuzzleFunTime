import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LogIn extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetPuzzleSize setSize;
	public static PuzzleLoader puzzle;
	JFrame frame;
	JPanel panel;
	JLayeredPane layer;
	JButton login, back;
	JLabel intro, emailLabel, passwordLabel, passwordRetypeLabel;
	Font font, font2, font3;
	JTextField email, password;
	ImageIcon image, image2;
	Image newimg;
	Image img;
	public PuzzleLoader puzzleLoader;
	EmailValidator emailValidator;
	JPasswordField password2;
	
	public LogIn() throws IOException {
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		intro = new JLabel("Log In to Puzzle Solver");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setOpaque(false);
		intro.setBounds(0, 0, 500, 300);
		panel = new JPanel(new GridBagLayout());
		login = new JButton("Login");
		login.setFont(font2);
		login.setHorizontalAlignment(SwingConstants.CENTER);
		login.addActionListener(this);
		
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
		
		image = new ImageIcon( path1 );
		img = image.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image = new ImageIcon(newimg);
		image2 = new ImageIcon( path2 );

		img = image2.getImage();
		newimg = img.getScaledInstance(50, 30, java.awt.Image.SCALE_SMOOTH ) ; 
		image2 = new ImageIcon(newimg);
		
		emailValidator = new EmailValidator();
		
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

		layer = new JLayeredPane();
		layer.add(intro, new Integer(1));
		layer.add(back, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		emailLabel = new JLabel("Email: ");
		emailLabel.setFont(font2);
		
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(font2);
//		passwordRetypeLabel.setFont(font);
//		passwordRetypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		email = new JTextField("");
		mouseActionlabel(email);
		email.setFont(font2);
		//email.setDocument(new JTextFieldLimit(20));	//not quite right
		password = new JTextField("");
		password.setFont(font2);
		password2 = new JPasswordField(20);
		//password2.setColumns(8);
		password2.setText("password");
		password2.setFont(font2);
		frame = new JFrame("Auto Puzzle Generator");
		frame.setPreferredSize(new Dimension(500,400));
		frame.setSize(500, 400);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 2.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		panel.add(layer, c);
		
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		panel.add(emailLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,0);
		panel.add(email, c);
		
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		panel.add(passwordLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 2;	
		c.gridwidth = 1;
		c.insets = new Insets(0,0,20,0);
		panel.add(password2, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.ipady = 10;
		c.insets = new Insets(0,0,0,0);
		panel.add(login, c);
		
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
				if(e.getSource() == email){
					System.out.println("Pressed email");
					email.setForeground(Color.BLACK);
				}
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
	
	void mouseActionlabel(JTextField b) {
		b.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				//if(e.getSource() == email){
					System.out.println("Pressed email");
					email.setForeground(Color.BLACK);
				//}
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
		if(e.getSource() == login){
			char[] correctPass = new char[]{'p','a','s','s','w','o','r','d'};
			char[] password = password2.getPassword();
			System.out.println(password);
		   if(!emailValidator.validate(email.getText().trim())) {
		        System.out.print("Invalid Email ID");
		        email.setForeground(Color.RED);
		        
		   }else if(!Arrays.equals(password, correctPass)){
			   System.out.println("Wrong password");
			   password2.setText("");
		   }else{
			   System.out.println("Correct password");
			   try {
					puzzleLoader = new PuzzleLoader(email.getText());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				frame.dispose();
		   }
		}
		
		if(e.getSource() == back){
			try {
				puzzleLoader = new PuzzleLoader("");
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
