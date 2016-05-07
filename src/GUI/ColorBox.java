package GUI;

import java.awt.*;
import javax.swing.*;

/**
 * This class is a color box.
 * @author Romain
 *
 */
public class ColorBox extends JButton {

	private static final long serialVersionUID = 1L;
	private Color color;
	
	public ColorBox (Color ncolor) {
		color = ncolor;
	}
	
	public void paint(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.clearRect(0, 0, width*2, height*2);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ));

		// Main color
		/*g.setColor(this.color);
		g.fillRect(0, 0, width, height);*/
		
		g2d.setPaint(new GradientPaint(0, 0, this.color, width, height, this.color.darker(), false));
		g2d.fill(new Rectangle(0, 0, width, height));
		
		// Inner glow
		int size = 3;
		for (int i = 0; i < size; ++i) {
			g.setColor(new Color(255, 255, 255, 200-(i*200/size)));
			g.drawLine(i, i, width-1-i, i);
			g.drawLine(width-1-i, i+1, width-1-i, height-1-i);
			g.drawLine(width-i, height-1-i, i, height-1-i);
			g.drawLine(i, height-1-i, i, i+1);
		}
		
		// Black 1px border
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, width-1, 0);
		g.drawLine(0, 0, 0, height-1);
		g.drawLine(width-1, 0, width-1, height-1);
		g.drawLine(0, height-1, width-1, height-1);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		repaint();
	}
}