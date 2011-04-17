package djay.tests;

import djay.AudioFileIndexer;
import java.io.*;

/**
 * This class produces a test executeable to check the functionality 
 * of metadata parsing from audio file headers.
 * 
 * The binary can be called with the following parameters
 * 
 * -f/--readMp3/--readOgg
 * 		takes an _absolute_ filename and analyzes a the file
 * 		prints out the metadata
 * -d/--readDir
 * 		takes an _absolute_ directory and analyzes all audio files
 * 		it finds inside. prints out the metadata of each file
 * 
 * @author Florian Staudacher
 *
 */


public class UnitTests {

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
				
			} else out("unknown command");
			
		}
	}
	
	protected static void out(String text) {
		System.out.println(text);
	}

}
