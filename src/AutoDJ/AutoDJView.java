/**
 * AutoDJGUI.java
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;


public class AutoDJView extends Observable implements Observer {
	private JFrame gui;
	private JPanel mainPanel;
	private JPanel imagePanel;
	private JPanel configPanel;
	private JPanel playerPanel;
	private JTextArea logPanel;

	public AutoDJView (String appName) {
		gui = new JFrame();
		// close program on GUI exit
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set title of window to application name
		gui.setTitle(appName);
		
		// build main panel for playlist and library
		createMainPanel();
		// build panel for display of album cover
		createImagePanel();
		// build panel for configuration
		createConfigPanel();
		// Create a tabbed pane and add panels
		JTabbedPane mainPane = new JTabbedPane();
		mainPane.addTab( "Playlist and Library", mainPanel );
		mainPane.addTab( "Album Cover", imagePanel );
		mainPane.addTab( "Configuration", configPanel );
		gui.add(mainPane, BorderLayout.CENTER);
		
		// create player panel
		createPlayerPanel();
		gui.add(playerPanel, BorderLayout.PAGE_END);
    }
	
	private void createPlayerPanel() {
		playerPanel = new JPanel();
		playerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JToggleButton playButton = new JToggleButton("Play/Pause");
		c.fill=GridBagConstraints.NONE;
		c.weightx=0.0;
		c.ipadx=10;
		c.insets=new Insets(10,10,10,10);
		c.gridx=0;
		c.gridy=0;
		playerPanel.add(playButton, c);
		
		JProgressBar progressBar = new JProgressBar();
		c.fill=GridBagConstraints.HORIZONTAL;
		c.weightx=1.0;
		c.ipadx=10;
		c.insets=new Insets(10,10,10,10);
		c.gridx=1;
		c.gridy=0;
		playerPanel.add(progressBar, c);
		
		JButton nextSongButton = new JButton("Next Song");
		c.fill=GridBagConstraints.NONE;
		c.weightx=0.0;
		c.ipadx=10;
		c.insets=new Insets(10,10,10,10);
		c.gridx=2;
		c.gridy=0;
		playerPanel.add(nextSongButton, c);
	}
	
	private void createMainPanel() {
		// TODO
		// create a string array to display in the jlists
		// remove this when possible
		String[] data = {"one", "two", "three", "four", "one", "two", "three", "four"};
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints leftConstraints = new GridBagConstraints();
		GridBagConstraints rightConstraints = new GridBagConstraints();

		JLabel playListLabel = new JLabel("Playlist");
		leftConstraints.gridx = 0;
		leftConstraints.gridy = 0;
		leftConstraints.insets=new Insets(5,5,5,5);
		mainPanel.add(playListLabel, leftConstraints);
		JList playlistList = new JList(data);
		JScrollPane playlistScrollpane = new JScrollPane (playlistList);
		playlistScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		leftConstraints.fill=GridBagConstraints.BOTH;
		leftConstraints.gridy = 1;
		leftConstraints.gridheight = 2;
		leftConstraints.weightx = 0.5;
		leftConstraints.weighty = 1.0;
		playlistList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(playlistScrollpane, leftConstraints);
		
		JLabel libraryLabel = new JLabel("Song Library");
		rightConstraints.gridx = 2;
		rightConstraints.gridy = 0;
		rightConstraints.insets=new Insets(5,5,5,5);
		mainPanel.add(libraryLabel, rightConstraints);
		JTextField librarySearchField = new JTextField();
		rightConstraints.fill=GridBagConstraints.HORIZONTAL;
		rightConstraints.weightx = 0.5;
		rightConstraints.gridy = 1;
		mainPanel.add(librarySearchField, rightConstraints);
		JList libraryList = new JList(data);
		JScrollPane libraryScrollpane = new JScrollPane (libraryList);
		libraryScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rightConstraints.fill=GridBagConstraints.BOTH;
		rightConstraints.gridy = 2;
		rightConstraints.weighty = 1.0;
		libraryList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(libraryScrollpane, rightConstraints);
	}

	private void createImagePanel() {
		// TODO
		// change image dynamically
		imagePanel = new JPanel();
		imagePanel.setLayout(new GridBagLayout());
		GridBagConstraints constr = new GridBagConstraints();
		
		imagePanel.setBackground(Color.BLACK);
		ImageIcon cover = new ImageIcon("/home/christian/Development/AutoDJ/img/Judas_priest_painkiller.jpg");
		JLabel coverLabel = new JLabel();
		coverLabel.setIcon(cover); 
		imagePanel.add(coverLabel, constr);
	}

	private void createConfigPanel() {
		configPanel = new JPanel();
		configPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton rescanButton = new JButton("Rescan Library");
		rescanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.RESCAN_LIBRARY));
			}
		});
		c.gridx = 0;
		c.gridy = 0;
		c.insets=new Insets(5,5,5,5);
		c.ipadx=10;
		configPanel.add(rescanButton, c);
		
		c.gridx = 1;
		configPanel.add(new JButton("Testbutton 1"), c);
		
		c.gridx = 2;
		configPanel.add(new JButton("Testbutton 2"), c);
		
		logPanel = new JTextArea();
		logPanel.setLineWrap(true);
		logPanel.setWrapStyleWord(true);
		JScrollPane scrollpane = new JScrollPane (logPanel);
		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 3;
		c.fill=GridBagConstraints.BOTH;
		scrollpane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		configPanel.add(scrollpane, c);
	}


	public void setLocation(int x, int y) {
		gui.setLocation(x,y);
	}
	
	public void setSize(int x, int y) {
		gui.setSize(x,y);
	}
	
	public void setVisible(boolean b) {
		gui.setVisible(b);
	}

	@Override
	public void update(Observable arg0, Object m) {
		ObserverMessage message = (ObserverMessage) m;
		if (m instanceof ObserverMessage) {
			if (message.getMessage()==ObserverMessage.NEW_LOG_MESSAGE) {
				String logmessage = ((AutoDJModel) arg0).getLogtext();
				if (!logmessage.endsWith("\n")) logmessage+="\n";
				logPanel.append(logmessage);
				logPanel.setCaretPosition(logPanel.getDocument().getLength());
			}
		}
	}
}
