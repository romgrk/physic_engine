package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import utils.*;

/**
 * This class contains method to draw assuming an origin on the bottom left
 * corner of the screen, growing upward on Y axis and to the right on X axis.
 * @author Romain
 *
 */
public class WDrawer {
	
	private Graphics g;
	private Graphics2D g2;
	
	private int width;
	private int height;
	
	WDrawer (Graphics g, int w, int h) {
		this.g = g;
		g2 = (Graphics2D) g;
		setColor(Color.BLACK);
		width = w;
		height = h;
	}
	
	public int getInverseY(int y) {
		return height - y;
	}
	
	public double getInverseY(double y) {
		return  (height - y);
	}
	
	// GOOD
	public void drawCircle (Point p, double ray) {
		int x = (int) (p.getX()-ray);
		int y = (int) (getInverseY(p.getY())-ray);
		int r = (int) ray;
		g.drawOval(x, y, 2*r, 2*r);
	}
	
	public void fillCircle (Point p, double ray) {
		int r = (int) ray;
		int x = (int) (p.getX()-r);
		int y = (int) (getInverseY(p.getY())-ray);
		g.fillOval(x, y, 2*r, 2*r);
	}
	
	public void drawRect (Point p, double wt, double ht) {
		int w = (int) wt;
		int h = (int) ht;
		int x = (int) p.getX();
		int y = (int) (getInverseY(p.getY()) - h);
		g.drawRect(x, y, w, h);
	}
	
	public void fillRect (Point p, double wt, double ht) {
		int w = (int) wt;
		int h = (int) ht;
		int x = (int) p.getX();
		int y = (int) (getInverseY(p.getY()) - h);
		g.fillRect(x, y, w, h);
	}
	
	public void drawLine (double dx1, double dy1, double dx2, double dy2) {
		if (dx1 == dx2 && dy1 == dy2)
			return;
		g2.draw(new Line2D.Double(dx1, getInverseY(dy1), dx2, getInverseY(dy2)));
	}
	
	public void drawLine (Point p1, Point p2) {
		drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public void drawLine (Point p1, Vector p2) {
		drawLine(p1.getX(), p1.getY(), p1.getX() + p2.getX(), p1.getY() + p2.getY());
	}
	
	public void drawLine (Segment s) {
		drawLine(s.getP1(), s.getP2());
	}
	
	public void drawLine(Line line) {
		if (line.getA() == Double.POSITIVE_INFINITY) {
			int b = (int) line.getB();
			drawLine(new Point(b, 0), new Point(b, getHeight()));
		} else {
			double a = line.getA();
			Point p1;
			Point p2;
			if (Math.abs(a) <= 1) {
				p1 = new Point(0, line.yAt(0));
				p2 = new Point(getWidth(), line.yAt(getWidth()));
			} else {
				p1 = new Point(line.xAt(0), 0);
				p2 = new Point(line.xAt(getHeight()), getHeight());
			}
			drawLine(p1, p2);
		}
	}
	

	public void drawCross(Point p){
		double x = p.getX();
		double y = p.getY();
		setStroke(4);
		setColor(Color.red);
		drawLine(x-5, y-5, x+5, y+5);
		drawLine(x+5, y-5, x-5, y+5);
		setStroke(2);
		setColor(Color.black);
		drawLine(x-4, y-4, x+4, y+4);
		drawLine(x+4, y-4, x-4, y+4);
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public Graphics getG () {
		return g;
	}
	
	public void setG(Graphics g) {
		this.g = g;
	}

	public Color getColor() {
		return g.getColor();
	}

	public void setColor(Color color) {
		g.setColor(color);
	}
	
	public void setStroke(Stroke s) {
		g2.setStroke(s);
	}
	
	public void setStroke(float s) {
		g2.setStroke(new BasicStroke(s));
	}
	
}
