import java.awt.Color;
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

public class SetPuzzleSize extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public PuzzleLoader puzzleLoader;
	JFrame frame;
	JPanel panel, panel1;
	JButton generate;
	JButton back;
	ButtonModel blah;
	JLayeredPane layer;
	JLabel intro;
	SpinnerNumberModel model;
	JSpinner spinner;
	Font font, font2;
	String puzzle;
	ImageIcon image, image2;
	Image newimg;
	Image img;
	
	public SetPuzzleSize(String puzzle) throws IOException {
		this.puzzle = puzzle;
		font = new Font("Century Gothic", Font.BOLD, 36);
		font2 = new Font("Century Gothic", Font.PLAIN, 24);
		panel = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		panel1.setOpaque(false);
		panel1.setBounds(0, 0, 500, 320);
		generate = new JButton("Generate");
		generate.setFont(font2);
		generate.setHorizontalAlignment(SwingConstants.CENTER);
		generate.addActionListener(this);
		
		// Set image path depending on OS
		String path1 = "";
		String path2 = "";
		//System.out.println(System.getProperty("os.name").toLowerCase());
		if( System.getProperty("os.name").toLowerCase().equals("linux")   ){
			
			path1 = "src/back.png";
			path2 = "src/back1.png";
		}
		else if(  System.getProperty("os.name").toLowerCase().contains("windows") ){
			System.out.println("You are on " + System.getProperty("os.name").toString());
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
		mouseActionlabel(back);
		back.addActionListener(this);
		back.setIcon(image);
		back.setOpaque(false);
		
		back.setBounds(0, 0, 100, 100);
		back.setBackground(new Color(255,255,255,255));
		back.setBorder(null);
		frame = new JFrame("Set Puzzle Size");
		frame.setSize(500, 400);
		frame.setPreferredSize(new Dimension(500,400));
		model = new SpinnerNumberModel(12, 3, 30, 1);
		spinner = new JSpinner(model);
		spinner.setForeground(Color.WHITE);
		spinner.setEditor(new JSpinner.DefaultEditor(spinner));
		spinner.setFont(font2);
		
		intro = new JLabel("Set " + puzzle + " Size");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		panel1.add(intro, c);
		
		c.gridx = 1;
		c.gridy = 1;
		panel1.add(spinner, c);
		
		layer = new JLayeredPane();
		layer.add(panel1, new Integer(1));
		layer.add(back, new Integer(0));
		layer.setVisible(true);
		layer.setOpaque(true);
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = (int)(frame.getHeight()*0.8);
		panel.add(layer, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		panel.add(generate, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
		if(e.getSource() == generate){
			if(puzzle.equals("Crossword")){
				try {
					crossword = new CrosswordGenerator((Integer)spinner.getValue());
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(puzzle.equals("Word Search")){
				try {
					wordsearch = new WordSearchGenerator((Integer)spinner.getValue());
				}catch (IOException e1) {
					e1.printStackTrace();
				}
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
}
