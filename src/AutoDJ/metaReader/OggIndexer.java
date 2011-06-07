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
		//raf.skipBytes(58); // that's the file identification header: not interesting
		readIdentificationHeader();
		readCommentHeader();   // we need to read that in order to know where to start looking
		//System.out.println("currently at " + raf.getFilePointer());
		
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
	 * read the ogg bitstream package header and do some sanity checks
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
				
		// we now skip the following:
		// 	version         = 1 byte,
		//	headerType      = 1 byte,
		//	granulePosition = 8 bytes,
		// 	bitstreamSerial = 4 bytes,
		//	pageSequenceNum = 4 bytes,
		//	crcChecksum     = 4 bytes
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
	 * reads the OGG Vorbis identification header
	 * @throws Exception 
	 */
	protected void readIdentificationHeader() throws Exception {
	    readOggHeader();
	    
	    // if this is not an identificationHeader, we can stop going any further
	    if( raf.read() != 0x01 ) 
		return;
	    
	    byte[] vorbisIdent = new byte[6];
	    raf.read(vorbisIdent);
	    
	    // this needs to be a vorbis header...
	    if( ! (new String(vorbisIdent).equals("vorbis")) )
		return;
	    
	    // we will now skip the following:
	    //     vorbisVersion = 4 bytes,
	    //     audioChannels = 1 byte,
	    //     sampleRate    = 4 bytes,
	    //     bitrateMax    = 4 bytes,
	    //     bitrateNom    = 4 bytes,
	    //     bitrateMin    = 4 bytes,
	    //     blocksize0    = 1 byte,
	    //     blocksize1    = 1 byte
	    // is 23 bytes of info we don't care about...
	    raf.skipBytes(23);
	}	
	
	/**
	 * starts reading the vorbis comment header.
	 * the actual metadata is parsed later
	 * @throws Exception 
	 */
	protected void readCommentHeader() throws Exception {
	    readOggHeader();
	    
	    byte[] headerTest = new byte[7];
	    
	    do {
		raf.read(headerTest);
		raf.seek(raf.getFilePointer() - 6);
		
	    } while (headerTest[0] != 0x03 && // comment header ident
		     headerTest[1] != 0x76 && // 'v'
		     headerTest[2] != 0x6F && // 'o'
		     headerTest[3] != 0x72 && // 'r'
		     headerTest[4] != 0x62 && // 'b'
		     headerTest[4] != 0x69 && // 'i'
		     headerTest[4] != 0x73    // 's'
		     );
	    
	    // assume, we found the header - skip until after the ident string
	    raf.skipBytes(6);
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
