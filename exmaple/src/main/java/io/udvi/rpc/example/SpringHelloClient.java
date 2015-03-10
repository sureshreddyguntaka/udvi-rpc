package io.udvi.rpc.example;

import io.udvi.rpc.common.client.RPCClient;
import io.udvi.rpc.example.obj.TestInterface;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Configuration
@ComponentScan(basePackages = {
        "io.udvi.rpc.common"
})
public class SpringHelloClient {

    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringHelloClient.class);
        ((DefaultListableBeanFactory)context.getAutowireCapableBeanFactory()).registerSingleton("testInterfaceImpl",RPCClient.createObjectProxy(TestInterface.class));
        TestInterface test = context.getBean(TestInterface.class);
        System.out.println(test.thisIsAnotherMehtod("Suresh Reddy"));
        System.out.println(test.methodWithOutParams());

    }
}
