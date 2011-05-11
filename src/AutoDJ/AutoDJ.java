/**
 * AutoDJ.java
 * (C) 2011 Florian Staudacher, Christian Wurst
 * 
 * This file is part of AutoDJ.
 *
 * AutoDJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoDJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AutoDJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package AutoDJ;

import javax.swing.UIManager;

/**
 * AutoDJ is a program to play MP3 files. It remembers which files users
 * played in what order and how often and saves this information to a MySQL
 * database. Because of this the database not only stores the available
 * MP3 files, but also in which combinations they can be played. You could
 * call this a "dynamic playlist".
	
 * Later it uses this information to automatically determine which file to
 * play after the current file without any user interference. Given enough
 * information it behaves like a DJ, hence the name.
 *
 * @author Florian Staudacher, Christian Wurst
 * @version 0.1
 */

public class AutoDJ {
	/**
	 * The program name and version number.
	 */
	public static final String VERSION = "AutoDJ v0.1";
	
	/**
	 * @param args command line parameters (currently not in use)
	 */
	public static void main(String[] args) {
		// try to use systems look and feel for GUI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// create a new model
		AutoDJModel model = new AutoDJModel();
		
		// create a new controller
		AutoDJController controller = new AutoDJController(model);
		
		// create a new view (GUI)
		AutoDJView view = new AutoDJView(VERSION);
		view.setLocation(400, 250);
		view.setSize(600, 400);
		view.setVisible(true);

		// controller observes view
		view.addObserver(controller);
		
		// view observes model
		model.addObserver(view);
		
		// get initial song library
		controller.filterSongLibrary("");
		
		// done starting up
		model.setLogtext(VERSION + " started!");
	}
}
