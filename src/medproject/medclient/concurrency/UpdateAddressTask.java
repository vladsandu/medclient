package medproject.medclient.concurrency;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.patientDataWindow.patientRecordScene.PatientRecordController;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.concurrency.RequestStatus;
import medproject.medlibrary.patient.Address;
import medproject.medlibrary.patient.PatientRecord;

public class UpdateAddressTask extends CustomTask{

	private final DataLoader dataLoader;
	private final PatientRecordController controller;
	private final Address address;
	
	public UpdateAddressTask(DataLoader dataLoader, PatientRecordController controller, Address address) {
		super(RequestCodes.UPDATE_PATIENT_ADDRESS_REQUEST);
		this.dataLoader = dataLoader;
		this.controller = controller;
		this.address = address;
	}

	@Override
	public void run() {
		try {
			getLatch().await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		if(requestStatus == RequestStatus.REQUEST_COMPLETED){
			try{
				dataLoader.updatePatientAddress(address);
			}catch(ClassCastException e){
				GUIUtils.showErrorDialog("Update patient address error", "Data corrupted");
			}
		}
		
		controller.closeWindow();
	}



}
