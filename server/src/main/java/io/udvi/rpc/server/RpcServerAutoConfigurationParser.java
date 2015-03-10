package io.udvi.rpc.server;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;


/**
 * Created by sureshreddyguntaka on 10/03/15.
 */
@Configuration(value = "serverConfig")
@ComponentScan(basePackages = {
        "io.udvi.rpc.server.exception",
        "io.udvi.rpc.server",

})
public class RpcServerAutoConfigurationParser implements ImportAware{


        @Override
        public void setImportMetadata(AnnotationMetadata annotationMetadata) {

        }
}
