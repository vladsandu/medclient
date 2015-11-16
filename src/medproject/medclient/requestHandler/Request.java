package medproject.medclient.requestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Request implements Serializable{
	
		/**
	 * 
	 */
	private static final long serialVersionUID = 4049178970664507569L;
	
	private int REQUEST_CODE;
	private final Object data;
	private final int REQUEST_STATUS;
	private boolean waitForReply = true;
	
	public Request(int REQUEST_CODE, Object data, int REQUEST_STATUS) {
		this.REQUEST_CODE = REQUEST_CODE;
		this.data = data;
		this.REQUEST_STATUS = REQUEST_STATUS;
	}
	
	public int getREQUEST_CODE() {
		return REQUEST_CODE;
	}

	public void setREQUEST_CODE(int rEQUEST_CODE) {
		REQUEST_CODE = rEQUEST_CODE;
	}

	public Object getData() {
		return data;
	}
	
	public int getREQUEST_STATUS() {
		return REQUEST_STATUS;
	}


	public boolean isWaitForReply() {
		return waitForReply;
	}

	public void setWaitForReply(boolean waitForReply) {
		this.waitForReply = waitForReply;
	}

	private void readObject(
		     ObjectInputStream aInputStream
		   ) throws ClassNotFoundException, IOException {
		     //always perform the default de-serialization first
		     aInputStream.defaultReadObject();

		     //ensure that object state has not been corrupted or tampered with maliciously
		   //  validateState();
		  }
	
	private void writeObject(
		      ObjectOutputStream aOutputStream
		    ) throws IOException {
		      //perform the default serialization for all non-transient, non-static fields
		      aOutputStream.defaultWriteObject();
		    }
}
