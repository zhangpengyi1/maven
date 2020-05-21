package com.zxs.common.utils;

import static com.zxs.common.utils.HttpClientUtils.CONNECT_TIMEOUT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpDownloadUtils {

    /**
     * 从网络Url中下载文件
     *
     * @param urlStr
     * @throws IOException
     */
    public static File downLoadFromUrl(String userName, String pswd, String urlStr, String fileName, String savePath) throws IOException {
        InputStream inputStream = null;
        if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(pswd)) {
            // ------------ 该网页需要认证（用户名、密码） ------------
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get=new HttpGet(urlStr);
            HttpClientContext context=new HttpClientContext();
            CredentialsProvider credentialsProvider=new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, pswd));
            context.setCredentialsProvider(credentialsProvider);
            CloseableHttpResponse execute = client.execute(get, context);
            HttpEntity entity = execute.getEntity();
            inputStream = entity.getContent();
        } else {
            // ------------ 不需要 用户名、密码的普通下载 ------------
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            inputStream = conn.getInputStream();
        }
        // 获取自己数组
        byte[] getData = readInputStream(inputStream);
        // 文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        return file;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static void main(String[] args) {
        String userName = "625618553@qq.com";
        String pswd = "xiao73649482";
        String url = "https://mail.qq.com/";
        String savepath = "/Users/v_zhangpengyi/Desktop/templates";
        try {
            downLoadFromUrl(userName, pswd, url, "1.txt", savepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
