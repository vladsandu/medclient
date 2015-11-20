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
	
	@FXML private TextField operator_field;
	@FXML private TextField password_field;
	@FXML private Button login_button;
	//TODO: Check if things are entered in the fields before enabling
	@FXML protected void onLoginPress(){
		//encryption here
		dataLoader.makeLoginRequest(operator_field.getText(), password_field.getText().toCharArray());
	}

	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;
	}
	
}
