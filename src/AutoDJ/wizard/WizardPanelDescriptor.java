/**
 * WizardPanelDescriptor.java
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

import java.awt.Component;

import javax.swing.JPanel;

/**
 * @author Florian Staudacher
 * 
 * WizardPanelDescriptor Class
 * Inspired by http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/
 *
 */
public class WizardPanelDescriptor {
	
	public static final String DEFAULT_PANEL_IDENTIFIER = "defaultPanelIdentifier";
	public static final String FINISH_IDENTIFIER = "thisIsTheAwesomeFinishIdentifier";
	
	private Wizard wizard;
    private Component targetPanel;
    private String panelIdentifier;
    
    public boolean visited = false;
    
    /**
     * default constructor
     * creates an empty panel
     */
    public WizardPanelDescriptor() {
        panelIdentifier = DEFAULT_PANEL_IDENTIFIER;
        targetPanel = new JPanel();
    }
    
    /**
     * constructor
     * initializes members with given parameters
     */
    public WizardPanelDescriptor(String id, Component panel) {
        panelIdentifier = id;
        targetPanel = panel;
    }
    
    /**
     * returns the panel component
     * @return Component targetPanel
     */
    public final Component getPanelComponent() {
        return targetPanel;
    }
    
    /**
     * returns the panel descriptor
     * @return String panelIdentifier
     */
    public final String getPanelDescriptorIdentifier() {
        return panelIdentifier;
    }
    
    /**
     * Set the wizard we are working with 
     * @param Wizard w
     */
    public final void setWizard(Wizard w) {
        wizard = w;
    }
    
    /**
     * get the wizard we are working with
     * @return Wizard w
     */
    public final Wizard getWizard() {
        return wizard;
    } 
    
    /**
     * Get the model of the wizard we are working with
     * @return WizardModel model
     */
    public WizardModel getWizardModel() {
        return wizard.getModel();
    }
    
    /**
     * override this method to specify next panel descriptor
     * @return null
     */
    public String getNextPanelDescriptor() {
        return null;
    }
 
    /**
     * Override this method to specify back panel descriptor
     * @return null
     */
    public String getBackPanelDescriptor() {
        return null;
    }
}
