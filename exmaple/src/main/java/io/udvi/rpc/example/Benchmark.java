package io.udvi.rpc.example;


import io.udvi.rpc.client.RPCClient;
import io.udvi.rpc.client.proxy.AsyncRPCCallback;
import io.udvi.rpc.client.proxy.IAsyncObjectProxy;
import io.udvi.rpc.example.async.AsyncHelloWorldCallback;
import io.udvi.rpc.example.obj.IHelloWordObj;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;



public class Benchmark {


	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		final String host = "127.0.0.1";// 192.168.0.51 127.0.0.1
		final int port = 9090;

		int threadNum = 1;
		final int requestNum = 400000;
		Thread[] threads = new Thread[threadNum];
		
		ArrayList<InetSocketAddress> serverList =new ArrayList<InetSocketAddress>();
		serverList.add(new InetSocketAddress(host, port));
		serverList.add(new InetSocketAddress(host, port));
		final IAsyncObjectProxy client = RPCClient.createAsyncObjPrx(serverList, IHelloWordObj.class);
		final AsyncHelloWorldCallback callback = new AsyncHelloWorldCallback(requestNum*threadNum);
		for (int i = 0; i < threadNum; i++) {
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < requestNum; i++) {
						client.call("hello", new Object[] { "hello world!" + i }, callback);
					}
				}
			});
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++)
			threads[i].join();
		
		synchronized (Benchmark.class) {
			Benchmark.class.wait();
		}



		System.out.print("shutdownGracefully");
		RPCClient.shutdown();
	}
	
	
	public static class AsyncHelloWorldCallback implements AsyncRPCCallback {

		AtomicLong requestCount = new AtomicLong(0);
		private long requestNum;
		private long startTime;
		
		public AsyncHelloWorldCallback(long requestNum){
			this.requestNum = requestNum;
			this.startTime = System.currentTimeMillis();
		}

		@Override
		public void fail(Exception e) {
			System.out.println("fail"+e.getMessage());
		}

		@Override
		public void success(Object result) {
			if(requestNum ==requestCount.addAndGet(1)){
				long timeCosted= System.currentTimeMillis() - startTime;
				System.out.println("total time costed:" + timeCosted + "|total requests="+requestNum+"|req/s=" + requestNum  / (double) (timeCosted / 1000));
				synchronized (Benchmark.class) {
					Benchmark.class.notify();
				}
			}
		}
	}

}
