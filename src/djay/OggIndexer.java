package djay;

import java.net.URI;
import java.io.*;
import java.util.HashMap;

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
	 * - ogg container header 				= 28 bytes
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
	protected RandomAccessFile raf;

	public OggIndexer(String path) {
		filePath = path;
		try {
			readFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
		audioFile = new File(new URI(path));
		raf = new RandomAccessFile(audioFile, "r");
		raf.seek(headerStart);
		int venLen = getIntFromBuff();
		raf.skipBytes(venLen); // skip over the vendor string
		
		numberVorbisComments = getIntFromBuff();
		vorbisComments = new HashMap<String, String> ();
		
		for (int i = 0; i < numberVorbisComments; i++) {
			int readLength = getIntFromBuff();
			byte[] content = new byte[readLength];
			raf.read(content);
			addToMap(content);
		}			
	}
	
	protected void addToMap(byte[] pair) {
		String[] vals = new String(pair).split("=");
		vorbisComments.put(vals[0].toLowerCase(), vals[1]);
	}
	
	/**
	 * how nice, java only uses big endian encoding, we need little endian
	 * also, all integers are signed per default, which cannot be turned off
	 * ... therefore we need to juggle around some bytes by hand
	 */
	protected int getIntFromBuff() {
		byte[] tmp = new byte[4];
		try {
			raf.read(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unsignedBytesToInt(tmp);
	}
	
	protected int unsignedBytesToInt(byte[] buf) {
		int i = 0;
		for (int k = 0; k < 4; k++) {
			i += unsignedByteToInt(buf[k]) << (24-(8*k)); 
		}
		return swabInt(i);
	}
	
	protected int unsignedByteToInt(byte b) {
	    return (int) b & 0xFF;
	}

	protected int swabInt(int v) {
	    return  (v >>> 24) | (v << 24) | 
	      ((v << 8) & 0x00FF0000) | ((v >> 8) & 0x0000FF00);
	}

	
	public String toString() {
		return "Ogg-File: "+super.toString();
	}
}
