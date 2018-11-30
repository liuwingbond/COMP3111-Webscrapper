package comp3111.webscraper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;



/**
 * Class of a single item scrapped from a web portal
 */
public class Item {
	private String title ; 
	private double price ;
	private String url ;
	private LocalDateTime date;
	private String source;
	
	/**
	 * Get title of the item
	 * @return title - Title of the item
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set title of the item
	 * @param title - Title of the item
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Get price of the item
	 * @return price - Price of the item
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Set title of the item
	 * @param price - Price of the item
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/**
	 * Get URL of the item
	 * @return url - URL of the item
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Set URL of the item
	 * @param url - URL of the item
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Get Date of the item
	 * @return date - Date of the item
	 */
	public LocalDateTime getDate() {
		return date;
	}
	
	/**
	 * Get date of the item
	 * @return date - Date of the item in String
	 */
	public String getDateString() {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}
	
	/**
	 * Set date of the item
	 * @param date - date of the item
	 */
	public void setDate(String date) {
		this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}
	
	/**
	 * Set date of the item
	 * @param amountToSubtract - Time passed since the item is being put on stack
	 * @param unit - Unit of the time passed
	 */
	public void setDate(long amountToSubtract, ChronoUnit unit) {
		this.date = LocalDateTime.now().minus(amountToSubtract, unit);
	}
	
	/**
	 * Set date of the item
	 * @param date - Date of the item
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	/**
	 * Get Source of the item
	 * @return source - Source of the item being scrapped
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * Set title of the item
	 * @param src - Source of the item being scrapped
	 */
	public void setSource(String src) {
		this.source = src;
	}
}
