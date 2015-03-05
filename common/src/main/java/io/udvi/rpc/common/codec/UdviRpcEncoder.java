package io.udvi.rpc.common.codec;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.udvi.rpc.common.RPCContext;
import io.udvi.rpc.common.RPCSerializer;
import io.udvi.rpc.common.serializers.KryoSerializer;


public class UdviRpcEncoder extends ChannelOutboundHandlerAdapter {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
    	
    	if(msg instanceof RPCContext){
    		RPCContext rpcContext = (RPCContext)msg;
    		Object objToEncode;
    		char serializer;
    		if(rpcContext.getResponse() !=null){
    			objToEncode = rpcContext.getResponse();
    			serializer = rpcContext.getResponse().getSerializer();
    		}else{
    			objToEncode = rpcContext.getRequest();
    			serializer = rpcContext.getRequest().getSerializer();
    		}
    		
    		byte[] bytes;
			//TODO: bson and msg pack implementation still pending
    		if(serializer == RPCSerializer.KRYO){
    			bytes = KryoSerializer.write(objToEncode);
    		}else if(serializer == RPCSerializer.JSON){
    			bytes = objectMapper.writeValueAsBytes(objToEncode);
    		}else{
    			bytes = KryoSerializer.write(objToEncode);
    		}
    		
    		ByteBuf byteBuf = ctx.alloc().buffer(6+bytes.length);
    		//header
    		byteBuf.writeInt(bytes.length);
    		byteBuf.writeChar(serializer);
    		//body
    		byteBuf.writeBytes(bytes);
    		ctx.writeAndFlush(byteBuf, promise); 
    	}
    }
}