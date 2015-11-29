package medproject.medclient.concurrency;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonController;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.patient.Patient;

public class DeletePatientTask extends CustomTask{

	private final DataLoader dataLoader;
	private final Patient patient;
	
	public DeletePatientTask(DataLoader dataLoader, Patient patient) {
		super(RequestCodes.DELETE_PATIENT_REQUEST, "Se sterge pacientul...");
		this.dataLoader = dataLoader;
		this.patient = patient;
	}

	@Override
	public void run() {
		try {
			loadingWindow.start();
			getLatch().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		if(requestStatus == RequestStatus.REQUEST_COMPLETED){
			dataLoader.deletePatient(patient);
		}
		
		loadingWindow.stop();
	}
}
