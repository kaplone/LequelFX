package LequelFX.LequelFX;
	
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application implements javafx.fxml.Initializable {
	Parent root;
	Stage stage_tableau;
	Stage stage_browser;
	BorderPane tableau;
	BorderPane browser;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			stage_browser = new Stage();
			stage_tableau = new Stage();
			root = FXMLLoader.load(getClass().getResource("/fxml/Group.fxml"));
			tableau = FXMLLoader.load(getClass().getResource("/fxml/Gui.fxml"));
			browser =  FXMLLoader.load(getClass().getResource("/fxml/BrowserGui.fxml"));

			Scene scene_tableau = new Scene(tableau);
			Scene scene_browser = new Scene(browser);
			scene_tableau.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			stage_tableau.setScene(scene_tableau);
			stage_tableau.initModality(Modality.APPLICATION_MODAL);
			stage_browser.setScene(scene_browser);
			stage_browser.initModality(Modality.APPLICATION_MODAL);
			//primaryStage.show();
			stage_tableau.show();
			stage_browser.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public BorderPane getRoot(){
		return tableau;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Stub de la méthode généré automatiquement
		
	}
}

