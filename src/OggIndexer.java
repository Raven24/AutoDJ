import java.net.URI;
import java.nio.ByteBuffer;
import java.io.*;

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

	void populateMetadata() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * open the file, jump to the comment header and put it in the buffer
	 */
	void readFile(String path) throws Exception {
		RandomAccessFile raf = new RandomAccessFile(new File(new URI(path)), "r");
		raf.seek(109);
		raf.skipBytes(raf.readInt()); // skip over the vendor string
		numberVorbisComments = raf.readInt();
		
		int readLength = (int) raf.length()/4; 
		byte[] tagData = new byte[readLength];
		raf.read(tagData);
				
		buff = ByteBuffer.allocate(readLength); 
		buff.put(tagData);
		buff.rewind();	
	}

}
