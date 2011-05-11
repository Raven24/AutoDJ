/**
 * StreamRedirecter.java
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

import java.io.*;

/** 
 * Redirect streams line by line from input to output
 * code taken from http://beradrian.wordpress.com/2008/01/30/jmplayer/
 *
 */
public class StreamRedirecter extends Thread {
    /** The input stream to read from. */
    private InputStream in;
    /** The output stream to write to. */
    private OutputStream out;

    /**
     * @param in the input stream to read from.
     * @param out the output stream to write to.
     * @param prefix the prefix used to prefix the lines when outputting to the logger.
     */
    StreamRedirecter(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void run()
    {
        try {
            // creates the decorating reader and writer
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintStream printStream = new PrintStream(out);
            String line;

            // read line by line
            while ( (line = reader.readLine()) != null) {
                printStream.println(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
