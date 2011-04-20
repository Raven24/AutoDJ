import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;


public class Song {
	private int id;
	private String artist;
	private String title;
	private int trackno;
	private String album;
	private int year;
	private File filename;
	private String md5sum;
	
	/*
	 * 
	 * constructor from file
	 * 
	 */
	public Song(File file) {
		filename = file;
		// calculate the md5sum
		try {
			md5sum = calculateMD5(filename);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// get the ID3 tag information
		try {
			MP3File mp3File = new MP3File (filename);
			if (mp3File.hasID3v2Tag()) {
				AbstractID3v2Tag tag = mp3File.getID3v2TagAsv24();
				artist = tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
				title = tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
				album = tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
				year = Integer.parseInt(tag.getFirst(ID3v24Frames.FRAME_ID_YEAR));
				trackno = Integer.parseInt(tag.getFirst(ID3v24Frames.FRAME_ID_TRACK));
				// 	FRAME_ID_GENRE, 
			} else {
				throw new RuntimeException ("File "+this.filename.getAbsolutePath()+" has no ID3v2 tag!");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	 * 
	 * constructor from database
	 * 
	 */
	public Song(int id, String artist, String title, int trackno, String album, int year, File filename, String md5sum) {
		this.id=id;
		this.artist=artist;
		this.title=title;
		this.trackno=trackno;
		this.album=album;
		this.year=year;
		this.filename=filename;
		this.md5sum=md5sum;
	}
	
	/*
	 * 
	 * calculate a MD5sum of a given file
	 * 
	 */
	private String calculateMD5(File file) throws NoSuchAlgorithmException, FileNotFoundException {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(file);				
		byte[] buffer = new byte[8192];
		int read = 0;
		try {
			while( (read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}		
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			return bigInt.toString(16);
		} catch(IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} finally {
			try {
				is.close();
			}
			catch(IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}		
	}

	/*
	 * 
	 * return the id of this song
	 * 
	 */
	public int getId() {
		return this.id;
	}
	
	/*
	 * 
	 * return the artist of this song
	 * 
	 */
	public String getArtist() {
		return this.artist;
	}
	
	/*
	 * 
	 * return the title of this song
	 * 
	 */
	public String getTitle() {
		return this.title;
	}
	
	/*
	 * 
	 * return the track number of this song
	 * 
	 */
	public int getTrackno() {
		return this.trackno;
	}

	/*
	 * 
	 * return the album of this song
	 * 
	 */
	public String getAlbum() {
		return this.album;
	}
	
	/*
	 * 
	 * return the release year of this song
	 * 
	 */
	public int getYear() {
		return this.year;
	}
	
	/*
	 * 
	 * return the file of this song
	 * 
	 */
	public File getFile() {
		return this.filename;
	}
	
	/*
	 * 
	 * return the MD5sum of this song
	 * 
	 */
	public String getMD5sum() {
		return this.md5sum;
	}
	
	/*
	 * 
	 * print all info about this song
	 * 
	 */
	public String toString() {
		String retval="Info about Song";
		retval+=getFile().getAbsolutePath() + "\n";
		retval+=getArtist() + " - " + getTitle() + "\n";
		retval+="from the album: " + getAlbum() + ", released in " +getYear() + "\n";
		retval+="file has a MD5sum of " + getMD5sum();
		return retval;
	}
	
	/*
	 * 
	 * compare 2 songs
	 * 
	 */
	public boolean equals(Song song2) {
		if (this.getMD5sum().compareTo(song2.getMD5sum())==0) {
			if (this.getFile().compareTo(song2.getFile())==0) return true;
		}
		return false;
	}
}
