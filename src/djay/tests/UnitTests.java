package djay.tests;

import djay.AudioFileIndexer;


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
				
			} else if (command.equalsIgnoreCase("readMp3") || command.equalsIgnoreCase("readOgg")) {
				// see if we have a filename here
				if(!param.startsWith("-")) {
					out("reading "+param+" ...");
					
					AudioFileIndexer audio = AudioFileIndexer.initIndexer(param);
					audio.getFileInfo();
					out(audio.toString());
				}
				
			} else out("unknown command");
			
		}
	}
	
	protected static void out(String text) {
		System.out.println(text);
	}

}
