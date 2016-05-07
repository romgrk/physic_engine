package handlers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import constants.Settings;

import objects.WLine;

import utils.Point;

import GUI.Drawable;
import GUI.Scene;
import GUI.WDrawer;

/**
 * 
 * This class manage the creation of new lines
 * @author Yanick Sévigny
 * 
 *
 */

public class DrawLineHandler 
extends Handler
implements MouseListener, MouseMotionListener, Drawable{
	
	private Point p1, p2;
	private boolean dragging = false;
	private int xDragged, yDragged;
	
	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public DrawLineHandler(Scene s) {
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
		s.addDrawable(this);
	}
	
	/**
	 * The method paint defines what the scene is going to show when a creation of a line will be occuring
	 * It takes in the WDrawer which will be shown in the scene
	 * @param w WDrawer
	 */
	public void paint(WDrawer w){
		if(dragging){
			w.setColor(Settings.GRID_COLOR);
			w.setStroke(2);
			w.drawLine(p1, new Point(xDragged, yDragged));
		}
	}

	//Unused mouseListener method
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	
	//Redefines the method mouseDragged when this class is active
	public void mouseDragged(MouseEvent e){
		if(!active)
			return;
		dragging = true;
		xDragged = e.getX();
		yDragged = scene.getHeight() - e.getY();
		
		scene.repaint();
	}
	
	//Redefines the method mousePressed when this class is active
	public void mousePressed(MouseEvent e) {
		if(!active)
			return;
		p1 = new Point(e.getX(), scene.getHeight() - e.getY());
	}

	//Redefines the method mouseReleased when this class is active
	public void mouseReleased(MouseEvent e) {
		if(!active)
			return;
		dragging = false;
		p2 = new Point(e.getX(), scene.getHeight() - e.getY());
		if(p1.distanceTo(p2) > 10){
			WLine line = new WLine(p1, p2);
			line.setFixed(true);
			scene.getWorld().addObject(line);// TODO see WLine constructor
		}
	}

}
