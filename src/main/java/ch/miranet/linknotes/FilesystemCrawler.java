/*********************************************************************
  This file is part of linknotes,
  see <http://www.miranet.ch/projects/linknotes>

  Copyright (C) 2011 Michael Rauch

  linknotes is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  linknotes is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with linknotes.  If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************/

package ch.miranet.linknotes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.miranet.commons.collection.TransientOneToManyMap;

public class FilesystemCrawler {

	protected static final String TAG_PREFIX = "YYYY-MM-DD_nb_XXX_XXX";
	protected static final char TAG_SEPARATOR = '_';
	
	public TransientOneToManyMap<String, File> createIndex(File srcDir) {
		final TransientOneToManyMap<String, File> index = new TransientOneToManyMap<String, File>();
		crawlDirectory(srcDir, index);
		return index;
	}
	
	private void crawlDirectory(File dir, TransientOneToManyMap<String, File> index) {
		for (File f : dir.listFiles()) {
			if (f.isFile()) {
				final List<String> tags = extractTags(f);
				if ( ! tags.isEmpty()) {
					for (String tag : tags) {
						index.add(tag, f);
					}				
				} else {
					System.out.println("no tags: " + f.getPath());
				}
				
			} else {
				crawlDirectory(f, index);
			}
		}
		
	}
	
	protected List<String> extractTags(File f) {
		final String fName = f.getName();
		
		final int tagBeginIndex = TAG_PREFIX.length();
		if (fName.length() > tagBeginIndex && fName.charAt(tagBeginIndex)==TAG_SEPARATOR) {
			final int tagEndIndex = fName.indexOf('.', tagBeginIndex);
			final String tagSection = tagEndIndex > 0 ? fName.substring(tagBeginIndex, tagEndIndex) : fName.substring(tagBeginIndex);
			
			final List<String> result = new ArrayList<String>();
			for (String tag : tagSection.split(""+TAG_SEPARATOR)) {
				if (tag != null && ! tag.isEmpty()) {
					result.add(tag);
				}
			}
			return result;
			
		} else {
			return Collections.emptyList();
		}
	}

}
