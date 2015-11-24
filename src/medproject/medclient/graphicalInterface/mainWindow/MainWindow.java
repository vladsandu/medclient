package medproject.medclient.graphicalInterface.mainWindow;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.mainWindow.loadingScene.LoadingController;

public class MainWindow{
	
	public DataLoader dataLoader;
	private ExecutorService updateExecutor;
	
	private Stage primaryWindow;
	//TODO: Consider refactoring the navigator
	public void startWindow(DataLoader dataLoader) throws Exception {
		this.updateExecutor = Executors.newSingleThreadScheduledExecutor();
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
        Navigator.loadScene(Navigator.LOGIN_SCENE);
        primaryWindow.show();    
    }
	
	public void stop(){
		dataLoader.stop();
		primaryWindow.close();
		updateExecutor.shutdown();
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
			throw new RuntimeException(e);//FIXME: refactor
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
	
	public ExecutorService getUpdateExecutor() {
		return updateExecutor;
	}

	public DataLoader getDataLoader() {
		return dataLoader;
	}

	public Stage getPrimaryWindow() {
		return primaryWindow;
	}
}
