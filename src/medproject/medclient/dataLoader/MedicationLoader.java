package medproject.medclient.dataLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
	private final List<Drug> drugList;
	
	public MedicationLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
		drugList = new ArrayList<Drug>();
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.DRUG_LIST_REQUEST:
			processDrugListRequest(request);		break;
		case RequestCodes.MEDICATION_LIST_REQUEST:
			processMedicationListRequest(request);	break;
		default:	
			processGUITaskRequest(request);			break;
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

	private void processGUITaskRequest(Request request) {
		LOG.info(request.getMessage());

		if(request.getStatus() != RequestStatus.REQUEST_COMPLETED){
			
			GUIUtils.showErrorDialog("Error", request.getMessage());
		}

		dataLoader.processGuiTask(request);
	}

	public void loadDrugList() {
		dataLoader.makeRequest(new Request(RequestCodes.DRUG_LIST_REQUEST, null));
	}
	
	public void loadMedicationList() {
		dataLoader.makeRequest(new Request(RequestCodes.MEDICATION_LIST_REQUEST, null));
	}
}
