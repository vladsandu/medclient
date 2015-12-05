package medproject.medclient.graphicalInterface;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medlibrary.logging.LogWriter;

public class Navigator {
	//TODO: Refactor the navigator
	private static final Logger LOG = LogWriter.getLogger("Navigator");

	public static final String REQUEST_LOADING_SCENE = "./requestLoadingWindow/requestLoadingScene.fxml";

	public static final String LOGIN_SCENE = "./mainWindow/loginScene/loginScene.fxml";
	public static final String LOADING_SCENE = "./mainWindow/loadingScene/loadingScene.fxml";
	public static final String MAIN_SCENE = "./mainWindow/mainScene/mainScene.fxml";
	public static final String PERSON_TAB_SCENE = "./mainWindow/personTabScene/personTabScene.fxml";
	public static final String EXAMINATION_TAB_SCENE = "./mainWindow/examinationTabScene/examinationTabScene.fxml";
	
	public static final String ADD_PERSON_SCENE = "./addPersonWindow/addPersonScene.fxml";
	public static final String PATIENT_DATA_SCENE = "./patientDataWindow/patientDataScene/patientDataScene.fxml";
	public static final String PATIENT_RECORD_SCENE = "./patientDataWindow/patientRecordScene/patientRecordScene.fxml";

	public static final String EXAMINATION_WINDOW_SCENE = "./examinationWindow/examinationWindowScene.fxml";
	
	public static final String DEFAULT_STYLESHEET = "medproject/medclient/graphicalInterface/style/style.css";

	
	private static MainWindow mainWindow = null;
	private static String currentScene = "";
	private static ControllerInterface currentController = null;

	public static void setMainWindow(MainWindow mainWindow) {
		Navigator.mainWindow = mainWindow;
	}

	public static void loadSceneToMainWindow(final String fxml) {
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
					scene.getStylesheets().add(DEFAULT_STYLESHEET);
					ControllerInterface currentController = (ControllerInterface)loader.getController();
					currentController.init(mainWindow.getDataLoader(), mainWindow.getPrimaryWindow());
					
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
	
	public static Scene getScene(final String fxml, final Stage stage){
		Scene scene = null;
		
		try {
			FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
			Pane pane = (Pane) loader.load();
			scene = new Scene(pane);
			scene.getStylesheets().add(DEFAULT_STYLESHEET);
			ControllerInterface currentController = (ControllerInterface)loader.getController();
			currentController.init(mainWindow.getDataLoader(), stage);
		} catch (IOException e) {
			LOG.severe("Cannot find fxml file location for value: " + fxml);
		}
		
		return scene;
	}

	
	public static Pane getPaneFromScene(final String fxml, final Stage stage){
		FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
		Pane pane = null;
		try {
			pane = (Pane) loader.load();
			ControllerInterface currentController = (ControllerInterface)loader.getController();
			currentController.init(mainWindow.getDataLoader(), stage);
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
