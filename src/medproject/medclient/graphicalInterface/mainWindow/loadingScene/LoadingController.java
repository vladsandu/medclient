package medproject.medclient.graphicalInterface.mainWindow.loadingScene;

import java.util.concurrent.ExecutorService;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;

public class LoadingController implements ControllerInterface{

	DataLoader dataLoader;
	ExecutorService executor;
	
	@FXML private ProgressBar progress_bar;
	@FXML private Label message_label;
	
	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		// TODO Auto-generated method stub
		message_label.setText("Se incarca pacientii");
		progress_bar.setProgress(0.4f);
	}

}
