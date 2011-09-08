/**
 * Firstrun.java
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

import AutoDJ.wizard.Wizard;
import AutoDJ.wizard.WizardPanelDescriptor;

/** 
 * @author Florian Staudacher
 *
 * This class shows a wizard dialogue that is intended to
 * determine the necessary settings on the first run of AutoDJ
 * 
 */
public class Firstrun {
	
	private Wizard wizard;
	
	/**
	 * constructor, initialize members and set defaults
	 */
	public Firstrun() {
		wizard = new Wizard();
		wizard.setTitle("Welcome to AutoDJ!");
		
		initComponents();
	}
	
	/**
	 * Initialize and register the wizard panels
	 */
	private void initComponents() {
		WizardPanelDescriptor welcomeDescr = new WelcomeDescriptor();
        wizard.registerWizardPanel(WelcomeDescriptor.IDENTIFIER, welcomeDescr);
        
        WizardPanelDescriptor folderDescr = new FolderDescriptor();
        wizard.registerWizardPanel(FolderDescriptor.IDENTIFIER, folderDescr);

        WizardPanelDescriptor dbDescr = new DatabaseDescriptor();
        wizard.registerWizardPanel(DatabaseDescriptor.IDENTIFIER, dbDescr);
	}
	
	/**
	 * Begins the first step and returns the wizard result
	 * 
	 * @return Integer wizard status
	 */
	public int begin() {
		try {
			wizard.setCurrentPanel(WelcomeDescriptor.IDENTIFIER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int ret = wizard.showModalDialog();
				
		if(ret == Wizard.CANCEL_RETURN_CODE) {
			// TODO: handle this case more gracefully...
			System.exit(0);
		}
		
		return ret;
	}
	
}
