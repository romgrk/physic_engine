/*
Rotation Handler : Permet la rotation des object par Drag du deuxieme bouton de la souris
Ne pas oublier de cree un RotationHandler dans la classe Application
********** Revoir un moyen d'avoir un triangle rectangle avec les MouseEvents *************
*/

package handlers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import constants.Settings;

import objects.WObject;
import utils.Angle;
import utils.Point;
import GUI.Scene;

/**
 * 
 * This class manage the modification of the orientation of the selected object
 * @author Yanick Sevigny
 * 
 *
 */
public class RotationHandler
	extends Handler
	implements MouseListener, MouseMotionListener{
	
	private int xPressed, yPressed, xDragged, yDragged;
	private WObject selectedObject = null;
	private boolean rotate = false;
	private Angle oldAngle, draggedAngle, pressedAngle;
	private Point objectCenter;
	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public RotationHandler(Scene s) {
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
	}
	
	/**
	 * This method sets the current selectedObject to null and check if there is a selected object in the scene's world
	 */
	public void verifySelectedObject(){
		selectedObject = Settings.selectedObject;
		if(selectedObject != null){
			objectCenter = selectedObject.getPosition();
			oldAngle = selectedObject.getOrientation();
		}
	}
	
	/**
	 * This method calculates the angle between 2 points
	 */	
	public double getAngle(Point origin,Point other) {
	    double dy = other.getY() - origin.getY();
	    double dx = other.getX() - origin.getX();
	    double angle;
	    if (dx == 0)angle = (dy >= 0)? Math.PI/2 : -Math.PI/2;
	    else{
	        angle = Math.atan(dy/dx);
	        if (dx < 0) angle += Math.PI;
	    }
	    if (angle < 0) angle += 2*Math.PI;
	    
	    return angle;
	}

	//Redefines the method mouseDragged when this class is active
	public void mouseDragged(MouseEvent arg0) {
		if(!active)
			return;
		xDragged = arg0.getX();
		yDragged = scene.getHeight() - arg0.getY();
		if(rotate && selectedObject!=null){
			draggedAngle = new Angle(getAngle(objectCenter, new Point(xDragged,yDragged)));
			selectedObject.setOrientation(new Angle(oldAngle.value() + draggedAngle.value() - pressedAngle.value()));
			scene.repaint();
		}
	}
	
	//Redefines the method mouseDragged when this class is active
	public void mousePressed(MouseEvent arg0) {
		if(!active)
			return;
		xPressed = arg0.getX();
		yPressed = scene.getHeight()-arg0.getY();
		if(arg0.getButton() == 3){
			verifySelectedObject();
			rotate = true;
			if(selectedObject != null)pressedAngle = new Angle(getAngle(objectCenter, new Point(xPressed,yPressed)));
		}
	}
	
	//Redefines the method mouseDragged when this class is active
	public void mouseReleased(MouseEvent e) {
		if(!active)
			return;
		
		if(e.getButton() == 3){
			rotate = false;
		}
	}
	
	//Unused mouseListener method
	public void mouseMoved(MouseEvent arg0) {}	
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
}
