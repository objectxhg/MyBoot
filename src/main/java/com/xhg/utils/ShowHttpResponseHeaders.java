package com.xhg.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Map;
import java.util.Set;

@Slf4j
public class ShowHttpResponseHeaders {

    public static void showHeaders(HttpHeaders httpHeaders){

       Map<String, String> headerMap =  httpHeaders.toSingleValueMap();

       Set<String> set =  headerMap.keySet();
        for(String name : set){
            log.info("响应头：{}={}", name, headerMap.get(name));
        }
    }

}
