package AutoDJ.metaReader;

import java.nio.ByteBuffer;
import java.io.File;
import java.io.RandomAccessFile;
import java.lang.Exception;


/**
 * abstract class AudioFileIndexer
 * 
 * provides an interface to read the metadata of an audio file
 * subclasses have to implement the actual parsing according to the file format
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public abstract class AudioFileIndexer {

	protected File audioFile;	// contains the file handle to the audio file
	protected ByteBuffer buff;  // contains the actual metadata
	protected RandomAccessFile raf;
	protected String filePath; 
	
	// here comes the metadata
	protected String title, artist, album, comment, genre, year;
	protected long 	lastModified,	// milliseconds since 1970-01-01 00:00 
					length;			// filesize in bytes
	
	/**
	 * reads the metadata in the audio file and saves it to local member variables
	 * 
	 * TODO: implement in the subclasses
	 */
	abstract void populateMetadata();
	
	/**
	 * opens the file and puts the metadata in ByteBuffer buff
	 * and places the file handle in audioFile
	 * 
	 * TODO: implement in the subclass
	 * 
	 * @param path
	 * @throws Exception 
	 */
	abstract void readFile(String path) throws Exception;
	
	/**
	 * gets infos about the file, that are not music-related
	 */
	public void getFileInfo() {
		//System.out.println(filePath);
		populateMetadata();
		lastModified = audioFile.lastModified();
		length = audioFile.length();
	}
	
	/**
	 * return the matching indexer determined by the file extension
	 * 
	 * @param fileName
	 * @return
	 */
	public static AudioFileIndexer initIndexer(String fileName) {
		String ext = fileName.substring(fileName.length()-3);
				
		if(ext.equalsIgnoreCase("mp3")) return new Mp3Indexer(fileName);
		if(ext.equalsIgnoreCase("ogg") ||
		   ext.equalsIgnoreCase("oga")) return new OggIndexer(fileName);
		
		System.out.println("unknown filetype");
		return null;
	}
	
	public String toString() {
		return title+" by "+artist; //+"\n\t"+filePath;
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
	
/*	protected long getLongFromBuff() {
		byte[] tmp = new byte[8];		
		try {
			raf.read(tmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unsignedBytesToLong(tmp);
	}
	
	protected long unsignedBytesToLong(byte[] tmp) {
		long res = 0;
		for(int i=0; i<tmp.length; i++) {
               res|=(1L<<i);
        }
		return res;
	}*/
	
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
}
