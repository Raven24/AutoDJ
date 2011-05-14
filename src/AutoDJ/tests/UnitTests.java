package AutoDJ.tests;

import djay.AudioFileIndexer;
import java.io.*;
import AutoDJ.*;
import AutoDJ.audioPlayer.*;
import AutoDJ.prefs.FilePreferencesFactory;
import AutoDJ.prefs.Settings;

/**
 * This class produces a test executeable to check the functionality 
 * of various parts of the AutoDJ program.
 * 
 * The binary can be called with the following parameters
 * 
 * -f/--readMp3/--readOgg
 * 		takes an _absolute_ filename and analyzes a the file
 * 		prints out the metadata
 * -d/--readDir
 * 		takes an _absolute_ directory and analyzes all audio files
 * 		it finds inside. prints out the metadata of each file
 * -p/--play
 * 		takes an _absolute_ filename and tries to play it
 * -s/--settings
 * 		tries to write and then read settings to the user's confif file
 * 
 * @author Florian Staudacher
 *
 */


public class UnitTests {

    	static PlayerThread t;
    	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String command = "";
		// parsing args
		for(String param: args) {	// "foreach"
			if(param.startsWith("-")) {
				// remove the "-" and "--"s
				if(param.startsWith("--")) param = param.substring(1);
				command = param.substring(1);
			}
			
			if(command.equalsIgnoreCase("aaa")) {
				out("test");
				
			// read one or more files
			} else if (command.equalsIgnoreCase("readMp3") 
				|| command.equalsIgnoreCase("readOgg")
				|| command.equalsIgnoreCase("f")) {
				// see if the param is not just another command
				if(!param.startsWith("-")) {
					out("reading "+param+" ...");
					
					AudioFileIndexer audio = AudioFileIndexer.initIndexer(param);
					audio.getFileInfo();
					out(audio.toString());
				}
				
			// read a whole directory
			} else if (command.equalsIgnoreCase("d")
				|| command.equalsIgnoreCase("readDir")) {
				// see if the param is not just another command
				if(!param.startsWith("-")) {
					File dir = new File(param);
					FilenameFilter filter = new FilenameFilter() {
						public boolean accept(File dir, String name) {
							return (name.endsWith("ogg") ||
									name.endsWith("oga") ||
									name.endsWith("mp3"));
						}
					};
					String[] entries = dir.list(filter);
					for (int i = 0; i < entries.length; i++) {
						AudioFileIndexer audio = AudioFileIndexer.initIndexer(dir.getAbsolutePath()+"/"+entries[i]);
						audio.getFileInfo();
						out(audio.toString());
					}
					
				}
			// play back an audio file
			} else if( command.equalsIgnoreCase("p")
				|| command.equalsIgnoreCase("play") ) {
			    if( param.startsWith("-")) continue; // just another command, not a filename
			    
			    Song test = new Song(new File(param));
			    
			    out("Now playing: " + test.getArtist() + " - " + test.getTitle() + " for 5 Seconds");
			    
			    t = new PlayerThread();
			    t.start();
			    t.loadSong(test);
			
			    ( new Thread() {
				public void run() {
				    try {
					Thread.sleep(5000);
				    } catch(Exception e) {}
				    
				    t.kill();
				    out("\nPlayback stopped.");
				}
			    }).start();
			   
			} else if( command.equalsIgnoreCase("s")
				|| command.equalsIgnoreCase("settings") ) {
			    if( param.startsWith("-")) continue; // just another command, ignore
			    
			    // initialize preferences 
			    System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
			    			    
			    out("## testing settings storage implementation");
			    
			    Settings.set("tests/test1", "123456789");
			    Settings.set("tests/test2", "abcdefghi");
			    Settings.set("tests/time", String.valueOf(System.currentTimeMillis()));
			   
			} else 
			    out("unknown command");
			
		}
	}
	
	protected static void out(String text) {
		System.out.println(text);
	}

}
