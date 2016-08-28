import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * Class to take String[][] grid and paint the crossword as it should look
 */
public class DrawProblem extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static final int SITE_LENGTH = 20;
	private String[][] gridInit, grid;
	private ArrayList<String> cluesACROSS;
	private ArrayList<String> cluesDOWN;	
	int x,y;
	JPanel panel;
	JScrollPane scroller;
	JScrollBar vertical;
	JButton reveal;
	DrawSolution sol;
	
	public DrawProblem(String[][] grid_init, String[][] grid, int x, int y, ArrayList<String> cluesACROSS, ArrayList<String> cluesDOWN){
		this.gridInit = grid_init;
		this.grid = grid;
		this.x = x;
		this.y = y;
		this.cluesACROSS = cluesACROSS;
		this.cluesDOWN = cluesDOWN;
		sol = new DrawSolution(grid, x, y);
		panel = new JPanel(new BorderLayout());
		reveal = new JButton("Show Solution");
		reveal.setPreferredSize(new Dimension(200, 200));
		reveal.setSize(200, 200);
		reveal.setOpaque(true);
		reveal.setEnabled(true);
		reveal.addActionListener(this);
		setOpaque(true);
		setBackground(Color.WHITE);
		JFrame frame = new JFrame("Auto-crossword!");
		frame.setPreferredSize(new Dimension(800,600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scroller = new JScrollPane(this);
		scroller.getVerticalScrollBar().setUnitIncrement(5);
		setPreferredSize(new Dimension(600, 800));
		JScrollPane area = new JScrollPane(scroller, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		panel.add(area);
		panel.add(reveal, BorderLayout.SOUTH);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	protected void paintComponent(Graphics g) {
		int width = getWidth(), height = getHeight();
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		int ox = 0, oy = 0;
		int sx = SITE_LENGTH; int sy = SITE_LENGTH;
		Font myFont = new Font("Times New Roman", Font.PLAIN, 11);
		g.setFont(myFont);
		for(int q =1; q<x-1; q++){
			for(int qq = 1; qq<y-1; qq++){
				if (grid[q][qq] == "_") {			
					g.setColor(Color.BLACK);
					new DrawRectangle(g, ox+q*sx, oy+qq*sy, SITE_LENGTH, gridInit[q][qq]);							
				}
				if (grid[q][qq] != "_") {
					g.setColor(Color.WHITE);
					new DrawRectangle(g, ox+q*sx, oy+qq*sy, SITE_LENGTH, gridInit[q][qq]);
				}	
			}					
		}
		myFont = new Font("Times New Roman", Font.BOLD, 12);
		g.setFont(myFont);
		g.drawString("ACROSS:", ox+(x*SITE_LENGTH)+SITE_LENGTH, oy+SITE_LENGTH);
		int y_coord = 0;
		myFont = new Font("Times New Roman", Font.PLAIN , 12);
		g.setFont(myFont);
		int lineNumber = -1;
		for(int cl=0; cl<cluesACROSS.size(); cl++){
			lineNumber++;
			y_coord = oy+SITE_LENGTH + (17*(lineNumber + 1));
			g.drawString(cluesACROSS.get(cl), ox+(x*SITE_LENGTH)+SITE_LENGTH, y_coord);
		}
		myFont = new Font("Times New Roman", Font.BOLD , 12);
		g.setFont(myFont);
		int startHere = y_coord + 25;
		g.drawString("DOWN:", ox+(x*SITE_LENGTH)+SITE_LENGTH, startHere);
		myFont = new Font("Times New Roman", Font.PLAIN , 12);
		g.setFont(myFont);
		for(int cl=0; cl<cluesDOWN.size(); cl++){
			y_coord = startHere + (17*(cl+1));
			g.drawString(cluesDOWN.get(cl), ox+(x*SITE_LENGTH)+SITE_LENGTH, y_coord);
		}		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==reveal){
			sol.frame.setVisible(!sol.frame.isVisible());
			if(sol.frame.isVisible()){
				reveal.setText("Hide Solution");
			}
			else{
				reveal.setText("Show Solution");
			}
		}
	}
}
