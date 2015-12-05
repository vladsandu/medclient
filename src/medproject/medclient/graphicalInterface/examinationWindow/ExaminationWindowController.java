package medproject.medclient.graphicalInterface.examinationWindow;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.patient.PatientRecord;

public class ExaminationWindowController implements ControllerInterface{
	private DataLoader dataLoader;
	private Stage stage;
	
	@Override
	public void init(DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.stage = stage;
	}
}
