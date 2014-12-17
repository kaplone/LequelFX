package LequelFX.LequelFX;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import utils.CellFields;
import utils.JsonUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;



public class BrowserGuiController  implements Initializable {
	
	@FXML
	private TableColumn<CellFields, ImageView> type; 
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
	
	ObservableMap<String, Date> contenu = FXCollections.observableHashMap();
	
	DBObject n;
	Date n_old ;
	
	String cle;
	
	ObjectId oid_pere ;
	
	Image image;
	
	DropShadow dropShadow = new DropShadow();
	
	String currentLevel;
	
	
	
	private GuiController gc = Main.getLoader_tableau().getController();
	
	JsonNode chemin_pere;
	JsonNode id_pere_node;
	String id_pere;
	ObjectId id_pere_obj;
	
	
    MongoClient mongoclient;
	
	DB db;
	DBCollection coll;
	
	public void populatePath(CellFields currCellFiefd ){
		
		if(currCellFiefd != null){
		
			pathBox.getChildren().clear();
			currentCellFiefd = currCellFiefd;
			
			final ObservableList<Button> boutons = FXCollections.observableArrayList();
			
			String [] fpn = currentCellFiefd.getFieldPathName().split("  ");
			
			for (int i = 0; i < fpn.length -1; i++){
				String s = fpn[i];
				final Button b = new Button(s.substring(2, s.length() - 2));
				boutons.add(b);
	            
	            b.setOnAction(new EventHandler<ActionEvent>() {
	            	 
	                public void handle(ActionEvent event) {
	                	System.out.println(b.getText());
	                	
	                	
	                }
	            });
			}
			pathBox.getChildren().addAll(boutons);
			populateMediasCells();
		}
	}
	
    public void populatePathChild(CellFields currCellFiefd ){
		
		if(currCellFiefd != null && currCellFiefd.fieldTypeProperty().get().equals("Dossier")){
		
			pathBox.getChildren().clear();
			currentCellFiefd = currCellFiefd;
			
			final ObservableList<Button> boutons = FXCollections.observableArrayList();
			
			String [] fpn = currentCellFiefd.getFieldPathName().split("  ");
			
			for (int i = 0; i < fpn.length; i++){
				String s = fpn[i];
				final Button b = new Button(s.substring(2, s.length() - 2));
				boutons.add(b);
	            
	            b.setOnAction(new EventHandler<ActionEvent>() {
	            	 
	                public void handle(ActionEvent event) {
	                	System.out.println(b.getText());
	                	
	                }
	            });
	            
	        
			}
			pathBox.getChildren().addAll(boutons);
			populateMediasCells(currentCellFiefd.getFieldNode().get("_id"), currentCellFiefd.getFieldType());
			currentCellFiefd = resArray.get(0);
		}
	}
    
    public void populatePathUp(){
		
		pathBox.getChildren().remove(pathBox.getChildren().size() -1);

		JsonNode current_node = resArray.get(0).getFieldNode();
		
		resArray.clear();
    	
    	id_pere  = current_node.get("id_pere").get("$oid").textValue();
    	oid_pere =  new ObjectId(id_pere);
    	
    	res = coll.find(new BasicDBObject("_id", oid_pere));
    			
    	while (res.hasNext()){
    	   	
    	   DBObject d = res.next();
    	   System.out.println(d.toString());
    	   JsonUtils.loadList(d.toString(), resArray);
    	}
    	
    	System.out.println(currentCellFiefd.getFieldNameFull());
    	System.out.println("up !");
    	
		populateMediasCells( resArray.get(0).getFieldNode().get("id_pere"), resArray.get(0).getFieldType());
		currentCellFiefd = resArray.get(0);
		
		System.out.println(currentCellFiefd.getFieldNameFull());

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
		coll = db.getCollection("Lequel_V03");
		

		
	}
	
	
	public void populateMediasCells(){	
		
		 //Set up the table data
		
		
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		
        //currentCellFiefd = GuiController.getCurrentCellFields();
        
        System.out.println(coll);
        
        id_pere_node = currentCellFiefd.getFieldNode().get("id_pere");
        if(currentCellFiefd.getFieldNode().get("fichier").asBoolean()){
        	id_pere  = id_pere_node.textValue();
        	oid_pere =  new ObjectId(id_pere);
        }
        else {
        	id_pere  = id_pere_node.get("$oid").textValue();
        	oid_pere =  new ObjectId(id_pere);
        }
        
		
		resArray.clear();
		contenu.clear();
        
        
        
		res = coll.find(new BasicDBObject("id_pere", oid_pere));

		
		while (res.hasNext()){
			
			n = res.next();
			
			cle = (String) n.get("nom");
			
			if (! contenu.containsKey(cle)){
				contenu.put(cle, (Date) n.get("scan"));
				JsonUtils.loadList(n.toString(), resArray);
			}
			else {
				if ((contenu.get(cle)).compareTo((Date) n.get("scan")) < 0){
			    contenu.put(cle, (Date) n.get("scan"));
			    //resArray.remove
				}
			}
			
			n_old = (Date) n.get("scan");
			
			
		}
		
        res = coll.find(new BasicDBObject("id_pere", id_pere));

		
		while (res.hasNext()){
			
			n = res.next();
			
			cle = n.get("nom") + "." + n.get("extension");
			
			if (! contenu.containsKey(cle)){
				contenu.put(cle, (Date) n.get("scan"));
				JsonUtils.loadList(n.toString(), resArray);
			}
			else {
				if ((contenu.get(cle)).compareTo((Date) n.get("scan")) < 0){
			    contenu.put(cle, (Date) n.get("scan"));
			    //resArray.remove
				}
			}
			
			n_old = (Date) n.get("scan");
			
			
		}

	    
	    type.setCellValueFactory(new Callback<CellDataFeatures<CellFields, ImageView>, ObservableValue<ImageView>>() {
	    	
	    	
	        public ObservableValue<ImageView> call(CellDataFeatures<CellFields, ImageView> p) {
	        	
	        	
	        	ObjectProperty<ImageView> imageview = new SimpleObjectProperty<ImageView>();
	        	image = p.getValue().getFieldImage();
	        	
	        	imageview.set(new ImageView(image));
	        	imageview.get().setEffect(dropShadow);
                imageview.get().setFitHeight(24);
                imageview.get().setFitWidth(24);
	            return imageview;
	        }
	     });
	    
	    nom.setCellValueFactory(
	        new PropertyValueFactory<CellFields,String>("fieldNameFull")
	    );
	    taille.setCellValueFactory(
		    new PropertyValueFactory<CellFields,Integer>("fieldSize")
		);

 	    table.setItems(resArray);

	}
	
