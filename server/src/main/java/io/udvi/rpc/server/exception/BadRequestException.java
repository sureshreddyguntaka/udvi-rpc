package io.udvi.rpc.server.exception;


import io.udvi.rpc.common.RPCContext;

public class BadRequestException extends Exception {

	public RPCContext context;
	private static final long serialVersionUID = 8166629097983704842L;

	public BadRequestException(RPCContext context, Throwable cause) {
		super(cause);
		this.context = context;
	}
}