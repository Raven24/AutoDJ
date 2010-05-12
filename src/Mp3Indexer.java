import java.net.URI;
import java.nio.ByteBuffer;
import java.io.*;

/**
 * class Mp3Indexer
 * 
 * provides the functionality to read metadata (ID3-Tags) from MP3 files
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public class Mp3Indexer extends AudioFileIndexer {

	protected int tagSize = 128;	// size of an ID3 tag
	
	public void populateMetadata() {
		byte[] tag = new byte[3];
        byte[] tagTitle = new byte[30];
        byte[] tagArtist = new byte[30];
        byte[] tagAlbum = new byte[30];
        byte[] tagYear = new byte[4];
        byte[] tagComment = new byte[30];
        byte[] tagGenre = new byte[1];
        buff.get(tag).get(tagTitle).get(tagArtist).get(tagAlbum)
                        .get(tagYear).get(tagComment).get(tagGenre);
        if(!"TAG".equals(new String(tag))){
                throw new IllegalArgumentException(
                        "ByteBuffer does not contain ID3 tag data"
                );
        }
        
        title 	= new String(tagTitle).trim();
        artist 	= new String(tagArtist).trim();
        album	= new String(tagAlbum).trim();
        year	= new String(tagYear).trim();
        comment	= new String(tagComment).trim();
        setGenre(tagGenre[0]);
        
	}
	
	
	public void readFile(String path) throws Exception {
		audioFile = new File(new URI(path));
		RandomAccessFile raf = new RandomAccessFile(audioFile, "r");
		byte[] tagData = new byte[tagSize];
		raf.seek(raf.length() - tagSize);
		raf.read(tagData);
		
		buff = ByteBuffer.allocate(tagSize);
		buff.put(tagData);
		buff.rewind();		
	}
	
	/**
	 * setting the genre according to the ID3 specification
	 * http://id3.org/d3v2.3.0
	 * 
	 * @param num
	 */
	public void setGenre(byte num) {
		switch (new Byte(num).intValue()) {
			case 0: genre 	= "Blues"; break;
			case 1: genre 	= "Classic Rock"; break;
			case 2: genre 	= "Country"; break;
			case 3: genre 	= "Dance"; break;
			case 4: genre 	= "Disco"; break;
			case 5: genre 	= "Funk"; break;
			case 6: genre	= "Grunge"; break;
			case 7: genre 	= "Hip-Hop"; break;
			case 8: genre 	= "Jazz"; break;
			case 9: genre 	= "Metal"; break;
			case 10: genre 	= "New Age"; break;
			case 11: genre 	= "Oldies"; break;
			case 12: genre 	= "Other"; break;
			case 13: genre 	= "Pop"; break;
			case 14: genre 	= "R&B"; break;
			case 15: genre 	= "Rap"; break;
			case 16: genre 	= "Reggae"; break;
			case 17: genre 	= "Rock"; break;
			case 18: genre 	= "Techno"; break;
			case 19: genre 	= "Industrial"; break;
			case 20: genre 	= "Alternative"; break;
			case 21: genre 	= "Ska"; break;
			case 22: genre 	= "Death Metal"; break;
			case 23: genre 	= "Pranks"; break;
			case 24: genre 	= "Soundtrack"; break;
			case 25: genre 	= "Euro-Techno"; break;
			case 26: genre 	= "Ambient"; break;
			case 27: genre 	= "Trip-Hop"; break;
			case 28: genre 	= "Vocal"; break;
			case 29: genre 	= "Jazz+Funk"; break;
			case 30: genre 	= "Fusion"; break;
			case 31: genre 	= "Trance"; break;
			case 32: genre 	= "Classical"; break;
			case 33: genre 	= "Instrumental"; break;
			case 34: genre 	= "Acid"; break;
			case 35: genre 	= "House"; break;
			case 36: genre 	= "Game"; break;
			case 37: genre 	= "Sound Clip"; break;
			case 38: genre 	= "Gospel"; break;
			case 39: genre 	= "Noise"; break;
			case 40: genre 	= "AlternRock"; break;
			case 41: genre 	= "Bass"; break;
			case 42: genre 	= "Soul"; break;
			case 43: genre 	= "Punk"; break;
			case 44: genre 	= "Space"; break;
			case 45: genre 	= "Meditative"; break;
			case 46: genre 	= "Instrumental Pop"; break;
			case 47: genre 	= "Instrumental Rock"; break;
			case 48: genre 	= "Ethnic"; break;
			case 49: genre 	= "Gothic"; break;
			case 50: genre 	= "Darkwave"; break;
			case 51: genre 	= "Techno-Industrial"; break;
			case 52: genre 	= "Electronic"; break;
			case 53: genre 	= "Pop-Folk"; break;
			case 54: genre 	= "Eurodance"; break;
			case 55: genre 	= "Dream"; break;
			case 56: genre 	= "Southern Rock"; break;
			case 57: genre 	= "Comedy"; break;
			case 58: genre 	= "Cult"; break;
			case 59: genre 	= "Gangsta"; break;
			case 60: genre 	= "Top 40"; break;
			case 61: genre 	= "Christian Rap"; break;
			case 62: genre 	= "Pop/Funk"; break;
			case 63: genre 	= "Jungle"; break;
			case 64: genre 	= "Native American"; break;
			case 65: genre 	= "Cabaret"; break;
			case 66: genre 	= "New Wave"; break;
			case 67: genre 	= "Psychadelic"; break;
			case 68: genre 	= "Rave"; break;
			case 69: genre 	= "Showtunes"; break;
			case 70: genre 	= "Trailer"; break;
			case 71: genre 	= "Lo-Fi"; break;
			case 72: genre 	= "Tribal"; break;
			case 73: genre 	= "Acid Punk"; break;
			case 74: genre 	= "Acid Jazz"; break;
			case 75: genre 	= "Polka"; break;
			case 76: genre 	= "Retro"; break;
			case 77: genre 	= "Musical"; break;
			case 78: genre 	= "Rock & Roll"; break;
			case 79: genre 	= "Hard Rock"; break;
			// the following genres are Winamp extensions
			case 80: genre 	= "Folk"; break;
			case 81: genre 	= "Folk-Rock"; break;
			case 82: genre 	= "National Folk"; break;
			case 83: genre 	= "Swing"; break;
			case 84: genre 	= "Fast Fusion"; break;
			case 85: genre 	= "Bebob"; break;
			case 86: genre 	= "Latin"; break;
			case 87: genre 	= "Revival"; break;
			case 88: genre 	= "Celtic"; break;
			case 89: genre 	= "Bluegrass"; break;
			case 90: genre 	= "Avantgarde"; break;
			case 91: genre 	= "Gothic Rock"; break;
			case 92: genre 	= "Progressive Rock"; break;
			case 93: genre 	= "Psychedelic Rock"; break;
			case 94: genre 	= "Symphonic Rock"; break;
			case 95: genre 	= "Slow Rock"; break;
			case 96: genre 	= "Big Band"; break;
			case 97: genre 	= "Chorus"; break;
			case 98: genre 	= "Easy Listening"; break;
			case 99: genre 	= "Acoustic"; break;
			case 100: genre	= "Humor"; break;
			case 101: genre	= "Speech"; break;
			case 102: genre	= "Chanson"; break;
			case 103: genre	= "Opera"; break;
			case 104: genre	= "Chamber Music"; break;
			case 105: genre	= "Sonata"; break;
			case 106: genre	= "Symphony"; break;
			case 107: genre	= "Booty Bass"; break;
			case 108: genre	= "Primus"; break;
			case 109: genre	= "Porn Groove"; break;
			case 110: genre	= "Satire"; break;
			case 111: genre	= "Slow Jam"; break;
			case 112: genre	= "Club"; break;
			case 113: genre	= "Tango"; break;
			case 114: genre	= "Samba"; break;
			case 115: genre	= "Folklore"; break;
			case 116: genre	= "Ballad"; break;
			case 117: genre	= "Power Ballad"; break;
			case 118: genre	= "Rhythmic Soul"; break;
			case 119: genre	= "Freestyle"; break;
			case 120: genre	= "Duet"; break;
			case 121: genre	= "Punk Rock"; break;
			case 122: genre	= "Drum Solo"; break;
			case 123: genre	= "Acapella"; break;
			case 124: genre	= "Euro-House"; break;
			case 125: genre	= "Dance Hall"; break;
			default: return;
		}
	}
}
