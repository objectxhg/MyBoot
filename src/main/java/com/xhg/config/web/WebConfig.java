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

    }
}
