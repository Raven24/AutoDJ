/**
 * FilePreferencesFactory.java
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

package AutoDJ.prefs;

import java.io.File;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

/**
 * create a preference object that stores the values in a file
 * 
 * mostly taken from 
 * http://www.davidc.net/programming/java/java-preferences-using-file-backing-store
 * 
 * @author Florian Staudacher
 */
public class FilePreferencesFactory implements PreferencesFactory {

    /**
     * the name of the config file relative to the user's home.
     */
    private static final String prefsFileName = ".autodj";
    
    Preferences rootPrefs;
    private static File prefsFile;
        
    
    /**
     * this method calls userRoot()
     */
    @Override
    public Preferences systemRoot() {
	return userRoot();
    }

    /**
     * return the preferences object,
     * if it hasn't been instantiated yet, create one
     */
    @Override
    public Preferences userRoot() {
	if( rootPrefs == null ) {
	    rootPrefs = new FilePreferences(null, "");
	}
	return rootPrefs;
    }
    
    
    
    /**
     * get a reference to the config file in the user's home dir
     * on Windows this will be something like
     *     "C:\Documents and Settings\User\.autodj
     * on unix-like systems it's going to be like
     *     "/home/user/.autodj"
     *      
     * @return File settings directory
     */
    public static File getPreferencesFile() {
	if( prefsFile == null ) {
	    String userHome = System.getProperty("user.home");
	    if(userHome == null) {
		throw new IllegalStateException("user.home==null");
	    }
	    File home = new File(userHome);
	    prefsFile = new File(home, prefsFileName);
	    if(!prefsFile.exists()) {
		try {
		    if(!prefsFile.createNewFile()) {
			throw new IllegalStateException(prefsFile.toString());
		    }
		} catch( Exception e ) {
		    e.printStackTrace();
		}
	    }
	}
	return prefsFile;
    }

}
