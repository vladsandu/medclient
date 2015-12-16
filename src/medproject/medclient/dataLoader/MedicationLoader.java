package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import medproject.medclient.concurrency.MedicationWindowTask;
import medproject.medclient.graphicalInterface.prescriptionWindow.PrescriptionWindowController;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.logging.LogWriter;
import medproject.medlibrary.medication.Drug;
import medproject.medlibrary.medication.Medication;

public class MedicationLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;
	private final ObservableList<Drug> drugList;
	
	public MedicationLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
		drugList = FXCollections.observableArrayList();
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.DRUG_LIST_REQUEST:
			processDrugListRequest(request);		break;
		case RequestCodes.MEDICATION_LIST_REQUEST:
			processMedicationListRequest(request);	break;
		default:	
			dataLoader.processGuiTask(request);			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void processMedicationListRequest(Request request) {
		LOG.info(request.getMessage());
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Medication> list = (List<Medication>) request.getDATA();

			for(Medication medication : list)
				dataLoader.addMedication(medication);
			
			dataLoader.getInitialLoader().setMedicationLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Medication List Error", request.getMessage());
			//fatal	
		}				
	}

	@SuppressWarnings("unchecked")
	private void processDrugListRequest(Request request){
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Drug> list = (List<Drug>) request.getDATA();

			for(Drug drug : list)
				drugList.add(drug);
			
			dataLoader.getInitialLoader().setDrugListLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Drug List Error", request.getMessage());
			//fatal	
		}		
	}

	public void loadDrugList() {
		dataLoader.makeRequest(new Request(RequestCodes.DRUG_LIST_REQUEST, null));
	}
	
	public void loadMedicationList() {
		dataLoader.makeRequest(new Request(RequestCodes.MEDICATION_LIST_REQUEST, null));
	}
	
	public void makeAddMedicationRequest(PrescriptionWindowController controller, Medication medication, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_MEDICATION_REQUEST, medication, pin));
		dataLoader.addGuiTask(new MedicationWindowTask(
				dataLoader, controller, medication, RequestCodes.ADD_MEDICATION_REQUEST, "Se adauga medicatia..."));	
	}

	public void makeDeleteMedicationRequest(PrescriptionWindowController controller, Medication medication, int pin){
		dataLoader.makeRequest(new Request(RequestCodes.DELETE_MEDICATION_REQUEST, medication.getID(), pin));
		dataLoader.addGuiTask(new MedicationWindowTask(
				dataLoader, controller, medication, RequestCodes.DELETE_MEDICATION_REQUEST, "Se sterge medicatia..."));	
	}

	public Drug getDrugForID(int drugID){
		for(Drug drug : drugList)
			if(drug.getID() == drugID)
				return drug;
		
		return null;
	}

	public ObservableList<Drug> getDrugList() {
		return drugList;
	}
}
