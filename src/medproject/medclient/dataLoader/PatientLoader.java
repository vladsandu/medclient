package medproject.medclient.dataLoader;

import java.util.List;
import java.util.logging.Logger;

import medproject.medclient.concurrency.AddPatientTask;
import medproject.medclient.concurrency.PatientRecordListTask;
import medproject.medclient.concurrency.PatientTabTask;
import medproject.medclient.concurrency.UpdateAddressTask;
import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonController;
import medproject.medclient.graphicalInterface.patientDataWindow.patientRecordScene.PatientRecordController;
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
			dataLoader.processGuiTask(request);			break;
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
				dataLoader.addPatient(patient, true);

			dataLoader.getInitialLoader().setPatientsLoaded(true);
		}
		else{
			GUIUtils.showErrorDialog("Patient List Error", request.getMessage());
			//fatal
		}
	}
//TODO: maybe refactor
	public void makePatientRecordByCNPRequest(AddPersonController controller, String cnp){
		dataLoader.makeRequest(new Request(RequestCodes.PATIENT_RECORD_BY_CNP_REQUEST, cnp));
		dataLoader.addGuiTask(new PatientRecordListTask(controller));
	}

	public void makeUpdatePatientAddressRequest(PatientRecordController controller, Address address){
		dataLoader.makeRequest(new Request(RequestCodes.UPDATE_PATIENT_ADDRESS_REQUEST, address));
		dataLoader.addGuiTask(new UpdateAddressTask(dataLoader, controller, address));
	}

	public void makeAddPatientRequest(AddPersonController controller, int PID, int PIN){
		dataLoader.makeRequest(new Request(RequestCodes.ADD_PATIENT_REQUEST, PID, PIN));
		dataLoader.addGuiTask(new AddPatientTask(dataLoader, controller));	
	}

	public void makeDeletePatientRequest(Patient patient){
		if(patient == null)
			return;

		dataLoader.makeRequest(new Request(RequestCodes.DELETE_PATIENT_REQUEST, patient.getPatientID()));
		dataLoader.addGuiTask(new PatientTabTask(dataLoader, patient, RequestCodes.DELETE_PATIENT_REQUEST, "Se sterge pacientul..."));
	}

	public void makeDeceasedPatientRequest(Patient patient){
		if(patient == null)
			return;

		dataLoader.makeRequest(new Request(RequestCodes.DECEASED_PATIENT_REQUEST, patient.getPatientID()));
		dataLoader.addGuiTask(new PatientTabTask(dataLoader, patient, RequestCodes.DECEASED_PATIENT_REQUEST, "Se actualizeaza datele..."));
	}

	public void makeUnregisterPatientRequest(Patient patient){
		if(patient == null)
			return;

		dataLoader.makeRequest(new Request(RequestCodes.UNREGISTER_PATIENT_REQUEST, patient.getPatientID()));
		dataLoader.addGuiTask(new PatientTabTask(dataLoader, patient, RequestCodes.UNREGISTER_PATIENT_REQUEST, "Se dezinscrie pacientul..."));
	}

	public void makeRegisterPatientRequest(Patient patient){
		if(patient == null)
			return;

		dataLoader.makeRequest(new Request(RequestCodes.REGISTER_PATIENT_REQUEST, patient.getPatientID()));
		dataLoader.addGuiTask(new PatientTabTask(dataLoader, patient, RequestCodes.REGISTER_PATIENT_REQUEST, "Se inscrie pacientul..."));
	}
	
	public Patient getPatientByID(int id){
		for(Patient patient : dataLoader.getPatientList())
			if(patient.getPatientID() == id)
				return patient;
		
		return null;
	}
}
