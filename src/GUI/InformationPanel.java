package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import layout.TableLayout;

import constants.Settings;
import constants.WType;

import objects.WObject;
import utils.Angle;
import utils.F;

/**
 * This class shows relevant information about the selected object.
 * @see Settings
 */
public class InformationPanel extends JDialog implements UpdateEventListener, ActionListener {
	private static final long serialVersionUID = -2288433349098188732L;
	
	private Scene scene;
	
	private JLabel lblType = new JLabel("Type : ");
	private JLabel lblSpeed = new JLabel("Vitesse : ");
	private JLabel lblPosition = new JLabel("Position : ");
	private JLabel lblRotation = new JLabel("Rotation : ");
	private JLabel lblOrientation = new JLabel("Orientation : ");
	private JLabel lblMass = new JLabel("Masse : ");
	private JLabel lblInertia = new JLabel("Inertie : ");
	private JLabel lblRestitution = new JLabel("Rebond : ");
	private JLabel lblFriction = new JLabel("Friction : ");
	
	private JLabel txtType = new JLabel("");
	private JLabel txtSpeed = new JLabel("");
	private JLabel txtPosition = new JLabel("");
	private JTextField txtRotation = new JTextField("");
	private JTextField txtOrientation = new JTextField("");
	private JTextField txtMass = new JTextField("");
	private JTextField txtInertia = new JTextField("");
	private JTextField txtRestitution = new JTextField("");
	private JTextField txtFriction = new JTextField("");
	
	private JCheckBox chkMove = new JCheckBox("Move?");
	private JCheckBox chkSpin = new JCheckBox("Spin?");
	
	public InformationPanel (Scene s) {
		
		scene = s;
		scene.addUpdateEventListener(this);
		
		setBounds(930, 100, 300, 500);
		setAlwaysOnTop(true);
		setResizable(false);
		
		double[][] boxSize = {
				{10, TableLayout.FILL, 10, }, 
				{10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10, 25, 10}
		};
		JPanel panel = new JPanel(new TableLayout(boxSize));
		
		Box boxType = Box.createHorizontalBox();
		boxType.add(lblType);
		boxType.add(txtType);
		
		Box boxSpeed = Box.createHorizontalBox();
		boxSpeed.add(lblSpeed);
		boxSpeed.add(txtSpeed);
		
		Box boxPosition = Box.createHorizontalBox();
		boxPosition.add(lblPosition);
		boxPosition.add(txtPosition);
		
		Box boxRotation = Box.createHorizontalBox();
		boxRotation.add(lblRotation);
		boxRotation.add(txtRotation);
		
		Box boxOrientation = Box.createHorizontalBox();
		boxOrientation.add(lblOrientation);
		boxOrientation.add(txtOrientation);
		
		Box boxFix = Box.createHorizontalBox();
		boxFix.add(chkMove);
		boxFix.add(chkSpin);
		
		Box boxMass = Box.createHorizontalBox();
		boxMass.add(lblMass);
		boxMass.add(txtMass);
		
		Box boxInertia = Box.createHorizontalBox();
		boxInertia.add(lblInertia);
		boxInertia.add(txtInertia);
		
		Box boxRestitution = Box.createHorizontalBox();
		boxRestitution.add(lblRestitution);
		boxRestitution.add(txtRestitution);
		
		Box boxFriction = Box.createHorizontalBox();
		boxFriction.add(lblFriction);
		boxFriction.add(txtFriction);
		
		panel.add(boxType, "1, 1");
		panel.add(boxSpeed, "1, 3");
		panel.add(boxPosition, "1, 5");
		panel.add(boxRotation, "1, 7");
		panel.add(boxOrientation, "1, 9");
		panel.add(boxFix, "1, 11");
		panel.add(boxMass, "1, 13");
		panel.add(boxInertia, "1, 15");
		panel.add(boxRestitution, "1, 17");
		panel.add(boxFriction, "1, 19");
		
		panel.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(5, 5, 5, 5), 
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.BLACK, 2),
								"Information panel"))
		);
		
		setContentPane(panel);
		
		// Action listeners
		chkMove.addActionListener(this);
		chkSpin.addActionListener(this);
		txtMass.addActionListener(this);
		txtRestitution.addActionListener(this);
		txtFriction.addActionListener(this);
		txtInertia.addActionListener(this);
		txtRotation.addActionListener(this);
		txtOrientation.addActionListener(this);
	}
	
	public void getInfo (WObject w) {
		txtType.setText((w.getType() == WType.CIRCLE)? "cercle" : "polygone");
		txtSpeed.setText(
				"(" 
				+ Math.round(w.getSpeed().getX())
				+ ", "
				+ Math.round(w.getSpeed().getY())
				+ ")"
		);
		txtPosition.setText("(" 
				+ Math.round(w.getPosition().getX())
				+ ", "
				+ Math.round(w.getPosition().getY())
				+ ")"
		);
		chkMove.setSelected(!w.isUnmovable());
		chkSpin.setSelected(!w.isUnspinnable());
		txtMass.setText(""+w.getMass());
		txtRestitution.setText(""+w.getRestitution());
		txtFriction.setText(""+w.getFriction());
		txtInertia.setText(""+w.getMoI());
		txtRotation.setText(""+w.getRotation().value());
		txtOrientation.setText(""+w.getOrientation().value());
	}
	
	public void refresh () {
		if (Settings.selectedObject != null)
			getInfo(Settings.selectedObject);
	}
	
	public void physicUpdate(UpdateEvent e) {
		refresh();
	}
	
	public void actionPerformed(ActionEvent e) {
		
		Object source = e.getSource();
		
		if (Settings.selectedObject == null) {
			return;
		}
		
		if (txtMass == source) {
			Settings.selectedObject.setMass(
					Double.parseDouble(txtMass.getText())
			);
		} else if (txtRestitution == source) {
			Settings.selectedObject.setRestitution(
					Double.parseDouble(txtRestitution.getText())
			);
		} else if (txtFriction == source) {
			Settings.selectedObject.setFriction(
					Double.parseDouble(txtFriction.getText())
			);
		} else if (txtInertia == source) {
			Settings.selectedObject.setMoI(
					Double.parseDouble(txtInertia.getText())
			);
		} else if (txtRotation == source) {
			Settings.selectedObject.setRotation(
					Double.parseDouble(txtRotation.getText())
			);
		} else if (txtOrientation == source) {
			Settings.selectedObject.setOrientation(
					new Angle(Double.parseDouble(txtOrientation.getText()))
			);
		} else if (chkMove == source) {
			Settings.selectedObject.setNoMove(
					!chkMove.isSelected()
			);
		} else if (chkSpin == source) {
			Settings.selectedObject.setNoSpin(
					!chkSpin.isSelected()
			);
		}
		
		refresh();
	}
}
