/**
 * AutoDJView.java
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;

/**
 * AutoDJView is a class which represents AutoDJ's View
 * part as specified in MVC. It notifies AutoDJController
 * if any user input happens and reacts if AutoDJModel
 * is changed and displays the changes.<br \>
 * <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 * Wikipedia: model-view-controller</a>
 * @see AutoDJController
 * @see AutoDJModel
 */

public class AutoDJView extends Observable implements Observer {
	/**
	 * The AutoDJ main window.
	 */
	private JFrame gui;
		/**
		 * The first panel containing the playlist, song library and a search field.
		 */
		private JPanel mainPanel;
			/**
			 * A SongJList displaying the current playlist.
			 */
			private JList playlistList;
			/**
			 * A text field to enter a search string for the song library.
			 */
			private JTextField librarySearchField;
			/**
			 * A SongJList displaying the current library search results.
			 */
			private JList libraryList;
	
		/**
		 * The second panel which displays the cover art, if available.
		 */
		private JPanel imagePanel;
	
		/**
		 * The third panel for all the configuration.
		 */
		private JPanel configPanel;
			/**
			 * The text area where all log messages are displayed.
			 */
			private JTextArea logPanel;
		
		/**
		 * The player panel containing the play/pause-button, the next-song-button
		 * and a progress bar.
		 */
		private JPanel playerPanel;

