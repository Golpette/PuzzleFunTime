package GUIs;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Crossword.CrosswordGenerator;
import Sudolu.SudokuGenerator;
import UserCredentials.EmailValidator;
import WordSearch.WordSearchGenerator;

public class SignUp extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetDifficulty setSize;
	public static PuzzleLoader puzzle;
	JFrame frame;
	JLayeredPane layer;
	JPanel panel;
	JButton  signup, back;
	JLabel intro, emailLabel, usernameLabel, passwordLabel, passwordRetypeLabel;
	Font font, font2, font3, font4;
	JTextField email, username, password, passwordRetype;
	ImageIcon image, image2;
	Image newimg;
	Image img;
	JPasswordField pass1, pass2;
	public PuzzleLoader puzzleLoader;
	EmailValidator emailValidator;
	
	public SignUp() throws IOException {
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		font4 = new Font("Century Gothic", Font.ITALIC, 24);
		font3 = new Font("Century Gothic", Font.PLAIN, 20);
		intro = new JLabel("Sign Up to Puzzle Solver");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		intro.setOpaque(false);
		intro.setBounds(0, 0, 550, 170);
		panel = new JPanel(new GridBagLayout());
		signup = new JButton("Sign Up");
		signup.setFont(font3);
		signup.setHorizontalAlignment(SwingConstants.CENTER);
		signup.addActionListener(this);
		
		emailValidator = new EmailValidator();
		
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
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
		
		back = new JButton("");
		back.setFont(font2);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		keyActionTextField(back);
		mouseActionlabel(back);
		back.addActionListener(this);
		back.setIcon(image);
		back.setOpaque(false);
		
		back.setBounds(0, 0, 100, 100);
		back.setBackground(new Color(255,255,255,255));
		back.setBorder(null);
		
		layer = new JLayeredPane();
		layer.add(back, new Integer(0));
		layer.add(intro, new Integer(1));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		emailLabel = new JLabel("Email: ");
		emailLabel.setFont(font2);
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setFont(font2);
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(font2);
		passwordRetypeLabel = new JLabel("Retype: ");
		passwordRetypeLabel.setFont(font2);
		email = new JTextField("");
		mouseActionlabel(email);
		keyActionTextField(email);
		email.setFont(font2);
		username = new JTextField("");
		username.setFont(font2);
		password = new JTextField("");
		pass1 = new JPasswordField(20);
		pass1.setFont(font2);
		pass1.setText("pass");
		pass2 = new JPasswordField(20);
		pass2.setFont(font2);
		pass2.setText("pass");
		passwordRetype = new JTextField("");
		frame = new JFrame("Auto Puzzle Generator");
		frame.setPreferredSize(new Dimension(600,400));
		frame.setSize(550, 400);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 1.0;
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
		panel.add(usernameLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,10);
		panel.add(username, c);
		
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		panel.add(emailLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,10);
		panel.add(email, c);
		
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		panel.add(passwordLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,10);
		panel.add(pass1, c);
		
		c.weightx = 1.0;
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0,10,0,0);
		panel.add(passwordRetypeLabel, c);
		
		c.weightx = 5.0;
		c.weighty = 0.1;
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,20,10);
		panel.add(pass2, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.ipady = 10;
		c.insets = new Insets(0,0,0,0);
		panel.add(signup, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(550,400));
		frame.setVisible(true);		
		frame.getRootPane().setDefaultButton(signup);
		frame.setResizable(false);
	}

	void keyActionTextField(JButton l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if(e.getSource()== back){
					if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
						//actionPerformed(back.this);
					}
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {

			}
		});
	}
	
	void keyActionTextField(JTextField l) {

		l.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if(e.getSource() == email){
					email.setFont(font2);
					email.setForeground(Color.BLACK);
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
	
	void mouseActionlabel(JTextField b) {
		b.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				//if(e.getSource() == email){
					System.out.println("Pressed email");
					email.setForeground(Color.BLACK);
					email.setFont(font2);
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
	
	void writeToFile(String fileName, String text) throws Exception {
		   FileOutputStream out = new FileOutputStream(fileName, true);
		   out.write(text.getBytes());
		} 
	
	@SuppressWarnings("null")
	public void actionPerformed(ActionEvent e) {
	System.out.println("ActionEvent e: " + e.toString());
		if(e.getSource() == signup){
			char[] password1 = pass1.getPassword();
			char[] password2 = pass2.getPassword();
			String a = new String(password1);
			//String b = new String(ID);
			System.out.println(password1);
			if(username.getText().equals("")){
				JOptionPane.showMessageDialog(frame, "Username Required");
			}
			else if(!emailValidator.validate(email.getText().trim())) {
			   
		        System.out.print("Invalid Email ID");
		        if(!email.getText().equals("")){
			        email.setForeground(Color.RED);
			        email.setFont(font4);
		        }else{
		        	JOptionPane.showMessageDialog(frame, "Email Required");
		        }
		   }else if(!Arrays.equals(password1, password2)){
			   System.out.println("Passwords do not match!");
			   JOptionPane.showMessageDialog(frame, "Passwords do not match!");
			   pass1.setText("");
			   pass2.setText("");
			   
		   }else if(password1.length < 5){
			   JOptionPane.showMessageDialog(frame, "Password too short!");
		   }else{
			   System.out.println("Correct password");   
			 
			   String user = email.getText();
				   try{
					   String str = "<user>\n\t<username>" + username.getText() + "</username>\n\t<email>"+user + "</email>\n\t<password>" + a + "</password>\n</user>\n\n";
					    writeToFile("Users.txt", str);
						puzzleLoader = new PuzzleLoader(email.getText());
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
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
