/**
 * SongListRenderer.java
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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * SongListRenderer is a subclass of JLabel and is used
 * for formatting song information in the library and playlist
 * view in JLists in AutoDJView.
 * @see AutoDJView
 */

@SuppressWarnings("serial")
public class SongListRenderer extends JLabel implements ListCellRenderer {
	
	/**
	 * Creates a new SongListRenderer object.
	 */
	public SongListRenderer() {
		setOpaque(true);
	}

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
    	// get the colors from the JList we "work for"
		if(isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}		
		
		Song song = (Song) value;
		String label = "<html>";
		label = label + "<b>" + song.getArtist() + " - " + song.getTitle() + "</b>";
		label = label + ": " + song.getAlbum() + ", #" + song.getTrackno() + ", ";
		label = label + song.getYear() + " (" + song.getGenre() + ")</html>";
		
		this.setText(label);
		return this;
	}

}