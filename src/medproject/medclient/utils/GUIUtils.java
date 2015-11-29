package medproject.medclient.utils;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import medproject.medlibrary.logging.LogWriter;

public class GUIUtils {

	private static Logger LOG = LogWriter.getLogger("GUI Alert");

	private static void runOnGUIThread(Runnable runnable){
		if (Platform.isFxApplicationThread()) {
			runnable.run();
			return;
		}

		try {
			FutureTask<Void> future = new FutureTask<>(runnable, null);
			Platform.runLater(future);
			future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			LOG.severe("Couldn't execute task on JFX thread");
		}
	}

	public static int makeIntDialog(String title, String message, int size){

		TextInputDialog dialog = new TextInputDialog();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle(title);
		dialog.setContentText(message);

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()){
			return Integer.parseInt(result.get());
		}
		//TODO: throws in case of parsing went wrong
		return 0;
	}

	public static void showErrorDialog(final String title, final String message){
		runOnGUIThread(new Runnable(){

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle(title);
				alert.setContentText(message);
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.showAndWait();
			}
		});
		
		LOG.severe(title + ": " + message);
	}
	//TODO: exception error dialog
}