	public void populateMediasCells(JsonNode c, String type_){	
		
		 //Set up the table data		
		
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
         
		 if (type_.equals("Dossier")){
		 
	         id_pere  = c.get("$oid").textValue();
	         oid_pere =  new ObjectId(id_pere);
		 }
		 else {
			 id_pere  = id_pere_node.textValue();
	         oid_pere =  new ObjectId(id_pere);
		 }
	
		resArray.clear();
		contenu.clear();
       
       
       
		res = coll.find(new BasicDBObject("id_pere", oid_pere));

		
		while (res.hasNext()){
			
			n = res.next();
			
			cle = (String) n.get("nom");
			
			if (! contenu.containsKey(cle)){
				contenu.put(cle, (Date) n.get("scan"));
				JsonUtils.loadList(n.toString(), resArray);
			}
			else {
				if ((contenu.get(cle)).compareTo((Date) n.get("scan")) < 0){
			    contenu.put(cle, (Date) n.get("scan"));
			    //resArray.remove
				}
			}
			
			n_old = (Date) n.get("scan");
			
			
		}
		
       res = coll.find(new BasicDBObject("id_pere", id_pere));

		
		while (res.hasNext()){
			
			n = res.next();
			
			cle = n.get("nom") + "." + n.get("extension");
			
			if (! contenu.containsKey(cle)){
				contenu.put(cle, (Date) n.get("scan"));
				JsonUtils.loadList(n.toString(), resArray);
			}
			else {
				if ((contenu.get(cle)).compareTo((Date) n.get("scan")) < 0){
			    contenu.put(cle, (Date) n.get("scan"));
			    //resArray.remove
				}
			}
			
			n_old = (Date) n.get("scan");
			
			
		}

	    
	    type.setCellValueFactory(new Callback<CellDataFeatures<CellFields, ImageView>, ObservableValue<ImageView>>() {
	    	
	    	
	        public ObservableValue<ImageView> call(CellDataFeatures<CellFields, ImageView> p) {
	        	
	        	
	        	ObjectProperty<ImageView> imageview = new SimpleObjectProperty<ImageView>();
	        	image = p.getValue().getFieldImage();
	        	
	        	imageview.set(new ImageView(image));
	        	imageview.get().setEffect(dropShadow);
               imageview.get().setFitHeight(24);
               imageview.get().setFitWidth(24);
	            return imageview;
	        }
	     });
	    
	    nom.setCellValueFactory(
	        new PropertyValueFactory<CellFields,String>("fieldNameFull")
	    );
	    taille.setCellValueFactory(
		    new PropertyValueFactory<CellFields,Integer>("fieldSize")
		);

	    table.setItems(resArray);

	}
	
	@FXML
	protected void onTableClicked(){
		currentCellFiefd = table.getSelectionModel().getSelectedItem();
		System.out.println(currentCellFiefd.getFieldNameFull());
		populatePathChild(currentCellFiefd);
		
		
	}
	
	@FXML
	protected void onUpClicked(){
		populatePathUp();
	}

	public void initialize(URL location, ResourceBundle resources) {
		connecter();
	
	}

}
