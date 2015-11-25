package medproject.medclient._clientRunner;

import java.io.IOException;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;


public class MainClient extends Application{	

	private static final Logger LOG = LogWriter.getLogger(MainClient.class.getName());
	
	@Override
	public void start(Stage primaryWindow) throws Exception {

		LogWriter.useFileLogging();
		LogWriter.setDebugMode(true);
		
		final MainWindow mainWindow = new MainWindow();

		final DataLoader dataLoader = new DataLoader(mainWindow);
		

		try {
			dataLoader.start();
			mainWindow.startWindow(dataLoader);
		} catch (IOException e) {
			LOG.severe("Fatal Error: " + e.getMessage());
		}

	}

	public static void main(String[] args) throws Exception {

		launch(args);    
	}
}