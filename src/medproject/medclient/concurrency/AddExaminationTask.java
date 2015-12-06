package medproject.medclient.concurrency;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.graphicalInterface.examinationWindow.ExaminationWindow;
import medproject.medclient.graphicalInterface.examinationWindow.addExaminationScene.AddExaminationController;
import medproject.medclient.utils.GUIUtils;
import medproject.medlibrary.concurrency.CustomTask;
import medproject.medlibrary.concurrency.RequestCodes;
import medproject.medlibrary.examination.Examination;

public class AddExaminationTask extends CustomTask {

	private final DataLoader dataLoader;
	private final AddExaminationController controller;
	
	public AddExaminationTask(DataLoader dataLoader, AddExaminationController controller) {
		super(RequestCodes.ADD_EXAMINATION_REQUEST, "Se adauga consultatia...");
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
				final Examination examination = (Examination) data;
				
				dataLoader.makeWindowRequest(new Runnable(){

					@Override
					public void run() {
						dataLoader.addExamination(examination);
						
						ExaminationWindow window = new ExaminationWindow(examination, true);
						window.show();
					}
					
				});
			
			}catch(ClassCastException e){
				GUIUtils.showErrorDialog("Add Examination Error", "Data corrupted");
			}
			
			
		}

		loadingWindow.stop();
		controller.closeWindow();
	}
}
