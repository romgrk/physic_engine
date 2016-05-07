package handlers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import constants.Settings;

import GUI.Scene;

import objects.WCircle;

import utils.Angle;
import utils.Point;
import utils.Vector;

/**
 * This class creates particles, in different ways depending of the button pressed.
 * @author Romain
 *
 */
public class ParticleGeneratorHandler
extends Handler
implements MouseListener, MouseMotionListener {
	
	public static final int CIRCLES = 20;
	public static final int RAY = 5;
	public static final int DISTANCE = 15;
	public static final int FORCE = 50;
	
	public static final long repeat = 100;
	
	private ParticleTimer timer = new ParticleTimer();
	
	private Point e = new Point(0, 0);
	
	private int button = 0;
	private boolean pressed = false;
	
	/**
	 * Constructor
	 * @param s The scene to manage
	 */
	public ParticleGeneratorHandler (Scene s) {
		super(s);
		
		s.addMouseListener(this);
		s.addMouseMotionListener(this);
		
		timer.start();
	}
	
	class ParticleTimer extends Thread {
		public void run () {
			while (true) {
				generate();
				try {
					sleep(repeat);
				} catch (InterruptedException e) {}
			}
		}
	}
	
	public void mousePressed(MouseEvent me) {
		if (!active)
			return;
		updateE(me);
		pressed = true;
		button = me.getButton();
	}
	public void mouseReleased(MouseEvent me) {
		if (!active || me.getButton() != button)
			return;
		updateE(me);
		pressed = false;
	}
	public void mouseDragged(MouseEvent me) {
		if (!active)
			return;
		updateE(me);
	}
	
	public void updateE (MouseEvent me) {
		e = new Point(me.getX(), getInverseY(me.getY()));
	}
	
	public void generate () {
		if (!active || !pressed) 
			return;
		if (button == 1) {
			for (int i = 0; i < CIRCLES; i++) {
				WCircle wd = new WCircle(RAY);
				Angle a = new Angle (2*Math.PI * i/CIRCLES);
				Point p = new Point(e);
				Vector v = new Vector(Math.cos(a.value()), Math.sin(a.value()));
				float h = i;
				h /= CIRCLES;
				Color c = new Color(Color.HSBtoRGB(h, 0.9f, 0.9f));
				
				v.multiply(DISTANCE);
				p.add(v);
				v.multiply(FORCE/DISTANCE);
				
				wd.setPosition(p);
				wd.setSpeed(v);
				wd.setColor(c);
				
				scene.getWorld().addObject(wd);
			}
		} else {
			WCircle wd = new WCircle(RAY);
			wd.setColor(Settings.DEFAULT_COLOR);
			Point p = new Point(e);
			wd.setPosition(p);
			
			scene.getWorld().addObject(wd);
		}
	}
	
	public void mouseClicked(MouseEvent me) {}
	public void mouseEntered(MouseEvent me) {}
	public void mouseExited(MouseEvent me) {}
	public void mouseMoved(MouseEvent e) {}
}
