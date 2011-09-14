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
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import AutoDJ.prefs.Settings;

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
	
	private HashMap<String, HashMap<String, String> > queryPresets = new HashMap<String, HashMap<String, String> >();
	
	private String DESCRIBE_TABLE_QUERY = "";
	private String ADD_SONG_QUERY = "";
	private String GET_SONG_QUERY = "";
	private String CHANGE_SONG_QUERY = "";
	private String CREATE_SONG_TABLE_QUERY = "";
	
	/**
	 * Creates a new SongDatabase instance to work with and checks, if the
	 * database exists, if a connection is accepted and the tables have
	 * the correct format.
	 * TODO: actually check if the tables have the correct format. Maybe
	 * also do some sanity-checking on the table: Is there only one entry
	 * for each song? Does each song exist in the database which is
	 * referenced in the played-table?
	 */
	public SongDatabase(String db) {
		url = db;
		
		initQueryStrings();
		createConnection();
		
		// do we have the tables we need?
		if( !checkTable("songs", CREATE_SONG_TABLE_QUERY) ) {
		    PreparedStatement stmt = null;	
		    try {
		    	// try to create the songs table
				stmt = conn.prepareStatement(CREATE_SONG_TABLE_QUERY);
				stmt.execute();
				
				if( !checkTable("songs", CREATE_SONG_TABLE_QUERY) ) {
					System.out.println("fatal database failure");
					System.exit(0);
				}
			} catch (SQLException ex) {
				printDbError(ex);
			}
		}
		
		closeConnection();
	}
	
	/**
	 *  Create a connection to the mysql-server
	 */
	private void createConnection() {
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException ex) {
			printDbError(ex);
		}
		
	}
	
	/**
	 * Close the connection to the mysql-server
	 */
	private void closeConnection () {
		try {
			this.conn.close();
		} catch (SQLException ex) {
	        printDbError(ex);
		}
	}

	/**
	 * Adds a song to the database
	 * @param song The song to be added to the database.
	 */
	public void addSong (Song song) {
		try {
			createConnection();
			PreparedStatement statement = conn.prepareStatement(ADD_SONG_QUERY);
			statement.setString(1, song.getArtist());
			statement.setString(2, song.getTitle());
			statement.setInt(3, song.getTrackno());
			statement.setString(4, song.getAlbum());
			statement.setBytes(5, song.getCoverBytes() );
			statement.setInt(6, song.getYear());
			statement.setString(7, song.getGenre());
			statement.setString(8, song.getFile().getAbsolutePath());
			statement.setString(9, song.getMD5sum());
			statement.execute();
			closeConnection();
		} catch (SQLException ex) {
	        printDbError(ex, "occured for song " + song.getArtist() + " - " + song.getTitle());
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
			PreparedStatement statement = conn.prepareStatement(GET_SONG_QUERY);
			statement.setString(1, "%"+search+"%");
			statement.setString(2, "%"+search+"%");
			statement.setString(3, "%"+search+"%");
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("id");
				String artist = rs.getString("artist");
				String title        = rs.getString("title");
				int trackno         = rs.getInt("trackno");
				String album        = rs.getString("album");
				byte[] coverBlob      = rs.getBytes("cover");
				BufferedImage cover = null;
				try {
					cover = ImageIO.read(new ByteArrayInputStream(coverBlob));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int year = rs.getInt("year");
				String genre = rs.getString("genre");
				File filename = new File (rs.getString("filename"));
				String md5sum = rs.getString("md5sum");
				Song thisSong = new Song (id, artist, title, trackno, album,
						cover, year, genre, filename, md5sum);
				songList.add(thisSong);
			}
			closeConnection();
		} catch (SQLException ex) {
			printDbError(ex);
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
			PreparedStatement statement = conn.prepareStatement(CHANGE_SONG_QUERY);
			statement.setString(1, newSong.getArtist());
			statement.setString(2, newSong.getTitle());
			statement.setInt(3, newSong.getTrackno());
			statement.setString(4, newSong.getAlbum());
			statement.setBytes(5, newSong.getCoverBytes());
			statement.setInt(6, newSong.getYear());
			statement.setString(7, newSong.getFile().getAbsolutePath());
			statement.setString(8, newSong.getMD5sum());
			statement.setInt(9, oldSong.getId());
			statement.executeUpdate();
			closeConnection();
		} catch (SQLException ex) {
			printDbError(ex, "occured for song " + newSong.getArtist() + " - " + newSong.getTitle());
		}
	}	
	
	/**
	 * print a database error nicely
	 * 
	 * @param SQLException ex
	 */
	private void printDbError(SQLException ex) {
		printDbError(ex, "");
	}
	
	/**
	 * print a database error with an additional explanatory text
	 * 
	 * @param SQLException ex
	 * @param String additionalText
	 */
	private void printDbError(SQLException ex, String additionalText) {
		System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        if( !additionalText.isEmpty() ) {
        	System.out.println(additionalText);
        }
        
        ex.printStackTrace(System.err);
	}
	
	/**
	 * populates the query strings
	 */
	private void initQueryStrings() {
		
		// populate the mysql query container
		// use this as starting point for other db types
		HashMap<String, String> mysqlQueries = new HashMap<String, String>();
		mysqlQueries.put(
				"DESCRIBE_TABLE_QUERY",
				"DESCRIBE ");
		
		// this has to look exactly like the DESCRIBE_TABLE_QUERY returns it
		mysqlQueries.put(
				"CREATE_SONG_TABLE_QUERY",
				"CREATE TABLE songs ( " +
				"id INT AUTO_INCREMENT PRIMARY KEY NOT NULL, " +
				"artist VARCHAR(50) NOT NULL, " +
				"title VARCHAR(100) NOT NULL, " +
				"trackno TINYINT, " +
				"album VARCHAR(50), " +
				"cover MEDIUMBLOB, " +
				"year INT, " +
				"genre VARCHAR(30), " +
				"filename VARCHAR(200) NOT NULL, "+
				"md5sum CHAR(32) NOT NULL "+
				")");
		mysqlQueries.put(
				"ADD_SONG_QUERY", 
				"INSERT INTO songs " +
				"(artist, title, trackno, album, cover, year, genre, filename, md5sum)" +
				"VALUES (?,?,?,?,?,?,?,?,?)");
		mysqlQueries.put(
				"GET_SONG_QUERY",
				"SELECT * FROM songs WHERE artist LIKE ? " +
				"OR title LIKE ? OR album LIKE ? ORDER BY artist, year, trackno, album");
		mysqlQueries.put(
				"CHANGE_SONG_QUERY", 
				"UPDATE songs SET artist=?, title=?, " +
				"trackno=?, album=?, cover=?, year=?, filename=?, md5sum=? WHERE id=?");
		
		// populate sqlite query container
		// just copy the mysql strings and overwrite what's different
		HashMap<String, String> sqliteQueries = new HashMap<String, String>();
		sqliteQueries.putAll(mysqlQueries);
		
		sqliteQueries.put(
			"DESCRIBE_TABLE_QUERY", 
			"SELECT sql FROM sqlite_master WHERE name = ?");
		
		// this has to look exactly like the DESCRIBE_TABLE_QUERY returns it
		sqliteQueries.put(
				"CREATE_SONG_TABLE_QUERY", 
				"CREATE TABLE songs\n" +
				"(id INTEGER PRIMARY KEY NOT NULL,\n"+
				"artist TEXT(50) NOT NULL,\n"+
				"title TEXT(100) NOT NULL,\n"+
				"trackno INTEGER,\n"+
				"album TEXT(50),\n"+
				"cover BLOB,\n"+
				"year INTEGER,\n"+
				"genre TEXT(30),\n"+
				"filename TEXT(200) NOT NULL,\n"+
				"md5sum TEXT(32) NOT NULL\n"+
				")");
		
		queryPresets.put("mysql", mysqlQueries);
		queryPresets.put("sqlite", sqliteQueries);
		
		// assign the query strings to the variables that get used in the code
		String dbType = Settings.get("dbType", "mysql");
		
		DESCRIBE_TABLE_QUERY = queryPresets.get(dbType).get("DESCRIBE_TABLE_QUERY");
		ADD_SONG_QUERY = queryPresets.get(dbType).get("ADD_SONG_QUERY");
		GET_SONG_QUERY = queryPresets.get(dbType).get("GET_SONG_QUERY");
		CHANGE_SONG_QUERY = queryPresets.get(dbType).get("CHANGE_SONG_QUERY");
		CREATE_SONG_TABLE_QUERY = queryPresets.get(dbType).get("CREATE_SONG_TABLE_QUERY");
	}
	
	/**
	 * see, if the a table is useable
	 *
	 * @param String table name
	 * @param String createStatement (must be equal to what describe returns)
	 */
	private boolean checkTable(String table, String createStatement) {
		try {
			String query = DESCRIBE_TABLE_QUERY + table;
			PreparedStatement stmt = conn.prepareStatement(query);
	
			if( !stmt.execute() ) {
				return false;
			}
	
			ResultSet rs = stmt.getResultSet();
			
			if( !rs.next() ) {
				rs.close();
				return false;
			}
	
			rs.close();
			return true;
			
		} catch(SQLException ex) {
			printDbError(ex);
		}
		
		return false;
	}
}
