package com.patrick.example.util;


import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;
/**
 * Desciption 调用post请求（中文不乱码）
 * Create By  li.bo
 * CreateTime 2017/9/14 12:21
 * UpdateTime 2017/9/14 12:21
 */



public class Demo {

    /*public static JSONObject getCardListS0(){

        JSONObject joParam = new JSONObject();
        joParam.put("method", "getCardListS0");
        String renResult = doHttpPost(joParam.toString(), "http://tgapitest.homefax.cn/web/portal");
        System.out.println(renResult);
        return JSONObject.fromObject(renResult);

    }*/

    public static String doHttpPost(String param,String URL){
        System.out.println("发起的数据:"+param);
        InputStream instr  = null;
        java.io.ByteArrayOutputStream out = null;
        try{
            byte[] data = param.getBytes("utf-8");

            URL url = new URL(URL);
            URLConnection urlCon = url.openConnection();
            urlCon.setDoOutput(true);
            urlCon.setDoInput(true);
            urlCon.setUseCaches(false);
            urlCon.setRequestProperty("Content-Type", "text/plain");
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

    public static void main(String[] args) {
        /*getCardListS0();*/
    }

}
