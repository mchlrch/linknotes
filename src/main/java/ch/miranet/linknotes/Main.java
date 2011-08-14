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

import ch.miranet.commons.TK;
import ch.miranet.commons.collection.TransientOneToManyMap;

public class Main {

	public static void main(String[] args) throws IOException {
		final Config config = parseArguments(args);
		if (config != null) {
			System.out.println("notes: " + config.notesDir.getCanonicalPath());
			System.out.println("index: " + config.indexDir.getCanonicalPath());
		} else {
			System.exit(0);
		}

		final FilesystemCrawler crawler = new FilesystemCrawler();
		final TransientOneToManyMap<String, File> filesByTag = crawler.createIndex(config.notesDir);

		final SymlinkCreator linker = new SymlinkCreator();
		linker.createSymlinks(config.indexDir, filesByTag);
	}

	private static Config parseArguments(String[] args) {
		if (args.length < 1) {
			System.out.println("usage: java -jar linknotes.jar [notesdir] indexdir");
			System.out.println("       if notesdir is not specified then the current working directory will be used.");
			return null;
		} else if (args.length == 1) {
			return new Config(new File("."), new File(args[0]));
		} else {
			return new Config(new File(args[0]), new File(args[1]));
		}
	}

	private static class Config {
		private final File notesDir;
		private final File indexDir;

		public Config(final File srcDir, final File targetDir) {
			this.notesDir = TK.Objects.assertNotNull(srcDir, "srcDir");
			this.indexDir = TK.Objects.assertNotNull(targetDir, "targetDir");
		}
	}

}
