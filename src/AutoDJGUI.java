import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;


public class AutoDJGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JPanel imagePanel;
	private JPanel configPanel;
	private JPanel playerPanel;

	public AutoDJGUI (String appName) {
		// close program on GUI exit
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// set title of window to application name
		this.setTitle(appName);
		
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
		add(mainPane, BorderLayout.CENTER);
		
		// create player panel
		createPlayerPanel();
		add(playerPanel, BorderLayout.PAGE_END);
    }
	
	public void createPlayerPanel() {
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
	
	public void createMainPanel() {
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
		leftConstraints.fill=GridBagConstraints.BOTH;
		leftConstraints.gridy = 1;
		leftConstraints.gridheight = 2;
		leftConstraints.weightx = 0.5;
		leftConstraints.weighty = 1.0;
		playlistList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(playlistList, leftConstraints);
		
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
		rightConstraints.fill=GridBagConstraints.BOTH;
		rightConstraints.gridy = 2;
		rightConstraints.weighty = 1.0;
		libraryList.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		mainPanel.add(libraryList, rightConstraints);
	}

	public void createImagePanel() {
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

	public void createConfigPanel() {
		configPanel = new JPanel();
		configPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JButton rescanButton = new JButton("Rescan Library");
		c.gridx = 0;
		c.gridy = 0;
		c.insets=new Insets(5,5,5,5);
		c.ipadx=10;
		configPanel.add(rescanButton, c);
		
		c.gridx = 1;
		configPanel.add(new JButton("Testbutton 1"), c);
		
		c.gridx = 2;
		configPanel.add(new JButton("Testbutton 2"), c);
		
		JTextArea logPanel = new JTextArea(5, 1);
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1.0;
		c.gridwidth = 3;
		c.fill=GridBagConstraints.HORIZONTAL;
		logPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		logPanel.append("AutoDJ started!\n");
		configPanel.add(logPanel, c);
		logPanel.append("AutoDJ started!\n");
	}
}
