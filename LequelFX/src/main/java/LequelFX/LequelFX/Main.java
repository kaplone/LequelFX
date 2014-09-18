package LequelFX.LequelFX;
	
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;

public class Main extends Application implements javafx.fxml.Initializable {
	private FXMLLoader loader_tableau;
	private static FXMLLoader loader_browser;
	private Stage stage_tableau;
	private static Stage stage_browser;
	private BorderPane tableau;
	private static BorderPane browser;
	private static Scene scene_browser;
	private Scene scene_tableau;

	@Override
	public void start(Stage primaryStage) {
		try {
			
			stage_tableau = new Stage();
			tableau = FXMLLoader.load(getClass().getResource("/fxml/Gui.fxml"));
			scene_tableau = new Scene(tableau);
			scene_tableau.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
			stage_tableau.setScene(scene_tableau);
			stage_tableau.initModality(Modality.APPLICATION_MODAL);
			stage_tableau.show();
			
			stage_browser = new Stage();
			loader_browser = new FXMLLoader();
			URL location = getClass().getResource("/fxml/BrowserGui.fxml");
			loader_browser.setLocation(location);
			loader_browser.setBuilderFactory(new JavaFXBuilderFactory());

			browser =  loader_browser.load(location.openStream());
			//browser =  loader_browser.load(getClass().getResource("/fxml/BrowserGui.fxml"));
			
			scene_browser = new Scene(browser);
			stage_browser.setScene(scene_browser);
			stage_browser.initModality(Modality.APPLICATION_MODAL);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public BorderPane getRoot(){
		return tableau;
	}
	
	public static Stage getStageBrowser(){
		return stage_browser;
	}
	public static Scene getSceneBrowser(){
		return scene_browser;
	}
	public static BorderPane getBrowser(){
		return browser;
	}
	
	public static FXMLLoader getLoader_browser(){
		return loader_browser;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public void initialize(URL location, ResourceBundle resources) {
		// TODO Stub de la méthode généré automatiquement
		
	}
}

