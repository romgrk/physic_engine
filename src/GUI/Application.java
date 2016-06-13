package GUI;

import handlers.DrawCircleHandler;
import handlers.DrawLineHandler;
import handlers.DrawPolygonHandler;
import handlers.OptionPaneHandler;
import handlers.ParticleGeneratorHandler;
import handlers.RotationHandler;
import handlers.SelectionHandler;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import constants.Settings;
import data.Data;
import data.WorldData;

import layout.TableLayout;

import objects.WCircle;
import objects.WPolygon;
import objects.World;

import utils.F;
import utils.Point;

/**
 * This class extends JFrame and is the main Frame of the application
 * @author Yanick Sevigny
 *
 */

@SuppressWarnings("serial")
public class Application extends JFrame implements ComponentListener{
	
	int posX = Settings.APPLICATIONX;
	int posY = Settings.APPLICATIONY;
	
	private World world = new World();
	private Scene scene = new Scene(world);
	
	private ParticleGeneratorHandler particle	= new ParticleGeneratorHandler(scene);
	private SelectionHandler select = new SelectionHandler(scene);
	private DrawLineHandler lineDrawer = new DrawLineHandler(scene);
	private DrawPolygonHandler polygonDrawer = new DrawPolygonHandler(scene);
	private DrawCircleHandler circleDrawer = new DrawCircleHandler(scene);
	private OptionPaneHandler opHandler = new OptionPaneHandler(scene);
	private RotationHandler rotHandler = new RotationHandler(scene);
	
	private Container cont;
	
	private JPanel sceneinformations, options, statusPane;
	
	private ColorBox statusColorField;
	private Color stoppedStatusColor = Color.red, runningStatusColor = Color.green;
	private JButton toggleplaypauseButton;
	
	private InformationPanel informations;
	
	private double sizeCont[][] = {{TableLayout.FILL},{75,TableLayout.FILL, 30}};
	
	private ImageIcon 
	selectionIcon = null, 
	lineIcon = null, 
	polygonIcon = null, 
	circleIcon = null, 
	particleIcon = null, 
	playIcon = null, 
	pauseIcon = null;
	
	private JButton 
	selectionButton = null, 
	lineButton = null, 
	polygonButton = null, 
	circleButton = null,
	particleButton = null;
	
	private ColorChooser quickColor;
	private JSlider sldGravity;
	private JLabel lblGravity;
	
	private WPolygon 
	floor = null, 
	leftWall = null, 
	rightWall = null, 
	roof = null;
	
	private boolean 
	activeFloor = false, 
	activeRoof = false, 
	activeLeftWall = false, 
	activeRightWall = false;
	
	private JCheckBoxMenuItem toggleFloor, toggleRoof, toggleLeftWall, toggleRightWall;
	
	private SelectionAction selectionListener = new SelectionAction();
	private LineAction lineListener = new LineAction();
	private PolygonAction polygonListener = new PolygonAction();
	private CircleAction circleListener = new CircleAction();
	private ParticleAction particleListener = new ParticleAction();
	
