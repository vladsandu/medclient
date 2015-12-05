package medproject.medclient.graphicalInterface.mainWindow;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.Navigator;
import medproject.medclient.graphicalInterface.mainWindow.loadingScene.LoadingController;
import medproject.medlibrary.logging.LogWriter;

public class MainWindow{
	
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	public DataLoader dataLoader;
	
	private Stage primaryWindow;
	//TODO: Consider refactoring the navigator
	public void startWindow(DataLoader dataLoader) throws Exception {
		this.dataLoader = dataLoader;
		Navigator.setMainWindow(this);
		start();
	}

	public void start() throws Exception {
		primaryWindow = new Stage();
		primaryWindow.setTitle("Med Client");//TODO: Constants class refactoring
        primaryWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                stop();
            }
        });  
        Navigator.loadSceneToMainWindow(Navigator.LOGIN_SCENE);
        primaryWindow.show();    
    }
	
	public void stop(){
		dataLoader.stop();
		primaryWindow.close();
		Platform.exit();
		System.exit(0);
	}
	
	public void modifyConnectionStatus(boolean connectionStatus){
	}
	
	public void runAndWait(final Runnable runnable) {
		// running this from the FAT 
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}
		// running this from another thread
		try {
			FutureTask<Void> future = new FutureTask<>(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			LOG.severe("Couldn't execute task on JFX thread: " + e.getMessage());
		}
	}
	
	public void setLoadingMessage(final String text){
		runAndWait(new Runnable(){

			@Override
			public void run() {
				if(Navigator.getCurrentScene().equals(Navigator.LOADING_SCENE)
						&& Navigator.getCurrentController() != null){
					LoadingController controller = (LoadingController) Navigator.getCurrentController();
					controller.setText(text);
				}
			}
		});
	}
	
	public void setLoadingProgress(final Double value){
		runAndWait(new Runnable(){

			@Override
			public void run() {
				if(Navigator.getCurrentScene().equals(Navigator.LOADING_SCENE)
						&& Navigator.getCurrentController() != null){
					LoadingController controller = (LoadingController) Navigator.getCurrentController();
					controller.setProgress(value);
				}
			}
		});
	}	
	
	public void setScene(Scene scene){
		primaryWindow.setScene(scene);
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public Stage getPrimaryWindow() {
		return primaryWindow;
	}
}
