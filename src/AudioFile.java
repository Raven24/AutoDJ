import java.net.URI; 

/**
 * This class contains the information to an audio file 
 * 
 * @author Florian Staudacher
 *
 */
public class AudioFile {
	
	/**
	 * declaration of the public variables  
	 */
	public String 	title;		// title of the track
	public String 	artist;		// artist of the track
	public String 	album;		// album of the track
	public int 		length; 	// length in seconds
	public URI 		path;		// path in the filesystem
	
	/**
	 * constructor 
	 * 
	 * @param title
	 * @param artist
	 * @param album
	 * @param length
	 * @param path
	 */
	public AudioFile(String title, String artist, String album, int length, URI path) {
		this.assignMemberVars(title, artist, album, length, path);
	}
	
	/**
	 * alternative constructor
	 * 
	 * @param title
	 * @param artist
	 * @param album
	 * @param length
	 * @param path
	 */
	public AudioFile(String title, String artist, String album, int length, String path) {
		try {
			URI tmpPath = new URI(path);
			this.assignMemberVars(title, artist, album, length, tmpPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * assign the member variables
	 * 
	 * @param title
	 * @param artist
	 * @param album
	 * @param length
	 * @param path
	 */
	public void assignMemberVars(String title, String artist, String album, int length, URI path) {
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.length = length;
		this.path = path;
	}

	/**
	 * toString for easy debugging and printing
	 */
	public String toString() {
		return title + " " + artist + ", located at: " + path.toString();
	}
}