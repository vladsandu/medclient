package medproject.medclient.concurrency;

import java.util.List;

import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonController;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.patient.PatientRecord;

public class PatientRecordListTask extends CustomTask{

	private final AddPersonController controller;

	public PatientRecordListTask(AddPersonController controller) {
		super(RequestCodes.PATIENT_RECORD_BY_CNP_REQUEST);
		this.controller = controller;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			getLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		if(data != null){
			List<PatientRecord> patientRecordList = (List<PatientRecord>) data;
			controller.updateRecordList(patientRecordList);
		}
		controller.resetInterface();
	}

}
