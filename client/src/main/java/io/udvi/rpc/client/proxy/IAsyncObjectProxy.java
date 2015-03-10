package io.udvi.rpc.client.proxy;


import io.udvi.rpc.client.RPCFuture;

public interface IAsyncObjectProxy {
	public RPCFuture call(String funcName, Object[] args, AsyncRPCCallback callback);
	public RPCFuture call(String funcName, Object[] args);
	void notify(String funcName, Object[] args);
}