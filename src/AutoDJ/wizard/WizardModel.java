/**
 * WizardModel.java
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

package AutoDJ.wizard;

import java.util.HashMap;

/**
 * @author Florian Staudacher
 * 
 * WizardModel Class
 * Inspired by http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/
 *
 */
public class WizardModel {

	private WizardPanelDescriptor currentPanel;
    
    private HashMap<String, WizardPanelDescriptor> panelHashmap;
    
    /**
     * initialize members
     */
    public WizardModel() {
    	panelHashmap = new HashMap<String, WizardPanelDescriptor>();
    }
    
    /** 
     * Return the descriptor for the current panel
     * @return PanelDescriptor currentPanel 
     */
    public WizardPanelDescriptor getCurrentPanelDescriptor() {
        return currentPanel;
    }
    
    /**
     * register a new panel with the model
     * @param String id
     * @param WizardPanelDescriptor descriptor
     */
    public void registerPanel(String id, WizardPanelDescriptor descriptor) {
        //  Place a reference to it in a hashtable so we can access it later
        //  when it is about to be displayed.
        panelHashmap.put(id, descriptor);
        
    }  
    
    /**
     * Set a panel active by id
     * 
     * @param String id
     * @return boolean success
     * @throws Exception 
     */
    public boolean setCurrentPanel(String id) throws Exception {

        //  First, get the hashtable reference to the panel that should
        //  be displayed.
        WizardPanelDescriptor nextPanel =
            (WizardPanelDescriptor)panelHashmap.get(id);
        
        //  If we couldn't find the panel that should be displayed, return
        //  false.
        if (nextPanel == null)
            throw new Exception("Wizard Panel not found!");   

        currentPanel = nextPanel;
        
        return true;
    }
}
