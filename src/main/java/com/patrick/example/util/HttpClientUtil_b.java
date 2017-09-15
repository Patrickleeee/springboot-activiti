package com.patrick.example.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.Date;

/**
 * Desciption 发送方-发送请求-工具类
 * Create By  li.bo
 * CreateTime 2017/6/27 17:04
 * UpdateTime 2017/6/27 17:04
 */
public class HttpClientUtil_b {

    /**
     * get请求方式
     *
     * @return
     * @throws Exception
     */
    public static String doGet(String url) throws Exception {
        //创建默认的httpClient实例
        DefaultHttpClient getClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        Object response = null;
        try {
//            s.setContentEncoding("UTF-8");
            HttpResponse res = getClient.execute(get);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response.toString();
    }

    /**
     * post请求
     *
     * @param url
     * @param json
     * @return
     */

    public static String doPost(String url, JSONObject json) {
        DefaultHttpClient postClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        Object response = null;
        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            HttpResponse res = postClient.execute(post);
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(res.getEntity());// 返回json格式：
//                response = JSONObject.fromObject(result);
                return result;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response.toString();
    }


}
