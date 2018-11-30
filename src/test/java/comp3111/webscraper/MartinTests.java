package comp3111.webscraper;

import static org.junit.Assert.*;
import org.junit.Test;
import javafx.fxml.FXML;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class MartinTests {

	@Test
	public void testController() {
		/*try {
			Controller ctrl = new Controller();
			Method method = ctrl.getClass().getDeclaredMethod("actionSearch", null);
			method.setAccessible(true);
			Object[] obj2 = new Object[1];
			method.invoke(ctrl.getClass().newInstance(), obj2);
		} catch(Exception e) {
			System.out.println(e);
		}*/
	}
	
	@Test
	public void testItem() {
		try {
			Item i = new Item();
			i.setTitle("test");
			assertEquals(i.getTitle(), "test");
			
			i.setPrice(0);
			i.getPrice();
			
			i.setUrl("test.com");
			assertEquals(i.getUrl(), "test.com");
			
			i.setDate("2018-11-30 12:00");
			i.getDate();
			assertEquals(i.getDateString(), "2018-11-30 12:00");
			i.setDate(12, ChronoUnit.HOURS);
			i.setDate(LocalDateTime.now());
			
			i.setSource("test");
			assertEquals(i.getSource(), "test");
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Test
	public void testWebScraper() {
		try {
			WebScraper ws = new WebScraper();
			ws.scrape("test");
		} catch(Exception e) {
			System.out.println(e);
		}
	}
}
