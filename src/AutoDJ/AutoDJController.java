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


public class AutoDJController implements Observer {
	private AutoDJModel model;
	private SongDatabase myDatabase;
	
	public AutoDJController (AutoDJModel m) {
		myDatabase = new SongDatabase();
		model = m;
	}
	
	/**
	 * rescan music database
	 */
	private void rescanDatabase () {
		// get all songs from DB
		Vector<Song> databaseList = new Vector<Song>();
		databaseList = myDatabase.getSongs("");
		
		// get all songs from HD
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

	/*
	 *  recursively get all mp3s in subdirs
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

	@Override
	public void update(Observable arg0, Object m) {
		// TODO Auto-generated method stub
		if (m instanceof ObserverMessage) {
			ObserverMessage message = (ObserverMessage) m;
			if (message.getMessage()==ObserverMessage.PLAY) {
				//someone told us to play
			} else if (message.getMessage()==ObserverMessage.PAUSE) {
				//someone told us to stop playing
			} else if (message.getMessage()==ObserverMessage.RESCAN_LIBRARY) {
				rescanDatabase ();
			}
		} else {
			System.out.println ("Unknown Observer-Message caught!");
		}
	}

}
