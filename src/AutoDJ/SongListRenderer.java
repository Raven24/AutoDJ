package AutoDJ;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class SongListRenderer extends JLabel implements ListCellRenderer {
	
    public SongListRenderer() {
        setOpaque(true);
    }
	
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		
		if(isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}		
		
		Song song = (Song) value;
		String label = "";
		label = label + song.getArtist() + " - " + song.getTitle();
		label = label + " (" + song.getAlbum() + " [" + song.getTrackno() + " ], ";
		label = label + song.getYear() + ")";
		
		this.setText(label);
		return this;
	}

}
