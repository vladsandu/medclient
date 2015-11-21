package medproject.medclient.dataLoader;

public class InitialLoader {

	private final DataLoader dataLoader;
	
	public volatile boolean pacientsLoaded = false;
	
	public InitialLoader(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	private void checkLoadingStatus(){
		
	}
	
	public void setPacientsLoaded(boolean pacientsLoaded) {
		this.pacientsLoaded = pacientsLoaded;
	}
}
