package objects;

import java.awt.Color;

import utils.Angle;
import utils.Point;
import utils.Segment;
import utils.Vector;

import GUI.WDrawer;

/**
 * Representation of a circle in the World.
 * @author Romain
 *
 */
public class WCircle extends WObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6495571785713605049L;

	public WCircle (double r) {
		m_flags[index] |= TYPE_CIRCLE;
		m_radius[index] = r;
		init();
	}
	
	public boolean contains (Point p) {
		return worldToLocal(p).squareDistanceTo(Point.ORIGIN) < m_radius[index]*m_radius[index];
	}
	
	public void paint (WDrawer w) {
		
		Point position = getPosition();
		//F.s(position+"");
		w.setStroke(2);
		w.setColor(color);
		w.fillCircle(position, m_radius[index]);
		w.setColor(Color.BLACK);
		w.drawCircle(position, m_radius[index]);
		
		Vector axe1 = new Vector(new Angle(m_orientation[index]));
		axe1.multiply(m_radius[index]);
		Vector axe2 = axe1.getRightNormal();
		w.drawLine(position.copy().add(axe1), position.copy().add(axe1.getInverseVector()));
		w.drawLine(position.copy().add(axe2), position.copy().add(axe2.getInverseVector()));
	}
	
	public double calculateArea() {
		return m_radius[index]*m_radius[index]*Math.PI;
	}
	
	public double calculateMoI () {
		return m_mass[index] * m_radius[index]*m_radius[index] / 2;
	}
	
	public int getVertexCount() { return 1; }

	
	public Vector getNormal(int i) {
		return new Vector();
	}

	@Override
	public Point getPoint(int i) {
		return getPosition();
	}

	@Override
	public Point getExtremePoint(Vector v) {
		return getPosition().add(v.getNormalizedVector().multiply(m_radius[index]));
	}

	@Override
	public Segment getEdge(int i) {
		return new Segment();
	}
	
	public WObject getCopy(){
		WObject copy = new WCircle(m_radius[index]);
		copy.setColor(color);
		copy.setMass(m_mass[index]);
		copy.setOrientation(new Angle(m_orientation[index]));
		copy.setNoMove(isUnmovable());
		copy.setNoSpin(isUnspinnable());
		copy.setDensity(m_density[index]);
		copy.setFriction(m_friction[index]);
		copy.setRestitution(m_restitution[index]);
		copy.setSpeed(new Vector(m_speed[index],m_speed[index+1]));
		copy.setRotation(new Angle(m_rotation[index]));
		return copy;
	}
}
