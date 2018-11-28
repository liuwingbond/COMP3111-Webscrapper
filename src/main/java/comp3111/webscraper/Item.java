package comp3111.webscraper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;



public class Item {
	private String title ; 
	private double price ;
	private String url ;
	private LocalDateTime date;
	private String source;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public String getDateString() {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}
	public void setDate(String date) {
		this.date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}
	public void setDate(long amountToSubtract, ChronoUnit unit) {
		this.date = LocalDateTime.now().minus(amountToSubtract, unit);
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String src) {
		this.source = src;
	}
}
