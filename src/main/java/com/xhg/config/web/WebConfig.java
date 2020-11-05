package com.xhg.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration					//避免和其他组件冲突(例如：swagger2) 所以优先使用 实现 WebMvcConfigurer
public class WebConfig implements WebMvcConfigurer { // 单独使用 继承 WebMvcConfigurationSupport没问题

    @Value("${upload.localUrl}")
    private String localUrl;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	     
	      registry.addResourceHandler("/boot/upload/picture/**").addResourceLocations("file:///" + localUrl + "picture");
	      registry.addResourceHandler("/upload/video/**").addResourceLocations("file:///" + localUrl + "video");
	      registry.addResourceHandler("/upload/hexo/**").addResourceLocations("file:///" + localUrl + "hexo");
	      
	      /**
	       * 只有使用swagger2原生的UI 才需要如下配置
	       */
	      //registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
	      //registry.addResourceHandler("/docs.html").addResourceLocations("classpath:/META-INF/resources/");
    	
    }
    
//	  @Override
//  protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//      super.addResourceHandlers(registry);
//      //拦截所有upload/picture开头的请求 映射为本地路径
//      registry.addResourceHandler("/upload/picture/**").addResourceLocations("file:///" + localUrl + "picture");
//      registry.addResourceHandler("/upload/video/**").addResourceLocations("file:///" + localUrl + "video");
//      
//      /**
//       * 只有使用swagger原生的UI 才需要如下配置
//       */
//      registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//      registry.addResourceHandler("/docs.html").addResourceLocations("classpath:/META-INF/resources/");
//      
//  }
    
}
