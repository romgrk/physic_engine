package start;

import javax.swing.UIManager;

import GUI.Application;

public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());  
		}  
		catch(Exception e) {}
		
		Application app = new Application();
		app.setVisible(true);
	}
}
