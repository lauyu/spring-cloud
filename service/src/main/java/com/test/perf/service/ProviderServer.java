package com.test.perf.service;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages= {"com.test"})
//@ImportResource("classpath:jedis-single.xml")
public class ProviderServer
{
	@Value("${sys.http.maxKeepAlive:100}")
	Integer maxKeepAlive;
	
	@Value("${sys.http.keepAliveTimeout:60}")
	Integer keepAliveTimeout;
	
    public static void main( String[] args )
    {
    	System.out.println(">>>>>> service开始启动");
        SpringApplication.run(ProviderServer.class, args);
        System.out.println(">>>>>> service启动结束");
    }
    
    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        factory.addConnectorCustomizers(connector ->
                ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(maxKeepAlive));
        factory.addConnectorCustomizers(connector ->
        ((AbstractHttp11Protocol) connector.getProtocolHandler()).setKeepAliveTimeout(keepAliveTimeout));
        factory.addConnectorCustomizers(connector -> {
        	System.out.println();
        	System.out.println("MaxThreads: "+((AbstractHttp11Protocol) connector.getProtocolHandler()).getMaxThreads());
        	System.out.println("MaxConnections: "+((AbstractHttp11Protocol) connector.getProtocolHandler()).getMaxConnections());
        	System.out.println("AcceptorThreadCount: "+((AbstractHttp11Protocol) connector.getProtocolHandler()).getAcceptorThreadCount());
        	System.out.println("MaxKeepAliveRequests:"+((AbstractHttp11Protocol) connector.getProtocolHandler()).getMaxKeepAliveRequests());
        	System.out.println("KeepAliveTimeout: "+((AbstractHttp11Protocol) connector.getProtocolHandler()).getKeepAliveTimeout());
        	System.out.println();
        });
        
//        System.out.println("设置MaxKeepAliveRequests= "+maxKeepAlive);
//        System.out.println("设置KeepAliveTimeout= "+keepAliveTimeout);
        return factory;
    }

}
