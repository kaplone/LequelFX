package LequelFX.LequelFX;

import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ResourceBundle;

import utils.CellFields;
import utils.JsonUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GuiController implements Initializable{
	
	@FXML
	private Button chercher;
	
	@FXML
	private TextField pattern;
	
	@FXML
	private TableColumn<CellFields, String> disque;
	@FXML
	private TableColumn<CellFields, String> chemin;
	@FXML
	private TableColumn<CellFields, String> nom;
	@FXML
	private TableColumn<CellFields, Integer > taille;
	@FXML
	private TableColumn<CellFields, String>  date;
	@FXML
	private TableColumn<CellFields, String>  ext;
	@FXML
	private TableColumn<CellFields, String>  type;
	
	@FXML
	private TableView<CellFields> table;
	
	private DBCursor res;
	
	private ObservableList<CellFields> resArray = FXCollections.observableArrayList(); 
	
	private static CellFields currentCellFiefd;
	
	public static CellFields getCurrentCellFields(){
		return currentCellFiefd;
	}
	
    ObservableMap<String, Date> contenu = FXCollections.observableHashMap();
	
	DBObject n;
	Date n_old ;
	
	String cle;
	
	
	
	
	@FXML
	protected void onButtonChercher(){
		
		resArray.clear();
	
		
		//res = coll.find(new BasicDBObject("nom" , new BasicDBObject("$regex", pattern.getText() )));
		BasicDBObject search = new BasicDBObject("$search", String.format("\"%s\"", pattern.getText()));
		BasicDBObject textSearch = new BasicDBObject("$text", search);
		res = coll.find(textSearch);
		
		while (res.hasNext()){
			
            n = res.next();
			
			
			cle = (String) n.get("chemin");
			
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
		
		populateMediasCells();
		
		
	}
	
	@FXML
	protected void onClickList(){
		
		currentCellFiefd = table.getSelectionModel().getSelectedItem();
		BrowserGuiController bc = Main.getLoader_browser().getController();
		bc.populatePath(currentCellFiefd);
		Main.getStageBrowser().show();
	}
		
		
		public void populateMediasCells(){	
					
			 //Set up the table data
			
			
		    disque.setCellValueFactory(
		        new PropertyValueFactory<CellFields,String>("fieldDiskName")
		    );
		    nom.setCellValueFactory(
		        new PropertyValueFactory<CellFields,String>("fieldName")
		    );
		    taille.setCellValueFactory(
			    new PropertyValueFactory<CellFields,Integer>("fieldSize")
			);
		    date.setCellValueFactory(
			    new PropertyValueFactory<CellFields,String>("fieldDate")
			);
		    ext.setCellValueFactory(
			    new PropertyValueFactory<CellFields,String>("fieldExt")
			);
		    type.setCellValueFactory(
				new PropertyValueFactory<CellFields,String>("fieldType")
			);
		    chemin.setCellValueFactory(
				new PropertyValueFactory<CellFields,String>("fieldPathName")
			);
			    
		    table.setItems(resArray);

		}

	
	
	MongoClient mongoclient;
	
	DB db;
	DBCollection coll;

	
	public void connecter(){

		try {
		mongoclient = new MongoClient( "192.168.0.201" , 27017 );
		}
		catch (UnknownHostException uhe) {
		}
		
	
		db = mongoclient.getDB( "Lequel" );
		coll = db.getCollection("Lequel_V03");
		

		
	}

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Stub de la méthode généré automatiquement
		
		connecter();
	}


}
