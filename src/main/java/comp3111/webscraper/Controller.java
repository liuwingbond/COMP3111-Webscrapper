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
    	List<Item> result = scraper.scrape(textFieldKeyword.getText());
    	String output = "";
    	double TotalPrice = 0;
    	double LowestPrice = result.get(0).getPrice();
    	for (Item item : result) {
    		output += item.getTitle() + "\t" + item.getPrice() + "\t" + item.getUrl() + "\n";
    		
    		//Calculate total price
    		TotalPrice += item.getPrice();
    		
    		//Find lowest price
    		if (item.getPrice() < LowestPrice)
    			LowestPrice = item.getPrice();		
    	}
    			
    	textAreaConsole.setText(output);
    	
    	UpdateSummary(result, TotalPrice, LowestPrice);	
    }
    
    @FXML
    private void UpdateSummary(List<Item> result, double TotalPrice, double LowestPrice) {
    	double AvgPrice = 0;
    	int NumOfItems = result.size();
    	
    	//Calculate average price
    	AvgPrice = TotalPrice / NumOfItems;
    	
    	labelCount.setText(Integer.toString(NumOfItems));
    	labelPrice.setText(Double.toString(AvgPrice));
    	labelMin.setText(Double.toString(LowestPrice));
    }
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionNew() {
    	System.out.println("actionNew");
    }
}

