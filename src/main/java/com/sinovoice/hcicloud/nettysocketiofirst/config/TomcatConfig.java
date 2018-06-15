package com.sinovoice.hcicloud.nettysocketiofirst.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;


/**
 * 使用tomcat配置
 *
 * @version
 * @author
 *
 */
@Configuration
public class TomcatConfig {
    @Value("${spring.server.port}")
    private String port;
    @Value("${spring.server.connectionTimeout}")
    private String connectionTimeout;
    @Value("${spring.server.acceptorThreadCount}")
    private String acceptorThreadCount;
    @Value("${spring.server.minSpareThreads}")
    private String minSpareThreads;
    @Value("${spring.server.maxSpareThreads}")
    private String maxSpareThreads;
    @Value("${spring.server.maxThreads}")
    private String maxThreads;
    @Value("${spring.server.maxConnections}")
    private String maxConnections;
    @Value("${spring.server.protocol.tomcat}")
    private String protocol;


    //文件请求大小
    @Value("${spring.server.MaxFileSize}")
    private String MaxFileSize;
    @Value("${spring.server.MaxRequestSize}")
    private String MaxRequestSize;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(new GwsTomcatConnectionCustomizer());
        return tomcat;
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize(MaxFileSize); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize(MaxRequestSize);
        return factory.createMultipartConfig();
    }

    /**
     *
     * 默认http连接
     *
     * @version
     * @author liuyi  2016年7月20日 下午7:59:41
     *
     */
    public class GwsTomcatConnectionCustomizer implements TomcatConnectorCustomizer {

        public GwsTomcatConnectionCustomizer() {
        }

        @Override
        public void customize(Connector connector) {
            connector.setPort(Integer.valueOf(port));
            connector.setAttribute("connectionTimeout", connectionTimeout);
            connector.setAttribute("acceptorThreadCount", acceptorThreadCount);
            connector.setAttribute("minSpareThreads", minSpareThreads);
            connector.setAttribute("maxSpareThreads", maxSpareThreads);
            connector.setAttribute("maxThreads", maxThreads);
            connector.setAttribute("maxConnections", maxConnections);
            connector.setAttribute("protocol", protocol);

        }
    }
}
