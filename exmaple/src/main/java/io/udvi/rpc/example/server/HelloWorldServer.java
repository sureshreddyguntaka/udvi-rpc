package io.udvi.rpc.example.server;

import io.udvi.rpc.server.RPCServer;

public class HelloWorldServer {

	public static void main(String[] args) throws Exception {
		new RPCServer().run();
	}
}
