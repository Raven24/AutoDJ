/**
 * WizardPanel.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author Florian Staudacher
 * 
 * This class provides a standard layout for wizard panels.
 * Subclass to customize 
 *
 */
public class WizardPanel extends JPanel {

	/**
	 * inherited from JPanel
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * gui components
	 */
	private JPanel title;
	private JPanel content;
	
	private JLabel titleText;
	
	/**
	 * constants
	 */
	public static final String DEFAULT_TITLE = "[title] change me!";
	
	/**
	 * constructor
	 * initialize the gui elements
	 */
	public WizardPanel() {
			
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(new Insets(10, 0, 10, 0)));
		
		content = new JPanel();
		
		title = new JPanel();
		title.setLayout(new BorderLayout());
		title.setBorder(new EmptyBorder(new Insets(0, 0, 10, 0)));
		
		titleText = new JLabel();
		titleText.setFont(titleText.getFont().deriveFont(14f));
		titleText.setBackground(Color.lightGray);
		titleText.setText(DEFAULT_TITLE);
		titleText.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
		titleText.setOpaque(true);
		
		title.add(titleText, BorderLayout.CENTER);
		
		add(title, BorderLayout.PAGE_START);
		add(content, BorderLayout.LINE_START);
		
		initComponents();
	}
	
	/**
	 * set the title text
	 * @param String s
	 */
	public void setTitle(String s) {
		titleText.setText(s);
	}
	
	/**
	 * set the main content to something
	 */
	public void setContent(Component comp) {
		content.add(comp, BorderLayout.LINE_START);
	}
	
	/**
	 * initialize the current panel
	 * reimplement to do actual stuff
	 */
	public void initComponents() {
		return;
	}
	
}
