/**
 * AutoDJCore.java
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

import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.activation.MimetypesFileTypeMap;

/**
 * AutoDJController is a class which represents AutoDJ's Controller
 * part as specified in MVC. It reacts on user input coming from
 * AutoDJView, does all necessary calculations and updates
 * AutoDJModel accordingly.<br \>
 * <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 * Wikipedia: model-view-controller</a>
 * @see AutoDJModel
 * @see AutoDJView
 */

public class AutoDJController implements Observer {
	/**
	 * The AutoDJModel-Object this class modifies.
	 * @see AutoDJModel
	 */
	private AutoDJModel model;
	/**
	 * The SongDatabase this class works with.
	 * @see SongDatabase
	 */
	private SongDatabase myDatabase;
	
	/**
	 * Creates a new AutoDJController object which interacts with
	 * the specified AutoDJModel.
	 * @param m The AutoDJModel object this AutoDJController will
	 * interact with.
	 */
	public AutoDJController (AutoDJModel m) {
		myDatabase = new SongDatabase();
		model = m;
	}
	
	/**
	 * Rescans the harddisk for all MP3 files and updates the
	 * song database, if necessary.
	 */
	private void rescanDatabase() {
		// get all songs from DB
		Vector<Song> databaseList = new Vector<Song>();
		databaseList = myDatabase.getSongs("");
		
		// get all songs from HD
		// FIXME!
		// get the directory from a config file
		final String dirname="/home/christian/Musik/TEST";
		File mp3dir = new File(dirname);
		Vector<File> mp3Files = new Vector<File>();
		mp3Files = getAllmp3Files(mp3dir, mp3Files);
		
		// create a new Vector to store all mp3-infos in
		Vector<Song> songFiles = new Vector<Song>();
		for (int i=0; i<mp3Files.size(); i++) {
			songFiles.add(new Song(mp3Files.elementAt(i)));
		}
		
		model.setLogtext("Database size: "+databaseList.size()+" Song(s)");
		model.setLogtext("Files on HD: "+songFiles.size()+" Song(s)");
		
		// find and remove all files which have a DB entry
		// and are unchanged from songFiles
		// mark all such files in databaseList (set them null)
		for(int i=0; i<databaseList.size(); i++) {
			for (int j=0; j<songFiles.size(); j++) {
				if (databaseList.elementAt(i).equals(songFiles.elementAt(j))) {
					databaseList.set(i,null);
					songFiles.remove(j);
					break;
				} else if ( databaseList.elementAt(i).compareMD5sum (songFiles.elementAt(j)) ||
							databaseList.elementAt(i).compareFile   (songFiles.elementAt(j))) {
					model.setLogtext("Found possible match:");
					model.setLogtext("DB:   "+databaseList.elementAt(i).toString());
					model.setLogtext("File: "+songFiles.elementAt(j).toString());
					// update DB
					myDatabase.changeSong(databaseList.elementAt(i), songFiles.elementAt(j));
					databaseList.set(i,null);
					songFiles.remove(j);
					break;
				}
			}
		}
		
		// remove all found files from databaseList
		while(databaseList.remove(null)) {
			continue;
		}

		for (int i=0; i<songFiles.size(); i++) {
			myDatabase.addSong(songFiles.elementAt(i));
		}
		model.setLogtext("Added "+songFiles.size()+" song(s) to database.");
	}

	/**
	 * Recursively gets all MP3 files in a given directory.
	 * @param file A File object representing the directory we search in.
	 * @param mp3file A Vector of File objects representing all MP3 files
	 * we found so far.
	 * @return A Vector of File objects representing all MP3 files
	 * we found.
	 */
	private static Vector<File> getAllmp3Files(File file, Vector<File> mp3file) {
		if (file.isDirectory()) {
			String[] children = file.list();
			for (int i=0; i<children.length; i++) {
				getAllmp3Files(new File(file, children[i]), mp3file);
			}
		} else {
			// it's a file, not a dir, but it is a mp3-file?
			// this checks the mime type via the file extension
			// other, more sophisticated solutions exist, but they are reported as slow
			MimetypesFileTypeMap map = new MimetypesFileTypeMap();
			// unfortunately this method doesn't know about audio/mpeg
			map.addMimeTypes("audio/mpeg mp3 MP3 mP3 Mp3");
			if (map.getContentType(file).equals("audio/mpeg")) mp3file.add(file);
		}
		return mp3file;
	}

	/**
	 * Query the song database for songs matching the search string
	 * and update AutoDJModel accordingly.
	 * @param search The search string, which will be matched against
	 * the songs artist, title or album.
	 * @see SongDatabase
	 */
	public void filterSongLibrary(String search) {
		model.setSongLibrary(myDatabase.getSongs(search));
	}
	
	/**
	 * Updates this object if changes in an other object occurs. At the moment
	 * this class is notified only if something in AutoDJView has changed,
	 * e.g. a button was pressed etc.
	 * @param arg0 The object which notified this class of some change.
	 * @param m The ObserverMessage the object which changed sent.
	 * @see ObserverMessage
	 */
	@Override
	public void update(Observable arg0, Object m) {
		if (m instanceof ObserverMessage) {
			ObserverMessage message = (ObserverMessage) m;
			if (message.getMessage()==ObserverMessage.PLAY) {
				//someone told us to play
			} else if (message.getMessage()==ObserverMessage.PAUSE) {
				//someone told us to stop playing
			} else if (message.getMessage()==ObserverMessage.RESCAN_LIBRARY) {
				rescanDatabase();
			}
		} else {
			System.out.println ("Unknown Observer-Message caught!");
		}
	}
}
