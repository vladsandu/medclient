package medproject.medclient.concurrency;

import java.sql.Date;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.patient.Patient;

public class PatientTabTask  extends CustomTask{

	private final DataLoader dataLoader;
	private final Patient patient;

	public PatientTabTask(DataLoader dataLoader, Patient patient, int requestCode, String loadingMessage) {
		super(requestCode, loadingMessage);
		this.dataLoader = dataLoader;
		this.patient = patient;
	}

	@Override
	public void run() {
		try {
			loadingWindow.start();
			getLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	

		if(requestStatus == RequestStatus.REQUEST_COMPLETED){
			switch(getRequestCode()){
			case RequestCodes.DELETE_PATIENT_REQUEST:
				deletePatient();
				break;
			case RequestCodes.UNREGISTER_PATIENT_REQUEST:
				unregisterPatient();
				break;
			case RequestCodes.REGISTER_PATIENT_REQUEST:
				registerPatient();
				break;
			case RequestCodes.DECEASED_PATIENT_REQUEST:
				makePatientDeceased();
				break;
			}
		}
		loadingWindow.stop();
	}

	private void deletePatient(){
		dataLoader.deletePatient(patient);
	}

	private void unregisterPatient(){
		try{
			Date unregistrationDate = (Date) data;
			dataLoader.unregisterPatient(patient, unregistrationDate);
		}catch(ClassCastException e){
			GUIUtils.showErrorDialog("Unregister Patient Error", "Data corrupted");
		}
	}

	private void makePatientDeceased(){
		try{
			Date deceaseDate = (Date) data;
			dataLoader.makePatientDeceased(patient, deceaseDate);
		}catch(ClassCastException e){
			GUIUtils.showErrorDialog("Deceased date error", "Data corrupted");
		}
	}

	private void registerPatient(){
		try{
			Date registrationDate = (Date) data;
			dataLoader.registerPatient(patient, registrationDate);
		}catch(ClassCastException e){
			GUIUtils.showErrorDialog("Register Patient Error", "Data corrupted");
		}
	}
}
