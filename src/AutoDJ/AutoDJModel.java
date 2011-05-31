/**
 * AutoDJModel.java
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

import java.util.Arrays;
import java.util.Observable;
import java.util.Vector;

/**
 * AutoDJModel is a class which represents AutoDJ's Model
 * part as specified in MVC. It stores all data AutoDJ needs.
 * It is updated by AutoDJController and notifies AutoDJView
 * if data it contains is changed.<br \>
 * <a href="http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller">
 * Wikipedia: model-view-controller</a>
 * @see AutoDJController
 * @see AutoDJView
 */

public class AutoDJModel extends Observable {
	/**
	 * AutoDJ's current playlist as a Vector of Song objects.
	 * @see Song
	 */
	private Vector<Song> playlist;
	/**
	 * The current search result of AutoDJ's song library
	 * as a Vector of Song objects.
	 * @see Song
	 */
	private Vector<Song> songLibrary;
	/**
	 * A log message created by AutoDJ to display in the log window of AutoDJView.
	 * @see AutoDJView
	 */
	private String logtext;
	
	/**
	 * Creates a new AutoDJModel object.
	 */
	public AutoDJModel () {
		playlist = new Vector<Song>();
		songLibrary = null;
		logtext = "";
	}
	
	/**
	 * Returns all songs currently in the playlist.
	 * @return A Vector of Song objects in the playlist.
	 */
	public Vector<Song> getPlaylist () {
		return this.playlist;
	}
	
	/**
	 * Returns the log message to be displayed in the log window of AutoDJView.
	 * @return The log message as String.
	 */
	public String getLogtext () {
		return this.logtext;
	}
	
	/**
	 * Returns the search result of AutoDJ's song library
	 * as a Vector of Song objects.
	 * @return A Vector of Song objects.
	 */
	public Vector<Song> getSongLibrary() {
		return songLibrary;
	}

	/**
	 * Sets the search result of AutoDJ's song library
	 * as a Vector of Song objects.
	 * @param songLibraryView A Vector of Song objects.
	 */
	public void setSongLibrary(Vector<Song> songList) {
		songLibrary = songList;
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.LIBRARY_CHANGED));
	}

	/**
	 * Adds songs to the playlist.
	 * @param songs The songs to add to the playlist
	 */
	public void addToPlaylist(Song[] songs) {
		playlist.addAll(Arrays.asList(songs));
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.PLAYLIST_CHANGED));
	}
	
	/**
	 * Adds a song to the playlist.
	 * @param songs The songs to add to the playlist
	 */
	public void addToPlaylist(Song song) {
		playlist.add(song);
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.PLAYLIST_CHANGED));
	}
	
	/**
	 * Removes songs from the playlist.
	 * @param songs The songs to remove from the playlist
	 */
	public void removeFromPlaylist(Song[] songs) {
		playlist.removeAll(Arrays.asList(songs));
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.PLAYLIST_CHANGED));
	}
	
	/**
	 * Sets the current playlist.
	 * @param playlist A Vector of Song objects.
	 */
	public void setPlaylist(Vector<Song> playlist) {
		this.playlist = playlist;
	}

	/**
	 * Sets the log message to be displayed in the log window of AutoDJView.
	 * Also notifies AutoDJView that the message has changed.
	 * @param logtext The log message as String.
	 */
	public void setLogtext(String logtext) {
		this.logtext = logtext;
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.NEW_LOG_MESSAGE));
	}
}
