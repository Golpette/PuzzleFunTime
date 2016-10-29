package resources;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import Sudoku.DrawSudoku;

@SuppressWarnings("serial")
public class MouseWheelTest extends JPanel implements MouseWheelListener {
    private final static String SOME_ACTION = "control 1";
   
    JPanel jpan;
    GridBagConstraints c;
    JTextArea textArea;
    JTextField textField;
    JScrollPane scrollPane;
    JFrame frame;
    JLabel label;
    JButton button;
    int x, y;
	
    public MouseWheelTest() {    
    	int x = 100;
    	int y = 100;
    	button = new JButton("This is a button");
    	label = new JLabel("�sdfglkj");
    	frame = new JFrame("MouseWheelTest");
    	textField = new JTextField();
    	textField.addMouseWheelListener(this);
    	textField.setOpaque(true);
    	textField.setVisible(true);
    	jpan = new JPanel(new GridLayout(1,1));
    	c = new GridBagConstraints();
        textArea = new JTextArea(100, 40);
        textArea.addMouseWheelListener(this);
        textArea.setOpaque(true);
        textArea.setVisible(true);
        label.addMouseWheelListener(this);
        label.setOpaque(true);
        label.setVisible(true);
        button.addMouseWheelListener(this);
        button.setOpaque(true);
        button.setVisible(true);
        button.setSize(new Dimension(x, y));
        Action someAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("do some action");
            }
        };
        textArea.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        textArea.getActionMap().put(SOME_ACTION, someAction);
        
        textField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        textField.getActionMap().put(SOME_ACTION, someAction);
        
//        
        label.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        label.getActionMap().put(SOME_ACTION, someAction);
        
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(SOME_ACTION), SOME_ACTION);
        button.getActionMap().put(SOME_ACTION, someAction);
        
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(100,100,100,100);
        c.weightx = 0.0;
        c.weighty = 0.0;
        jpan.add(button, c);
        
        
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(100,100,100,100);
        c.weightx = 0.0;
        c.weighty = 0.0;
        jpan.add(label, c);
        //jpan.add(button);
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(jpan);
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setLocationRelativeTo( null );
        frame.setVisible(true);
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
       MouseWheelTest mouse = new MouseWheelTest();
    }
}
