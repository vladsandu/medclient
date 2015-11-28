package medproject.medclient.graphicalInterface.mainWindow.loadingScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;

public class LoadingController implements ControllerInterface{

	DataLoader dataLoader;

	@FXML private ProgressBar progress_bar;
	@FXML private Label message_label;

	@Override
	public void init(final DataLoader dataLoader, Stage stage) {
		// TODO Auto-generated method stub
	}
	
	public void setText(String text){
		message_label.setText(text);
	}
	
	public void setProgress(Double value){
		progress_bar.setProgress(value);
	}
}
