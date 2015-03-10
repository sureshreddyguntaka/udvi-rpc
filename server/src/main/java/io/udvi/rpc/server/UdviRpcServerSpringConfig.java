package io.udvi.rpc.server;

import io.udvi.rpc.common.client.RPCClient;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Configuration(value = "serverConfig")
@ComponentScan(basePackages = {
        "io.udvi.rpc.server.exception",
        "io.udvi.rpc.server",

})
public class UdviRpcServerSpringConfig {


}
