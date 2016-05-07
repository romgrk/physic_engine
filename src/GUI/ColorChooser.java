package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * This class is a color chooser that calls its method colorChanged 
 * whenever there is a change of color.
 * @see colorChanged()
 * @author Romain
 *
 */
public class ColorChooser extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	public static Image palette;
	private final ColorChooser self = this;
	private int width;
	private int height;
	private Color color;
	
	public ColorChooser (int nwidth, int nheight, Color ncolor) {
		super();
		
		try {
			palette = ImageIO.read(new File("palette.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.width = (nwidth > 0) ? nwidth : 50;
		this.height = (nheight > 0) ? nheight : 50;
		this.color = ncolor;
		this.setSize(width, height);
		this.addActionListener(this);
	}
	
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		super.setSize(w, h);
		super.setPreferredSize(new Dimension(w, h));
	}
		
	
	public void paint(Graphics g) {
		g.clearRect(0, 0, width*2, height*2);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ));

		// Main color
		/*g.setColor(this.color);
		g.fillRect(0, 0, width, height);*/
		
		g2d.setPaint(new GradientPaint(0, 0, this.color, width, height, this.color.darker(), false));
		g2d.fill(new Rectangle(0, 0, width, height));
		
		// Inner glow
		int size = 4;
		for (int i = 0; i < size; ++i) {
			g.setColor(new Color(255, 255, 255, 200-(i*200/size)));
			g.drawLine(i, i, width-1-i, i);
			g.drawLine(width-1-i, i+1, width-1-i, height-1-i);
			g.drawLine(width-i, height-1-i, i, height-1-i);
			g.drawLine(i, height-1-i, i, i+1);
		}
		
		// White ellipse
		g.setColor(new Color(255, 255, 255, 80));
		g.fillOval(-width, -width*2/5, width*2, width);
		
		// Black 1px border
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, width-1, 0);
		g.drawLine(0, 0, 0, height-1);
		g.drawLine(width-1, 0, width-1, height-1);
		g.drawLine(0, height-1, width-1, height-1);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.askNewColor();
	}
	
	public void askNewColor () {
		new Thread () {
			public void run () {
				new QuickColorChooser(self);
			}
		}.start();
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		colorChanged(this.color);
	}
	
	/**
	 * Can be redefined to know when the color has changed.
	 * @param c The new color
	 */
	public void colorChanged ( Color c ) {}
	
	class QuickColorChooser extends JDialog {
		private static final long serialVersionUID = 1L;
		private ColorChooser cb;
		private QuickColorChooser self = this;
		
		public QuickColorChooser (ColorChooser colorChooser) {
			
			this.cb = colorChooser;
			
			setTitle("Pick a color");
			setResizable(false);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setUndecorated(true);
			setBounds(colorChooser.getLocationOnScreen().x+colorChooser.getWidth(), colorChooser.getLocationOnScreen().y+colorChooser.getHeight(), 255, 200);
			addMouseListener(new MouseColor(this));
			addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent arg0) {
				}
				public void focusLost(FocusEvent arg0) {
					self.setVisible(false);
				}
			});
			setVisible(true);
		}
		
		public void setColor(Color r) {
			cb.setColor(r);
			setVisible(false);
		}
		
		public void paint (Graphics g) {
			g.drawImage(ColorChooser.palette, 0, 0, null);
		}
	}
	
	class MouseColor implements MouseListener {
		public QuickColorChooser q;
		
		public MouseColor (QuickColorChooser f) {
			this.q = f;
		}
		
		public void mouseClicked(MouseEvent e) {
			q.setColor(new Color(Color.HSBtoRGB(((float)(e.getX())/255f), 1.0f, ((float)(e.getY())/200f))));
			q.setVisible(false);
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	}

}
