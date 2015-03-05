package io.udvi.rpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.udvi.rpc.common.codec.UdviRpcDecoder;
import io.udvi.rpc.common.codec.UdviRpcEncoder;

public class DefaultServerInitializer extends ChannelInitializer<SocketChannel> {


	public DefaultServerInitializer() {

	}

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		// Create a default pipeline implementation
		final ChannelPipeline p = ch.pipeline();

		p.addLast("decoder", new UdviRpcDecoder(true));

		p.addLast("encoder", new UdviRpcEncoder());

		p.addLast("handler", new DefaultHandler());
		
		p.addLast("httpExceptionHandler", new DefaultExceptionHandler());
	}
}