/**
 * WizardController.java
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

import java.awt.event.ActionListener;

/**
 * @author Florian Staudacher
 * 
 * WizardController Class
 * Inspired by http://java.sun.com/developer/technicalArticles/GUI/swing/wizard/
 *
 */
public class WizardController 
	implements ActionListener {
	
	private Wizard wizard;
	
	/**
	 * constructor 
	 * 
	 * @param Wizard w
	 */
	public WizardController(Wizard w) {
        wizard = w;
    }
	
	/**
	 * listen for performed actions (pressed buttons)
	 * @param ActionEvent evt 
	 */
	public void actionPerformed(java.awt.event.ActionEvent evt) {
		if (evt.getActionCommand().equals(Wizard.CANCEL_ACTION))
			cancelButtonPressed();
		else if (evt.getActionCommand().equals(Wizard.BACK_ACTION))
			backButtonPressed();
		else if (evt.getActionCommand().equals(Wizard.NEXT_ACTION))
			nextButtonPressed();
		
		resetButtonsToPanelRules();
	}
	
	/**
	 * cancel the wizard
	 */
	private void cancelButtonPressed() {
        wizard.close(Wizard.CANCEL_RETURN_CODE);
    }
	
	/**
	 * go to the next panel
	 */
	private void nextButtonPressed() {
        WizardModel model = wizard.getModel();
        WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
        
        //  If it is a finishable panel, close down the dialog. Otherwise,
        //  get the ID that the current panel identifies as the next panel,
        //  and display it.
        
        String nextPanelDescriptor = descriptor.getNextPanelDescriptor();
        
        if (nextPanelDescriptor.equals(WizardPanelDescriptor.FINISH_IDENTIFIER) ) {
            wizard.close(Wizard.FINISH_RETURN_CODE);
        } else {        
            try {
				wizard.setCurrentPanel(nextPanelDescriptor);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
	
	/**
	 * go to the previous panel
	 */
	private void backButtonPressed() {
        WizardModel model = wizard.getModel();
        WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
 
        //  Get the descriptor that the current panel identifies as the previous
        //  panel, and display it.
        String backPanelDescriptor = descriptor.getBackPanelDescriptor();        
        try {
			wizard.setCurrentPanel(backPanelDescriptor);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	void resetButtonsToPanelRules() {
        //  Reset the buttons to support the original panel rules,
        //  including whether the next or back buttons are enabled or
        //  disabled, or if the panel is finishable.
		
        WizardModel model = wizard.getModel();
        WizardPanelDescriptor descriptor = model.getCurrentPanelDescriptor();
        if(descriptor == null) 
        	return;
        
        //  If the panel in question has another panel behind it, enable
        //  the back button. Otherwise, disable it.
        if (descriptor.getBackPanelDescriptor() != null)
            wizard.setBackButtonEnabled(true);
        else
        	wizard.setBackButtonEnabled(false);

        //  If the panel in question has one or more panels in front of it,
        //  enable the next button. Otherwise, disable it.
        if (descriptor.getNextPanelDescriptor() != null)
        	wizard.setNextButtonEnabled(true);
        else
        	wizard.setNextButtonEnabled(false);
 
        //  If the panel in question is the last panel in the series, change
        //  the Next button to Finish. Otherwise, set the text back to Next.
        if (descriptor.getNextPanelDescriptor() != null && descriptor.getNextPanelDescriptor().equals(WizardPanelDescriptor.FINISH_IDENTIFIER)) {
        	wizard.setNextButtonText(Wizard.FINISH_TEXT);
        } else {
        	wizard.setNextButtonText(Wizard.NEXT_TEXT);
        }        
    }
}
