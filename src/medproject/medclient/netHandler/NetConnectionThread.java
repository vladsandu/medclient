package medproject.medclient.netHandler;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import medproject.medclient.dataLoader.DataLoader;
import medproject.medlibrary.concurrency.Request;

public class NetConnectionThread implements Runnable{

	protected static final Logger LOG = Logger.getAnonymousLogger();
	private static final long INITIAL_RECONNECT_INTERVAL = 500; // 500 ms.
	private static final long MAXIMUM_RECONNECT_INTERVAL = 30000; // 30 sec.
	private static final int READ_BUFFER_SIZE = 1000000;//0x100000;
	private static final int WRITE_BUFFER_SIZE = 65000;//0x100000; //NU IL FOLOSESC LA NIMIC!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	private long reconnectInterval = INITIAL_RECONNECT_INTERVAL;

	private ByteBuffer readBuf = ByteBuffer.allocateDirect(READ_BUFFER_SIZE); // 1Mb
	private ByteBuffer writeBuf = ByteBuffer.allocateDirect(WRITE_BUFFER_SIZE); // 1Mb

	private final Thread thread = new Thread(this);
	private SocketAddress address;

	private Selector selector;
	private SocketChannel channel;

	private AtomicLong bytesOut = new AtomicLong(0L);
	private AtomicLong bytesIn = new AtomicLong(0L);

	private final NetRead reader;
	private final NetSend sender;

	private final DataLoader dataLoader;

	public NetConnectionThread(DataLoader dataLoader) {

		this.dataLoader = dataLoader;

		reader = new NetRead(dataLoader);
		sender = new NetSend();

		NetConnectionThread.LOG.setLevel(Level.INFO);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				LOG.info("out bytes: " + bytesOut.get());
				LOG.info("in bytes:  " + bytesIn.get());
			}
		}, 5000, 5000);

	}

	@PostConstruct
	public void init() {
		assert address != null: "server address missing";
	}

	public void start() throws IOException {
		LOG.info("starting event loop");
		thread.start();
	}

	public void join() throws InterruptedException {
		if (Thread.currentThread().getId() != thread.getId()) thread.join();
	}

	public void stop() throws IOException, InterruptedException {
		LOG.info("stopping event loop");
		thread.interrupt();
		selector.wakeup();
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

	};

	/**
	 * Override with something meaningful
	 * @param buf
	 */
	protected void onDisconnected(){

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
	}

	@Override
	public void run() {
		LOG.info("event loop running");
		try {
			while(! Thread.interrupted()) { // reconnection loop
				try {
					selector = Selector.open();
					channel = SocketChannel.open();
					configureChannel(channel);

					channel.connect(address);
					channel.register(selector, SelectionKey.OP_CONNECT);
					while(!thread.isInterrupted() && channel.isOpen()) { // events multiplexing loop
						checkPendingRequestStatus();  
						if (selector.selectNow() > 0) processSelectedKeys(selector.selectedKeys());
					}
				} catch (Exception e) {
					LOG.severe("exception");
				} finally {
					dataLoader.setConnectionStatus(false);
					onDisconnected();
					writeBuf.clear();
					readBuf.clear();
					if (channel != null) channel.close();
					if (selector != null) selector.close();
					LOG.info("connection closed");
				}

				try {
					Thread.sleep(reconnectInterval);
					if (reconnectInterval < MAXIMUM_RECONNECT_INTERVAL) reconnectInterval *= 2;
					LOG.info("reconnecting to " + address);
				} catch (InterruptedException e) {
					break;
				}
			}
		} catch (Exception e) {
			LOG.severe("unrecoverable error");
		}

		LOG.info("event loop terminated");
	}

	private void processSelectedKeys(Set<SelectionKey> keys) throws Exception {
		Iterator<SelectionKey> itr = keys.iterator();

		while (itr.hasNext()) {
			SelectionKey currentKey = (SelectionKey) itr.next();
			if (currentKey.isConnectable()) processConnect(currentKey);

			else if (currentKey.isReadable()){	
				System.out.println("ClientReadable");
				reader.read(currentKey, readBuf, bytesIn);
			}
			else if (currentKey.isWritable()) {	System.out.println("ClientWritable");
			if(dataLoader.getCurrentRequestCompleted() == false && dataLoader.getCurrentRequestSent() == false)
				if(sender.send(currentKey, dataLoader.getCurrentRequest(), bytesOut) == false){	
					dataLoader.getPendingRequests().add(dataLoader.getCurrentRequest());	    			 
				}
				else{
					dataLoader.setCurrentRequestSent(true);
					if(!dataLoader.getCurrentRequest().isWaitForReply())
						dataLoader.setCurrentRequestCompleted(true);				}
			}
			if (currentKey.isAcceptable()) ;

			itr.remove();
			Thread.sleep(20);
		}   
	}

	private void checkPendingRequestStatus(){
		synchronized(dataLoader.getPendingRequests()){
			SelectionKey key = channel.keyFor(selector);

			if(dataLoader.getCurrentRequestCompleted() && ! dataLoader.getPendingRequests().isEmpty()){
				dataLoader.setCurrentRequest(dataLoader.getPendingRequests().get(0));
				dataLoader.getPendingRequests().remove(0);
				dataLoader.setCurrentRequestCompleted(false);
				dataLoader.setCurrentRequestSent(false);
			}
			//de jucat pe aici
			if(dataLoader.getCurrentRequestCompleted() == false && dataLoader.getCurrentRequestSent() == false)
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
			LOG.info("connected to " + address);
			key.interestOps(SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
			reconnectInterval = INITIAL_RECONNECT_INTERVAL;
			dataLoader.setConnectionStatus(true);
			onConnected();
		}
	}

	public SocketAddress getAddress() {
		return address;
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

}
