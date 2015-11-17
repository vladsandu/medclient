package medproject.medclient.graphicalInterface.loginWindow;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.loginWindow.mainFrame.MainLoginController;

public class LoginNavigator {

	public static final String MAIN_FRAME = "./mainFrame/mainFrame.fxml";
	public static final String LOGIN_LOADING_SCENE = "./loadingScene/loginLoadingScene/loginLoadingScene.fxml";
	public static final String REGISTER_LOADING_SCENE = "./loadingScene/registerLoadingScene/registerLoadingScene.fxml";
	public static final String LOGIN_SCENE = "./loginScene/loginScene.fxml";
	public static final String REGISTER_WELCOME_SCENE = "./registerScene/welcomeScene/welcomeScene.fxml";
	public static final String REGISTER_USERNAME_SCENE = "./registerScene/usernameScene/usernameScene.fxml";
	public static final String REGISTER_PASSWORD_SCENE = "./registerScene/passwordScene/passwordScene.fxml";
	public static final String REGISTER_SECURITY_QUESTION_SCENE = "./registerScene/securityQuestionScene/securityQuestionScene.fxml";
	public static final String REGISTER_DISPLAY_NAME_SCENE = "./registerScene/displayNameScene/displayNameScene.fxml";
	    
	private static MainLoginController mainLoginController;

	public static MainLoginController getMainLoginController() {
		return mainLoginController;
	}

	public static void setMainLoginController(MainLoginController mainLoginController) {
		LoginNavigator.mainLoginController = mainLoginController;
	}
	 
	public static void loadScene(String fxml) {

	       try {
	    	   FXMLLoader loader = new FXMLLoader(LoginNavigator.class.getResource(fxml));
	    	   Pane scene = (Pane) loader.load();
	    	   
	    	   mainLoginController.setScene(scene);
	    	   
	           ControllerInterface currentController = (ControllerInterface)loader.getController();
	    	   currentController.init(mainLoginController.getDataLoader(), mainLoginController.getUpdateExecutor());
	    	   
	       } catch (IOException e) {
	           e.printStackTrace();
	       }
	   }
	
}
