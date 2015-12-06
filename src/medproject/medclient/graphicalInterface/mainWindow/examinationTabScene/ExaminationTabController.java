package medproject.medclient.graphicalInterface.mainWindow.examinationTabScene;

import java.util.function.Predicate;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.ControllerInterface;
import medproject.medclient.graphicalInterface.examinationWindow.ExaminationWindow;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.graphics.structures.ExaminationTableView;
import medproject.medlibrary.patient.Patient;

public class ExaminationTabController implements ControllerInterface{

	private DataLoader dataLoader;
	private ExaminationTableView examinationTable;
	@FXML TextField examinationSearchField;
	@FXML ChoiceBox<String> searchTypeBox;
	@FXML Button adaugaConsultatieButton;
	@FXML Button vizualizeazaButton;
	@FXML Button stergeConsultatieButton;

	@FXML Button tiparireButton;
	@FXML Button exportButton;

	@FXML ScrollPane tableScrollPane;

	@Override
	public void init(final DataLoader dataLoader, Stage stage) {
		this.dataLoader = dataLoader;
		this.examinationTable = new ExaminationTableView();
		setExaminationTable();
		setTableListeners();
		setTableFilters();
	}

	private void setTableListeners() {
		dataLoader.getExaminationList().addListener(new ListChangeListener<Examination>(){

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Examination> event) {
				String value = examinationSearchField.getText();
				examinationSearchField.setText(null);
				examinationSearchField.setText(value);
			}
			
		});
		
		vizualizeazaButton.disableProperty().bind(examinationTable.getSelectionModel().selectedItemProperty().isNull());
		stergeConsultatieButton.disableProperty().bind(examinationTable.getSelectionModel().selectedItemProperty().isNull());
	}
	
	private void setTableFilters(){
		//TODO: REFACTOR
		searchTypeBox.setItems(FXCollections.observableArrayList(
				"Nume", "CNP", "PID"));
		searchTypeBox.getSelectionModel().select(0);
		
		final FilteredList<Examination> filteredData = new FilteredList<Examination>(dataLoader.getExaminationList());

		examinationSearchField.textProperty().addListener(new ChangeListener<String>(){

			@Override
			public void changed(ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
				filteredData.setPredicate(new Predicate<Examination>(){

					@Override
					public boolean test(Examination examination) {
						if(newValue == null)
							return true;
						if(newValue.isEmpty())
							return true;
						if(examination == null)
							return false;
						if(examination.getPatient() == null)
							return false;
						
						String searchString = newValue.toLowerCase();
						
						switch(searchTypeBox.getSelectionModel().getSelectedItem()){
						case "Nume":
							if(examination.getPatientName().toLowerCase().contains(searchString))
								return true;
							break;
						case "CNP":
							if(examination.getPatientCNP().toLowerCase().contains(searchString))
								return true;
							break;
						case "PID":
							if(Integer.toString(examination.getPatientID()).toLowerCase().contains(searchString))
								return true;
							break;
						}
						
						return false;
					}
				});
			}
		});
		SortedList<Examination> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(examinationTable.comparatorProperty());
		
		examinationTable.setItems(sortedData);
		
		examinationSearchField.setText(null);
	}

	private void setExaminationTable() {
		tableScrollPane.setContent(examinationTable);
	}

	@FXML protected void onPressAdaugaConsultatie(){
		ExaminationWindow examinationWindow = new ExaminationWindow(null, true);	
		examinationWindow.show();
	}

	@FXML protected void onPressVizualizeaza(){
		ExaminationWindow examinationWindow = new ExaminationWindow(examinationTable.getSelectedExamination(), false);	
		examinationWindow.show();
	}

	@FXML protected void onPressStergeConsultatie(){

	}


}
