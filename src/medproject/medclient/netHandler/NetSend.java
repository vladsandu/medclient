package medproject.medclient.netHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.logging.LogWriter;

public class NetSend {

	private Logger LOG = LogWriter.getLogger(this.getClass().getName());
	private int bytesForMessageSize = 8;
	
	boolean send(SelectionKey key, Request currentRequest, AtomicLong bytesOut) throws IOException{
		if(currentRequest == null)
			return true;
		
		ByteBuffer finalBuffer = requestToByteBuffer(currentRequest);
		
		WritableByteChannel ch = (WritableByteChannel)key.channel();
	      
	    int bytesOp = 0, bytesTotal = 0;
	    
	    while (finalBuffer.hasRemaining() && (bytesOp = ch.write(finalBuffer)) > 0) bytesTotal += bytesOp;

	    bytesOut.addAndGet(bytesTotal);
	    
	    if (finalBuffer.remaining() != 0) {
	     	return false;
	    }
	    if (bytesOp == -1) {
	      //LOG.info("peer closed write channel");
	      ch.close();
	    }
	    
	    LOG.info("Request sent");
	    return true;
	    
	}
	
	private ByteBuffer requestToByteBuffer(Request currentRequest) throws IOException{
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
		objectOutputStream.writeObject(currentRequest);
		byteOutputStream.flush();
	    
        byte[] finalMessage = new byte[byteOutputStream.size() + 8];
        
        int messageSize = byteOutputStream.size();
        
        for(int i=bytesForMessageSize - 1; i>= 0; i--){
        	if(messageSize != 0){
        		finalMessage[i] = (byte) (messageSize % 10);
        		messageSize /= 10;
        	}
        	else
        		finalMessage[i] = (byte) 0;
        }
        
        System.arraycopy(byteOutputStream.toByteArray(), 0, finalMessage, 8, byteOutputStream.size());
		final ByteBuffer finalBuffer = ByteBuffer.wrap(finalMessage);

		objectOutputStream.close();
		finalBuffer.clear();
	    
	    return finalBuffer;
	}
}
