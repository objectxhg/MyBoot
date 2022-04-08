package com.xhg.config.web;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Author xiaoh
 * @create 2020/12/17 10:58
 */
@Configuration
public class WebServerConfig {

    @Bean
    public RestTemplate RestTemplateUtil() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //连接超时
        factory.setConnectTimeout(15000);
        //读取超时
        factory.setReadTimeout(20000);
        return new RestTemplate(factory);
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                connector.setProperty("relaxedQueryChars", "|{}[]");
            }
        });
        return factory;
    }

}