package comp3111.webscraper;

import java.net.URLEncoder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTime;
import java.util.Vector;


/**
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br>
 * In this particular sample code, it access to craigslist.org. You can directly search on an entry by typing the URL
 * <br>
 * https://newyork.craigslist.org/search/sss?sort=rel&amp;query=KEYWORD
 *  <br>
 * where KEYWORD is the keyword you want to search.
 * <br>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; section class="page-container" &rarr; form id="searchform" &rarr; div class="content" &rarr; ul class="rows" &rarr; any one of the multiple 
 * li class="result-row" &rarr; p class="result-info". You might see something like this:
 * <br>
 * <pre>
 * {@code
 *    <p class="result-info">
 *        <span class="icon icon-star" role="button" title="save this post in your favorites list">
 *           <span class="screen-reader-text">favorite this post</span>
 *       </span>
 *       <time class="result-date" datetime="2018-06-21 01:58" title="Thu 21 Jun 01:58:44 AM">Jun 21</time>
 *       <a href="https://newyork.craigslist.org/que/clt/d/green-star-polyp-gsp-on-rock/6596253604.html" data-id="6596253604" class="result-title hdrlnk">Green Star Polyp GSP on a rock frag</a>
 *       <span class="result-meta">
 *               <span class="result-price">$15</span>
 *               <span class="result-tags">
 *                   pic
 *                   <span class="maptag" data-pid="6596253604">map</span>
 *               </span>
 *               <span class="banish icon icon-trash" role="button">
 *                   <span class="screen-reader-text">hide this posting</span>
 *               </span>
 *           <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
 *           <a href="#" class="restore-link">
 *               <span class="restore-narrow-text">restore</span>
 *               <span class="restore-wide-text">restore this posting</span>
 *           </a>
 *       </span>
 *   </p>
 *}
 *</pre>
 * <br>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()). It also extracts  
 * 
 *
 */
public class WebScraper {

	private static final String DEFAULT_URL = "https://newyork.craigslist.org/";
	private static final String DEFAULT_URL2 = "https://www.preloved.co.uk/";
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public WebScraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	/**
	 * Sort all items in the parameter by price in Descending order
	 * @param list - a List of items
	 */
	private void sortItemsList (List<Item> list) {
		list.sort(Comparator.comparingDouble(Item::getPrice).reversed());
	}
	
	/**
	 * To scrape web content from the craigslist
	 * 
	 * @param keyword - the keyword you want to search
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	public List<Item> scrape(String keyword) {

		try {
			String searchUrl = DEFAULT_URL + "search/sss?sort=pricedsc&query=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);

			
			List<?> items = (List<?>) page.getByXPath("//li[@class='result-row']");
			
			List<?> totalcount = (List<?>) page.getByXPath("//span[@class='buttons']");
			HtmlElement countitem = (HtmlElement) totalcount.get(0);
			HtmlElement total = ((HtmlElement) countitem.getFirstByXPath(".//span/span[@class='totalcount']"));
			HtmlAnchor totalAnchor = ((HtmlAnchor) countitem.getFirstByXPath(".//a[@class='button next']"));
			
			Vector<Item> result = new Vector<Item>();
			int j = 1;
			while (result.size() < Integer.parseInt(total.asText())) {
				String loading = "Scraping page " + j + "...";
				System.out.println(loading);
				j++;
				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = (HtmlElement) items.get(i);
					HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//p[@class='result-info']/a"));
					HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//a/span[@class='result-price']"));
					HtmlTime date = ((HtmlTime) htmlItem.getFirstByXPath(".//p[@class='result-info']/time"));
					
					// It is possible that an item doesn't have any price, we set the price to 0.0
					// in this case
					String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
	
					Item item = new Item();
					item.setTitle(itemAnchor.asText());
					item.setUrl(itemAnchor.getHrefAttribute());
					item.setPrice(new Double(itemPrice.replace("$", "")));
					item.setDate(date.getAttribute("datetime"));
					item.setSource(DEFAULT_URL);
					
					result.add(item);
				}
				searchUrl = DEFAULT_URL + totalAnchor.getHrefAttribute();
				page = client.getPage(searchUrl);
				items.clear();
				
				if (result.size() >= Integer.parseInt(total.asText()))
					break;
				items = (List<?>) page.getByXPath("//li[@class='result-row']");
				totalcount = (List<?>) page.getByXPath("//span[@class='buttons']");
				countitem = (HtmlElement) totalcount.get(0);
				total = ((HtmlElement) countitem.getFirstByXPath(".//span/span[@class='totalcount']"));
				totalAnchor = ((HtmlAnchor) countitem.getFirstByXPath(".//a[@class='button next']"));
			}
			sortItemsList(result);
			scrapePreloved(keyword, result);
			client.close();
			sortItemsList(result);
			return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 *  To scrape web content from the Preloved
	 * @param keyword - the keyword you want to search
	 * @return A list of Item that has found. A zero size list is return if nothing is found. Null if any exception (e.g. no connectivity)
	 */
	private void scrapePreloved(String keyword, Vector<Item> CraigslistResult) {
		try {
			System.out.println("Scraping from " + DEFAULT_URL2);
			String searchUrl = DEFAULT_URL2 + "search?orderBy=priceDesc&keyword=" + URLEncoder.encode(keyword, "UTF-8");
			HtmlPage page = client.getPage(searchUrl);

			
			List<?> items = (List<?>) page.getByXPath("//li[@class='search-result']");

			for (int i = 0; i < items.size(); i++) {
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				HtmlElement itemName = ((HtmlElement) htmlItem.getFirstByXPath(".//div/header/h2/a/span"));
				HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//div/header/h2/a"));
				HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//div/header/span/span[@class='search-result__meta bold t-color--2-2 u-capitalize is-price']/span"));

				HtmlPage itemPage = client.getPage(itemAnchor.getHrefAttribute());
				String date = ((HtmlElement) itemPage.getFirstByXPath("//li[@class='classified__additional__meta__item classified__timeago']")).asText();

				// It is possible that an item doesn't have any price, we set the price to 0.0
				// in this case
				String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText();
				Item item = new Item();
				item.setTitle(itemName.asText());
				item.setUrl(itemAnchor.getHrefAttribute());
				item.setPrice(new Double(itemPrice.replaceAll("\\D", "")));
				item.setSource(DEFAULT_URL2);
				
				date = date.replaceAll("This advert was updated | ago", "");

				if (date.matches("(.*)hour(.?)"))
					item.setDate(Long.parseLong(date.replaceAll("\\D", "")), ChronoUnit.HOURS);
				else if (date.matches("(.*)day(.?)"))
					item.setDate(Long.parseLong(date.replaceAll("\\D", "")), ChronoUnit.DAYS);
				else if (date.matches("(.*)month(.*)"))
					item.setDate(Long.parseLong(date.replaceAll("\\D", "")), ChronoUnit.MONTHS);
				else
					item.setDate(LocalDateTime.now());
				CraigslistResult.add(item);
			}
			client.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
