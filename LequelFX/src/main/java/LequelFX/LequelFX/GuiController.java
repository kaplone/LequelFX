	package LequelFX.LequelFX;
	
	import java.net.URL;
	import java.net.UnknownHostException;
	import java.util.Date;
	import java.util.ResourceBundle;
	
	import utils.CellFields;
	import utils.JsonUtils;
	import utils.TableViewGenerator;
	
	import com.mongodb.BasicDBObject;
	import com.mongodb.DB;
	import com.mongodb.DBCollection;
	import com.mongodb.DBCursor;
	import com.mongodb.DBObject;
	import com.mongodb.MongoClient;
	
	import javafx.beans.property.IntegerProperty;
	import javafx.beans.property.SimpleIntegerProperty;
	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import javafx.collections.ObservableMap;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.Tab;
	import javafx.scene.control.TabPane;
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
		private TextField et_;
		@FXML
		private TextField ou_;
		
		
		@FXML
		private Label taille_base_label;
		@FXML
		private Label nb_res_label;
		@FXML
		private Label temps_res_label;
		@FXML
		private TabPane onglets;
	
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
		
		String motif = "";
		String et = "";
		String ou = "";
		
		IntegerProperty compte = new SimpleIntegerProperty();
		
		Tab currentTab;
		
		TableViewGenerator tv;
		
	
		MongoClient mongoclient;
		
		DB db;
		DBCollection coll;
	
		
		@FXML
		protected void onButtonChercher(){
			
			motif = pattern.getText();
			et = et_.getText();
			ou = ou_.getText();
	
	        
			currentTab = new Tab(motif + "+" +  et + "/" + ou);
						
			onglets.getTabs().add(currentTab);
			
			table = new TableView<CellFields>();
			
			tv = new TableViewGenerator(table);
	
			resArray = tv.getResArray();
	
			if (motif.length() != 0 && et.length() == 0 && ou.length() == 0){
				BasicDBObject search = new BasicDBObject("$search", String.format("\"%s\"", motif));
				BasicDBObject textSearch = new BasicDBObject("$text", search);
				res = coll.find(textSearch);
			}
			else if (motif.length() != 0 && et.length() != 0 && ou.length() == 0){
				BasicDBObject et_search = new BasicDBObject("$search", String.format("\"%s\" \"%s\"", motif, et));
				BasicDBObject textSearch_final_et = new BasicDBObject("$text", et_search);
				res = coll.find(textSearch_final_et);
			}
			else if (motif.length() != 0 && et.length() == 0 && ou.length() != 0){
				BasicDBObject ou_search = new BasicDBObject("$search", String.format("\"%s\" %s", motif, et));
				BasicDBObject textSearch_final_ou = new BasicDBObject("$text", ou_search);
				res = coll.find(textSearch_final_ou);
			}
			else {
				System.out.println("pas de res");
			}
			
			taille_base_label.setText("" + coll.count() + " ");
			temps_res_label.setText("" + (Double.parseDouble(res.explain().get("millis").toString()) /1000) + " ");
			
			while (res.hasNext()){
				
	            n = res.next();
	
				cle = (String) n.get("chemin");
				
				if (! contenu.containsKey(cle)){
					contenu.put(cle, (Date) n.get("scan"));
					JsonUtils.loadList(n.toString(), resArray);
					compte.set(compte.get() + 1);
					if (compte.get() > 499){
						nb_res_label.setText("" + compte.get() + " (dépasse les 500) ");
						break;
					}
					else {
						nb_res_label.setText("" + compte.get() + " ");
					}
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
			currentTab.setContent(table);
			
			
		}
		
		@FXML
		protected void onClickList(){
			
			currentCellFiefd = table.getSelectionModel().getSelectedItem();
			
			if(currentCellFiefd != null && currentCellFiefd.getFieldName().length() != 0){
				BrowserGuiController bc = Main.getLoader_browser().getController();
				bc.populatePath(currentCellFiefd);
				Main.getStageBrowser().show();
			}
		}
			
			
		public void populateMediasCells(){	
					
			 //Set up the table data
			
			
		    tv.getDisque().setCellValueFactory(
		        new PropertyValueFactory<CellFields,String>("fieldDiskName")
		    );
		    tv.getNom().setCellValueFactory(
		        new PropertyValueFactory<CellFields,String>("fieldName")
		    );
		    tv.getTaille().setCellValueFactory(
			    new PropertyValueFactory<CellFields,Integer>("fieldSize")
			);
		    tv.getDate().setCellValueFactory(
			    new PropertyValueFactory<CellFields,String>("fieldDate")
			);
		    tv.getExt().setCellValueFactory(
			    new PropertyValueFactory<CellFields,String>("fieldExt")
			);
		    tv.getChemin().setCellValueFactory(
				new PropertyValueFactory<CellFields,String>("fieldPathName")
			);
			
		    System.out.println(resArray.size());
		    System.out.println(table.getChildrenUnmodifiable());
		    
		    table.setItems(resArray);
	
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
	
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Stub de la méthode généré automatiquement
			
			connecter();
		}
	
	
	}
