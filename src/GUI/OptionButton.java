package GUI;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JComponent;

import constants.Settings;

/**
 * 
 * This class extends JComponent and creates a customized button
 * @author Yanick Sevigny
 *
 */
public class OptionButton extends JComponent implements MouseListener{
	private static final long serialVersionUID = 1L;
	private String text;
	private int textWidth;
	private Color textColor = Color.BLACK;
    private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
    
    private int offsetBorder = 3;
    
    private boolean mouseOver = false, mousePressed = false;

	/**
	 * Constructor takes in parameter the text and the color of the customized button
	 * @param name The text to be shown in the button
	 */
	public OptionButton(String name){
		this.text = name;
		addMouseListener(this);
	}
	
	/**
	 * Redefines the method paint of the component
	 */
	public void paint(Graphics g){

		Graphics2D g2d = (Graphics2D)g;
		g2d.addRenderingHints( new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON ));
		
		
		g2d.setColor(new Color(216,216,216));
		if(mousePressed)g2d.setColor(new Color(175,175,175));
		g2d.fillRect(0, 0, getWidth(), getHeight());
		
		if(mouseOver){
			g2d.setColor(new Color(107,160,192,150));
			textColor = (new Color(107,160,192,150));
		}else{
			g2d.setColor(new Color(224,224,224));
			textColor = Color.black;
		}
		if(mousePressed)g2d.setColor(new Color(216,216,216));
		g2d.drawLine(offsetBorder, offsetBorder, getWidth()-offsetBorder-1, offsetBorder);
		g2d.drawLine(offsetBorder, offsetBorder, offsetBorder, getHeight()-offsetBorder-1);
		g2d.drawLine(getWidth()-offsetBorder-1, offsetBorder, getWidth()-offsetBorder-1, getHeight()-offsetBorder-1);
		g2d.drawLine(offsetBorder, getHeight()-offsetBorder-1, getWidth()-offsetBorder-1, getHeight()-offsetBorder-1);
		
		g2d.setColor(textColor);
		if(mousePressed)g2d.setColor(new Color(216,216,216));
		g2d.setFont(Settings.DEFAULT_FONT);
		FontMetrics fm = g2d.getFontMetrics();
		textWidth = fm.stringWidth(text);
		g2d.drawString(text, (getWidth()/2)-(textWidth/2), (getHeight()/2)+4);
		
		g2d.setColor(new Color(145,145,145));
		g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
	}
	
	//Unused mouseListener method
	public void mouseClicked(MouseEvent e) {}
	
	//Redefines the method mouseEntered
	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		repaint();
	}
	
	//Redefines the method mouseExited
	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		repaint();
	}
	
	//Redefines the method mousePressed
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		repaint();
	}
	
	//Redefines the method mouseReleased
	public void mouseReleased(MouseEvent e) {
		notifyListeners(e);
		mousePressed = false;
		repaint();
	}
	
	/**
	 * 
	 * @return Returns the text of the customized button
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param name Sets the text of the customized button
	 */
	public void setText(String name) {
		this.text = name;
		repaint();
	}
	
	/**
	 * Adds an action listener to the arraylist of action listeners of the customized button
	 * @param listener
	 */
    public void addActionListener(ActionListener listener)
    {
        listeners.add(listener);
    }
    
    /**
     * Notify the customized button of a new actions performed and synchronize all the action listeners of the customized button
     * @param e
     */
    private void notifyListeners(MouseEvent e)
    {
        ActionEvent evt = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,new String(),e.getWhen(),e.getModifiers());
        
        synchronized(listeners)
        {
            for (int i = 0; i < listeners.size(); i++)
            {
                ActionListener tmp = listeners.get(i);
            	tmp.actionPerformed(evt);
            }
        }
    }
}
