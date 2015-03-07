package io.udvi.rpc.example;

import io.udvi.rpc.server.RPCServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sureshreddyguntaka on 07/03/15.
 */
@Configuration
@ComponentScan(basePackages = {
        "io.udvi.rpc.server",
        "io.udvi.rpc.example.obj"
    })
public class SpringHelloWorldServer {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(SpringHelloWorldServer.class);
        RPCServer server = context.getBean(RPCServer.class);
        try {
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
