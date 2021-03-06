package com.xhg.config.swaagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;


import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "swagger.enable", havingValue = "true")
@SuppressWarnings("all")
public class SwaggerConfig {
	
	@Bean
	public Docket ProductApi() {
	    return new Docket(DocumentationType.SWAGGER_2)
	            .genericModelSubstitutes(DeferredResult.class)
	            .useDefaultResponseMessages(false)
	            .forCodeGeneration(false)
	            .pathMapping("/")
	            .select()
	            .build()
	            .apiInfo(productApiInfo());
	}

	private ApiInfo productApiInfo() {
	    ApiInfo apiInfo = new ApiInfo("接口文档",
	            "文档描述。。。",
	            "1.0.0",
	            "API TERMS URL",
	            "联系人邮箱",
	            "license",
	            "http://localhost:9000/boot/docs.html");
	    return apiInfo;
	}
	
}
