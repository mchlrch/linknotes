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
import java.io.IOException;

import ch.miranet.commons.collection.TransientOneToManyMap;

public class SymlinkCreator {

	public void createSymlinks(File baseDir, TransientOneToManyMap<String, File> filesByTag) {
		rmdir(baseDir);
		for (String tag : filesByTag.keySet()) {
			final File tagDir = mkdir(baseDir, tag);
			for (File linkTarget : filesByTag.get(tag)) {
				createSymlink(tagDir, linkTarget);
			}
		}
	}
	
	private void rmdir(File dir) {
		if (dir == null || ! dir.exists()) return;
																		
		for (File f : dir.listFiles()) {
			final String fName = f.getName();
			if (".".equals(fName) || "..".equals(fName)) {
				continue;
				
			} else if (f.isFile()) {
				f.delete();
			} else {				
				rmdir(f);
			}
		}
		dir.delete();
	}

	private File mkdir(File parent, String name) {
		final File dir = new File(parent, name);
		return dir.mkdirs() ? dir : null;
	}

	private void createSymlink(File tagDir, File linkTarget) {
		final String linkTargetPath = canonicalPathOf(linkTarget);
		final String tagDirPath = canonicalPathOf(tagDir);
		
		final StringBuilder cmd = new StringBuilder();
		cmd.append("ln -s ");
		cmd.append(linkTargetPath).append(" ");
		cmd.append(tagDirPath);
		
		exec(cmd.toString());
	}
	
	private String canonicalPathOf(File f) {		
		try {
			return f.getCanonicalPath();
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
		
	}
	
	private void exec(String cmd) {
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException ioex) {
			throw new RuntimeException(ioex);
		}
	}

}
