package AutoDJ;

import java.util.Observable;
import java.util.Vector;

public class AutoDJModel extends Observable {
	private Vector<Song> playlist;
	private Vector<Song> songLibraryView;
	private String logtext;
	
	public AutoDJModel () {
		playlist = null;
		songLibraryView = null;
		logtext = "";
	}
	
	public Vector<Song> getPlaylist () {
		return this.playlist;
	}
	
	public String getLogtext () {
		return this.logtext;
	}
	
	public Vector<Song> getSongLibraryView() {
		return songLibraryView;
	}

	public void setSongLibraryView(Vector<Song> songLibraryView) {
		this.songLibraryView = songLibraryView;
	}

	public void setPlaylist(Vector<Song> playlist) {
		this.playlist = playlist;
	}

	public void setLogtext(String logtext) {
		this.logtext = logtext;
		setChanged();
		notifyObservers(new ObserverMessage(ObserverMessage.NEW_LOG_MESSAGE));
	}
}
