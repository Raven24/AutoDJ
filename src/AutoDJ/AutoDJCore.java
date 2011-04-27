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
				System.out.println("Button pressed");
				rescanDatabase ();
			}
		} else {
			System.out.println ("Unknown Observer-Message caught!");
		}
	}

}
