package com.patrick.example.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/9/14 11:06
 * UpdateTime 2017/9/14 11:06
 */
public class PostUtil {

    public static JSONObject postJson(String urlString , JSONObject jsonObject) throws IOException {
        JSONObject returnJson = new JSONObject();
        try {
            // 创建连接
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");//**注意点1**，需要此格式，后边这个字符集可以不设置
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(jsonObject.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }
            System.out.println("sb:"+sb);
            returnJson = JSONObject.parseObject(sb.toString());
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnJson;
    }

    public static void test(String url) throws IOException {
        Map<String, String> params = new HashMap<>(); //参数

        HttpURLConnection urlCon = null;
        URL urlInstance;
        StringBuilder sbResult = new StringBuilder();
        try {
            urlInstance = new URL(url);
            urlCon = (HttpURLConnection) urlInstance.openConnection();
            urlCon.setRequestMethod("POST");

            urlCon.setDoOutput(true); // 是否可以发送数据到服务器
            urlCon.setDoInput(true); // 设置是否读服务端
            urlCon.setUseCaches(false); // 设置是否缓存
            urlCon.setConnectTimeout(15000);// 设置响应超时
            // 固定格式
            urlCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlCon.setRequestProperty("Charset", "utf-8");

            // 对参数进行处理
            String data = "";
            if (params != null) {
                String value;
                Set<String> set = params.keySet();// 获取到所有map的键
                for (String string : set) {// 遍历参数，拼接data
                    value = params.get(string);
                    data += string + "=" + URLEncoder.encode(String.valueOf(value), "utf-8") + "&";
                }
            }

            urlCon.setRequestProperty("Content-Length", String.valueOf(data.length())); // 设置长度

            // 往服务器写入数据
            OutputStream out = urlCon.getOutputStream();
            out.write(data.getBytes());
            out.flush();

            // 接收服务器返回的数据
            InputStream in = urlCon.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;// 每一行的数据
            while ((line = br.readLine()) != null) {
                sbResult.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String doHttpPost(String param,String URL){
        System.out.println("发起的数据:"+param);
        InputStream instr  = null;
        java.io.ByteArrayOutputStream out = null;
        try{
            byte[] data = param.getBytes("utf-8");
            URL url = new URL(URL);
            if("https".equalsIgnoreCase(url.getProtocol())){
                SslUtils.ignoreSsl();
            }
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("Content-Type", "text/plain");
            //urlCon.setRequestProperty("Content-Type", "application/json");
            urlCon.setRequestProperty("Content-length",String.valueOf(data.length));
            urlCon.setRequestProperty("Accept-Charset", "utf-8");
            urlCon.setRequestProperty("contentType", "utf-8");
            DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());
            printout.write(data);
            printout.flush();
            printout.close();
            instr = urlCon.getInputStream();
            byte[] bis = IOUtils.toByteArray(instr);
            String ResponseString = new String(bis, "UTF-8");
            if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
                System.out.println("返回空");
            }
            System.out.println("返回数据为:" + ResponseString);
            return ResponseString;
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
        finally {
            try {
                if(out != null){
                    out.close();
                }
                instr.close();
            }catch (Exception ex) {
                ex.printStackTrace();
                return "0";
            }
        }
    }

    public JSONObject sendRequest2Today(String method, String idName, String id) {
        String today_url = "http://localhost:8080/web/portal";
        org.activiti.engine.impl.util.json.JSONObject jo = new org.activiti.engine.impl.util.json.JSONObject();
        jo.put("method", method);
        jo.put(idName, id);

        String result = HttpClientUtil.doPost(today_url, jo);
        System.out.println(result);

        JSONObject resultObject = JSONObject.parseObject(result);
        if (!StringUtils.equals("0000", resultObject.getString("state"))) {
            return null;
        }

        return resultObject.getJSONObject("data");

    }

    public static void main(String[] args) throws ParseException {
        PostUtil b = new PostUtil();
        System.out.println(b.sendRequest2Today("factoryAccess", "factoryId", "19").toString());

    }

}
