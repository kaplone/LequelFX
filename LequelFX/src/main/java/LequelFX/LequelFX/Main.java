package LequelFX.LequelFX;
	
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application implements javafx.fxml.Initializable {
	BorderPane root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/gui.fxml"));
			
			Scene scene = new Scene(root,1280,500);
			scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public BorderPane getRoot(){
		return root;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Stub de la méthode généré automatiquement
		
	}
}

