package com.patrick.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.patrick.example.service.Audit2Service;
import com.patrick.example.service.DynamicDiagramService;
import com.patrick.example.util.RequestJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/11 10:12
 * UpdateTime 2017/7/11 10:12
 */

@RestController
public class TestController {

    @Autowired
    private Audit2Service audit2Service;

    @Autowired
    private DynamicDiagramService dynamicDiagramService;

    /**
     * 测试传入参数格式为application/json
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/test666/audit/create", method = RequestMethod.POST)
    public void create(HttpServletRequest request, HttpServletResponse response) throws Exception{

        // 判断权限
        /*JSONObject obj = RequestJsonUtils.getRequestJsonStr(request);*/

        JSONObject obj = RequestJsonUtils.getRequestJsonString(request);
        JSONObject bodyObj = obj.getJSONObject("params");

        String user = bodyObj.getString("user");
        if(!"leee".equals(user)){
            System.out.println("用户" + user + "没有create任务!");
            return;
        }

        int expense = bodyObj.getIntValue("expense");

        audit2Service.startProcess();

        InputStream imageStream = audit2Service.create(user, expense, obj);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 测试传入参数格式为text时开启流程入口
     * @param request
     * @param response
     */
    @RequestMapping(value = "/test666", method = RequestMethod.POST)
    public void startProcessInstance(HttpServletRequest request, HttpServletResponse response){
        audit2Service.startProcess();
    }

    /**
     * 测试传入参数格式为text
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 解析请求参数

        JSONObject paramsObj = RequestJsonUtils.getRequestJsonStr(request);
        String name = paramsObj.getString("name");
        System.out.println(name);
        JSONObject bodyObj = paramsObj.getJSONObject("params");
        String name1 = bodyObj.getString("name1");
        System.out.println(name1);

    }
}
