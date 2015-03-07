package io.udvi.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.udvi.rpc.common.proxy.BaseObjectProxy;
import io.udvi.rpc.common.util.UdviExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RPCServer {


    private final ApplicationContext applicationContext;

    private static Config conf = ConfigFactory.load();

    private static HashMap<String,Object> objects =new HashMap<String,Object>();

    public static Config getConfig(){
        return conf;
    }

    public static Object getObject(String objName){
        return objects.get(objName);
    }

    private static UdviExecutorService threadPool;

    public static void submit(Runnable task){
        if(threadPool == null){
            synchronized (BaseObjectProxy.class) {
                if(threadPool==null){
                    LinkedBlockingDeque<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>();
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 600L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
                    threadPool = new UdviExecutorService(linkedBlockingDeque, executor,"Client async thread pool",RPCServer.getConfig().getInt("server.asyncThreadPoolSize"));
                }
            }
        }

        threadPool.submit(task);
    }

    public RPCServer() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        this(null);
    }

    @Autowired
    public RPCServer(ApplicationContext applicationContext) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        this.applicationContext = applicationContext;
        List<String> objClassList = RPCServer.getConfig().getStringList("server.objects");
        for( String objClass :objClassList){
            Object obj = null;
            if(this.applicationContext!=null){
                Class classType = Class.forName(objClass);
                obj = applicationContext.getBean(classType);
            }else{
                obj = RPCServer.class.forName(objClass).newInstance();
            }
            Class[] interfaces= obj.getClass().getInterfaces();
            for(int i =0;i<interfaces.length;i++){
                objects.put(interfaces[i].getSimpleName(), obj);
            }
        }
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(RPCServer.getConfig().getInt("server.ioThreadNum"));
        try {
            int backlog = RPCServer.getConfig().getInt("server.backlog");
            int port = RPCServer.getConfig().getInt("server.port");

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new DefaultServerInitializer())
                    .option(ChannelOption.SO_BACKLOG, backlog)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
