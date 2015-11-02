package LequelFX.LequelFX;
	
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

//import org.bson.NewBSONDecoder;

import utils.CellFields;
import utils.JsonUtils;
import utils.TableViewGenerator;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
	
	public class GuiController implements Initializable{
		
		@FXML
		private BorderPane racine;
		
		@FXML
		private Button chercher;
		
		@FXML
		private TextField pattern;
		@FXML
		private TextField et_;
		@FXML
		private TextField sauf_;
		
		
		@FXML
		private Label taille_base_label;
		@FXML
		private Label nb_res_label;
		@FXML
		private Label temps_res_label;
		
		@FXML
		private TextField regex;
		@FXML
		private Button recherche_regex;
	
		private TableView<CellFields> table;
		
		private DBCursor res;
		
		private static CellFields currentCellFiefd;
		
		public static CellFields getCurrentCellFields(){
			return currentCellFiefd;
		}
		
	    ObservableMap<String, Date> contenu = FXCollections.observableHashMap();
	    
	    TabPane onglets = new TabPane();
	    //racine.setCenter(onglets);
		
		DBObject n;
		Date n_old ;
		
		String cle;
		
		String motif = "";
		String et = "";
		String sauf = "";
		
		String reg = "";
		
		Tab currentTab;
		
		String disque =  "";
		String disque_temp = "";
		
		
		SingleSelectionModel<Tab> selectionTab = onglets.getSelectionModel();
		
		
		
		TableViewGenerator tv;
		
	
		MongoClient mongoclient;
		
		DB db;
		DBCollection coll;
		
		@FXML
		protected void onButtonRegexChercher(){
			
			reg = regex.getText();
			
			currentTab = new Tab("[motif] " + reg);
			
			table = new TableView<CellFields>();
			
			tv = new TableViewGenerator(table);
			
			BasicDBObject search = new BasicDBObject("nom", Pattern.compile(reg, Pattern.CASE_INSENSITIVE));
			res = coll.find(search);
			
			affichage();
			
		}
	
		
		@FXML
		protected void onButtonChercher(){
			
			motif = pattern.getText();
			et = et_.getText();	
			sauf = sauf_.getText();	
	
			table = new TableView<CellFields>();
			table.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					onClickList();
		            }
		    });
			
			tv = new TableViewGenerator(table);
			
			if (motif.length() != 0 && et.length() == 0 && sauf.length() == 0){
				
				System.out.println("\n" + motif);
				
				currentTab = new Tab("[strict] " + motif);
				
				BasicDBObject search = new BasicDBObject("$search", String.format("\"%s\"", motif));
				BasicDBObject textSearch = new BasicDBObject("$text", search);
				res = coll.find(textSearch);
				System.out.println(res.count());

			}
			else if (motif.length() != 0 && et.length() != 0){
				
				System.out.println("\n" + motif + " " + et);
				
				currentTab = new Tab("[strict] " + motif + "+" +  et);
				
				BasicDBObject et_search = new BasicDBObject("$search", String.format("\"%s\" \"%s\"", motif, et));
				BasicDBObject textSearch_final_et = new BasicDBObject("$text", et_search);
				res = coll.find(textSearch_final_et);
			}
			
            else if (motif.length() != 0 && sauf.length() != 0){
				
				System.out.println("\n" + motif + " " + sauf);
				
				currentTab = new Tab("[strict] " + motif + "-" +  sauf);
				
				BasicDBObject sauf_search = new BasicDBObject("$search", String.format("\"%s\" -\"%s\"", motif, sauf));
				BasicDBObject textSearch_final_sauf = new BasicDBObject("$text", sauf_search);
				res = coll.find(textSearch_final_sauf);
			}

			affichage();
		}
		
		protected void affichage(){
			
			onglets.getTabs().add(currentTab);
			
			taille_base_label.setText("" + coll.count() + " ");

			DBCursor res1 = res.sort(new BasicDBObject("chemin", 1));
			
			System.out.println(res1.explain());
			System.out.println(res1.explain().get("executionStats"));
			System.out.println(res1.explain().get("executionStats.executionTimeMillis"));
			
			//tv.setTemps(Double.parseDouble( res.explain().get("executionStats.executionTimeMillis") /1000);
			
			temps_res_label.setText("" + (tv.getTemps()) + " ");
			
			while (res1.hasNext()){
				
				n = res1.next();
				cle = (String) n.get("chemin");
				
				disque_temp = cle.split("/")[1];

				
				if (! disque.equals("") && ! disque.equals(disque_temp)){
					JsonUtils.loadList(new BasicDBObject("chemin", "/ /")
					                             .append("nom", "")
					                             .append("scan", new Date())
					                             .append("date", new Date())
					                             .append("fichier", null)
					                             .append("extension", "")
					                             .append("taille", 0)
							                     .toString(), tv.getResArray());

					
				}
				
				disque = disque_temp;
	
				if (! contenu.containsKey(cle)){
					contenu.put(cle, (Date) n.get("scan"));
					
//					System.out.println("n : " + n);
//					System.out.println("tv : " + tv);
//					System.out.println("tv.getResArray() : " + tv.getResArray());
					JsonUtils.loadList(n.toString(), tv.getResArray());
					tv.setNb(tv.getNb() + 1);
					if (tv.getNb() > 499){
						nb_res_label.setText("" + tv.getNb() + " (dépasse les 500) ");
						res1 = null;
						break;
					}
					else {
						nb_res_label.setText("" + tv.getNb() + " ");
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
			// TODO : afficher le dernier onglet
			
			selectionTab.select(currentTab);
			
			
		}
		
		@FXML
		protected void onClickList(){
			
			currentCellFiefd = table.getSelectionModel().getSelectedItem();
			
			if(currentCellFiefd != null && currentCellFiefd.getFieldName().length() != 0){
				
				BrowserGuiController bc = Main.getLoader_browser().getController();
				
				if (currentCellFiefd.getFieldType().equals("Fichier")){
					bc.populatePath(currentCellFiefd);
				}
				else if (currentCellFiefd.getFieldType().equals("Dossier")){					
					bc.populatePathChild(currentCellFiefd);	
				}
				
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
		    
		    table.setItems(tv.getResArray());
	
		}
		
		public void connecter(){
	
			mongoclient = new MongoClient( "192.168.0.201" , 27017 );
			
		
			db = mongoclient.getDB( "LequelFX" );
			coll = db.getCollection("Lequel_V04");
			//coll = db.getCollection("Lequel_new_test");
			
	
			
		}
	
		public void initialize(URL location, ResourceBundle resources) {
			// TODO Stub de la méthode généré automatiquement
			
			racine.setCenter(onglets);
			
			connecter();
		}
	
	
	}
