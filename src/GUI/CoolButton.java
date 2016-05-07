package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * This class is a simple extension of JButton that draw an icon on itself.
 * @author Romain
 *
 */
public class CoolButton extends JButton implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3333169859619996924L;
	private boolean hover = false;
	
	public CoolButton () {
		addMouseListener(this);
	}
	
	public void paint (Graphics g) {

		Graphics2D g2d = (Graphics2D)g;
		g2d.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ));
		
		g.setColor(new Color(238, 238, 238));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (!isEnabled()) {
			g.setColor(new Color(255, 255, 255, 100));
			g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			
			g.setColor(new Color(200, 200, 200, 150));
			g.drawRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
		}
		
		int x = (getWidth() - getIcon().getIconWidth())/2;
		int y = (getHeight() - getIcon().getIconHeight())/2;
		getIcon().paintIcon(this, g, x, y);
		
		if (hover) {
			g.setColor(new Color(255, 255, 255, 150));
			g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
		}
	}
	
	public void mouseEntered(MouseEvent arg0) {
		hover = true;
	}
	
	public void mouseExited(MouseEvent arg0) {
		hover = false;
	}
	
	public void mouseClicked(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
}
