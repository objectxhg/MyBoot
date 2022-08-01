package com.xhg.utils;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

/**
 * Created by yuanjie on 2017/11/9.
 */
public class HttpClientUtil {

    private static final String DEFAULT_CHARSET = "UTF-8";

    private static final String TLS = "TLS";

    // 设置请求超时，默认30秒
    private static final int SO_TIMEOUT = 30000;

    // 设置连接超时时间，单位毫秒，默认30秒
    private static final int CONNECTION_TIMEOUT = 30000;

    // 设置从connect Manager获取Connection 超时时间，单位毫秒。这个属性是新加的属性，因为目前版本是可以共享连接池的
    private static final int CONNECTION_REQUEST_TIMEOUT = 30000;

    // 设置文件上传来连接超时时间，单位毫秒，默认120秒
    private static final int FILEUPLOAD_CONNECTION_TIMEOUT = 120000;

    // 设置从connect Manager获取Connection 超时时间，单位毫秒。
    private static final int FILEUPLOAD_CONNECTION_REQUEST_TIMEOUT = 120000;

    // 客户端链接默认超时时间，单位毫秒。
    private static final Long HOST_KEEPALIVE_STRATEGY_TIMEOUT = 30000L;

    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;
    private static ConnectionKeepAliveStrategy defaultKeepAliveStrategy = null;

