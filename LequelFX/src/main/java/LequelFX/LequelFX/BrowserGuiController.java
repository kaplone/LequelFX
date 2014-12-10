package LequelFX.LequelFX;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import utils.CellFields;
import utils.JsonUtils;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;



public class BrowserGuiController  implements Initializable {
	
	@FXML
	private TableColumn<CellFields, Image> type; 
	@FXML
	private TableColumn<CellFields, String> nom; 
	@FXML
	private TableColumn<CellFields, String> date; 
	@FXML
	private TableColumn<CellFields, Integer> taille; 
	@FXML
	private TableView<CellFields> table; 
	@FXML
	private HBox pathBox;
	
	private CellFields currentCellFiefd;
	
	private ObservableList<CellFields> resArray = FXCollections.observableArrayList(); 
	private DBCursor res;
	
	
	private GuiController gc = Main.getLoader_tableau().getController();
	
	JsonNode chemin;
	ArrayList<String> chemins = new ArrayList<String>();
	
	
    MongoClient mongoclient;
	
	DB db;
	DBCollection coll;
	
	public void populatePath(CellFields currCellFiefd ){
		
		pathBox.getChildren().clear();
		currentCellFiefd = currCellFiefd;
		
		ObservableList<Button> boutons = FXCollections.observableArrayList();
		for (String s : currentCellFiefd.getFieldPathName().split("  ")){
            boutons.add(new Button(s.substring(2, s.length() - 2)));
		}
		pathBox.getChildren().addAll(boutons);
		populateMediasCells();
	}
	
	public static void show_stage(){
		
	}
	
	public void connecter(){

		try {
		mongoclient = new MongoClient( "192.168.0.201" , 27017 );
		}
		catch (UnknownHostException uhe) {
		}
		
	
		db = mongoclient.getDB( "Lequel" );
		coll = db.getCollection("Lequel_V02");
		

		
	}
	
	
	public void populateMediasCells(){	
		
		 //Set up the table data
		
		
        currentCellFiefd = GuiController.getCurrentCellFields();
        
        System.out.println(coll);
        
        chemin = currentCellFiefd.getFieldNode().get("parents");
        Iterator<JsonNode> elements = chemin.elements();
        while(elements.hasNext()){
            chemins.add(elements.next().asText());
        }
        
        if (currentCellFiefd.getFieldType() == "Fichier"){
        	chemins.remove(chemins.size()-1);
        }
        
        
        
        System.out.println(chemins);
        
		res = coll.find(new BasicDBObject("parents" , new BasicDBObject("$all", chemins )));
		
		while (res.hasNext()){
			
			JsonUtils.loadList(res.next().toString(), resArray);
		}

	    
	    type.setCellValueFactory(new Callback<CellDataFeatures<CellFields, Image>, ObservableValue<Image>>() {
	    	
	    	
	        public ObservableValue<Image> call(CellDataFeatures<CellFields, Image> p) {
	        	
	        	final ImageView imageview = new ImageView();
                imageview.setFitHeight(150);
                imageview.setFitWidth(50);
                imageview.setImage(p.getValue().getFieldImage());
	            // p.getValue() returns the Person instance for a particular TableView row
	            return p.getValue().fieldImageProperty();
	        }
	     });
	    
	    nom.setCellValueFactory(
	        new PropertyValueFactory<CellFields,String>("fieldNameFull")
	    );
	    taille.setCellValueFactory(
		    new PropertyValueFactory<CellFields,Integer>("fieldSize")
		);

		resArray.clear();
        //resArray.add(currentCellFiefd);

 	    table.setItems(resArray);

	}

	public void initialize(URL location, ResourceBundle resources) {
		connecter();
	
	}

}
