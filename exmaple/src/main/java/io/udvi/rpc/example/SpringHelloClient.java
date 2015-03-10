package io.udvi.rpc.example;


import io.udvi.rpc.client.annotation.EnableRpcClient;
import io.udvi.rpc.example.obj.TestInterface;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


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
        long t = System.currentTimeMillis();
        for(int i=0; i<=1000000; i++){
            test.methodWithOurReturnAndParams();
        }
        long total = (System.currentTimeMillis() - t);
        System.out.println("total time costed:" + total	+ "|req/s=" + 100000  / (long) (total / 1000));

    }
}
