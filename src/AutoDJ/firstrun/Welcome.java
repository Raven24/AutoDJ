/**
 * Welcome.java
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

import javax.swing.JLabel;
import javax.swing.JPanel;

import AutoDJ.wizard.WizardPanel;

/**
 * @author Florian Staudacher
 * 
 * wizard panel that shows a welcome message
 *
 */
public class Welcome extends WizardPanel {

	/**
	 * inherited from JPanel
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * initialize the panel
	 */
	public Welcome() {
		super();
		
		setTitle("Hello!");
	}
	
	/**
	 * put together the gui
	 */
	public void initComponents() {
		JPanel first = new JPanel();
		first.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// add welcome text to layout
		JLabel welcomeText = new JLabel("<html>You appear to be using AutoDJ for the first time.<br><br>" +
				"This short wizard will take you through the initial steps <br>" +
				"to get this program working for your setup.<br><br>" +
				"Click 'next' to continue.</html>");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		first.add(welcomeText, c);
		
		setContent(first);
	}
}
