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
	public static final String MAIN_SCENE = "./mainScene/mainScene.fxml";
	public static final String PERSON_TAB_SCENE = "./personTabScene/personTabScene.fxml";

	private static MainWindow mainWindow = null;
	private static String currentScene = "";
	private static ControllerInterface currentController = null;

	public static void setMainWindow(MainWindow mainWindow) {
		Navigator.mainWindow = mainWindow;
	}

	public static void loadScene(final String fxml) {
		if(mainWindow == null){
			LOG.severe("Navigator not initialized");
			return;
		}

		mainWindow.runAndWait(new Runnable(){

			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
					Pane pane = (Pane) loader.load();
					Scene scene = new Scene(pane);

					ControllerInterface currentController = (ControllerInterface)loader.getController();
					currentController.init(mainWindow.getDataLoader(), mainWindow.getUpdateExecutor());
					
					mainWindow.setScene(scene);
					mainWindow.getPrimaryWindow().sizeToScene();
					
					Navigator.currentController = currentController;
					currentScene = fxml;
				} catch (IOException e) {
					LOG.severe("Cannot find fxml file location for value: " + fxml);
				}				
			}

		});
	}

	public static Pane getPaneFromScene(final String fxml){
		FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
		Pane pane = null;
		try {
			pane = (Pane) loader.load();
			ControllerInterface currentController = (ControllerInterface)loader.getController();
			currentController.init(mainWindow.getDataLoader(), mainWindow.getUpdateExecutor());
		} catch (IOException e) {
			LOG.severe("Couldn't load pane from: " + fxml + "; " + e.getMessage());
		}
		
		return pane;
	}
		
	public static String getCurrentScene() {
		return currentScene;
	}

	public static ControllerInterface getCurrentController() {
		return currentController;
	}

}
