package medproject.medclient.netHandler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medlibrary.concurrency.Request;
import medproject.medlibrary.logging.LogWriter;

public class NetConnectionThread implements Runnable{

	private final Logger LOG = LogWriter.getLogger("DataLoader");

	private static final long INITIAL_RECONNECT_INTERVAL = 500; // 500 ms.
	private static final long MAXIMUM_RECONNECT_INTERVAL = 30000; // 30 sec.
	private static final int READ_BUFFER_SIZE = 1000000;//0x100000;
	//private static final int WRITE_BUFFER_SIZE = 65000;//0x100000;

	private long reconnectInterval = INITIAL_RECONNECT_INTERVAL;

	private ByteBuffer readBuf = ByteBuffer.allocateDirect(READ_BUFFER_SIZE); // 1Mb
	//private ByteBuffer writeBuf = ByteBuffer.allocateDirect(WRITE_BUFFER_SIZE); // 1Mb

	private SocketAddress address;

	private Selector selector;
	private SocketChannel channel;

	private AtomicLong bytesOut = new AtomicLong(0L);
	private AtomicLong bytesIn = new AtomicLong(0L);

	private final NetRead reader;
	private final NetSend sender;

	private final DataLoader dataLoader;

	private Request currentRequest = null;
	private Boolean currentRequestSent = true;
	private Boolean currentRequestCompleted = true;
	
	public NetConnectionThread(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
		reader = new NetRead(dataLoader);
		sender = new NetSend();
	}

	@PostConstruct
	public void init() {
		assert address != null: "server address missing";
	}

	public void start() throws IOException {
		LOG.info("Starting connection thread");
		connectToServer();
		Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
	}

	public void stop() {
		LOG.info("Stopping connection thread");
		selector.wakeup();
		//TODO: CLose the thread
	}

	/**
	 * Override with something meaningful
	 * @param buf
	 */
	protected void onRead(ByteBuffer buf) throws Exception{

	};

	/**
	 * Override with something meaningful
	 * @param buf
	 */
	protected void onConnected() throws Exception{
		dataLoader.setConnectionStatus(true);
	};

	/**
	 * Override with something meaningful
	 * @param buf
	 */
	protected void onDisconnected(){
		dataLoader.setConnectionStatus(false);
	};

	private void configureChannel(SocketChannel channel) throws IOException {
		channel.configureBlocking(false);
		channel.socket().setSendBufferSize(8196); // 1Mb
		channel.socket().setReceiveBufferSize(8196); // 1Mb
		channel.socket().setKeepAlive(true);
		channel.socket().setReuseAddress(true);
		channel.socket().setSoLinger(false, 0);
		channel.socket().setSoTimeout(0);
		channel.socket().setTcpNoDelay(true);
		channel.connect(address);
		channel.register(selector, SelectionKey.OP_CONNECT);
	}

	private void connectToServer() throws IOException{
		selector = Selector.open();
		channel = SocketChannel.open();
		configureChannel(channel);
	}
	@Override
	public void run() {
		try {
			if(channel.isOpen()) { // events multiplexing loop
				checkPendingRequestStatus();  
				if (selector.selectNow() > 0) processSelectedKeys(selector.selectedKeys());
			}
			else{
				onDisconnected();
				//writeBuf.clear();
				readBuf.clear();
				if (channel != null) channel.close();
				if (selector != null) selector.close();
				LOG.info("Connection closed");
				
				try {
					Thread.sleep(reconnectInterval);
					if (reconnectInterval < MAXIMUM_RECONNECT_INTERVAL) reconnectInterval *= 2;
					LOG.info("Reconnecting to " + address);
					connectToServer();
				} catch (InterruptedException e) {
					LOG.severe("Reconnection to server interrupted.");
				}
			}
		} catch (Exception e) {
			LOG.severe("Connection thread exception");
			//TODO: Refactor
		}
	}

	private void processSelectedKeys(Set<SelectionKey> keys) throws Exception {
		Iterator<SelectionKey> itr = keys.iterator();

		while (itr.hasNext()) {
			SelectionKey currentKey = (SelectionKey) itr.next();

			if (currentKey.isConnectable())		processConnect(currentKey);
			else if (currentKey.isReadable()){	
				reader.read(currentKey, readBuf, bytesIn);
			}
			else if (currentKey.isWritable()) {	
				if(currentRequestSent == false)
					if(sender.send(currentKey, currentRequest, bytesOut) == false){	
						dataLoader.makeRequest(currentRequest);
					}
					else{
						currentRequestSent = true;	
						
						if(!currentRequest.isWaitForReply())
							currentRequestCompleted = true;
					}
			}
			if (currentKey.isAcceptable()) ;

			itr.remove();
			Thread.sleep(20);
		}   
	}

	private void checkPendingRequestStatus(){
		synchronized(dataLoader.getPendingRequests()){
			SelectionKey key = channel.keyFor(selector);

			if(currentRequestCompleted && currentRequestSent && ! dataLoader.getPendingRequests().isEmpty()){
				currentRequest = dataLoader.getPendingRequests().remove(0);
				currentRequestSent = false;
				currentRequestCompleted = false;
			}
			//de jucat pe aici
			if(currentRequestSent == false)
				key.interestOps(SelectionKey.OP_CONNECT | SelectionKey.OP_WRITE);
			else{
				key.interestOps(SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
				return;
			}
		}
	}

	private void processConnect(SelectionKey key) throws Exception {
		SocketChannel ch = (SocketChannel) key.channel();
		if (ch.finishConnect()) {
			LOG.info("Connected to " + address);
			key.interestOps(SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
			reconnectInterval = INITIAL_RECONNECT_INTERVAL;
			onConnected();
		}
	}

	public void setAddress(SocketAddress address) {
		this.address = address;
	}

	public long getBytesOut() {
		return bytesOut.get();
	}

	public long getBytesIn() {
		return bytesIn.get();
	}

	public void setCurrentRequestCompleted(Boolean currentRequestCompleted) {
		this.currentRequestCompleted = currentRequestCompleted;
	}

	public Request getCurrentRequest() {
		return currentRequest;
	}
}
