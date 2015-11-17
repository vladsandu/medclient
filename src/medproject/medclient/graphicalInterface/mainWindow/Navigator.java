package medproject.medclient.graphicalInterface.mainWindow;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.mainWindow.mainFrame.MainController;

public class Navigator {
	//TODO: Refactor the navigator
	public static final String MAIN_FRAME = "./mainFrame/mainFrame.fxml";
	public static final String LOGIN_SCENE = "./loginScene/loginScene.fxml";
	
	private static MainController mainController;

	public static MainController getMainController() {
		return mainController;
	}

	public static void setMainController(MainController mainController) {
		Navigator.mainController = mainController;
	}
	 
	public static void loadScene(String fxml) {

	       try {
	    	   FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
	    	   Pane scene = (Pane) loader.load();
	    	   
	    	   mainController.setScene(scene);
	    	   
	           ControllerInterface currentController = (ControllerInterface)loader.getController();
	    	   currentController.init(mainController.getDataLoader(), mainController.getUpdateExecutor());
	    	   
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	   }
	
}
