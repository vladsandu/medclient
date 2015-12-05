package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.examination.Examination;
import medproject.medlibrary.logging.LogWriter;
import medproject.medlibrary.patient.Patient;
import medproject.medlibrary.prescription.Prescription;

public class PrescriptionLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;

	public PrescriptionLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}

	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.PRESCRIPTION_LIST_REQUEST:
			processPrescriptionListRequest(request);	break;
		default:	
			processGUITaskRequest(request);			break;
		}
	}
	
	public void loadPrescriptionList() {
		dataLoader.makeRequest(new Request(RequestCodes.PRESCRIPTION_LIST_REQUEST, null));
	}

	@SuppressWarnings("unchecked")
	private void processPrescriptionListRequest(Request request) {
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Prescription> prescriptionList = (List<Prescription>) request.getDATA();

			for(Prescription prescription : prescriptionList)
				dataLoader.addPrescription(prescription);

			dataLoader.getInitialLoader().setPrescriptionsLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Prescription List Error", request.getMessage());
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


	public Prescription getPrescriptionByID(int id){
		for(Patient patient : dataLoader.getPatientList())
			for(Examination examination : patient.getExaminationList())
				for(Prescription prescription : examination.getPrescriptionList())
					if(prescription.getID() == id)
						return prescription;
		
		return null;
	}
}
