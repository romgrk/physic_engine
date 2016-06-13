package handlers;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;

import objects.WObject;
import utils.Point;
import constants.Settings;

import GUI.OptionButton;
import GUI.OptionPane;
import GUI.Scene;

/**
 * 
 * This class manage the creation of a small option panel for a specific object or world
 * @author Yanick Sevigny
 * 
 *
 */
public class OptionPaneHandler
extends Handler
implements MouseListener{
	
	private int xPressed, yPressed, xReleased, yReleased;
	private WObject selectedObject = null;
	private OptionPane optionPane = null;
	private boolean objectCopied = false;
	private WObject copiedObject = null;
	
	/**
	 * Constructor takes in parameter the scene to manage
	 * @param s Scene
	 */
	public OptionPaneHandler(Scene s) {
		super(s);
		optionPane = new OptionPane();
		
		s.addMouseListener(this);
	}
	
	/**
	 * This method sets the current selectedObject to null and check if there is a selected object in the scene's world
	 */
	public void verifySelectedObject(){
		selectedObject = Settings.selectedObject;
	}
	
	//Unused mouseListener method
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	
	//Redefines the method mousePressed when this class is active
	public void mousePressed(MouseEvent e) {
		if(!active)
			return;
		xPressed = e.getX();
		yPressed = e.getY();
		optionPane.setVisible(false);
	}
	
	//Redefines the method mouseReleased when this class is active
	public void mouseReleased(MouseEvent e) {
		if(!active)
			return;
		xReleased = e.getX();
		yReleased = e.getY();
		if(e.getButton() == 3){
			verifySelectedObject();
			if(selectedObject!= null){
				if(selectedObject.contains(new Point(e.getX(), getInverseY(e.getY()))) && 
						selectedObject.contains(new Point(xPressed, getInverseY(yPressed)))){
					
					switch (selectedObject.getType()){
					case CIRCLE:
						circleOptionPane();
					break;
					case POLYGON:
						polygonOptionPane();
						break;
					}
				}
			}else{
				worldOptionPane();
			}
		}
	}
	
	/**
	 * This method, when called, creates a new OptionPane with options for the world
	 */
	public void worldOptionPane(){
		optionPane = new OptionPane();
		optionPane.setX(xReleased+Settings.APPLICATIONX+10);
		optionPane.setY(yReleased+Settings.APPLICATIONY+133);
		if(objectCopied){
			OptionButton pasteObject = new OptionButton("Coller");
			pasteObject.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(objectCopied){
						copiedObject.setPosition(new Point(xReleased, scene.getHeight()-yReleased));
						copiedObject.setColor(Settings.DEFAULT_COLOR);
						scene.getWorld().addObject(copiedObject);
						copiedObject = copiedObject.getCopy();
						optionPane.setVisible(false);
					}
				}
			});
			optionPane.addComponent(pasteObject);
		}
		OptionButton resetWorld = new OptionButton("Reeinitialiser");
		resetWorld.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				scene.getWorld().clear();
				optionPane.setVisible(false);}		
		});
		optionPane.addComponent(resetWorld);
		optionPane.setVisible(true);
	}
	
	/**
	 * This method, when called, creates a new OptionPane with options for the current selected circle
	 */
	public void circleOptionPane(){
		optionPane = new OptionPane();
		optionPane.setX(xReleased+Settings.APPLICATIONX+10);
		optionPane.setY(yReleased+Settings.APPLICATIONY+133);
		JCheckBox fix = new JCheckBox("     Fixe");
		fix.setFont(new Font("Comic Sans ms", Font.PLAIN, 11));
		fix.setBackground(new Color(216,216,216));
		fix.setSelected(selectedObject.isFixed());
		fix.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				selectedObject.setFixed(((JCheckBox) e.getSource()).isSelected());
			}
		});
		optionPane.addComponent(fix);
		OptionButton copyObject = new OptionButton ("Copier");
		copyObject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				objectCopied = true;
				copiedObject = selectedObject.getCopy();
				optionPane.setVisible(false);
			}
		});
		optionPane.addComponent(copyObject);
		OptionButton deleteObject = new OptionButton("Supprimer");
		deleteObject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				scene.getWorld().removeObject(selectedObject);
				optionPane.setVisible(false);}
		});
		optionPane.addComponent(deleteObject);
		optionPane.setVisible(true);
		
	}

	
	/**
	 * This method, when called, creates a new OptionPane with options for the current selected polygon
	 */
	public void polygonOptionPane(){
		optionPane = new OptionPane();
		optionPane.setX(xReleased+Settings.APPLICATIONX+10);
		optionPane.setY(yReleased+Settings.APPLICATIONY+133);
		JCheckBox fix = new JCheckBox("Fixe");
		fix.setFont(new Font("Comic Sans ms", Font.PLAIN, 11));
		fix.setBackground(new Color(216,216,216));
		fix.setSelected(selectedObject.isFixed());
		fix.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				selectedObject.setFixed(((JCheckBox) e.getSource()).isSelected());
			}
		});
		optionPane.addComponent(fix);
		OptionButton copyObject = new OptionButton ("Copier");
		copyObject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				objectCopied = true;
				copiedObject = selectedObject.getCopy();
				optionPane.setVisible(false);
			}
		});
		optionPane.addComponent(copyObject);
		OptionButton deleteObject = new OptionButton("Supprimer");
		deleteObject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				scene.getWorld().removeObject(selectedObject);
				optionPane.setVisible(false);}
		});
		optionPane.addComponent(deleteObject);
		optionPane.setVisible(true);
	}
	
}
