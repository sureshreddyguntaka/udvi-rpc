package io.udvi.rpc.client;

import io.udvi.rpc.client.annotation.EnableRpcClient;
import io.udvi.rpc.common.annotation.Remote;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Configuration
@ComponentScan(basePackages = {
        "io.udvi.rpc.common",
})
public class RpcClientAutoConfigurationParser implements ImportAware{

    @Autowired
    public ApplicationContext applicationContext;

    private AnnotationMetadata annotationMetadata;

    private Map<String, Object> enableRpcClientData;


    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        System.out.println("Annotation Metadata");
        Map<String, Object> enableRpcClientData = annotationMetadata.getAnnotationAttributes(EnableRpcClient.class.getName());
        System.out.println(enableRpcClientData);
        this.annotationMetadata = annotationMetadata;
        Reflections reflections = new Reflections();
        Set<Class<?>> classes =  reflections.getTypesAnnotatedWith(Remote.class, true);
        for(Class<?> clazz : classes ){
            if(clazz.isInterface()){
                ((DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory()).registerSingleton(clazz.getCanonicalName(), RPCClient.createObjectProxy(/*(String) enableRpcClientData.get("remoteHost"), (Integer) enableRpcClientData.get("remotePort"),*/ clazz));

            }else{
                //TODO: warn users that it is scanning classes as well

            }
        }
    }
}
