/**
 * Database.java
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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import AutoDJ.prefs.Settings;
import AutoDJ.wizard.WizardPanel;

/**
 * @author Florian Staudacher
 * 
 * This wizard panel asks the user for his database preference
 * and (if necessary) the needed credentials
 *
 */
public class Database extends WizardPanel implements ActionListener, FocusListener {

	/**
	 * inherited from JPanel
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * gui components
	 */
	JComboBox dbTypeDropdown;
	JPanel dbOptions;
	JLabel sqlitePathLbl;
	JFileChooser fileChooser;
	JButton fileChooserBtn;
	CardLayout cardLayout;     
	
	JPanel sqliteOptions;
	JPanel mysqlOptions;
	
	HashMap<String, JTextField> mysqlCredentials;
	
	/**
	 * values
	 */
	String dbType;
	String sqlitePath;
		
	/**
	 * initialize panel
	 */
	public Database() {
		super();
		setTitle("Music Library");
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		initDbOptions();
	}
	
	/**
	 * build the gui components and form
	 */
	public void initComponents() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// add explanatory text to layout
		JLabel explanationText = new JLabel("<html>Enter the database you want to use.</html");
		c.anchor = GridBagConstraints.LINE_START;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 4;
		panel.add(explanationText, c);
		c.gridwidth = 1; // reset
		
		// type dropdown
		dbType = Settings.get("dbType", "...");
		Vector<String> dbTypes = new Vector<String>();
		dbTypes.add("...");
		dbTypes.add("sqlite");
		dbTypes.add("mysql");
		int selIndex = dbTypes.indexOf(dbType);
		
		dbTypeDropdown = new JComboBox(dbTypes);
		dbTypeDropdown.setSelectedIndex(selIndex);
		dbTypeDropdown.addActionListener(this);		
		c.gridy = 2;
		c.insets = new Insets(30, 0, 40, 20);
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		panel.add(dbTypeDropdown, c);
		
		// additional db option panel
		dbOptions = new JPanel();
		dbOptions.setLayout(new FlowLayout(FlowLayout.LEADING));
		c.gridx = 2;
		c.insets = new Insets(30, 0, 10, 0);
		panel.add(dbOptions, c);
		
		// add final text to layout
		JLabel continueText = new JLabel("<html>When you are finished, click 'finish' to start AutoDJ.</html>");
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 4;
		panel.add(continueText, c);
		
		setContent(panel);
	}

	/**
	 * save the selected db type in the settings
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		if( evt.getSource().equals(dbTypeDropdown)) {
			dbType = dbTypeDropdown.getSelectedItem().toString();
			Settings.set("dbType", dbType);
			
			showDbOptions();
		}
		if( evt.getSource().equals(fileChooserBtn)) {
			int retVal = fileChooser.showOpenDialog(this);
			
			if (retVal == JFileChooser.APPROVE_OPTION) {
	            sqlitePath = fileChooser.getSelectedFile().getPath();
	            sqlitePathLbl.setText(sqlitePath);
	            Settings.set("dbPath", sqlitePath);
			}
		}
	}
	
	/**
	 * populate panels with additional db options
	 * 
	 * @param String type
	 */
	private void initDbOptions() {
		Vector<JPanel> panels = new Vector<JPanel>();
		
		panels.add(initSqliteDbOptions());
		panels.add(initMysqlDbOptions());

		Dimension dim = dbOptions.getSize();
		
		Iterator<JPanel> itr = panels.iterator();
		while(itr.hasNext()) {
			JPanel p = itr.next();
			Dimension d = p.getPreferredSize();
			if( d.height > dim.height ) dim.height = d.height + 10;
			if( d.width > dim.width ) dim.width = d.width + 10;
		}
		
		dbOptions.setPreferredSize(dim);
		
		showDbOptions();
	}
	
	/**
	 * inizialize the sqlite option pane
	 */
	private JPanel initSqliteDbOptions() {
		sqliteOptions = new JPanel();
		sqliteOptions.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// add explanation
		JLabel sqliteLbl = new JLabel("<html>Specify the desired location of the database file</html>");
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		sqliteOptions.add(sqliteLbl, c);
		c.gridwidth = 1; // reset
		
		// add label for file path to layout
		sqlitePath = Settings.get("dbPath", "[nothing selected]");
		sqlitePathLbl = new JLabel(sqlitePath);
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(10, 0, 10, 20);
		sqliteOptions.add(sqlitePathLbl, c);
		
		// add "select" button to layout
		fileChooserBtn = new JButton("Select");
		fileChooserBtn.addActionListener(this);
		c.gridx = 3;
		c.insets = new Insets(10, 0, 10, 0);
		sqliteOptions.add(fileChooserBtn, c);
		
		return sqliteOptions;
	}
	
	/**
	 * initialize the mysql option pane
	 */
	private JPanel initMysqlDbOptions() {
		mysqlCredentials = new HashMap<String, JTextField>();
		mysqlOptions = new JPanel();
		mysqlOptions.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// add explanation
		JLabel sqliteLbl = new JLabel("<html>Specify the credentials of your database</html>");
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 4;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		mysqlOptions.add(sqliteLbl, c);
		c.gridwidth = 1; // reset
		
		// add input fields for credentials
		String[] fields = { "Host", "Name", "User", "Pass" };	// these strings must be the same as the
																// names of the db settings without 'db'
		for( int i = 0; i < fields.length; i++ ) {
			JLabel l = new JLabel(fields[i], JLabel.TRAILING);
			c.gridx = 1;
			c.gridy++;
			c.insets = new Insets(8, 0, 0, 6);
			mysqlOptions.add(l, c);
			
			JTextField t = new JTextField(10);
			t.setText(Settings.get("db"+fields[i], ""));
			t.addFocusListener(this);
			l.setLabelFor(t);
			c.gridx = 2;
			mysqlOptions.add(t, c);
			mysqlCredentials.put(fields[i], t);
		}
		
		return mysqlOptions;
	}
	
	/**
	 * show the option panel for the selected db type
	 */
	private void showDbOptions() {
		dbOptions.removeAll();
		
		if( dbType.equals("sqlite")) {
			showSqliteOptions();
		} else if( dbType.equals("mysql")) {
			showMysqlOptions();
		}
		
		// re-layout the container
		validate();
		repaint();
	}
	
	/**
	 * show the sqlite option pane
	 */
	private void showSqliteOptions() {
		dbOptions.add(sqliteOptions);
	}
	
	/**
	 * show the mysql option pane
	 */
	private void showMysqlOptions() {
		dbOptions.add(mysqlOptions);
	}

	@Override
	public void focusGained(FocusEvent evt) {
		// ignore
	}

	/**
	 * if a mysql credential field loses focus, save the entered value
	 */
	@Override
	public void focusLost(FocusEvent evt) {
		Set<Map.Entry<String, JTextField>> set = mysqlCredentials.entrySet();

	    for (Map.Entry<String, JTextField> me : set) {
	    	if( me.getValue().equals( ((JTextField)evt.getSource()) ) ) {
	    		Settings.set("db"+me.getKey(), me.getValue().getText());
	    		break;
	    	}
	    }
	}

}
