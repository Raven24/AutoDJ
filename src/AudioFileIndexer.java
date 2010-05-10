import java.nio.ByteBuffer;

/**
 * abstract class AudioFileIndexer
 * 
 * provides an interface to read the metadata of an audio file
 * subclasses have to implement the actual parsing according to the file format
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
abstract class AudioFileIndexer {

	protected ByteBuffer buff;  // contains the actual audio file
	
	// here comes the metadata
	protected String title, artist, album, comment, genre, year;
	
	/**
	 * reads the metadata in the audio file and saves it to local member variables
	 * 
	 * TODO: implement in the subclasses
	 */
	abstract void populateMetadata();
	
	/**
	 * opens the file and puts the metadata in ByteBuffer buff
	 * 
	 * TODO: implement in the subclass
	 * 
	 * @param path
	 * @throws Exception 
	 */
	abstract void readFile(String path) throws Exception;
}
