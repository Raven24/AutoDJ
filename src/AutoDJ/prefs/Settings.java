/**
 * Settings.java
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

import java.util.prefs.Preferences;

/**
 * @author Florian Staudacher
 *
 */
public class Settings {

    private static Preferences p;
    
    public static String get(String key, String defaultValue) {
	if( p == null ) {
	    p = Preferences.userNodeForPackage(Settings.class);
	}
	return p.get(key, defaultValue);
    }
    
    public static String get(String key) {
	return Settings.get(key, "");
    }
    
    public static void set(String key, String val) {
	if( p == null ) {
	    p = Preferences.userNodeForPackage(Settings.class);
	}
	p.put(key, val);
    }
    
    

}
