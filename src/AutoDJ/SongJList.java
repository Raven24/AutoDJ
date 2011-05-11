/**
 * SongJList.java
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

import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

public class SongJList extends JList {

	public SongJList() {
		// TODO Auto-generated constructor stub
		super();
	}

	public SongJList(ListModel dataModel) {
		super(dataModel);
		// TODO Auto-generated constructor stub
	}

	public SongJList(Object[] listData) {
		super(listData);
		// TODO Auto-generated constructor stub
	}

	public SongJList(Vector<?> listData) {
		super(listData);
		// TODO Auto-generated constructor stub
	}
}
