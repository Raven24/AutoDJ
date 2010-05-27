package djay;

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
			addToMap(content);
		}			
	}
		
	public String toString() {
		return "Ogg-File: "+super.toString();
	}
	
	protected void addToMap(byte[] pair) {
		String[] vals = new String(pair).split("=");
		if (vals.length > 1) {
			vorbisComments.put(vals[0].toLowerCase(), vals[1]);
		}
	}
	
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
}
