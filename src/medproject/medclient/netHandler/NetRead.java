
package medproject.medclient.netHandler;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medclient.logging.LogWriter;
import medproject.medlibrary.concurrency.Request;

public class NetRead {
	
	private final Logger LOG = LogWriter.getLogger(this.getClass().getName());
	
	DataLoader dataLoader;
	private int bytesForMessageSize = 8;
	
	private boolean finishedReading = true;
	private int currentMessageByteSize = 0;
	
	public NetRead(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}


	void read(SelectionKey key, ByteBuffer readBuf, AtomicLong bytesIn) throws Exception{
		ReadableByteChannel ch = (ReadableByteChannel)key.channel();
	    
		int bytesOp = 0, bytesTotal = 0;
	    
		if(finishedReading)
			readBuf.clear();
	    
	    while (readBuf.hasRemaining() && (bytesOp = ch.read((ByteBuffer) readBuf)) > 0) bytesTotal += bytesOp;
	    
	    if (bytesOp == -1) {
	      //LOG.info("peer closed read channel");
	      ch.close();
	      return;
	    }

	    bytesIn.addAndGet(bytesTotal);
	    
	    if(currentMessageByteSize == 0){
	    	int packetSize = 0;
		    for(int i=0; i<bytesForMessageSize; i++){
		    	packetSize = packetSize * 10 + readBuf.get(i);
			}
		    currentMessageByteSize = packetSize;
	    }
	    
	    if(currentMessageByteSize == readBuf.position() - bytesForMessageSize)
	    	finishedReading = true;
	    else
	    	finishedReading = false;
	    
	    if(finishedReading){
	    	
	    	readBuf.clear();
		    //send it to the requestHandler
		    
			byte[] packetBytes = new byte[currentMessageByteSize];
			readBuf.position(bytesForMessageSize);
			readBuf.get(packetBytes,0,packetBytes.length);		
			LOG.info("Pachet primit: " + packetBytes.length);
			 
			ByteArrayInputStream inputStream = new ByteArrayInputStream(packetBytes);
			ObjectInputStream objectStream = new ObjectInputStream(inputStream);
			   
			Request currentRequest = (Request) objectStream.readObject();
			objectStream.close();
			LOG.info("Request cerut: " + currentRequest.getREQUEST_CODE());
    
			dataLoader.processReceivedRequest(currentRequest);
			
	    	currentMessageByteSize = 0;
		}
	    else{
	    	
	    }

	}
}
