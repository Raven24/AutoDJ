/**
 * ObserverMessage.java
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

/**
 * ObserverMessage is a class which represents a specific message
 * which the AutoDJGUI "sends" to AutoDJCore. It consists of a single
 * integer field which represents the message an instance of this class
 * represents.
 * @see AutoDJController
 * @see AutoDJModel
 * @see AutoDJView
 */

public class ObserverMessage {
	// 1 to 99: GUI event
	// 100+: Model changed
	/**
	 * The integer value representing "start playing"
	 */
	public static final int PLAY = 1;
	/**
	 * The integer value representing "pause playing"
	 */
	public static final int PAUSE = 2;
	/**
	 * The integer value representing "rescan the harddisk for
	 * changed MP3 files and update the database accordingly"
	 */
	public static final int RESCAN_LIBRARY = 3;

	/**
	 * The integer value representing a change in the
	 * search text for the song library.
	 */
	public static final int SEARCHTEXT_CHANGED = 4;
	
	/**
	 * The integer value representing a change in the
	 * playlist: A song was added.
	 */
	public static final int ADD_SONG_TO_PLAYLIST = 5;
	
	/**
	 * The integer value representing a change in the
	 * playlist: A song was removed.
	 */
	public static final int REMOVE_SONG_FROM_PLAYLIST = 6;
	
	/**
	 * The integer value representing a change in the
	 * playlist: A song was moved up.
	 */
	public static final int MOVE_SONG_UP_IN_PLAYLIST = 7;
	
	/**
	 * The integer value representing a change in the
	 * playlist: A song was moved down.
	 */
	public static final int MOVE_SONG_DOWN_IN_PLAYLIST = 8;

	
	/**
	 * The integer value representing "player log was changed,
	 * display new log messages"
	 */
	public static final int NEW_LOG_MESSAGE = 101;
	
	/**
	 * The integer value representing a change in the
	 * song library.
	 */
	public static final int LIBRARY_CHANGED = 102;
	
	/**
	 * The integer value representing a change in the
	 * playlist.
	 */
	public static final int PLAYLIST_CHANGED = 103;
	
	/**
	 * The integer value which stores the message.
	 */
	private int message;

	/**
	 * Creates a new ObserverMessage object which contains the
	 * specified message.
	 * @param message The integer value of the message to be sent.
	 * Usually this will be one of the static filed values of
	 * this class.
	 */
	public ObserverMessage (int message) {
		this.message=message;
	}
	
	/**
	 * Returns the integer value of this message instance.
	 * @return The value of the integer encoding the message
	 * this instance represents. It usually will be compared
	 * with one of the static field values of this class
	 * to act accordingly.
	 */
	public int getMessage () {
		return this.message;
	}
}
