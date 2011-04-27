/**
 * SongDatabase.java
 * (C) 2011 Florian Staudacher, Christian Wurst
 * 
 * This file is part of AutoDJ.
 *
 * AutoDJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoDJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AutoDJ.  If not, see <http://www.gnu.org/licenses/>.
 */

package AutoDJ;
 
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * SongDatabase is a class which represents a song database for AutoDJ.
 * It does all the communication with the database.
 */

public class SongDatabase {
	/**
	 * the connection to the database
	 */
	private Connection conn;
	/**
	 * the URL to the database
	 */
	private final String url;
	
	/**
	 * Creates a new SongDatabase instance to work with and checks, if the
	 * database exists, if a connection is accepted and the tables have
	 * the correct format.
	 * TODO: actually check if the tables have the correct format. Maybe
	 * also do some sanity-checking on the table: Is there only one entry
	 * for each song? Does each song exist in the database which is
	 * referenced in the played-table?
	 */
	public SongDatabase() {
		// FIXME!
		url = "jdbc:mysql://localhost/autodj?user=christian&password=password";
		createConnection();
		// do we have the tables we need?
		Statement stmt = null;
	    ResultSet rs = null;		
		try {
		    stmt = conn.createStatement();
		    if (stmt.execute("DESCRIBE songs")) {
		    	rs = stmt.getResultSet();
		    }
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// Ressourcen sollten immer in einem
		    // finally{}-Block
		    // in der umgekehrten Reihenfolge ihrer Zuweisung
		    // freigegeben werden
			if (rs != null) {
				try {
		            rs.close();
		        } catch (SQLException sqlEx) { /* ignore */ }
		        rs = null;
			}
	        if (stmt != null) {
		        try {
		            stmt.close();
		        } catch (SQLException sqlEx) { /* ignore */ }
		        stmt = null;
		    }
		}
		closeConnection();
	}
	
	/**
	 *  Create a connection to the mysql-server
	 */
	private void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url);
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Close the connection to the mysql-server
	 */
	private void closeConnection () {
		try {
			this.conn.close();
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Adds a song to the database
	 * @param song The song to be added to the database.
	 */
	public void addSong (Song song) {
		try {
			createConnection();
			Statement statement = conn.createStatement();
			String query="INSERT INTO songs VALUES (0";
			query+=", \"" + song.getArtist() + "\"";
			query+=", \"" + song.getTitle() + "\"";
			query+=", " + song.getTrackno();
			query+=", \"" + song.getAlbum() + "\"";
			query+=", " + song.getYear();
			query+=", \"" + song.getFile().getAbsolutePath() + "\"";
			query+=", '" + song.getMD5sum() + "'";
			query+=")";
			statement.executeUpdate(query);
			closeConnection();
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	        System.out.println("occured for song " + song.getArtist() + " - " + song.getTitle());
		}
	}
	
	/**
	 * Searches the database for songs matching a given search string.
	 * The string is matched against the songs artist, title and album.
	 * @param search The string which is searched for.
	 * @return A Vector of Song objects which represent all songs that
	 * match the search string. If the search string is empty, all
	 * songs in the database are returned.
	 * @see Song
	 */
	public Vector<Song> getSongs (String search) {
		Vector<Song> songList = new Vector<Song>();
		try {
			createConnection();
			Statement statement = conn.createStatement();
			String query="SELECT * FROM songs WHERE ";
			query+="artist LIKE \"%" + search +"%\" OR ";
			query+="title LIKE \"%" + search +"%\" OR ";
			query+="album LIKE \"%" + search +"%\"";
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()) {
				int id = rs.getInt("id");
				String artist = rs.getString("artist");
				String title = rs.getString("title");
				int trackno = rs.getInt("trackno");
				String album = rs.getString("album");
				int year = rs.getInt("year");
				File filename = new File (rs.getString("filename"));
				String md5sum = rs.getString("md5sum");
				Song thisSong = new Song (id, artist, title, trackno, album, year, filename, md5sum);
				songList.add(thisSong);
			}
			closeConnection();
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
		}
		return songList;
	}

	/**
	 * Changes an entry for a song in the database.
	 * @param oldSong A Song object containing the outdated information
	 * about the song. Only the database id of this object is
	 * actually used.
	 * @param newSong A Song object containing the updated information
	 * about the song.
	 * @see Song
	 */
	public void changeSong(Song oldSong, Song newSong) {
		try {
			createConnection();
			Statement statement = conn.createStatement();
			String query="UPDATE songs SET ";
			query+="artist = \"" + newSong.getArtist() + "\", ";
			query+="title = \"" + newSong.getTitle() + "\", ";
			query+="trackno = " + newSong.getTrackno() + ", ";
			query+="album = \"" + newSong.getAlbum() + "\", ";
			query+="year = " + newSong.getYear() + ", ";
			query+="filename = \"" + newSong.getFile().getAbsolutePath() + "\", ";
			query+="md5sum = '" + newSong.getMD5sum() + "' ";
			query+="WHERE id = " + oldSong.getId();
			System.out.println(query);
			statement.executeUpdate(query);
			closeConnection();
		} catch (SQLException ex) {
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	        System.out.println("occured for song " + newSong.getArtist() + " - " + newSong.getTitle());
		}
	}	
}
