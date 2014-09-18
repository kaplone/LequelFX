package LequelFX.LequelFX;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import utils.CellFields;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;



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
	private HBox pathBox;
	
	public void populatePath(CellFields currentCellFiefd ){
		
		pathBox.getChildren().clear();
		ObservableList<Button> boutons = FXCollections.observableArrayList();
		System.out.println(currentCellFiefd.getFieldPathName());
		for (String s : currentCellFiefd.getFieldPathName().split(" ")){
			boutons.add(new Button(s));
		}
		pathBox.getChildren().addAll(boutons);
	}
	
	public static void show_stage(){
		
	}
	
	
	public void populateMediasCells(){	
		
		 //Set up the table data
		
		
	    type.setCellValueFactory(
	        new PropertyValueFactory<CellFields,Image>("fieldDiskName")
	    );
	    nom.setCellValueFactory(
	        new PropertyValueFactory<CellFields,String>("fieldName")
	    );
	    taille.setCellValueFactory(
		    new PropertyValueFactory<CellFields,Integer>("fieldSize")
		);
//	    date.setCellValueFactory(
//		    new PropertyValueFactory<CellFields,String>("fieldDate")
//		);
//	    ext.setCellValueFactory(
//		    new PropertyValueFactory<CellFields,String>("fieldExt")
//		);
//	    type.setCellValueFactory(
//			new PropertyValueFactory<CellFields,String>("fieldType")
//		);
//	    chemin.setCellValueFactory(
//			new PropertyValueFactory<CellFields,String>("fieldPathName")
//		);
//		    
//	    table.setItems(resArray);

	}

	public void initialize(URL location, ResourceBundle resources) {
		//currentCellFiefd = new CellFields();
		//populatePath();
	}

}
