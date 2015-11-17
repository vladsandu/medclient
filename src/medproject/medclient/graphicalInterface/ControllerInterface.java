package medproject.medclient.graphicalInterface;

import java.util.concurrent.ExecutorService;

import medproject.medclient.dataLoader.DataLoader;

public interface ControllerInterface {
	 public void init(DataLoader dataLoader, ExecutorService executor);
}
