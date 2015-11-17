package medproject.medclient.graphicalInterface.mainWindow.loginScene;

import java.util.concurrent.ExecutorService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.mainWindow.Navigator;

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
	}

	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;
	}
	
}