	public Application(){
		
		particle.setActive(false);
		select.setActive(true);
		lineDrawer.setActive(false);
		polygonDrawer.setActive(false);
		circleDrawer.setActive(false);
		opHandler.setActive(true);
		rotHandler.setActive(true);
		
		informations = new InformationPanel(scene);
		select.setInformationPane(informations);
		scene.setInfoPane(informations);
		
		addComponentListener(this);
		
		setJMenuBar(initialiazeMenu());
		
		setBounds(Settings.APPLICATIONX, Settings.APPLICATIONY, 900, 700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		cont = getContentPane();
		cont.setLayout(new TableLayout(sizeCont));
		
		selectionIcon = new ImageIcon("curseur.png");
		selectionButton = new CoolButton();
		selectionButton.setPreferredSize(new Dimension(50,50));
		selectionButton.setIcon(selectionIcon);
		selectionButton.addActionListener(selectionListener);
		selectionButton.setEnabled(false);
		
		lineIcon = new ImageIcon("line.png");
		lineButton = new CoolButton();
		lineButton.setPreferredSize (new Dimension(50, 50));
		lineButton.setIcon(lineIcon);
		lineButton.addActionListener (lineListener);
		
		polygonIcon = new ImageIcon("polygon.png");
		polygonButton = new CoolButton();
		polygonButton.setPreferredSize (new Dimension(50, 50));
		polygonButton.setIcon(polygonIcon);
		polygonButton.addActionListener (polygonListener);
		
		circleIcon = new ImageIcon("circle.png");
		circleButton = new CoolButton();
		circleButton.setPreferredSize (new Dimension(50, 50));
		circleButton.setIcon(circleIcon);
		circleButton.addActionListener (circleListener);
		
		particleIcon = new ImageIcon("particles.png");
		particleButton = new CoolButton();
		particleButton.setPreferredSize (new Dimension(50, 50));
		particleButton.setIcon(particleIcon);
		particleButton.addActionListener(particleListener);
		
		quickColor = new ColorChooser(50, 50, Settings.DEFAULT_COLOR) {
			public void colorChanged (Color c) {
				Settings.DEFAULT_COLOR = 
					new Color(c.getRed(), c.getGreen(), c.getBlue(), 150);
			}
		};
		
		playIcon = new ImageIcon("play.png");
		pauseIcon = new ImageIcon("pause.png");
		toggleplaypauseButton = new CoolButton();
		toggleplaypauseButton.setIcon(playIcon);
		toggleplaypauseButton.setSize(50,50);
		toggleplaypauseButton.setPreferredSize(new Dimension(50, 50));
		toggleplaypauseButton.addActionListener(new PlayPauseAction());
		
		sldGravity = new JSlider(0, 15);
		lblGravity = new JLabel("Gravite : 8.0");
		sldGravity.addChangeListener(new GravityListener());
		
		options = new JPanel();
		double[][] optionSize = {
				{
				5, 50, 5, 50, 5, 50, 
				5, 50, 5, 50, 5, 50,
				15, 1,
				15, 50,
				15, 100,
				5, 100,
				TableLayout.FILL}, 
				{5, 50}};
		options.setLayout(new TableLayout(optionSize));
		
		
		options.add(selectionButton, "1, 1");
		options.add(lineButton, "3, 1");
		options.add(polygonButton, "5, 1");
		options.add(circleButton, "7, 1");
		options.add(particleButton, "9, 1");
		options.add(quickColor, "11, 1");
		options.add(new JSeparator(JSeparator.VERTICAL), "13, 1");
		options.add(toggleplaypauseButton, "15, 1");
		options.add(lblGravity, "17, 1");
		options.add(sldGravity, "19, 1");
		
		
		sceneinformations = new JPanel();
		sceneinformations.setLayout(new GridLayout(1,1));
		sceneinformations.add(scene);
		
		statusPane = new JPanel();
		statusPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusColorField = new ColorBox(stoppedStatusColor);
		statusColorField.setBackground(stoppedStatusColor);
		statusColorField.setEnabled(false);
		statusColorField.setPreferredSize(new Dimension(50, 20));
		statusPane.add(statusColorField);
		
		
		cont.add(options, "0,0");
		cont.add(sceneinformations,"0,1");
		cont.add(statusPane, "0,2");
		
		
		setVisible(true);
		world.stop();
	}
	
	/**
	 * This method is used to initialize the menu of the JFrame
	 * @return JMenuBar
	 */
	private JMenuBar initialiazeMenu() {
		/*Debut initialisation du Menu*/
		JMenuBar menu = new JMenuBar();
		
		JMenu file = new JMenu("Ficher");
		
		JMenu animation = new JMenu("Animation");
		
		JMenu mode = new JMenu("Mode d'utilisation");
		
		JMenu help = new JMenu("Aide");
		
		JCheckBoxMenuItem toggleGrid = new JCheckBoxMenuItem("Afficher/Cacher le quadrillage");
		toggleGrid.setSelected(true);
		toggleGrid.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent arg0) {
				scene.toggleGrid();
			}});
		
		JMenuItem save = new JMenuItem("Sauvegarder");
		save.setToolTipText("Permet de sauvegarder les objects presents");
		save.addActionListener(new SaveAction());
		
		JMenuItem load = new JMenuItem("Charger");
		load.setToolTipText("Permet de ramener des objects enregistres");
		load.addActionListener(new LoadAction());
		
		JMenuItem exit = new JMenuItem("Quitter");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}});
		
		toggleFloor = new JCheckBoxMenuItem("Plancher");
		toggleFloor.setToolTipText("Active ou desactive un plancher");
		toggleFloor.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem jcb = (JCheckBoxMenuItem)e.getSource();
				if(jcb.isSelected()){
					activeFloor = true;
				}else{
					activeFloor = false;
				}
				toggleFloor();
			}
		});
		
		toggleRoof = new JCheckBoxMenuItem("Plafond");
		toggleRoof.setToolTipText("Active ou desactive un plafond");
		toggleRoof.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem jcb = (JCheckBoxMenuItem)e.getSource();
				if(jcb.isSelected()){
					activeRoof = true;
				}else{
					activeRoof = false;
				}
				toggleRoof();
			}
		});
		
		toggleLeftWall = new JCheckBoxMenuItem("Mur de gauche");
		toggleLeftWall.setToolTipText("Active ou desactive un mur de gauche");
		toggleLeftWall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem jcb = (JCheckBoxMenuItem)e.getSource();
				if(jcb.isSelected()){
					activeLeftWall = true;
				}else{
					activeLeftWall = false;
				}
				toggleLeftWall();
			}
		});
		
		toggleRightWall = new JCheckBoxMenuItem("Mur de droite");
		toggleRightWall.setToolTipText("Active ou desactive un mur de droite");
		toggleRightWall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JCheckBoxMenuItem jcb = (JCheckBoxMenuItem)e.getSource();
				if(jcb.isSelected()){
					activeRightWall = true;
				}else{
					activeRightWall = false;
				}
				toggleRightWall();
			}
		});
		
		JMenuItem reeinitialize = new JMenuItem("Reeinitialiser");
		reeinitialize.setToolTipText("Permet de revenir a l'etat originel de l'application");
		reeinitialize.addActionListener(new ActionListener(){		
			public void actionPerformed(ActionEvent e) {
			world.clear();
			toggleFloor.setSelected(false);
			toggleRoof.setSelected(false);
			toggleLeftWall.setSelected(false);
			toggleRightWall.setSelected(false);
		}});
		
		JMenuItem selection = new JMenuItem("Selection");
		selection.setToolTipText("Permet de selectionner un object");
		selection.addActionListener(selectionListener);
		
		JMenuItem line = new JMenuItem("Ligne");
		line.setToolTipText("Permet de cree une nouvelle ligne");
		line.addActionListener(lineListener);

		JMenuItem polygon = new JMenuItem("Polygon");
		polygon.setToolTipText("Permet de cree un nouveau polygone");
		polygon.addActionListener(polygonListener);
		
		JMenuItem circle = new JMenuItem("Circle");
		circle.setToolTipText("Permet de cree un nouveau cercle");
		circle.addActionListener(circleListener);
		
		JMenuItem particleGen = new JMenuItem("Particule");
		particleGen.setToolTipText("Permet de genere des particules");
		particleGen.addActionListener(particleListener);
		
		JMenuItem helpPane = new JMenuItem("A Propos");
		helpPane.setToolTipText("Fourni des informations sur l'utilisation");
		helpPane.addActionListener(new HelpAction());
		
		file.add(toggleGrid);
		file.add(save);
		file.add(load);
		file.add(exit);
		menu.add(file);
		
		//animation.add(playpause);
		animation.add(toggleRoof);
		animation.add(toggleFloor);
		animation.add(toggleLeftWall);
		animation.add(toggleRightWall);
		animation.add(reeinitialize);
		menu.add(animation);
		
		mode.add(selection);
		mode.add(line);
		mode.add(polygon);
		mode.add(circle);
		mode.add(particleGen);
		menu.add(mode);
		
		help.add(helpPane);
		menu.add(help);
		
		return menu;
		/*Fin initialisation du Menu*/
	}
	
	public void toggleFloor(){
		world.removeObject(floor);
		if(activeFloor){
			ArrayList<Point> pts = new ArrayList<Point>();
			
			pts.add(new Point(0, 0));
			pts.add(new Point(0, 50));
			pts.add(new Point(scene.getWidth(), 50));
			pts.add(new Point(scene.getWidth(), 0));
			
			floor = new WPolygon(pts);
			floor.setColor(new Color(230, 230, 30, 50));
			floor.setFixed(true);
			world.addObject(floor);
		}
	}
	
	public void toggleRoof(){
		world.removeObject(roof);
		if(activeRoof){
			ArrayList<Point> pts = new ArrayList<Point>();
			
			pts.add(new Point(0, scene.getHeight()));
			pts.add(new Point(0, scene.getHeight()-50));
			pts.add(new Point(scene.getWidth(),scene.getHeight()-50));
			pts.add(new Point(scene.getWidth(), scene.getHeight()));
			
			roof = new WPolygon(pts);
			roof.setColor(new Color(230, 230, 30, 50));
			roof.setFixed(true);
			world.addObject(roof);
		}
	}
	
	public void toggleLeftWall(){
		world.removeObject(leftWall);
		if(activeLeftWall){
			ArrayList<Point> pts = new ArrayList<Point>();
			
			pts.add(new Point(0, 50));
			pts.add(new Point(0, scene.getHeight()-50));
			pts.add(new Point(50,50));
			pts.add(new Point(50, scene.getHeight()-50));

			leftWall = new WPolygon(pts);
			leftWall.setColor(new Color(230, 230, 30, 50));
			leftWall.setFixed(true);
			world.addObject(leftWall);
		}
	}
	
	public void toggleRightWall(){
		world.removeObject(rightWall);
		if(activeRightWall){
			ArrayList<Point> pts = new ArrayList<Point>();
			
			pts.add(new Point(scene.getWidth()-50, 50));
			pts.add(new Point(scene.getWidth()-50, scene.getHeight()-50));
			pts.add(new Point(scene.getWidth(),50));
			pts.add(new Point(scene.getWidth(), scene.getHeight()-50));

			rightWall = new WPolygon(pts);
			rightWall.setColor(new Color(230, 230, 30, 50));
			rightWall.setFixed(true);
			world.addObject(rightWall);
		}
	}
	
	private class GravityListener implements ChangeListener{
		public void stateChanged(ChangeEvent arg0) {
			JSlider source = (JSlider) arg0.getSource();
			if(source.getValueIsAdjusting()){
				double gravityValue = Math.round(source.getValue()*100)*0.01;
				lblGravity.setText("Gravite : " + gravityValue);
				scene.getWorld().setGravity(-gravityValue*10);
			}
		}
	}
	
	public class HelpAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
		}
	}

	public class SelectionAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			particle.setActive(false);
			select.setActive(true);
			lineDrawer.setActive(false);
			polygonDrawer.setActive(false);
			circleDrawer.setActive(false);
			opHandler.setActive(true);
			
			selectionButton.setEnabled(false);
			lineButton.setEnabled(true);
			polygonButton.setEnabled(true);
			circleButton.setEnabled(true);
			particleButton.setEnabled(true);
		}
	}
	public class LineAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			particle.setActive(false);
			select.setActive(false);
			lineDrawer.setActive(true);
			polygonDrawer.setActive(false);
			circleDrawer.setActive(false);
			opHandler.setActive(false);
			
			selectionButton.setEnabled(true);
			lineButton.setEnabled(false);
			polygonButton.setEnabled(true);
			circleButton.setEnabled(true);
			particleButton.setEnabled(true);
		}
	}
	public class PolygonAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			particle.setActive(false);
			select.setActive(false);
			lineDrawer.setActive(false);
			polygonDrawer.setActive(true);
			circleDrawer.setActive(false);
			opHandler.setActive(false);
			
			selectionButton.setEnabled(true);
			lineButton.setEnabled(true);
			polygonButton.setEnabled(false);
			circleButton.setEnabled(true);
			particleButton.setEnabled(true);
		}
	}
	public class CircleAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			particle.setActive(false);
			select.setActive(false);
			lineDrawer.setActive(false);
			polygonDrawer.setActive(false);
			circleDrawer.setActive(true);
			opHandler.setActive(false);
			
			selectionButton.setEnabled(true);
			lineButton.setEnabled(true);
			polygonButton.setEnabled(true);
			circleButton.setEnabled(false);
			particleButton.setEnabled(true);
		}
	}
	
	public class ParticleAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			particle.setActive(true);
			select.setActive(false);
			lineDrawer.setActive(false);
			polygonDrawer.setActive(false);
			circleDrawer.setActive(false);
			opHandler.setActive(false);
			
			selectionButton.setEnabled(true);
			lineButton.setEnabled(true);
			polygonButton.setEnabled(true);
			circleButton.setEnabled(true);
			particleButton.setEnabled(false);
		}
	}
	
	public class PlayPauseAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton playpause = (JButton)(e.getSource());
			if(world.isRunning()){
				world.stop();
				playpause.setIcon(playIcon);
				statusColorField.setColor(stoppedStatusColor);
			}else{
				world.start();
				playpause.setIcon(pauseIcon);
				statusColorField.setColor(runningStatusColor);
			}
		}
	}
	
	public class SaveAction implements ActionListener{
		public void actionPerformed(ActionEvent e){
			World world = scene.getWorld();
			boolean running = world.isRunning();
			
			if (running) {
				toggleplaypauseButton.doClick();
			}
			
			String filename = JOptionPane.showInputDialog("Save as...");
			if (filename == null) {
				return;
			}
			if (filename.equals("")) {
				filename = "test";
			}
			filename += ".wd";
			WorldData data = new WorldData();
			data.gravity = world.getGravity();
			data.objects = world.getObjects();
			
			Data.save(data, Settings.DATA_DIRECTORY + "\\" + filename);
			
		}
	}
	
	public class LoadAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			World world = scene.getWorld();
			boolean running = world.isRunning();
			
			if (running) {
				toggleplaypauseButton.doClick();
			}
			
			File wdir = new File(Settings.DATA_DIRECTORY);
			String[] files = wdir.list(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".wd");
				}
			});
			if (files.length > 0) {
				String fileName = (String) JOptionPane.showInputDialog(
	                    null,
	                    "Choose world to load.",
	                    "Load",
	                    JOptionPane.PLAIN_MESSAGE,
	                    null,
	                    files,
	                    files[0]);
				if (fileName == null) {
					return;
				}
				WorldData data = Data.load(Settings.DATA_DIRECTORY + "\\" + fileName);
				Data.match(data, world);
				
				scene.repaint();
				
			} else {
				JOptionPane.showMessageDialog(null,
					    "No World file was found in the current directory.",
					    "Load",
					    JOptionPane.WARNING_MESSAGE);
				return;
			}
			
		}
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentShown(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {
		Settings.APPLICATIONX = getX();
		Settings.APPLICATIONY = getY();
	}
	public void componentResized(ComponentEvent arg0) {
		toggleFloor();
		toggleRoof();
		toggleLeftWall();
		toggleRightWall();
	}
}
