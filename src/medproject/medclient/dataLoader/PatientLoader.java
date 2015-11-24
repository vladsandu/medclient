package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.logging.LogWriter;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.patient.Patient;

public class PatientLoader {
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private final DataLoader dataLoader;
	
	public PatientLoader(DataLoader dataLoader){
		this.dataLoader = dataLoader;
	}
	
	public void processRequest(Request request){
		switch(request.getREQUEST_CODE()){
		case RequestCodes.PATIENT_LIST_REQUEST:
			processPatientListRequest(request);
			break;
		}
	}

	public void loadPatientList(){
		dataLoader.makeRequest(new Request(RequestCodes.PATIENT_LIST_REQUEST, null));
	}
	
	@SuppressWarnings("unchecked")
	private void processPatientListRequest(Request request) {
		LOG.info(request.getMessage());
		
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
			List<Patient> patientList = (List<Patient>) request.getDATA();
			
			for(Patient patient : patientList)
				dataLoader.addPatient(patient);
			
			dataLoader.getInitialLoader().setPatientsLoaded(true);
		}
		else{
			
		}
	}

}
