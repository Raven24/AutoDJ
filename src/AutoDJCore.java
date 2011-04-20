import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;


public class AutoDJCore implements Observer {
	
	private SongDatabase myDatabase;
	
	public AutoDJCore () {
		// create a new SongDB
		myDatabase = new SongDatabase();
	}
	
	/*
	 * 
	 * rescan music database
	 * 
	 */
	private void rescanDatabase () {
		// get all songs from DB
		Vector<Song> databaseList = new Vector<Song>();
		databaseList = myDatabase.getSongs("");
		
		// get all songs from HD
		final String dirname="/home/christian/Musik/Fertig";
		File mp3dir = new File(dirname);
		Vector<File> mp3Files = new Vector<File>();
		mp3Files = getAllmp3Files(mp3dir, mp3Files);
		
		// create a new Vector to store all mp3-infos in
		Vector<Song> songFiles = new Vector<Song>();
		for (int i=0; i<mp3Files.size(); i++) {
			songFiles.add(new Song(mp3Files.elementAt(i)));
		}
		
		// TODO
		System.out.println("database size: "+databaseList.size());
		System.out.println("file list size: "+songFiles.size());
		
		// find and remove all files which have a DB entry
		// and are unchanged from songFiles
		// mark all such files in databaseList (set them null)
		for(int i=0; i<databaseList.size(); i++) {
			for (int j=0; j<songFiles.size(); j++) {
				if (databaseList.elementAt(i).equals(songFiles.elementAt(j))) {
					databaseList.set(i,null);
					songFiles.remove(j);
					//System.out.println("current i and j: " + i + " " + j);
					//System.out.println("sizeof songFiles: " + songFiles.size());
					break;
				}
			}
		}
		
		// remove all found files from databaseList
		while(databaseList.remove(null)) {
			continue;
		}
		
		// now there are only changed files in both Vectors
		// TODO
		
		// TODO
		System.out.println("database size: "+databaseList.size());
		System.out.println("file list size: "+songFiles.size());
		
		/*
		for (int i=0; i<databaseList.size(); i++) {
			System.out.println(databaseList.elementAt(i).toString() + "\n");
		}*/
	}
	
/*
	// create a new Vector to store our filenames in


	// and fill it
	for (int i=0; i<songFiles.size(); i++) {
		myDatabase.addSong(songFiles.elementAt(i));
	}
	
	// print out information about each song:
	//for (int i=0; i<songFiles.size(); i++) {
	//	System.out.println(songFiles.elementAt(i).toString() + "\n");
	//}*/

	/*
	 * 
	 *  recursively get all mp3s in subdirs
	 * 
	 */
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

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg1 instanceof Integer) {
			System.out.println("Button pressed");
			rescanDatabase ();
		}
	}

}
