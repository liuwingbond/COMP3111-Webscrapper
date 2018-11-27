package comp3111.webscraper;


import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;


public class ItemTest {

	@Test
	public void testSetTitle() {
		Item i = new Item();
		i.setTitle("ABCDE");
		assertEquals(i.getTitle(), "ABCDE");
	}
	
	@Test
	public void scrapeOther() {
		WebScraper w = new WebScraper();
		Vector<Item> result = new Vector<Item>();
		w.scrapeOther("aaaaa", result);
	}
}
