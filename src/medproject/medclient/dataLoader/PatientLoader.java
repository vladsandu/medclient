package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.logging.LogWriter;
import medproject.medlibrary.patient.Address;
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
			processPatientListRequest(request);	break;
		default:	
			processGUITaskRequest(request);		break;
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
			GUIUtils.showErrorDialog("Patient List Error", request.getMessage());
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
//TODO: maybe refactor
	public void makeAddPatientRequest(int PID, int PIN){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_PATIENT_REQUEST, PID, PIN));
	}

	public void makeUpdatePatientAddressRequest(Address address){
		dataLoader.makeRequest(new Request(RequestCodes.UPDATE_PATIENT_ADDRESS_REQUEST, address));
	}

	public void makePatientRecordByCNPRequest(String cnp){
		dataLoader.makeRequest(new Request(RequestCodes.PATIENT_RECORD_BY_CNP_REQUEST, cnp));
	}
	
	public void makeDeletePatientRequest(int patientID) {
		dataLoader.makeRequest(new Request(RequestCodes.DELETE_PATIENT_REQUEST, patientID));
	}

	public void makeUnregisterPatientRequest(int patientID) {
		dataLoader.makeRequest(new Request(RequestCodes.UNREGISTER_PATIENT_REQUEST, patientID));
	}

	public void makeRegisterPatientRequest(int patientID) {
		dataLoader.makeRequest(new Request(RequestCodes.REGISTER_PATIENT_REQUEST, patientID));
	}
	
	public void makeDeceasedPatientRequest(int patientID) {
		dataLoader.makeRequest(new Request(RequestCodes.DECEASED_PATIENT_REQUEST, patientID));
	}
}
