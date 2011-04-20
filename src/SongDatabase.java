import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class SongDatabase {

	private Connection conn;
	private final String url;
	
	/*
	 * 
	 * constructor
	 * 
	 */
	public SongDatabase() {
		url = "jdbc:mysql://localhost/autodj?user=christian&password=password";
		createConnection();
		// do we have the tables we need?
		Statement stmt = null;
	    ResultSet rs = null;		
		try {
		    stmt = conn.createStatement();
		    if (stmt.execute("DESCRIBE songs")) {
		    	rs = stmt.getResultSet();
		    	//System.out.println(rs.toString());
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
		// good, we're ready to go
	}
	
	/*
	 * 
	 *  create a connection to the mysql-server
	 * 
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
	
	/*
	 * 
	 * close the connection to the mysql-server
	 * 
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

	/*
	 * 
	 * add a song to the DB
	 * 
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
	
	/*
	 * 
	 * get songlist from the DB
	 * 
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
			// TODO
			//System.out.println(query);
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
	
	
	
}
