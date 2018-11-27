package comp3111.webscraper;

import java.time.*;
import java.time.format.DateTimeFormatter;



public class Item {
	private String title ; 
	private double price ;
	private String url ;
	private LocalDateTime date;
	
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
	public void setDate(String date) {
		this.date = this.date.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}
	public LocalDateTime getDate() {
		return date;
	}
	public String getDateString() {
		return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm"));
	}

}
