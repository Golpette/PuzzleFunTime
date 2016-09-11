import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PuzzleLoader extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	public SetPuzzleSize setSize;
	JFrame frame;
	JPanel panel;
	JButton cwd, wds, sud;
	JLabel intro;
	Font font, font2;
	
	public PuzzleLoader() throws IOException {
		font = new Font("Times New Roman", Font.BOLD, 36);
		font2 = new Font("Times New Roman", Font.PLAIN, 24);
		intro = new JLabel("Welcome to puzzle solver!");
		intro.setFont(font);
		intro.setHorizontalAlignment(SwingConstants.CENTER);
		panel = new JPanel(new GridBagLayout());
		cwd = new JButton("Crossword");
		cwd.setFont(font2);
		cwd.setHorizontalAlignment(SwingConstants.CENTER);
		cwd.addActionListener(this);
		wds = new JButton("Word Search");
		wds.setFont(font2);
		wds.setHorizontalAlignment(SwingConstants.CENTER);
		wds.addActionListener(this);
		sud = new JButton("Sudoku");
		sud.setFont(font2);
		sud.setHorizontalAlignment(SwingConstants.CENTER);
		sud.addActionListener(this);
		
		frame = new JFrame("Auto Puzzle Generator");
		frame.setPreferredSize(new Dimension(500,400));
		frame.setSize(500, 400);
		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = (int)(frame.getHeight()*0.8);
		c.gridwidth = 3;
		panel.add(intro, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 0;
		c.gridwidth = 1;
		panel.add(cwd, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 1;
		c.gridy = 1;
		c.ipady = 0;
		c.gridwidth = 1;
		panel.add(wds, c);
		
		c.weightx = 1.0;
		c.weighty = 0.0;
		c.gridx = 2;
		c.gridy = 1;
		c.ipady = 0;
		c.gridwidth = 1;
		panel.add(sud, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == cwd){
//			try {
//				crossword = new CrosswordGenerator(10);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			try {
				setSize = new SetPuzzleSize("Crossword");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == wds){
//			try {
//				wordsearch = new WordSearchGenerator(15);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			try {
				setSize = new SetPuzzleSize("WordSearch");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == sud){
			try {
				sudoku = new SudokuGenerator(9);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static void main (String [] args){
		try {
			new PuzzleLoader();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
