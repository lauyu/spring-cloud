package com.test.perf.api;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 *
 */
@EnableCircuitBreaker
@SpringBootApplication(scanBasePackages= {"com.test"})
public class ApiServer
{

    public static void main( String[] args )
    {
    	System.out.println(">>>>>> 灰度服务开始启动");
        SpringApplication.run(ApiServer.class, args);
//        ProducerManager pm = (ProducerManager) ContextUtils.getBean("producerManager");
//		pm.init();
//        BaseConfig baseConfig = (BaseConfig) ContextUtils.getBean("baseConfig");
//		logger.info("-- this Release is {}",baseConfig.getRelease());
//		if(!"dev".equals(baseConfig.getRelease())){
//		    TransManager transManager = (TransManager) ContextUtils.getBean("transManager");
//		    PushMsgManager pushMsgManager = (PushMsgManager) ContextUtils.getBean("pushMsgManager");
//		    ReportMsgManager reportMsgManager = (ReportMsgManager) ContextUtils.getBean("reportMsgManager");
//			ProducerManager producerManager = (ProducerManager)ContextUtils.getBean("producerManager");
//		    transManager.initTrans("trans.producer");
//		    pushMsgManager.initSmartProducer("trans.producer");
//		    reportMsgManager.initSmartProducer("trans.producer");
//			producerManager.initSmartProducer("");
//		}
//		producerManager.initSmartProducer("kafka.producer");

        Converter
        
        System.out.println(">>>>>> api启动结束");
    }
    
//    @Bean
//    public EmbeddedServletContainerFactory servletContainerFactory() {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//
//        factory.addConnectorCustomizers(connector ->
//                ((AbstractHttp11Protocol) connector.getProtocolHandler()).setMaxKeepAliveRequests(walletConfig.getMaxKeepAliveRequests()));
//        factory.addConnectorCustomizers(connector ->
//        ((AbstractHttp11Protocol) connector.getProtocolHandler()).setKeepAliveTimeout(walletConfig.getKeepAliveTimeout()));
//        System.out.println("设置MaxKeepAliveRequests= "+walletConfig.getMaxKeepAliveRequests());
//        System.out.println("设置KeepAliveTimeout= "+walletConfig.getKeepAliveTimeout());
//        return factory;
//    }

}
