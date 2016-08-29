import java.awt.Color;
import java.awt.Graphics;

/**
 * Class to draw a box for each entry.
 */
public class DrawRectangle {

	public DrawRectangle(Graphics g, int x, int y, int r, String s){
		g.fillRect( x, y, r, r );
		g.setColor(Color.black);
		g.drawRect( x, y, r, r );
		
		g.drawString( s, x+2, y+(r/2));
	}
	
	public DrawRectangle(Graphics g, int x, int y, int r, String s, int squareSize) {
		g.fillRect(x, y, r, r);
		g.setColor(Color.black);
		g.drawRect(x, y, r, r );
		g.drawString(s.toUpperCase(), x + squareSize/4, y + r - squareSize/4);	
	}
	
	public DrawRectangle(Graphics g, int x, int y, int r){
		g.fillRect(x, y, r+1, r+1);
	}
}
