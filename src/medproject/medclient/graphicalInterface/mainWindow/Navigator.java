package medproject.medclient.graphicalInterface.mainWindow;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.logging.LogWriter;

public class Navigator {
	//TODO: Refactor the navigator
	private static final Logger LOG = LogWriter.getLogger("Navigator");

	public static final String LOGIN_SCENE = "./loginScene/loginScene.fxml";
	public static final String LOADING_SCENE = "./loadingScene/loadingScene.fxml";

	private static MainWindow mainWindow = null;
	
	public static void setMainWindow(MainWindow mainWindow) {
		Navigator.mainWindow = mainWindow;
	}

	public static void loadScene(String fxml) {

		if(mainWindow == null){
			LOG.severe("Navigator not initialized");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
			Pane pane = (Pane) loader.load();
			Scene scene = new Scene(pane);
			
			ControllerInterface currentController = (ControllerInterface)loader.getController();
			currentController.init(mainWindow.getDataLoader(), mainWindow.getUpdateExecutor());

			mainWindow.setScene(scene);
		} catch (IOException e) {
			LOG.severe("Cannot find fxml file location for value: " + fxml);
		}
	}

}
