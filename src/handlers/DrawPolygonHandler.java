package handlers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import constants.Settings;

import objects.WPolygon;
import utils.Point;

import GUI.Drawable;
import GUI.Scene;
import GUI.WDrawer;

/**
 * 
 * This class manage the creation of new polygons
 * @author Yanick Sévigny
 * 
 *
 */

public class DrawPolygonHandler 
extends Handler
implements MouseListener, MouseMotionListener, Drawable, KeyListener{
	
	private ArrayList<Point> points = new ArrayList<Point>();
	private int xMoved, yMoved;
	private boolean moving = false, ctrling = false;

	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public DrawPolygonHandler(Scene s) {
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
		s.addDrawable(this);
		s.addKeyListener(this);
	}

	/**
	 * The method paint defines what the scene is going to show when a creation of a polygon will be occuring
	 * It takes in the WDrawer which will be shown in the scene
	 * @param w WDrawer
	 */
	public void paint(WDrawer w) {
		if (!active)
			return;
		
		if(moving && !ctrling){
			w.setColor(Settings.GRID_COLOR);
			if(points.size() > 0)w.drawLine(points.get(points.size()-1), new Point(xMoved, yMoved));
			if(points.size() > 1){
				for(int i = 0 ; i < points.size()-1 ; i++){
					w.fillCircle(points.get(i), 2);
					w.drawLine(points.get(i), points.get(i+1));
				}
			}
		}
		if(ctrling){
			w.setColor(Color.BLACK);
			for (int i = 0; i < points.size(); i++) {
				w.fillCircle(points.get(i), 2);
			}
		}
		scene.repaint();
	}
	
	//Unused mouseListener and keyListener method
	public void keyTyped(KeyEvent arg0) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	
	//Redefines the method keyPressed when this class is active
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL && !ctrling) {
			ctrling = true;
		}
	}
	
	//Redefines the method keyReleased when this class is active
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrling = false;
			if (points.size() > 2) {
				WPolygon poly = new WPolygon(points);
				//WNPolygon poly = new WNPolygon(points);
				poly.setColor(Settings.DEFAULT_COLOR);
				scene.getWorld().addObject(poly);
				scene.repaint();
			} 
			points = new ArrayList<Point>();
		}
	}
	
	//Redefines the method mouseMoved when this class is active
	public void mouseMoved(MouseEvent e) {
		if(!active)
			return;
		moving = true;
		xMoved = e.getX();
		yMoved = scene.getHeight() - e.getY();
		scene.repaint();
	}
	
	//Redefines the method mousePressed when this class is active
	public void mousePressed(MouseEvent e) {
		if(active && !ctrling){
			points.add(new Point(e.getX(), scene.getHeight() - e.getY()));
		}
	}

	//Redefines the method mouseReleased when this class is active
	public void mouseReleased(MouseEvent e) {
		if(active){
			if(!ctrling){
				int last = points.size() - 1;
				if(points.size() > 2 && points.get(0).distanceTo(points.get(last)) < 10){
					WPolygon poly = new WPolygon(points);
					//poly.setRotation(Math.PI/4);
					poly.setColor(Settings.DEFAULT_COLOR);
					poly.setFixed(true);
					scene.getWorld().getObjects().add(poly);
					scene.repaint();
					points = new ArrayList<Point>();
				}	
			}else{
				Point p = new Point (e.getX(), scene.getHeight() - e.getY());
				if (points.size() == 0) {
					points.add(p);
				} else if (!points.get(points.size()-1).equals(p)) {
					points.add(p);
				}
				scene.repaint();
			}
		}
	}
}

