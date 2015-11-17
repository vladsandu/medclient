package medproject.medclient.graphicalInterface.mainWindow;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.utils.UtilMethods;

public class MainWindowController {

	private DataLoader dataLoader;
	private ExecutorService executor;
	
	@FXML ImageView display_image_view;
	@FXML ComboBox<String> availability_combo_box;
	@FXML Label display_name_label;
	@FXML TextField friend_search_text_field;
	@FXML TreeView contacts_tree_view;
	@FXML ListView messages_list_view;
	@FXML TextArea message_input_text_area;
	
	@FXML VBox message_container;
	@FXML GridPane window_gridpane_container;
	
	ObservableList<Node> messagePaneChildren = FXCollections.observableArrayList();;
	
		public void init(DataLoader dataLoader, ExecutorService updateExecutor) {
		
		this.dataLoader = dataLoader;
		this.executor = updateExecutor;
		
		}
	 
	@FXML private void onNewFriendClick(){
	}

	private void configureMessageTextArea() {		
	}


	private void configureContactListView() {

		}
	
	private void configureFriendSearchTextField(){
	}

	
}
