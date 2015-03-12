package io.udvi.rpc.example;


import io.udvi.rpc.client.annotation.EnableRpcClient;
import io.udvi.rpc.example.obj.TestEntity;
import io.udvi.rpc.example.obj.TestInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;


/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Configuration
@EnableRpcClient
public class SpringHelloClient {

    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringHelloClient.class);
        TestInterface test = context.getBean(TestInterface.class);
        System.out.println(test.thisIsAnotherMehtod("Suresh Reddy"));
        System.out.println(test.methodWithOutParams());
        System.out.println("invoking");
        TestEntity te = test.getTestEntity();
        System.out.println(te.getClass().getCanonicalName());

    }
}
