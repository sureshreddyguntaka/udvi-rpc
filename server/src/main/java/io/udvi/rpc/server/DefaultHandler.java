package io.udvi.rpc.server;

import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

import io.netty.channel.SimpleChannelInboundHandler;
import io.udvi.rpc.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class DefaultHandler extends SimpleChannelInboundHandler<RPCContext> {

	private final Logger logger = LoggerFactory.getLogger(DefaultHandler.class);

	public DefaultHandler() {
		super(false);
	}

	@Override
	protected void messageReceived(final ChannelHandlerContext ctx, final RPCContext rpcContext) throws Exception {
		if(RPCServer.getConfig().getBoolean("server.async")){
			RPCServer.submit(new Runnable(){
				@Override
				public void run() {
					processRequest(ctx,rpcContext);
				}
			});
		}else{
			processRequest(ctx,rpcContext);
		}
	}	

	public void processRequest(ChannelHandlerContext ctx, RPCContext rpcContext){
		Request req = rpcContext.getRequest();
		Response res= new Response();
		
		//copy properties
		res.setSeqNum(req.getSeqNum());
		res.setVersion(req.getVersion());
		res.setType(req.getType());
		res.setSerializer(req.getSerializer());
		res.setObjName(req.getObjName());
		res.setFuncName(req.getFuncName());

		try{
			Object[] args = req.getArgs();
			Class[] argTypes = new Class[0];
			if(args != null){
				argTypes = new Class[args.length];
				String methodKey ="";
				for(int i=0;i<args.length;i++){
					argTypes[i] = args[i].getClass();
					methodKey+=argTypes[i].getSimpleName();
				}
			}

	
			Object obj= RPCServer.getObject(req.getObjName());
			Class clazz= obj.getClass();
			Method func = clazz.getMethod(req.getFuncName(), argTypes);
			Object result= func.invoke(obj, req.getArgs());
			
			if(req.getType() != RPCType.ONEWAY){
				res.setResult(result);
				res.setStatus(RPCStatus.OK);
				res.setMsg("ok");

				rpcContext.setResponse(res);
				ctx.writeAndFlush(rpcContext);
			}
			
		} catch (Exception e) {
			
			//pass exception message to client
			if(req.getType() != RPCType.ONEWAY){
				res.setStatus(RPCStatus.EXCEPTION);
				res.setMsg("excepton="+e.getClass().getSimpleName()+"|msg="+e.getMessage());
				
				rpcContext.setResponse(res);
				ctx.writeAndFlush(rpcContext);
			}
			
			logger.error("DefaultHandler|processRequest got error",e);
		}
		
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		// TODO(adolgarev): cancel submitted tasks,
		// that works only for not in progress tasks
		// if (future != null && !future.isDone()) {
		// future.cancel(true);
		// }
	}

}