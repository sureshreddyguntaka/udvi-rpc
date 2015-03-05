package io.udvi.rpc.common.proxy;

public interface AsyncRPCCallback {

	public void success(Object result);
	
	public void fail(Exception e);
	
}