package GUI;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JWindow;

import constants.Settings;


/**
 * This class is the base to make a popup menu.
 * @author Yannick
 *
 */
public class OptionPane extends JWindow{
	private static final long serialVersionUID = 1L;
	
	private int x, y, width = Settings.OPTIONPANEWIDTH, height, nbButton;

	public OptionPane(){
		
	}
	
	public OptionPane(int nbButton){
		
	}
	
	public OptionPane(int x, int y, int width, int height){
		this.x = x; this.y = y; this.width = width; this.height = height; 
		setBounds(this.x, this.y, this.width, this.height);
	}
	
	public OptionPane(int x, int y, int width, int height, int nbButton){
		this.x = x; this.y = y; this.width = width; this.height = height; this.nbButton = nbButton;
		setBounds(this.x, this.y, this.width, this.height);

		setLayout(new GridLayout(1, this.nbButton));
	}
	
	public void addComponent(JComponent button){
		setHeight(height+25);
		nbButton++;
		setLayout(new GridLayout(this.nbButton, 1));
		add(button);
	}
	
	
	public void setX(int x) {
		this.x = x;
		setBounds(this.x, this.y, this.width, this.height);
	}
	
	public void setY(int y) {
		this.y = y;
		setBounds(this.x, this.y, this.width, this.height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
		setBounds(this.x, this.y, this.width, this.height);
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
		setBounds(this.x, this.y, this.width, this.height);
	}
	
	public int getNbButton() {
		return nbButton;
	}

	public void setNbButton(int nbButton) {
		this.nbButton = nbButton;

	}
}
