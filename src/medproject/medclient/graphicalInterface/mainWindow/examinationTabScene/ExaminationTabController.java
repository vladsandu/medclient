package medproject.medclient.graphicalInterface.mainWindow.examinationTabScene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
		ObservableList<Examination> examinationList = FXCollections.observableArrayList();
		this.examinationTable = new ExaminationTableView(examinationList);
		setExaminationTable();
	}
	
	private void setExaminationTable() {
		tableScrollPane.setContent(examinationTable);
	}

	@FXML protected void onPressAdaugaConsultatie(){
		ExaminationWindow examinationWindow = new ExaminationWindow(null);	
		examinationWindow.show();
	}
	
	@FXML protected void onPressVizualizeaza(){
		ExaminationWindow examinationWindow = new ExaminationWindow(examinationTable.getSelectedExamination());	
		examinationWindow.show();
	}
	
	@FXML protected void onPressStergeConsultatie(){
		
	}
	
	
}
