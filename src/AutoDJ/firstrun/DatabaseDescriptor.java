/**
 * DatabaseDescriptor.java
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

import AutoDJ.wizard.WizardPanelDescriptor;

/**
 * @author Florian Staudacher
 *
 */
public class DatabaseDescriptor extends WizardPanelDescriptor {
	public static final String IDENTIFIER = "DATABASE_PANEL";
    
    public DatabaseDescriptor() {
        super(IDENTIFIER, new Database());
    }
    
    public String getNextPanelDescriptor() {
        return WizardPanelDescriptor.FINISH_IDENTIFIER;
    }
    
    public String getBackPanelDescriptor() {
        return FolderDescriptor.IDENTIFIER;
    }	
}
