package io.udvi.rpc.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.udvi.rpc.common.RPCStatus;
import io.udvi.rpc.common.Request;
import io.udvi.rpc.common.Response;
import io.udvi.rpc.server.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DefaultExceptionHandler extends ChannelInboundHandlerAdapter {

	private final Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		
		logger.error("Exception caught", cause);

		if(cause instanceof BadRequestException){	//application layer exception.
			BadRequestException exc = (BadRequestException)cause;
			Request req = exc.context.getRequest();
			Response res= new Response();
			//copy properties
			res.setSeqNum(req.getSeqNum());
			res.setVersion(req.getVersion());
			res.setType(req.getType());
			res.setObjName(req.getObjName());
			res.setFuncName(req.getFuncName());
			
			//pass exception message to client
			res.setStatus(RPCStatus.EXCEPTION);
			res.setMsg(exc.getMessage());
			
			ctx.writeAndFlush(res);
		}else{	// unknow error
			ctx.close();
		}
	}
}