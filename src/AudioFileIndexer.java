import java.nio.ByteBuffer;

/**
 * class AudioFileIndexer
 * 
 * provides an interface to read the metadata of an audio file
 * subclasses have to implement the actual parsing according to the file format
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public class AudioFileIndexer {

	protected ByteBuffer buff;  // contains the actual audio file
	
	// here comes the metadata
	protected String title, artist, album, comment, genre, year;
	
	/**
	 * reads the metadata in the audio file and saves it to local member variables
	 * 
	 * TODO: implement in the subclasses
	 */
	public void populateMetadata() {
		return;
	}
	
	/**
	 * opens the file and puts the metadata in ByteBuffer buff
	 * 
	 * TODO: implement in the subclass
	 * 
	 * @param path
	 */
	public void readFile(String path) {
		return;
	}
}
