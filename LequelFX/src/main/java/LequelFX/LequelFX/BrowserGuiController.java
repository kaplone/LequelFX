package LequelFX.LequelFX;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
	@FXML
	private Button root;
	
	
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
	
	CellFields rep_vide;
	
	
	
	private GuiController gc = Main.getLoader_tableau().getController();
	
	//JsonNode chemin_pere;
	JsonNode id_pere_node;
	String id_pere;
	ObjectId id_pere_obj;
	
	
    MongoClient mongoclient;
	
	DB db;
	DBCollection coll;
	
	final ObservableList<Button> boutons = FXCollections.observableArrayList();
	
	@FXML
	public void goToRoot(){
		System.out.println("root");
		for (int i = 0; i < boutons.size(); i++){
      		populatePathUp();
      	}
	}
	
	public void populatePath(CellFields currCellFiefd ){
		
		if(currCellFiefd != null){
		
			pathBox.getChildren().clear();
			currentCellFiefd = currCellFiefd;
			boutons.clear();
	        
			System.out.println(currentCellFiefd.getFieldPathName());
			
			String [] fpn = currentCellFiefd.getFieldPathName().split("  ");
			
			for (int i = 0; i < fpn.length -1; i++){
				String s = fpn[i];
				final Button b = new Button(s.substring(1, s.length() - 1));
				boutons.add(b);
	            
	            b.setOnAction(new EventHandler<ActionEvent>() {
	            	 
	                public void handle(ActionEvent event) {

	                  	for (int i = boutons.indexOf(b) + 1; i < boutons.size(); i++){
	                  		populatePathUp();
	                  	}    	
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
			boutons.clear();
			
			String [] fpn = currentCellFiefd.getFieldPathName().split("  ");
			
			for (int i = 0; i < fpn.length; i++){
				String s = fpn[i];
				final Button b = new Button(s.substring(1, s.length() - 1));
				boutons.add(b);
	            
	            b.setOnAction(new EventHandler<ActionEvent>() {
	            	 
	                public void handle(ActionEvent event) {
	                	for (int i = boutons.indexOf(b) + 1; i < boutons.size(); i++){
	                  		populatePathUp();
	                  	}
	                	
	                }
	            });
	            
	        
			}
			pathBox.getChildren().addAll(boutons);
			populateMediasCells(currentCellFiefd.getFieldNode().get("_id"));
//			populateMediasCells(currentCellFiefd.getFieldNode().get("_id"), currentCellFiefd.getFieldType());
			
			if (resArray.size() == 0){
				
				System.out.println("vers rep vide");
				rep_vide = currentCellFiefd;
			}
			else {
				currentCellFiefd = resArray.get(0);
			}
			
			
		}
	}
    
    public void populatePathUp(){
		
    	pathBox.getChildren().remove(pathBox.getChildren().size() -1);
		
		if (resArray.size() == 0){
			
			System.out.println("depuis rep vide");
			System.out.println(rep_vide.getFieldNode().get("id_pere"));
			
			// verifier le fonctionnement
	    	
			populateMediasCells( rep_vide.getFieldNode().get("id_pere"));
			currentCellFiefd = resArray.get(0);

			
		}
		else {

			currentCellFiefd = resArray.get(0);
			JsonNode current_node = resArray.get(0).getFieldNode();
			
			resArray.clear();
			
			id_pere_node = current_node.get("id_pere");
	        
	        id_pere  = id_pere_node.textValue();
	        
	        if(id_pere == null){
	        	id_pere  = id_pere_node.get("$oid").textValue();
	        }
        	oid_pere =  new ObjectId(id_pere);
        	
	    	res = coll.find(new BasicDBObject("_id", oid_pere));
	    	
	    	DBObject d;
	    			
	    	while (res.hasNext()){
	    	   	
	    	   d = res.next();
	    	   JsonUtils.loadList(d.toString(), resArray);
	    	}
	    	
	    	currentCellFiefd = resArray.get(0);
	    	
			populateMediasCells( currentCellFiefd.getFieldNode().get("id_pere"));
		}
		populatePath(currentCellFiefd);

	}

	
	public static void show_stage(){
		
	}
	
	public void connecter(){

		mongoclient = new MongoClient( "192.168.0.201" , 27017 );
		
	
		db = mongoclient.getDB( "LequelFX" );
		coll = db.getCollection("Lequel_V04");
		

		
	}
	
	
	public void populateMediasCells(){	
		
		System.out.println("entrée populate()");
		
		 //Set up the table data
		
		
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		
        //currentCellFiefd = GuiController.getCurrentCellFields();
        
        id_pere_node = currentCellFiefd.getFieldNode().get("id_pere");

    	id_pere  = id_pere_node.textValue();
    	
    	// pas certain
//    	if (id_pere == null){
//			id_pere  = id_pere_node.get("$oid").textValue();
//		}
    	oid_pere =  new ObjectId(id_pere);
        
		
		resArray.clear();
		contenu.clear();
		
        res = coll.find(new BasicDBObject("id_pere", id_pere));

		
		while (res.hasNext()){
			
			n = res.next();
			
			if(currentCellFiefd.getFieldNode().get("fichier").asBoolean()){
			    cle = n.get("nom") + "." + n.get("extension");
			}
			else {
				cle = (String) n.get("nom");
			}
			
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
	
	public void populateMediasCells(JsonNode c){	
		
		System.out.println("entrée populate(JsonNode)");
		
		 //Set up the table data		
		
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
         
		 System.out.println("c : " + c);
		
		id_pere  = c.textValue();
		
		System.out.println("id_pere : " + id_pere);
		
		if (id_pere == null){
			id_pere  = c.get("$oid").textValue();
		}
		
	    oid_pere =  new ObjectId(id_pere);
  
	    System.out.println("oid_pere : " + oid_pere);
	
		resArray.clear();
		contenu.clear();

		res = coll.find(new BasicDBObject("id_pere", id_pere));
		
		if (res.size() == 0){
			res = coll.find(new BasicDBObject("id_pere", new ObjectId(id_pere)));
		}

		
		while (res.hasNext()){
			
			n = res.next();
			
			if (Boolean.getBoolean(n.get("fichier").toString())){
				cle = n.get("nom") + "." + n.get("extension");
			}
			else{
			   cle = (String) n.get("nom");
			}
			
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
		System.out.println("__" + currentCellFiefd.getFieldNameFull());
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
