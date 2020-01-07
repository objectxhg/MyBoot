package com.xhg.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Value("${upload.localUrl}")
    private String localUrl;

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        //拦截所有upload/picture开头的请求 映射为本地路径
        registry.addResourceHandler("/upload/picture/**").addResourceLocations("file:///" + localUrl + "picture");
        registry.addResourceHandler("/upload/video/**").addResourceLocations("file:///" + localUrl + "video");
        
        /**
         * 只有使用swagger原生的UI 才需要如下配置
         */
        // 解决 swagger-ui.html 404报错
        //registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // 解决 doc.html 404 报错
        //registry.addResourceHandler("/docs.html").addResourceLocations("classpath:/META-INF/resources/");
        
    }
}
