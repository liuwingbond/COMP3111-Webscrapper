/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import java.util.List;
import javafx.event.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * 
 * @author kevinw
 *
 *
 * Controller class that manage GUI interaction. Please see document about JavaFX for details.
 * 
 */
public class Controller {

    @FXML 
    private Label labelCount; 

    @FXML 
    private Label labelPrice; 

    @FXML 
    private Hyperlink labelMin; 

    @FXML 
    private Hyperlink labelLatest; 

    @FXML
    private TextField textFieldKeyword;
    
    @FXML
    private TextArea textAreaConsole;
    
    private WebScraper scraper;
    
    /**
     * Default controller
     */
    public Controller() {
    	scraper = new WebScraper();
    }

    /**
     * Default initializer. It is empty.
     */
    @FXML
    private void initialize() {
    	
    }
    
    /**
     * Called when the search button is pressed.
     */
    @FXML
    private void actionSearch() {
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
    	textAreaConsole.setText("Loading output...");
    	System.out.println("Loading output...");
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	
    	String output = "";
    	int NumOfItems = result.size(); //Do not remove! Need for subtracting "0" price items
    	double TotalPrice = 0;
    	int MinPriceItemIndex = 0;
    	int LatestItemIndex = 0;
    	
        if (NumOfItems > 0) {        	
	    	for (Item item : result) {
	    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
	    		
	    		if (item.getPrice() > 0) {
		    		//Calculate total price
		    		TotalPrice += item.getPrice();
		    		
		    		//Find lowest price
		    		if (item.getPrice() < result.get(MinPriceItemIndex).getPrice())
		    			MinPriceItemIndex = result.indexOf(item);
		    		
		    		//Find latest item
		    		if(item.getDate().compareTo(result.get(LatestItemIndex).getDate()) > 0)
		    			LatestItemIndex = result.indexOf(item);
	    		} else 
	    			NumOfItems--;
	    	}
	    			
	    	textAreaConsole.setText(output);

        } else
        	textAreaConsole.setText("Search result empty");
    	UpdateSummary(result, NumOfItems, TotalPrice, MinPriceItemIndex, LatestItemIndex);
    	System.out.println("Finish loading");
    }
    
    //Use parameter uri to open the browser
    private static void openURI(String uri) {
    	if(Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(uri));
            } catch (IOException | URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec("xdg-open " + uri);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void UpdateSummary(List<Item> result, int NumOfItems, double TotalPrice, int MinPriceItemIndex, int LatestItemIndex) {
    	if (NumOfItems > 0) {
	    	double AvgPrice = 0;
	    	
	    	//Calculate average price
	    	AvgPrice = TotalPrice / NumOfItems;
	    	
	    	labelCount.setText(Integer.toString(NumOfItems));
	    	
	    	labelPrice.setText(Double.toString(AvgPrice));
	    	
	    	Item MinPriceItem = result.get(MinPriceItemIndex);
	    	labelMin.setText(Double.toString(MinPriceItem.getPrice()));
	    	labelMin.setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override
	    	    public void handle(ActionEvent e) {
	    	        System.out.println("Opening URL of the lowest price item " + MinPriceItem.getUrl());
	    	        openURI(MinPriceItem.getUrl());
	    	    }
	    	});

	    	Item LatestItem = result.get(LatestItemIndex);
	    	labelLatest.setText(LatestItem.getDateString());
	    	labelLatest.setOnAction(new EventHandler<ActionEvent>() {
	    	    @Override
	    	    public void handle(ActionEvent e) {
	    	        System.out.println("Opening URL of the latest item " + LatestItem.getUrl());
	    	        openURI(LatestItem.getUrl());
	    	    }
	    	});
    	} else {
    		labelCount.setText("-");
	    	labelPrice.setText("-");
	    	labelMin.setText("-");
	    	labelLatest.setText("-");
    	}
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

