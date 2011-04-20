import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Vector;

import javax.swing.UIManager;

import org.jaudiotagger.audio.mp3.*;

public class AutoDJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// try to use systems look and feel for GUI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		AutoDJGUI gui = new AutoDJGUI("AutoDJ v0.1");
		gui.setLocation(400, 250);
		gui.setSize(600, 400);
		gui.setVisible(true);
		
		/*
		final String dirname="/home/christian/Musik/Fertig";
		File mp3dir = new File(dirname);

		// create a new Vector to store our filenames in
		Vector<File> mp3Files = new Vector<File>();
		mp3Files = getAllmp3Files(mp3dir, mp3Files);
		// create a new Vector to store all mp3-infos in
		Vector<Song> songFiles = new Vector<Song>();
		for (int i=0; i<mp3Files.size(); i++) {
			songFiles.add(new Song(mp3Files.elementAt(i)));
		}
		
		// create a new SongDB
		SongDatabase myDatabase = new SongDatabase();
		// and fill it
		for (int i=0; i<songFiles.size(); i++) {
			myDatabase.addSong(songFiles.elementAt(i));
		}
		
		// print out information about each song:
		//for (int i=0; i<songFiles.size(); i++) {
		//	System.out.println(songFiles.elementAt(i).toString() + "\n");
		//}*/
	}
	
	// recursively get all mp3s in subdirs
	private static Vector<File> getAllmp3Files(File file, Vector<File> mp3file) {
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i=0; i<children.length; i++) {
				getAllmp3Files(new File(file, children[i]), mp3file);
			}
		} else {
			// FIXME!
			// it's a file, not a dir, might want to check if it is a mp3-file
			mp3file.add(file);
		}
		return mp3file;
	}
	// end visitAllDirsAndFiles()

}
