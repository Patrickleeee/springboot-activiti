package com.patrick.example.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/11 13:50
 * UpdateTime 2017/7/11 13:50
 */
public class RequestJsonUtils {

    /***
     * 获取 request 中 json 字符串的内容
     *
     * @param request
     * @return : <code>byte[]</code>
     * @throws IOException
     */
    public static JSONObject getRequestJsonString(HttpServletRequest request)
            throws IOException {
        String submitMehtod = request.getMethod();
        String objString;
        // GET
        if (submitMehtod.equals("GET")) {
            objString = new String(request.getQueryString().getBytes("iso-8859-1"),"utf-8").replaceAll("%22", "\"");
            // POST
        } else {
            objString = getRequestPostStr(request);
        }
        return JSON.parseObject(objString);
    }

    /**
     * 描述:获取 post 请求内容
     * <pre>
     * 举例：
     * </pre>
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPostStr(HttpServletRequest request)
            throws IOException {
        byte buffer[] = getRequestPostBytes(request);
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }
        return new String(buffer, charEncoding);
    }


    /**
     * 描述:获取 post 请求的 byte[] 数组
     * <pre>
     * 举例：
     * </pre>
     * @param request
     * @return
     * @throws IOException
     */
    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if(contentLength<0){
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength;) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    /**
     * 获取请求的json数据
     * @param request
     * @return
     * @throws IOException
     */
    public static JSONObject getRequestJsonStr(HttpServletRequest request) throws IOException{

        Map<String, String[]> map = request.getParameterMap();
        Set keys = map.keySet();
        String params = "";
        for(Iterator iterator = keys.iterator(); iterator.hasNext();){
            Object obj = (Object)iterator.next();
            String[] value = map.get(obj);
            if(value.length > 0 && value[value.length-1] == ""){
                params = obj.toString();
            }
        }
        System.out.println(params);

        if(StringUtils.isEmpty(params)){
            return null;
        }

//        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(params);

        // 字符串转换成json对象
        /** 使用jsonTokener来转换json对象(net.sf.json-lib下的json-lib包）
         * JSONTokener jsonTokener = new JSONTokener(params);
         *JSONObject paramsObj = (JSONObject) jsonTokener.nextValue();
         * */

        // 使用fastjson来转换json对象(com.alibaba下的fastjson包
        JSONObject paramsObj = JSON.parseObject(params);

        return paramsObj;
    }

}

