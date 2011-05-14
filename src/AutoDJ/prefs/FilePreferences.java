/**
 * FilePreferences.java
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.prefs.*;

/**
 * @author Florian Staudacher
 * 
 * mostly taken from 
 * http://www.davidc.net/programming/java/java-preferences-using-file-backing-store
 *
 */
public class FilePreferences extends AbstractPreferences {

    private Map<String, String> root;
    private Map<String, FilePreferences> children;
    private boolean isRemoved = false;

    public FilePreferences(AbstractPreferences parent, String name)
    {
	super(parent, name);

	root = new TreeMap<String, String>();
	children = new TreeMap<String, FilePreferences>();

	try {
	    sync();
	}
	catch (BackingStoreException e) {
	    e.printStackTrace();
	}
    }

    protected void putSpi(String key, String value)
    {
	root.put(key, value);
	try {
	    flush();
	}
	catch (BackingStoreException e) {
	    e.printStackTrace();
	}
    }

    protected String getSpi(String key)
    {
	return root.get(key);
    }

    protected void removeSpi(String key)
    {
	root.remove(key);
	try {
	    flush();
	}
	catch (BackingStoreException e) {
	    e.printStackTrace();
	}
    }

    protected void removeNodeSpi() throws BackingStoreException
    {
	isRemoved = true;
	flush();
    }

    protected String[] keysSpi() throws BackingStoreException
    {
	return root.keySet().toArray(new String[root.keySet().size()]);
    }

    protected String[] childrenNamesSpi() throws BackingStoreException
    {
	return children.keySet().toArray(new String[children.keySet().size()]);
    }

    protected FilePreferences childSpi(String name)
    {
	FilePreferences child = children.get(name);
	if (child == null || child.isRemoved()) {
	    child = new FilePreferences(this, name);
	    children.put(name, child);
	}
	return child;
    }


    protected void syncSpi() throws BackingStoreException
    {
	if (isRemoved()) return;

	final File file = FilePreferencesFactory.getPreferencesFile();

	if (!file.exists()) return;

	synchronized (file) {
	    Properties p = new Properties();
	    try {
		p.load(new FileInputStream(file));

		StringBuilder sb = new StringBuilder();
		getPath(sb);
		String path = sb.toString();

		final Enumeration<?> pnen = p.propertyNames();
		while (pnen.hasMoreElements()) {
		    String propKey = (String) pnen.nextElement();
		    if (propKey.startsWith(path)) {
			String subKey = propKey.substring(path.length());
			// Only load immediate descendants
			if (subKey.indexOf('.') == -1) {
			    root.put(subKey, p.getProperty(propKey));
			}
		    }
		}
	    }
	    catch (IOException e) {
		throw new BackingStoreException(e);
	    }
	}
    }

    private void getPath(StringBuilder sb)
    {
	final FilePreferences parent = (FilePreferences) parent();
	if (parent == null) return;

	parent.getPath(sb);
	sb.append(name()).append('.');
    }

    protected void flushSpi() throws BackingStoreException
    {
	final File file = FilePreferencesFactory.getPreferencesFile();

	synchronized (file) {
	    Properties p = new Properties();
	    try {

		StringBuilder sb = new StringBuilder();
		getPath(sb);
		String path = sb.toString();

		if (file.exists()) {
		    p.load(new FileInputStream(file));

		    List<String> toRemove = new ArrayList<String>();

		    // Make a list of all direct children of this node to be removed
		    final Enumeration<?> pnen = p.propertyNames();
		    while (pnen.hasMoreElements()) {
			String propKey = (String) pnen.nextElement();
			if (propKey.startsWith(path)) {
			    String subKey = propKey.substring(path.length());
			    // Only do immediate descendants
			    if (subKey.indexOf('.') == -1) {
				toRemove.add(propKey);
			    }
			}
		    }

		    // Remove them now that the enumeration is done with
		    for (String propKey : toRemove) {
			p.remove(propKey);
		    }
		}

		// If this node hasn't been removed, add back in any values
		if (!isRemoved) {
		    for (String s : root.keySet()) {
			p.setProperty(path + s, root.get(s));
		    }
		}

		p.store(new FileOutputStream(file), "FilePreferences");
	    }
	    catch (IOException e) {
		throw new BackingStoreException(e);
	    }
	}
    }

}
