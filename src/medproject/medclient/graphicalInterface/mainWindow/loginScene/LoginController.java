package medproject.medclient.graphicalInterface.mainWindow.loginScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;

public class LoginController implements ControllerInterface{

	DataLoader dataLoader;
	
	private boolean isRunning = true;
	
	@FXML private TextField operator_field;
	@FXML private TextField password_field;
	@FXML private Button login_button;
	//TODO: Check if things are entered in the fields before enabling
	@FXML protected void onLoginPress(){
		//encryption here
		dataLoader.makeLoginRequest(operator_field.getText(), password_field.getText());
	}

	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
	}
	
}
