package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.logging.LogWriter;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
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
		case RequestCodes.PATIENT_RECORD_BY_CNP_REQUEST:
			processPatientRecordByCNPRequest(request);
			break;

		case RequestCodes.ADD_PATIENT_REQUEST:
			processAddPatientRequest(request);
			break;
		}
	}

	public void makeAddPatientRequest(int PID, int PIN){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_PATIENT_REQUEST, PID, PIN));
	}

	private void processAddPatientRequest(Request request){
		LOG.info(request.getMessage());
		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){
		}
		else{
			GUIUtils.showErrorDialog("Add Patient Error", request.getMessage());
		}
		dataLoader.processGuiTask(request);
	}
	
	public void makePatientRecordByCNPRequest(String cnp){
		dataLoader.makeRequest(new Request(RequestCodes.PATIENT_RECORD_BY_CNP_REQUEST, cnp));
	}

	private void processPatientRecordByCNPRequest(Request request){
		LOG.info(request.getMessage());

		if(request.getStatus() == RequestStatus.REQUEST_COMPLETED){

		}
		else{
			GUIUtils.showErrorDialog("Patient Record Request Error", request.getMessage());
		}

		dataLoader.processGuiTask(request);
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
			GUIUtils.showErrorDialog("Patient List Error", request.getMessage());
			//fatal
		}
	}

}
