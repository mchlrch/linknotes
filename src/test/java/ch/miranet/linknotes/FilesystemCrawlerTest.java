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
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FilesystemCrawlerTest {
	
	private final FilesystemCrawler crawler = new FilesystemCrawler();
	
	@Test
	public void shouldExtractNoTags() {
		final String[] none = {};
		
		extractAndAssertTags(mkFile("", ""), none);
		extractAndAssertTags(mkFile("", "_"), none);
		extractAndAssertTags(mkFile("", "_foo_"), none);		
		
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX.substring(1), ""), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX.substring(1), "foo"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX.substring(1), ".jpg"), none);
		
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, ""), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, "_"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, "__"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, "foo"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, ".jpg"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, "foo.jpg"), none);
		extractAndAssertTags(mkFile(FilesystemCrawler.TAG_PREFIX, "foo_.jpg"), none);
		
	}
	
	@Test
	public void shouldExtractOneTag() {
		extractAndAssertTags(mkFile("_bar"), "bar");
		extractAndAssertTags(mkFile("_bar_"), "bar");
		extractAndAssertTags(mkFile("__bar__"), "bar");
		
		extractAndAssertTags(mkFile("_bar.jpg"), "bar");
		extractAndAssertTags(mkFile("_bar_.jpg"), "bar");
		extractAndAssertTags(mkFile("__bar__.jpg"), "bar");
	}
	
	@Test
	public void shouldExtractTwoTags() {
		extractAndAssertTags(mkFile("_foo_bar"), "foo", "bar");
		extractAndAssertTags(mkFile("_foo_bar_"), "foo", "bar");
		extractAndAssertTags(mkFile("__foo__bar__"), "foo", "bar");
		
		extractAndAssertTags(mkFile("_foo_bar.jpg"), "foo", "bar");
		extractAndAssertTags(mkFile("_foo_bar_.jpg"), "foo", "bar");
		extractAndAssertTags(mkFile("__foo__bar__.jpg"), "foo", "bar");
	}
	
	private void extractAndAssertTags(File f, String... expectedTags) {
		final List<String> actualTags = crawler.extractTags(f);

		Assert.assertTrue(actualTags.size() == expectedTags.length);
		Assert.assertTrue(actualTags.containsAll(Arrays.asList(expectedTags)));
	}
	
	private File mkFile(String postfix) {
		return mkFile(FilesystemCrawler.TAG_PREFIX, postfix);
	}
	
	private File mkFile(String prefix, String postfix) {
		prefix = prefix != null ? prefix : "";
		postfix = postfix != null ? postfix : "";		
		return new File(prefix + postfix);
	}

}
