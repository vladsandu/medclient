package medproject.medclient.graphicalInterface.mainWindow.personTabScene;

import java.util.concurrent.ExecutorService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medlibrary.patient.Patient;
import medproject.medlibrary.patient.PatientCategory;

public class PersonTabController implements ControllerInterface{

	private DataLoader dataLoader;
	private ExecutorService executor;

	@FXML TableView<Patient> patientTable;
	@FXML TableColumn<Patient, String> cnpColumn;
	@FXML TableColumn<Patient, String> firstNameColumn;
	@FXML TableColumn<Patient, String> lastNameColumn;
	@FXML TableColumn<Patient, String> genderColumn;
	@FXML TableColumn<Patient, String> categoryColumn;
	@FXML TableColumn<Patient, String> birthDateColumn;
	@FXML TableColumn<Patient, String> deceaseDateColumn;
	@FXML TableColumn<Patient, String> enlistingDateColumn;
	@FXML TableColumn<Patient, String> delistingDateColumn;
	@FXML TableColumn<Patient, String> countyColumn;
	@FXML TableColumn<Patient, String> cityColumn;
	
	@Override
	public void init(DataLoader dataLoader, ExecutorService executor) {
		this.dataLoader = dataLoader;
		this.executor = executor;

		patientTable.setItems(dataLoader.getPatientList());
		patientTable.setEditable(false);
		setColumnValues();
	}
	
	private void setColumnValues(){
		cnpColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getCNP());
		     }
		  });
		
		firstNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getFirstName());
		     }
		  });
		
		lastNameColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getLastName());
		     }
		  });
		
		genderColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getGender());
		     }
		  });
		
		categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getCategory().toString());
		     }
		  });

		birthDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getBirthDate());
		     }
		  });
		
		deceaseDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getDeceaseDate());
		     }
		  });

		enlistingDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getEnlistingDate());
		     }
		  });
		
		delistingDateColumn.setCellValueFactory(new Callback<CellDataFeatures<Patient, String>, ObservableValue<String>>() {
		     public ObservableValue<String> call(CellDataFeatures<Patient, String> p) {
		         return new ReadOnlyObjectWrapper<String>(p.getValue().getDelistingDate());
		     }
		  });
	}
}
