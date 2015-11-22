package medproject.medclient.dataLoader;

import medproject.medclient.graphicalInterface.mainWindow.Navigator;

public class InitialLoader {

	private final DataLoader dataLoader;

	public volatile boolean pacientsLoaded = false;

	public InitialLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}


	public void loadData(){
		dataLoader.makeWindowRequest(new Runnable(){

			@Override
			public void run() {
				Navigator.loadScene(Navigator.LOADING_SCENE);
			}

		});
		
		dataLoader.getPatientLoader().loadPatientList();
	}

	private void checkLoadingStatus(){

	}

	public void setPacientsLoaded(boolean pacientsLoaded) {
		this.pacientsLoaded = pacientsLoaded;
	}
}
