package medproject.medclient._clientRunner;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.mainWindow.MainWindow;
import medproject.medclient.logging.LogWriter;


public class MainClient extends Application{	

	@Override
	public void start(Stage primaryWindow) throws Exception {

		LogWriter.useFileLogging();
		
		final MainWindow mainWindow = new MainWindow();

		final DataLoader dataLoader = new DataLoader(mainWindow);
		

		try {
			dataLoader.start();
			mainWindow.startWindow(dataLoader);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {

		launch(args);    
	}
}