    static {
        try {
            defaultKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
                public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                    return HOST_KEEPALIVE_STRATEGY_TIMEOUT;
                }
            };

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", sslsf).build();

            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            httpclient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(defaultKeepAliveStrategy).build();
            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(CONNECTION_TIMEOUT).setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                    .build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(500);
            connManager.setDefaultMaxPerRoute(50);
        } catch (KeyManagementException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (GeneralSecurityException e) {
        }
    }
    private static ConnectionKeepAliveStrategy getCustomKeepAliveStrategy() {

        return new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException e) {
                        }
                    }
                }

                HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
                String targetName = target.getHostName();
                return HOST_KEEPALIVE_STRATEGY_TIMEOUT;
            }
        };
    }


    public static String httpPostWithFile(String url, List<NameValuePair> headList, Map<String, Object> params,
                                          Map<String, String> urlParam) {
        final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setKeepAliveStrategy(getCustomKeepAliveStrategy())
                .build();

        HttpEntity entity = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        long start = System.currentTimeMillis();

        try {
            String processedUrl = connectUrlParam(url, urlParam);

            httpPost = new HttpPost(processedUrl);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(FILEUPLOAD_CONNECTION_TIMEOUT)
                    .setSocketTimeout(FILEUPLOAD_CONNECTION_REQUEST_TIMEOUT)
                    .build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            if (headList != null && headList.size() > 0) {
                for (NameValuePair head : headList) {
                    httpPost.addHeader(head.getName(), head.getValue());
                }
            }
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            multipartEntityBuilder.setCharset(Charset.forName(DEFAULT_CHARSET));
            multipartEntityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
            for (Map.Entry entry : params.entrySet()) {
                if (entry.getValue() instanceof File) {
                    FileBody bin = new FileBody((File) entry.getValue());
                    multipartEntityBuilder.addPart(String.valueOf(entry.getKey()), bin).setCharset(Charset.forName("UTF-8"));
                } else {
                    multipartEntityBuilder.addTextBody(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())).setCharset(Charset.forName("UTF-8"));
                }

            }
            httpPost.setEntity(multipartEntityBuilder.build());
            response = httpclient.execute(httpPost);

            String charset = DEFAULT_CHARSET;
            entity = response.getEntity();
            String respStr = EntityUtils.toString(entity, charset);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new Exception();
            }
            if (statusCode != HttpStatus.SC_OK) {
                throw new Exception(
                        String.format("request url: %s, response status error, statusCode: %s",
                                processedUrl, statusCode));
            }
            return respStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(entity, response, httpPost);
        }
        return null;
    }

    private static String connectUrlParam(String url, Map<String, String> urlParam) throws UnsupportedEncodingException {
        if (urlParam == null || urlParam.size() == 0) {
            return url;
        }
        StringBuilder connectParam = new StringBuilder();
        Boolean paramFlag = false;
        if (url.contains("?")) {
            paramFlag = true;
        }
        if (paramFlag) {
            connectParam.append("&");
        } else {
            connectParam.append("?");
        }
        for (Map.Entry<String, String> entry : urlParam.entrySet()) {
            connectParam.append(entry.getKey());
            connectParam.append("=");
            connectParam.append(URLEncoder.encode(entry.getValue(), DEFAULT_CHARSET));
            connectParam.append("&");
        }
        connectParam.deleteCharAt(connectParam.length() - 1);
        return url + connectParam;
    }

    private static void closeConnection(HttpEntity entity, CloseableHttpResponse response, HttpPost httpPost) {
        try {
            if (entity != null) {
                entity.getContent().close();
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
        }
        if (httpPost != null) {
            httpPost.releaseConnection();
        }
    }

    public static String httpGet(String url, List<NameValuePair> headList, Map<String, String> urlParam) {


        final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setKeepAliveStrategy(getCustomKeepAliveStrategy())
                .build();
        HttpEntity entity = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        long start = System.currentTimeMillis();
        try {
            String processedUrl = connectUrlParam(url, urlParam);
            httpGet = new HttpGet(processedUrl);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .build();// 设置请求和传输超时时间
            httpGet.setConfig(requestConfig);
            if (headList != null && headList.size() > 0) {
                for (NameValuePair head : headList) {
                    httpGet.addHeader(head.getName(), head.getValue());
                }
            }

            response = httpclient.execute(httpGet);
            entity = response.getEntity();
            String respStr = EntityUtils.toString(entity, DEFAULT_CHARSET);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new Exception();
            }
            if (statusCode != HttpStatus.SC_OK) {
                throw new RemoteException(
                        String.format("request url: %s, response status error, statusCode: %s",
                                processedUrl, statusCode));
            }
            return respStr;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection(entity, response, httpGet);
        }
        return null;
    }


    private static void closeConnection(HttpEntity entity, CloseableHttpResponse response, HttpGet httpGet) {
        try {
            if (entity != null) {
                entity.getContent().close();
            }
            if (response != null) {
                response.close();
            }
        } catch (Exception e) {
        }
        if (httpGet != null) {
            httpGet.releaseConnection();
        }
    }

    public static String httpPost(String url, List<NameValuePair> headList, String bodyParam, ContentType contentType) {
        return httpPost(url, headList, bodyParam, null, contentType, true);
    }

    public static String httpPost(String url, List<NameValuePair> headList, String bodyParam,
                                  Map<String, String> urlParam, ContentType contentType, boolean printParam) {


        final CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setKeepAliveStrategy(getCustomKeepAliveStrategy())
                .build();

        HttpEntity entity = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        long start = System.currentTimeMillis();

        try {
            String processedUrl = connectUrlParam(url, urlParam);

            httpPost = new HttpPost(processedUrl);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(CONNECTION_TIMEOUT)
                    .setConnectTimeout(CONNECTION_REQUEST_TIMEOUT)
                    .build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
            if (headList != null && headList.size() >0) {
                for (NameValuePair head : headList) {
                    httpPost.addHeader(head.getName(), head.getValue());
                }
            }
            httpPost.setEntity(new StringEntity(bodyParam, contentType));


            response = httpclient.execute(httpPost);

            String charset = DEFAULT_CHARSET;
            if (null != contentType.getCharset()) {
                charset = contentType.getCharset().name();
            }
            entity = response.getEntity();
            String respStr = EntityUtils.toString(entity, charset);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                throw new Exception();
            }
            if (statusCode != HttpStatus.SC_OK) {
                throw new RemoteException(
                        String.format("request url: %s, response status error, statusCode: %s",
                                processedUrl, statusCode));
            }
            return respStr;
        } catch (Exception e) {
        } finally {
            closeConnection(entity, response, httpPost);
        }
        return null;
    }
}
