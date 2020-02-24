package com.example.image.utils;

import com.example.image.exception.MyException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouxin
 * @description http工具
 */
public class HttpUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static String postByForm(String url, Map<String, String> heads, Map<String, String> params) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        //消息头设置
        if (heads == null) {
            heads = new HashMap<>();
        }
        if (!heads.containsKey("Content-Type")) {
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        for (String key : heads.keySet()) {
            post.addHeader(key, heads.get(key));
        }
        //消息body设置
        if (params != null) {
            List<NameValuePair> body = new ArrayList<>();
            for (String key : params.keySet()) {
                body.add(new BasicNameValuePair(key, params.get(key)));
            }
            UrlEncodedFormEntity entity = null;
            try {
                entity = new UrlEncodedFormEntity(body, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new MyException("http调用生成消息体失败", e);
            }
            post.setEntity(entity);
        }

        BufferedReader bf = null;
        try {
            CloseableHttpResponse response = client.execute(post);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            bf = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuffer json = new StringBuffer();
            String line = null;
            while ((line = bf.readLine()) != null) {
                json.append(line);
            }

            if (status.getStatusCode() == HttpStatus.SC_OK) {
//				log.info(json.toString());
            } else {
                log.error("http调用失败，返回码:" + status.getStatusCode());
            }
            return json.toString();
        } catch (Exception e) {
            throw new MyException("http调用失败", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String httpReturnJson(String url, Map<String, String> heads) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        //消息头设置
        if (heads == null) {
            heads = new HashMap<>();
        }
        for (String key : heads.keySet()) {
            get.addHeader(key, heads.get(key));
        }
        BufferedReader bf = null;
        try {
            CloseableHttpResponse response = client.execute(get);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            bf = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuffer json = new StringBuffer();
            String line = null;
            while ((line = bf.readLine()) != null) {
                json.append(line);
            }

            if (status.getStatusCode() == HttpStatus.SC_OK) {
//				log.info(json.toString());
            } else {
                log.error("http调用失败，返回码:" + status.getStatusCode());
            }
            return json.toString();
        } catch (Exception e) {
            throw new MyException("http调用失败", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String httpReturnJson404Pass(String url, Map<String, String> heads) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        //消息头设置
        if (heads == null) {
            heads = new HashMap<>();
        }
        for (String key : heads.keySet()) {
            get.addHeader(key, heads.get(key));
        }
        BufferedReader bf = null;
        try {
            CloseableHttpResponse response = client.execute(get);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            bf = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuffer json = new StringBuffer();
            String line = null;
            while ((line = bf.readLine()) != null) {
                json.append(line);
            }

            //维易淘店铺接口频繁刷新可能返回404
            if (status.getStatusCode() == HttpStatus.SC_OK) {
//				log.info(json.toString());
            } else if (status.getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                log.error("维易淘接口频繁，店铺信息获取失败-", json.toString());
                return null;
            } else {
                log.error("http调用失败，返回码:" + status.getStatusCode());
            }
            return json.toString();
        } catch (Exception e) {
            throw new MyException("http调用失败", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getStr(String url, Map<String, String> heads, String body) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        //消息头设置
        if (heads == null) {
            heads = new HashMap<>();
        }
        if (!heads.containsKey("Content-Type")) {
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        for (String key : heads.keySet()) {
            post.addHeader(key, heads.get(key));
        }

        HttpEntity bodyEntity = new StringEntity(body, "UTF-8");
        post.setEntity(bodyEntity);

        BufferedReader bf = null;
        try {
            CloseableHttpResponse response = client.execute(post);
            StatusLine status = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            bf = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            StringBuffer str = new StringBuffer();
            String line = null;
            while ((line = bf.readLine()) != null) {
                str.append(line);
            }

            if (status.getStatusCode() == HttpStatus.SC_OK) {
//				log.info(str.toString());
            } else {
                log.error("http调用失败，返回码:" + status.getStatusCode());
            }
            return str.toString();
        } catch (Exception e) {
            throw new MyException("http调用失败", e);
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
