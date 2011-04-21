/**
 * PlayerThread.java
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

package AutoDJ.audioPlayer;

import AutoDJ.*;
import java.io.*;

/**
 * Handle playback with mplayer (http://www.mplayerhq.hu)
 * 
 * most of the code is from http://beradrian.wordpress.com/2008/01/30/jmplayer/
 *
 */
public class PlayerThread extends Thread {
    
    PrintStream mplayerIn;
    BufferedReader mplayerOutErr;
    Process mplayerProcess;
    
    
    /**
     *  initialize connection to mplayer via buffers and stdin/stdout
     */
    public PlayerThread() {
	
	try {
	    mplayerProcess = Runtime.getRuntime().exec("/usr/bin/mplayer -slave -quiet -idle");
	    
	    // create the piped streams where to redirect the standard output and error of MPlayer
	    // specify a bigger pipesize than the default of 1024
	    PipedInputStream  readFrom = new PipedInputStream(256*1024);
	    PipedOutputStream writeTo = new PipedOutputStream(readFrom);
		
	    mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));

	    // create the threads to redirect the standard output and error of MPlayer
	    new StreamRedirecter(mplayerProcess.getInputStream(), writeTo).start();
	    new StreamRedirecter(mplayerProcess.getErrorStream(), writeTo).start();

	    // the standard input of MPlayer
	    mplayerIn = new PrintStream(mplayerProcess.getOutputStream());
	    
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    /**
     * stop playback and quit mplayer
     */
    public void kill() {
	mplayerIn.print("quit");
	mplayerIn.print("\n");
	mplayerIn.flush();
	try {
	    mplayerProcess.waitFor();
	}
	catch (InterruptedException e) {}
	
	interrupt();
    }
    
    public void run() {
	// IDLE
    }
    
    /**
     * load a song and start playback
     */
    public void loadSong(Song s) {
	String filename = s.getFile().getAbsolutePath();
	
	String command = "loadfile \""+filename+"\" 0";
	
	mplayerIn.print(command);
	mplayerIn.print("\n");
	mplayerIn.flush();
	
	getPlayingTime();
	
	try {
	    Thread.sleep(5000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
    }
    
    /**
     * pause playback
     */
    public void pause() {
	mplayerIn.print("pause");
	mplayerIn.print("\n");
	mplayerIn.flush();
    }
    
    /**
     * get the playing time in seconds as reported by mplayer
     */
    public int getPlayingTime() {
	mplayerIn.print("get_property length");
	mplayerIn.print("\n");
	mplayerIn.flush();
	
	String answer;
	int totalTime = -1;
	try {
	    while ((answer = mplayerOutErr.readLine()) != null) {
	        if (answer.startsWith("ANS_length=")) {
	            totalTime = (int)Double.parseDouble(answer.substring("ANS_length=".length()));
	            break;
	        }
	    }
	}
	catch (IOException e) {
	}
	
	return totalTime;
    }

}
