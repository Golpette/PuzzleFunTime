package resources;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import Sudoku.DrawSudokuSolution;

@SuppressWarnings("serial")
public class MouseWheelTest extends JPanel implements MouseWheelListener {
    private final static String SOME_ACTION = "control 1";
   
    
    
    /**
     * 
     */
	DrawSudokuSolution sud;
	static int[][] grid;
	/**
	 * 
	 */
	
	
	
    public MouseWheelTest() {
        super(new BorderLayout());

        
        
        /**
         * Trying to add sudoku to this for testing
         */
        try {
			sud = new DrawSudokuSolution(grid, 11, 11);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        sud.generateSudoku();
        /**
         * 
         */
        
        
        
        
        
        JTextArea textArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        textArea.addMouseWheelListener(this);

        Action someAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("do some action");
            }
        };

        // Control A is used by a text area so try a different key
        textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        textArea.getActionMap().put(SOME_ACTION, someAction);
        
        sud.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        sud.getActionMap().put(SOME_ACTION, someAction);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            if (e.getWheelRotation() < 0) {
                JComponent component = (JComponent)e.getComponent();
                Action action = component.getActionMap().get(SOME_ACTION);
                if (action != null)
                    action.actionPerformed( null );
            } else {
                System.out.println("scrolled down");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("MouseWheelTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add( new MouseWheelTest() );
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
    }
}