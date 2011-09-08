/**
 * Folder.java
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

package AutoDJ.firstrun;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import AutoDJ.prefs.Settings;
import AutoDJ.wizard.WizardPanel;

/**
 * @author Florian Staudacher
 * 
 * This wizard panel asks the user for the location of his music library
 * 
 */
public class Folder extends WizardPanel implements ActionListener {

	/**
	 * inherited from JPanel
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel libPathLbl;
	private String libPath;
	private JFileChooser fileChooser;
	
	/**
	 * initialize panel
	 */
	public Folder() {
		super();
		setTitle("Music Library");
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}
	
	/**
	 * build the gui components and form logic
	 */
	public void initComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		// add explanatory text to layout
		JLabel explanationText = new JLabel("<html>Select the root folder of your music library.<br><br>" +
				"We will also be indexing subfolders. <br>" +
				"Currently, the supported filetypes are MP3 and OGG Vorbis<br><br></html");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		panel.add(explanationText, c);
		
		// add label for file path to layout
		libPath = Settings.get("mp3Dir", "[nothing selected]");
		libPathLbl = new JLabel(libPath);
		c.gridy = 2;
		c.insets = new Insets(30, 0, 40, 0);
		panel.add(libPathLbl, c);
		
		// add "select" button to layout
		JButton fileChooser = new JButton("Select");
		fileChooser.addActionListener(this);
		c.gridx = 2;
		c.insets = new Insets(32, 0, 30, 0);
		panel.add(fileChooser, c);
		
		// reset constraints
		c.gridx = 1;
		c.insets = new Insets(0,0,0,0);

		// add final text to layout
		JLabel continueText = new JLabel("<html>When you are finished, click 'next' to continue.</html>");
		c.gridy = 4;
		panel.add(continueText, c);
		
		setContent(panel);
	}

	/**
	 * action listener for the 'select folder' button
	 * shows the file dialog to select a music library folder
	 * and saves the results in the settings
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		int retVal = fileChooser.showOpenDialog(this);
		
		if (retVal == JFileChooser.APPROVE_OPTION) {
            libPath = fileChooser.getSelectedFile().getPath();
            libPathLbl.setText(libPath);
            Settings.set("mp3Dir", libPath);
		}
	}

}
