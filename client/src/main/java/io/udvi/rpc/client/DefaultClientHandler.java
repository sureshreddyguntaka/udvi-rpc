package io.udvi.rpc.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.udvi.rpc.client.proxy.AsyncRPCCallback;
import io.udvi.rpc.client.proxy.BaseObjectProxy;
import io.udvi.rpc.common.RPCContext;
import io.udvi.rpc.common.RPCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class DefaultClientHandler extends SimpleChannelInboundHandler<RPCContext>{

	private final Logger logger = LoggerFactory.getLogger(BaseObjectProxy.class);
    
    private ConcurrentHashMap<Long, RPCFuture> pendingRPC = new ConcurrentHashMap<Long, RPCFuture>();

    private AtomicLong seqNumGenerator = new AtomicLong(0);
		
	public long getNextSequentNumber(){
		return seqNumGenerator.getAndAdd(1);
	}
	
    private volatile Channel channel;
    
    private BaseObjectProxy  objProxy;

	private SocketAddress remotePeer;
	
	public SocketAddress getRemotePeer(){
		return remotePeer;
	}
    
    
 
	public DefaultClientHandler(BaseObjectProxy objProxy) {
		this.objProxy = objProxy;
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext arg0, RPCContext rpcCtx)
			throws Exception {
		RPCFuture rpcFuture = pendingRPC.get(rpcCtx.getResponse().getSeqNum());
		
		if(rpcFuture != null){
			pendingRPC.remove(rpcCtx.getResponse().getSeqNum());
			rpcFuture.done(rpcCtx.getResponse());
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		channel = ctx.channel();
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.remotePeer = channel.remoteAddress();
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		super.exceptionCaught(ctx, cause);
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
        objProxy.doReconnect(ctx.channel(), remotePeer);
	}
	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		objProxy.doReconnect(ctx.channel(), remotePeer);
	}
	

	
	public RPCFuture doRPC(RPCContext rpcCtx){
		return doRPC(rpcCtx,null);
	}
	
	
	public RPCFuture doRPC(RPCContext rpcCtx, AsyncRPCCallback callback){
		if(rpcCtx.getRequest().getType() == RPCType.ONEWAY){
			channel.writeAndFlush(rpcCtx);
			return null;
		} else {
			RPCFuture rpcFuture = new RPCFuture(rpcCtx, this, callback);
			pendingRPC.put(rpcCtx.getRequest().getSeqNum(), rpcFuture);
			channel.writeAndFlush(rpcCtx);
			return rpcFuture;
		}

	}

	public void doNotify(RPCContext rpcCtx){
		channel.writeAndFlush(rpcCtx);
	}
}