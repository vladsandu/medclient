package medproject.medclient.graphicalInterface.mainWindow.loadingScene;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.mainWindow.Navigator;

public class LoadingController implements ControllerInterface{

	DataLoader dataLoader;

	@FXML private ProgressBar progress_bar;
	@FXML private Label message_label;

	@Override
	public void init(final DataLoader dataLoader) {
		// TODO Auto-generated method stub
	}
	
	public void setText(String text){
		message_label.setText(text);
	}
	
	public void setProgress(Double value){
		progress_bar.setProgress(value);
	}
}
