package handlers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import constants.Settings;

import objects.WObject;
import utils.Point;

import GUI.InformationPanel;
import GUI.Scene;

public class SelectionHandler 
extends Handler
implements MouseListener, MouseMotionListener{
	
	/**
	 * 
	 * This class manage the selection of an object and the information pane associated to it
	 * @author Yanick Sévigny
	 * 
	 *
	 */
	
	private Point 
	pressed = new Point(), 
	dragged = new Point();
	private boolean dragging = false;
	private InformationPanel infoPane;
	
	private Color color = null;
	
	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public SelectionHandler(Scene s){
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
	}
	
	public void mouseDragged(MouseEvent e) {
		if(!active)
			return;
		
		dragged.set(e.getX(), getInverseY(e.getY()));
		
		if (dragging) {
			Settings.selectedObject.setPosition(dragged);
			scene.repaint();
		}
	}

	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	//Redefines the method mousePressed when this class is active
	public void mousePressed(MouseEvent e) {
		if(!active)
			return;
		
		WObject selectedObject = Settings.selectedObject;
		
		pressed.set(e.getX(), getInverseY(e.getY()));
		
		if (e.getButton() == 1) {
			if (selectedObject != null) {
				if (selectedObject.contains(pressed)) {
					dragging = true;
					return;
				} else {
					selectedObject.setSelected(false);
					selectedObject.setColor(color);
					Settings.selectedObject = null;
				}
			}
			for(int i = 0; i < scene.getWorld().getObjects().size(); i++) {
				WObject wo = scene.getWorld().getObjects().get(i);
				
				if (wo.contains(pressed)){
					color = wo.getColor();
					Settings.selectedObject = wo;
					
					infoPane.refresh();
					
					wo.setSelected(true);
					wo.setColor(Settings.SELECTED);
					
					dragging = true;
					break;
				}
			}
			
			scene.repaint();
		}
	}
	
	//Redefines the method mouseReleased when this class is active
	public void mouseReleased(MouseEvent e) {
		if(!active)
			return;
		dragging = false;
		if (e.getButton() == 1 && e.getClickCount() == 2) {
			infoPane.refresh();
			infoPane.setVisible(true);
		}
	}

	/**
	 * This method sets the current InformationPane to the parameter infoPane
	 * @param informations InformationPane
	 */
	public void setInformationPane(InformationPanel informations) {
		this.infoPane = informations;
	}
	/**
	 * This method returns the current InformationPane
	 * @return infoPane
	 */
	public InformationPanel getInformationPane() {
		return infoPane;
	}
}

