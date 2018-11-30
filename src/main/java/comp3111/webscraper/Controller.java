/**
 * 
 */
package comp3111.webscraper;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import java.util.List;
import javafx.event.*;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.Hyperlink;
import javafx.util.Callback;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * 
 * Class that manage GUI interaction.
 * 
 */
public class Controller {
	
	@FXML
	private MenuItem LastSearch;
	
	@FXML
	private Button RefineButton;
	

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
   	private TextField textFieldRefine;
    
    @FXML
    private TextArea textAreaConsole;
    
    private WebScraper scraper;
    
    @FXML
    private TableView<Item> tableView;
    
    @FXML
    private TableColumn<Item, String> title;

    @FXML
    private TableColumn<Item, String> price;

    @FXML
    private TableColumn<Item, String> url;

    @FXML
    private TableColumn<Item,LocalDateTime> date;  
    
    public String lastrecord = "";
    public String nowrecord ="";
    
    
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
	 * Called when Go button is clicked
	 */
    @FXML
    private void actionSearch() {
    	LastSearch.setDisable(false);
    	lastrecord = nowrecord;
    	nowrecord = textFieldKeyword.getText();
    	tableView.getItems().clear();
    	System.out.println("actionSearch: " + textFieldKeyword.getText());
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
	    	
	    	RefineButton.setDisable(false);	
	    	textAreaConsole.setText(output);
	        UpdateTable(result);

        } else
        	textAreaConsole.setText("Search result empty");
    	UpdateSummary(result, NumOfItems, TotalPrice, MinPriceItemIndex, LatestItemIndex);
    	System.out.println("Finish loading");
    }
    
    /**
     * To open the specific URI in browser
     * @param uri - URI to be opened in browser
     */
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
    
    /**
     * Update the summary tab with scrapped data
     * @param result
     * @param NumOfItems
     * @param TotalPrice
     * @param MinPriceItemIndex
     * @param LatestItemIndex
     */
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
    
    @FXML
    private void UpdateTable(List<Item> result) {
		title.setCellValueFactory(new PropertyValueFactory<>("title"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		url.setCellValueFactory(new PropertyValueFactory<>("url"));
		date.setCellValueFactory(new PropertyValueFactory<Item,LocalDateTime>("date"));
		
		title.setEditable(false);
		title.setSortable(true); 
		price.setEditable(false);
		price.setSortable(true); 
		url.setEditable(false);
		url.setSortable(true); 
		date.setEditable(false);
		date.setSortable(true); 
		Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory0
        = (final TableColumn<Item, String> entry) -> {
            final TableCell<Item, String> cell = new TableCell<Item, String>()
    {

        Hyperlink hyperlink = new Hyperlink();

        @Override
        public void updateItem(String item, boolean empty)
        {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            }
            else {
            	Item tempParam = tableView.getItems().get(getIndex());
                hyperlink.setText(item);
                hyperlink.setOnAction(new EventHandler<ActionEvent>() {
    	    	    @Override
    	    	    public void handle(ActionEvent e) {
    	    	        System.out.println("Opening URL");
    	    	        openURI(item);
    	    	    }
    	    	});
                setGraphic(hyperlink);
                setText(null);
            }
        }
    };
            return cell;
        };
        url.setCellFactory(cellFactory0);
	
    	ObservableList<Item> data = FXCollections.observableArrayList();
		
    	if (result.size() > 0) {
    		
	    	for (Item item : result) {
	    		data.add(item);
	    	}
    	}
		tableView.setItems(data);
    }
    
    
    /**
     * Called when the new button is pressed. Very dummy action - print something in the command prompt.
     */
    @FXML
    private void actionLast() {
    	LastSearch.setDisable(true);
    	tableView.getItems().clear();
    	System.out.println("actionSearch: " + lastrecord);
    	System.out.println("Loading output...");
    	List<Item> result = scraper.scrape(lastrecord);
    	
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
	        UpdateTable(result);

        } else
        	textAreaConsole.setText("Search result empty");
    	UpdateSummary(result, NumOfItems, TotalPrice, MinPriceItemIndex, LatestItemIndex);
    	System.out.println("Finish loading");
    	
    }
    @FXML
    private void actionTeam() {
    	String msg = "<html>Shek Wing Chun,wcshekaa,wcshek<br>LAI Ho Man,hmlaiad,kelvinbbg<br>LIU Wing Bond,wbliu,liuwingbond";
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(msg);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(null, "About Your Team");
        dialog.setVisible(true);
     	
    	
    }
    @FXML
    private void actionRefine() {
    	
    	tableView.getItems().clear();
    	RefineButton.setDisable(true);
    	System.out.println("actionSearch: " + textFieldRefine.getText());
    	System.out.println("Loading output...");
    	List<Item> result = scraper.scrape(textFieldRefine.getText());
    	
    	
    	
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
	        UpdateTable(result);

        } else
        	textAreaConsole.setText("Search result empty");
    	UpdateSummary(result, NumOfItems, TotalPrice, MinPriceItemIndex, LatestItemIndex);
    	System.out.println("Finish loading");
    	
    }
    @FXML
    private void actionQuit() {
    	System.exit(0);
    }
    @FXML
    private void actionClose() {
    	tableView.getItems().clear();
    	textAreaConsole.setText("");
    	RefineButton.setDisable(true);
    	textFieldRefine.setText("");
    	textFieldKeyword.setText("");
    	labelCount.setText("<total>");
    	labelPrice.setText("<AvgPrice>");
    	labelMin.setText("<Lowest>");
    	labelLatest.setText("<Latest>");
    	
    }
}


