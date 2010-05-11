import java.net.URI;
import java.io.*;
import java.util.HashMap;

/**
 * class OggIndexer
 * 
 * provides the functionality to read metadata (VorbisComments) from Ogg Vorbis files
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public class OggIndexer extends AudioFileIndexer {
	
	/**
	 * - ogg container header 				= 28 bytes
	 * - header package						= 7  bytes
	 * - vorbis identification header 		= 22 bytes
	 * - ogg container footer/next header	= 45 bytes
	 * - header package						= 7  bytes
	 * -> vorbis comment header 			
	 * 
	 * thus the comment header starts somewhere around the 109th byte of the file 
	 */
	protected int headerStart = 109;
	protected int numberVorbisComments;
	protected HashMap<String, String> vorbisComments;

	public void populateMetadata() {
		title 	= vorbisComments.get("title");
		album	= vorbisComments.get("album");
		artist	= vorbisComments.get("artist");
		year	= vorbisComments.get("date");
		genre	= vorbisComments.get("genre");
	}
	
	/**
	 * open the file, jump to the comment header and put it in the buffer
	 */
	public void readFile(String path) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(new File(new URI(path)), "r");
		raf.seek(109);
		raf.skipBytes(raf.readInt()); // skip over the vendor string
		numberVorbisComments = raf.readInt();
		
		for (int i = 0; i < numberVorbisComments; i++) {
			int readLength =raf.readInt();
			byte[] content = new byte[readLength];
			raf.read(content);
			addToMap(content);
		}			
	}
	
	protected void addToMap(byte[] pair) {
		String[] vals = new String(pair).split("=");
		vorbisComments.put(vals[0].toLowerCase(), vals[1]);
	}
}
