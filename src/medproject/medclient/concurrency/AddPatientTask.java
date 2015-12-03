package medproject.medclient.concurrency;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.addPersonWindow.AddPersonController;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.patient.Patient;

public class AddPatientTask extends CustomTask{

	private final DataLoader dataLoader;
	private final AddPersonController controller;
	
	public AddPatientTask(DataLoader dataLoader, AddPersonController controller) {
		super(RequestCodes.ADD_PATIENT_REQUEST, "Se adauga pacientul...");
		this.dataLoader = dataLoader;
		this.controller = controller;
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
		if(data != null){
			try{
				Patient patient = (Patient) data;
				dataLoader.addPatient(patient, false);
			}catch(ClassCastException e){
				GUIUtils.showErrorDialog("Add Patient Error", "Data corrupted");
			}
			
			controller.closeWindow();
		}
		else{
			controller.resetInterface();
		}
		loadingWindow.stop();
	}

}
