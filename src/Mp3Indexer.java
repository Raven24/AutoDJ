
/**
 * class Mp3Indexer
 * 
 * provides the functionality to read metadata (ID3-Tags) from MP3 files
 * 
 * @author Florian Staudacher <florian_staudacher@yahoo.de>
 *
 */
public class Mp3Indexer extends AudioFileIndexer {

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
	
	/**
	 * setting the genre according to the ID3 specification
	 * http://id3.org/d3v2.3.0
	 * 
	 * @param num
	 */
	public void setGenre(byte num) {
		
		
	}
}
