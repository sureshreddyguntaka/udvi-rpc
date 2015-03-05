package io.udvi.rpc.common.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.udvi.rpc.common.RPCContext;
import io.udvi.rpc.common.RPCType;
import io.udvi.rpc.common.client.DefaultClientHandler;
import io.udvi.rpc.common.client.RPCFuture;

public class ObjectProxy<T> extends BaseObjectProxy<T> implements InvocationHandler,IAsyncObjectProxy {

	public ObjectProxy(ArrayList<InetSocketAddress> servers, Class<T> clazz){
		super(servers, clazz);
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		   if(Object.class  == method.getDeclaringClass()) {
		       String name = method.getName();
		       if("equals".equals(name)) {
		           return proxy == args[0];
		       } else if("hashCode".equals(name)) {
		           return System.identityHashCode(proxy);
		       } else if("toString".equals(name)) {
		           return proxy.getClass().getName() + "@" +
		               Integer.toHexString(System.identityHashCode(proxy)) +
		               ", with InvocationHandler " + this;
		       } else {
		           throw new IllegalStateException(String.valueOf(method));
		       }
		   }

		   DefaultClientHandler handler = chooseHandler();
		   long seqNum = handler.getNextSequentNumber();
		   RPCContext rpcCtx = createRequest(method.getName(), args, seqNum, RPCType.NORMAL);

		   RPCFuture rpcFuture = handler.doRPC(rpcCtx);
		   return rpcFuture.get(3000, TimeUnit.MILLISECONDS);
	}


	@Override
	public RPCFuture call(String funcName, Object[] args, AsyncRPCCallback callback){
		
		DefaultClientHandler handler = chooseHandler();
		long seqNum = handler.getNextSequentNumber();
		RPCContext rpcCtx = createRequest(funcName, args, seqNum, RPCType.ASYNC);
		
		RPCFuture rpcFuture = handler.doRPC(rpcCtx,callback);
		return rpcFuture;
	}
	
	@Override
	public RPCFuture call(String funcName, Object[] args){
		
		DefaultClientHandler handler = chooseHandler();
		long seqNum = handler.getNextSequentNumber();
		RPCContext rpcCtx = createRequest(funcName, args, seqNum, RPCType.ASYNC);

		RPCFuture rpcFuture = handler.doRPC(rpcCtx);
		return rpcFuture;
	}
	
	@Override
	public void notify(String funcName, Object[] args) {
		
		DefaultClientHandler handler = chooseHandler();
		long seqNum = handler.getNextSequentNumber();
		RPCContext rpcCtx = createRequest(funcName, args, seqNum, RPCType.ONEWAY);

		handler.doNotify(rpcCtx);
	}
	
}