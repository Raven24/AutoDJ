package AutoDJ.metaReader;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;


/**
 * class OggIndexer
 * 
 * provides the functionality to read metadata (VorbisComments) from Ogg Vorbis files
 * Format specification can be found at
 * http://xiph.org/vorbis/doc/Vorbis_I_spec.html
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public class OggIndexer extends AudioFileIndexer {
	
	/**
	 * - ogg container header 				= 27 bytes
	 * - header package						= 7  bytes
	 * - vorbis identification header 		= 22 bytes
	 * - ogg container footer/next header	= 45 bytes
	 * - header package						= 7  bytes
	 * -> vorbis comment header 			
	 * 
	 * thus the comment header starts somewhere around the 111th byte of the file 
	 */
	protected int headerStart = 111;
	protected int numberVorbisComments;
	protected HashMap<String, String> vorbisComments;

	/**
	 * initialize this object and start reading the file
	 * @param String path
	 */
	public OggIndexer(String path) {
		filePath = path;
		try {
			readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * get the values out of the metadata
	 */
	public void populateMetadata() {
		title 	= vorbisComments.get("title");
		album	= vorbisComments.get("album");
		artist	= vorbisComments.get("artist");
		year	= vorbisComments.get("date");
		trackno = vorbisComments.get("tracknumber");
		genre	= vorbisComments.get("genre");
		cover   = readAlbumImage(vorbisComments.get("metadata_block_picture"));
	}
	
	/**
	 * open the file, jump to the comment header and put it in the buffer
	 * 
	 * @param String path
	 */
	public void readFile(String path) throws Exception {
		audioFile = new File(path);
		raf = new RandomAccessFile(audioFile, "r");
		raf.skipBytes(58); // that's the file identification header: not interesting
		readOggHeader();   // we need to read that in order to know where to start looking
		raf.skipBytes(7);  // comment header identification: don't need that
		
		int venLen = getIntFromBuff();
		raf.skipBytes(venLen); // skip over the vendor string (always the same)
		
		numberVorbisComments = getIntFromBuff();
		vorbisComments = new HashMap<String, String> ();
		
		for (int i = 0; i < numberVorbisComments; i++) {
			int readLength = getIntFromBuff();
			byte[] content = new byte[readLength];
			raf.read(content);
			System.out.println(new String(content));
			addToMap(content);
		}			
	}
		
	public String toString() {
		return "Ogg-File: "+super.toString();
	}
	
	/**
	 * adds a vorbis comment tag to the metadata map
	 * 
	 * @param byte[] pair
	 */
	protected void addToMap(byte[] pair) {
		String[] vals = new String(pair).split("=");
		if (vals.length > 1) {
			vorbisComments.put(vals[0].toLowerCase(), vals[1]);
		}
	}
	
	/**
	 * read the vorbis header and do some sanity checks
	 * 
	 * @throws Exception
	 */
	protected void readOggHeader() throws Exception {
		byte[] header = new byte[27];
		raf.readFully(header);
		
		if(!(header[0] == 'O' &&
		   header[1] == 'g' &&
		   header[2] == 'g' &&
		   header[3] == 'S')) {
			
			// this is not an ogg! let's exit
			return;
		}
		
		// we skip version, granule position, serial number, sequence number and check sum
		// makes 22 bytes + 4 from "OggS" = 26
		
		int pageSegments = (int)header[26];
		int totalLength = 0;
		
		for (int i = 0; i < pageSegments; i++) {
			int l=0; byte[] tmp = new byte[1];
			raf.read(tmp);
			l=tmp[0]&0xff;
			totalLength += l;
		}
	}
	
	/**
	 * Extracts the Image from a FLAC picture structure
	 * http://flac.sourceforge.net/format.html#metadata_block_picture
	 * Expects a base64 encoded string
	 * 
	 * @param String pictureBlock (base64)
	 * @return BufferedImage
	 */
	BufferedImage readAlbumImage(String pictureBlock) {
		if( pictureBlock == null || pictureBlock.isEmpty() ) 
			return null;
		
		byte[] pictureBytes = Base64.decodeBase64(pictureBlock);
		BufferedImage img = null;
		
		String mimeString = "", description = "";
		ByteBuffer picBuff = ByteBuffer.allocate(pictureBytes.length);
		picBuff.put(pictureBytes);
		picBuff.rewind();
		
		int picType = picBuff.getInt(); // not interesting, discard
		
		int mimeStrLength = picBuff.getInt();
		byte[] mimeBytes = new byte[mimeStrLength];
		picBuff.get(mimeBytes);
		mimeString = new String(mimeBytes);
		
		int descStrLength = picBuff.getInt();
		byte[] descBytes = new byte[descStrLength];
		picBuff.get(descBytes);
		try {
			description = new String(descBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int picWidth  = picBuff.getInt();
		int picHeight = picBuff.getInt();
		int colDepth  = picBuff.getInt();
		int idxColors = picBuff.getInt();
		
		int picDataLength = picBuff.getInt();
		byte[] picBytes = new byte[picDataLength];
		picBuff.get(picBytes);
		try {
			img = ImageIO.read(new ByteArrayInputStream(picBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return img;
	}
	
}
