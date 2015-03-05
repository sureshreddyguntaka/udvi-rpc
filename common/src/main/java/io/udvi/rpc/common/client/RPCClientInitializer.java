package io.udvi.rpc.common.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import io.udvi.rpc.common.codec.UdviRpcDecoder;
import io.udvi.rpc.common.codec.UdviRpcEncoder;
import io.udvi.rpc.common.proxy.BaseObjectProxy;

public class RPCClientInitializer extends ChannelInitializer<SocketChannel> {

	private BaseObjectProxy objProxy;

    public  RPCClientInitializer(BaseObjectProxy objProxy){
		super();
		this.objProxy = objProxy;
	}
	
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast("decoder", new UdviRpcDecoder(false));

        p.addLast("encoder", new UdviRpcEncoder());
 
        p.addLast("handler", new DefaultClientHandler(objProxy));
    }
}