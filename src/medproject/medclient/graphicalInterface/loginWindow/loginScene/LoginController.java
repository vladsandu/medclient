package medproject.medclient.graphicalInterface.loginWindow.loginScene;

import java.util.concurrent.ExecutorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.loginWindow.LoginNavigator;

public class LoginController implements ControllerInterface{

	DataLoader dataLoader;
	ExecutorService executor;
	
	private boolean isRunning = true;
	
	@FXML private TextField username_field;
	@FXML private TextField password_field;
	@FXML private Button login_button;
	
	@FXML protected void onLoginPress(){
	}

	@FXML protected void onRegisterPress(){
		LoginNavigator.loadScene(LoginNavigator.REGISTER_WELCOME_SCENE);
	}

	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;
	}
	
}
