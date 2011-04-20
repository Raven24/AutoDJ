import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Vector;

import javax.swing.UIManager;

public class AutoDJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// try to use systems look and feel for GUI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		AutoDJGUI gui = new AutoDJGUI("AutoDJ v0.1");
		gui.setLocation(400, 250);
		gui.setSize(600, 400);
		gui.setVisible(true);
		
		AutoDJCore core = new AutoDJCore();
		
		gui.addObserver(core);
	}
}
