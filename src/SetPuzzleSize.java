import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class SetPuzzleSize extends JComponent implements ActionListener{
	private static final long serialVersionUID = 1L;
	public CrosswordGenerator crossword;
	public WordSearchGenerator wordsearch;
	public SudokuGenerator sudoku;
	JFrame frame;
	JPanel panel, panel1;
	JButton generate, back;
	JLabel intro;
	SpinnerNumberModel model;
	JSpinner spinner;
	Font font, font2;
	String puzzle;
	
	public SetPuzzleSize(String puzzle) throws IOException {
		this.puzzle = puzzle;
		font = new Font("Times New Roman", Font.BOLD, 36);
		font2 = new Font("Times New Roman", Font.PLAIN, 24);
		panel = new JPanel(new GridBagLayout());
		panel1 = new JPanel(new GridBagLayout());
		generate = new JButton("Generate");
		generate.setFont(font2);
		generate.setHorizontalAlignment(SwingConstants.CENTER);
		generate.addActionListener(this);
		back = new JButton("Back");
		back.setFont(font2);
		back.setHorizontalAlignment(SwingConstants.CENTER);
		back.addActionListener(this);
		frame = new JFrame("Set Puzzle Size");
		frame.setSize(500, 400);
		frame.setPreferredSize(new Dimension(500,400));
		model = new SpinnerNumberModel(12, 3, 30, 1);
		spinner = new JSpinner(model);
		intro = new JLabel("Set " + puzzle + " Size");
		intro.setFont(font);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		
		c.gridx = 0;
		c.gridy = 1;
		panel1.add(intro, c);
		
		c.gridx = 1;
		c.gridy = 1;
		panel1.add(spinner, c);
		
		
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.ipady = (int)(frame.getHeight()*0.8);
		panel.add(panel1, c);
		
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		c.ipady = 10;
		//c.gridwidth = 1;
		panel.add(generate, c);
		
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);		
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
			if(puzzle.equals("WordSearch")){
				try {
					wordsearch = new WordSearchGenerator((Integer)spinner.getValue());
				}catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if(e.getSource() == back){
			System.out.println("Is this visible"+ this.isVisible());
			
		}
	}
}
