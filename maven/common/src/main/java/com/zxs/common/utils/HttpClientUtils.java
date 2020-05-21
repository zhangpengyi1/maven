package com.zxs.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

/**
 * @author dxyun.com
 * @date 2018-03-14
 */
public class HttpClientUtils {
    // 连接上一个url，获取response的返回等待时间
    public static final int SOCKET_TIMEOUT = 20 * 1000;
    // 连接一个url的连接等待时间
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    private static Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    // 默认编码UTF8
    private static String CHARSET = "UTF-8";

    /**
     * POST请求
     */
    public static String doPost(String url, String body) {
        return doPost(url, null, body, CHARSET, null, null);
    }

    /**
     * POST请求
     */
    public static String doPost(String url, String mimeType, String body) {
        return doPost(url, mimeType, body, CHARSET, null, null);
    }

    /**
     * POST请求，body为json格式
     */
    public static String doPost(String url, String mimeType, String body, String charset, SSLContext sslcontext,
                                Map<String, String> headers) {
        FileInputStream instream = null;
        CloseableHttpClient httpClient = null;
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            // SSL证书
            if (sslcontext != null) {
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
                builder.setSSLSocketFactory(sslsf);
            }
            httpClient = builder.build();
            HttpPost httpPost = new HttpPost(url);

            // 构造RequestConfig
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);

            // 添加ContentType
            ContentType contentType = ContentType.create(mimeType, charset);
            StringEntity entity = new StringEntity(body, contentType);
            entity.setContentType(mimeType);
            httpPost.setEntity(entity);

            // 添加Header
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key, headers.get(key));
                }
            }

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                //网络异常处理
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() != 200) {
                    LOGGER.error("请求异常，POST url:" + url);
                    LOGGER.error("请求异常，POST " + statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                    return null;
                }
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    return EntityUtils.toString(resEntity, charset);
                }
            }
            return null;
        } catch (IOException | RuntimeException e) {
            LOGGER.error("网络请求异常，url:" + url + " charset: " + charset + " message:" + e.getMessage(), e);
            return null;
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException | RuntimeException e) {
                LOGGER.error("关闭失败 " + e);
            }
        }
    }

    /**
     * UTF8编码
     */
    public static String doPost(String url, Map<String, String> map) {
        return doPost(url, map, CHARSET, null, null);
    }

    /**
     * POST请求，body非json格式
     */
    public static String doPost(String url, Map<String, String> body, String charset, SSLContext sslcontext,
                                Map<String, String> headers) {
        String logId = "";
        CloseableHttpClient httpClient = null;
        String result = null;
        try {

            HttpClientBuilder builder = HttpClientBuilder.create();
            // SSL证书
            if (sslcontext != null) {
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
                builder.setSSLSocketFactory(sslsf);
            }
            httpClient = builder.build();
            HttpPost httpPost = new HttpPost(url);

            // 构造RequestConfig
            RequestConfig requestConfig =
                    RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
            httpPost.setConfig(requestConfig);

            // 设置参数
            List<NameValuePair> list = new ArrayList<>();
            Iterator<Entry<String, String>> iterator = body.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }

            // 添加Header
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key, headers.get(key));
                }
            }

            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error(logId, "网络请求异常，url:{},charset:{},message:{}", url, charset, e.getMessage(), e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException | RuntimeException e) {
                LOGGER.error(logId, "关闭失败", e);
            }
        }
        return result;
    }

    /**
     * GET请求
     */
    public static String doGet(String url, String charset, Map<String, String> headers) {
        String logId = "";
        CloseableHttpClient httpClient = null;
        String result = null;
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            httpClient = builder.build();
            HttpGet httpGet = new HttpGet(url);

            // 构造RequestConfig
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接上一个url，获取response的返回等待时间
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    // 连接一个url的连接等待时间
                    .setConnectTimeout(CONNECT_TIMEOUT).build();
            httpGet.setConfig(requestConfig);

            // 添加Header
            if (headers != null && headers.size() > 0) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error(logId, "网络请求异常，url:{},charset:{},message:{}", url, charset, e.getMessage(), e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException | RuntimeException e) {
                LOGGER.error(logId, "关闭失败", e);
            }
        }
        return result;
    }

    public static String doGet(String url) {
        return doGet(url, CHARSET, null);
    }

    public static SSLContext getPkcs12SSLContext(String certPath, char[] password) {
        String logId = "SSLContext";
        FileInputStream instream = null;
        try {
            //读取证书
            LOGGER.info(logId, "读取SSL证书:{}", certPath);
            instream = new FileInputStream(new File(certPath));
            //加载证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(instream, password);
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password).build();
            return sslcontext;
        } catch (KeyStoreException | IOException | KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | CertificateException e) {
            LOGGER.error(logId, "SSL证书加载失败，message:{}", e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                LOGGER.error(logId, "流关闭失败");
            }
        }
        return null;
    }

    public static String getInputStreamToFile(String url, String charset, String fileName) {
        String logId = "";
        CloseableHttpClient httpClient = null;
        InputStream result = null;
        FileOutputStream downloadFile = null;
        String fileType = "";
        try {
            HttpClientBuilder builder = HttpClientBuilder.create();
            httpClient = builder.build();
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接上一个url，获取response的返回等待时间
                    .setSocketTimeout(SOCKET_TIMEOUT)
                    // 连接一个url的连接等待时间
                    .setConnectTimeout(CONNECT_TIMEOUT).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet);
            String value = response.getFirstHeader("Content-Disposition").getValue();

            fileType = value.substring(value.indexOf("."), value.length());
            if (response != null) {
                HttpEntity resEntity = response.getEntity();

                if (resEntity != null) {
                    result = resEntity.getContent();
                }
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            if (null != result) {
                while (-1 != (n = result.read(buffer))) {
                    output.write(buffer, 0, n);
                }
            }
            byte[] bytes1 = output.toByteArray();
            BASE64Encoder base64Encoder = new BASE64Encoder();
            String encode = base64Encoder.encode(bytes1);

            // 写入文件
            int index;
            byte[] bytes = new byte[1024 * 1024];
            downloadFile = new FileOutputStream(fileName + fileType);

            if (null != result) {
                while ((index = result.read(bytes)) != -1) {
                    downloadFile.write(bytes, 0, index);
                    downloadFile.flush();
                }
            }

        } catch (IOException | RuntimeException e) {
            LOGGER.error(logId, "网络请求异常,url:{},charset:{},message:{}", url, charset, e.getMessage(), e);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (downloadFile != null) {
                    downloadFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileType;
    }

}