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
 * @see AutoDJView
 * @see AutoDJController
 */

public class ObserverMessage {
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
	 * The integer value representing "player log was changed,
	 * display new log messages"
	 */
	public static final int NEW_LOG_MESSAGE = 101;
	
	
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
