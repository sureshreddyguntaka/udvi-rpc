package io.udvi.rpc.client.annotation;

import io.udvi.rpc.client.RpcClientAutoConfigurationParser;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RpcClientAutoConfigurationParser.class)
public @interface EnableRpcClient {
    String remoteHost() default "localhost";
    int remotePort() default 9090;
    String configFile() default "application.conf";
}