	/**
	 * Creates a new AutoDJView object and displays it.
	 * @param appName A String which will be displayed
	 * as the window title.
	 */
	public AutoDJView (String appName) {
		gui = new JFrame();
		// close program on GUI exit
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set title of window to application name
		gui.setTitle(appName);
		
		// build all panels
		createMainPanel();
		createImagePanel();
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
	
    /**
	 * Creates the player panel containing the play/pause-button, the next-song-button
	 * and a progress bar.
	 */
	private void createPlayerPanel() {
		playerPanel = new JPanel();
		playerPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JToggleButton playButton = new JToggleButton(new ImageIcon("img/media-playback-start.png"));
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
		
		JButton nextSongButton = new JButton(new ImageIcon("img/media-seek-forward.png"));
		c.fill=GridBagConstraints.NONE;
		c.weightx=0.0;
		c.ipadx=10;
		c.insets=new Insets(10,10,10,10);
		c.gridx=2;
		c.gridy=0;
		playerPanel.add(nextSongButton, c);
	}
	
	/**
	 * Creates the first panel containing the playlist, song library and a search field.
	 */
	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		// left: playlist
		GridBagConstraints leftConstraints = new GridBagConstraints();
		JLabel playListLabel = new JLabel("Playlist");
		leftConstraints.gridx = 0;
		leftConstraints.gridy = 0;
		leftConstraints.insets=new Insets(5,5,5,5);
		mainPanel.add(playListLabel, leftConstraints);
		playlistList = new JList();
		playlistList.setCellRenderer(new SongListRenderer());
		playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane playlistScrollpane = new JScrollPane (playlistList);
		playlistScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		leftConstraints.fill=GridBagConstraints.BOTH;
		leftConstraints.gridy = 1;
		leftConstraints.gridheight = 2;
		leftConstraints.weightx = 0.5;
		leftConstraints.weighty = 1.0;
		playlistList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(playlistScrollpane, leftConstraints);
		
		// center: playlist control
		JPanel playListControlPanel = new JPanel();
		// buttons arranged in a gridbag-layout
		playListControlPanel.setLayout(new GridBagLayout());
		GridBagConstraints controlConstraints = new GridBagConstraints();
		JButton addButton = new JButton(new ImageIcon("img/go-previous.png"));
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.ADD_SONG_TO_PLAYLIST));
			}
		});
		JButton removeButton = new JButton(new ImageIcon("img/go-next.png"));
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.REMOVE_SONG_FROM_PLAYLIST));
			}
		});
		JButton downButton = new JButton(new ImageIcon("img/go-down.png"));
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.MOVE_SONG_DOWN_IN_PLAYLIST));
			}
		});
		JButton upButton = new JButton(new ImageIcon("img/go-up.png"));
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.MOVE_SONG_UP_IN_PLAYLIST));
			}
		});
		controlConstraints.gridx = 1;
		controlConstraints.gridy = 0;
		playListControlPanel.add(upButton, controlConstraints);
		controlConstraints.gridx = 0;
		controlConstraints.gridy = 1;
		playListControlPanel.add(addButton, controlConstraints);
		controlConstraints.gridx = 2;
		playListControlPanel.add(removeButton, controlConstraints);
		controlConstraints.gridx = 1;
		controlConstraints.gridy = 2;
		playListControlPanel.add(downButton, controlConstraints);
		GridBagConstraints centerConstraints = new GridBagConstraints();
		centerConstraints.gridx = 1;
		centerConstraints.gridy = 1;
		centerConstraints.gridheight = 2;
		mainPanel.add(playListControlPanel, centerConstraints);
			
		// right: song library
		GridBagConstraints rightConstraints = new GridBagConstraints();
		JLabel libraryLabel = new JLabel("Song Library");
		rightConstraints.gridx = 2;
		rightConstraints.gridy = 0;
		rightConstraints.insets=new Insets(5,5,5,5);
		mainPanel.add(libraryLabel, rightConstraints);
		librarySearchField = new JTextField();
		librarySearchField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setChanged();
				notifyObservers(new ObserverMessage(ObserverMessage.SEARCHTEXT_CHANGED));
			}
		});
		rightConstraints.fill=GridBagConstraints.HORIZONTAL;
		rightConstraints.weightx = 0.5;
		rightConstraints.gridy = 1;
		mainPanel.add(librarySearchField, rightConstraints);
		libraryList = new JList();
		libraryList.setCellRenderer(new SongListRenderer());
		JScrollPane libraryScrollpane = new JScrollPane (libraryList);
		libraryScrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		rightConstraints.fill=GridBagConstraints.BOTH;
		rightConstraints.gridy = 2;
		rightConstraints.weighty = 1.0;
		libraryList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(libraryScrollpane, rightConstraints);
	}

	/**
	 * Creates the second panel which displays the cover art, if available.
	 */
	private void createImagePanel() {
		// FIXME!
		// change image size dynamically, i.e. make it smaller when window is small
		imagePanel = new JPanel();
		imagePanel.setLayout(new GridBagLayout());
		GridBagConstraints constr = new GridBagConstraints();
		
		imagePanel.setBackground(Color.BLACK);
		ImageIcon cover = new ImageIcon("/home/christian/Development/AutoDJ/img/Judas_priest_painkiller.jpg");
		JLabel coverLabel = new JLabel();
		coverLabel.setIcon(cover); 
		imagePanel.add(coverLabel, constr);
	}

	/**
	 * Creates the third panel for all the configuration.
	 */
	// FIXME!
	// Does currently only do the rescan library thing and
	// the log messages correctly.
	// There are 2 buttons which do nothing at all and
	// should be removed if something meaningful can be
	// added.
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

	/**
	 * Moves the main window to a new location. The top-left corner of the
	 * new location is specified by the x and y parameters in the coordinate
	 * space of this component's parent (i.e. the screen).
	 * @param x the x-coordinate of the new location's top-left corner.
	 * @param y the y-coordinate of the new location's top-left corner.
	 */
	public void setLocation(int x, int y) {
		gui.setLocation(x,y);
	}
	
	/**
	 * Resizes the main window so that it has width <code>x</code>
	 * and height <code>y</code>.
	 * @param x the new width of this component in pixels
	 * @param y the new height of this component in pixels
	 */
	public void setSize(int x, int y) {
		gui.setSize(x,y);
	}
	
	/**
	 * Shows or hides the main window depending on the value of parameter b.
	 * @param b if <code>true</code>, shows this component; otherwise,
	 * hides this component
	 */
	public void setVisible(boolean b) {
		gui.setVisible(b);
	}

	/**
	 * Returns the entered text in the library search field.
	 * @return the String entered in the library search field.
	 */
	public String getSearchText() {
		return librarySearchField.getText();
	}
	
	/**
	 * Returns the current selected values of the playlist.
	 * @return the current selected values of the playlist.
	 */
	public Song[] getSelectedPlaylistSongs() {
		return convertObjectArrayToSongArray(playlistList.getSelectedValues());
	}
	
	/**
	 * Returns the current selected index of the playlist.
	 * @return the current selected index of the playlist.
	 */
	public int getSelectedPlaylistIndex() {
		return playlistList.getSelectedIndex();
	}
	
	/**
	 * Sets the current selected index of the playlist.
	 */
	public void setSelectedPlaylistIndex(int index) {
		playlistList.setSelectedIndex(index);
	}
	
	
	/**
	 * Returns the current selected values of the library.
	 * @return the current selected values of the library.
	 */
	public Song[] getSelectedLibrarySongs() {
		return convertObjectArrayToSongArray(libraryList.getSelectedValues());
	}
	
	/**
	 * Convert an Object-array to a Song-array
	 * @param objects[] the array of Objects to be converted
	 * @return a Song-array
	 */
	private Song[] convertObjectArrayToSongArray(Object[] objects) {
		Song[] songs = new Song[objects.length];
		for (int i=0; i<objects.length; i++) {
			songs[i] = (Song) objects[i];
		}
		return songs;
	}
	
	/**
	 * Updates this object if changes in an other object occurs. At the moment
	 * this class is notified only if something in AutoDJModel has changed,
	 * e.g. a new log message was created, the playlist changed, etc.
	 * @param model The object which notified this class of some change.
	 * @param msg The ObserverMessage the object which changed sent.
	 * @see ObserverMessage
	 */
	@Override
	public void update(Observable model, Object msg) {
		ObserverMessage message = (ObserverMessage) msg;
		if (msg instanceof ObserverMessage) {
			if (message.getMessage()==ObserverMessage.NEW_LOG_MESSAGE) {
				String logmessage = ((AutoDJModel) model).getLogtext();
				if (!logmessage.endsWith("\n")) logmessage+="\n";
				logPanel.append(logmessage);
				logPanel.setCaretPosition(logPanel.getDocument().getLength());
			} else if (message.getMessage()==ObserverMessage.LIBRARY_CHANGED) {
				// display new content
				libraryList.setListData(((AutoDJModel) model).getSongLibrary());
			} else if (message.getMessage()==ObserverMessage.PLAYLIST_CHANGED) {
				// display new content
				playlistList.setListData(((AutoDJModel) model).getPlaylist());
			}
		}
	}
}
