package com.patrick.example.controller;

import com.patrick.example.service.DynamicDiagramService;
import org.activiti.engine.*;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/5 16:46
 * UpdateTime 2017/7/5 16:46
 */
@Controller
@RequestMapping(value = "/first")
public class DynamicMagicController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    DynamicDiagramService dynamicDiagramService;

    /**
     * 读取带跟踪的图片
     */
    @RequestMapping(value = "/test123")
    public void test(HttpServletResponse response) throws Exception {

        // 开启流程，myprocess是流程的ID
        System.out.println("流程【启动】，环节推动到【一次审批】环节");
        runtimeService.startProcessInstanceByKey("purchasingflow");
        // 查询历史表中的Task
        List<Task> task = taskService.createTaskQuery().list();
        Task task1 = task.get(task.size() - 1);
        //解开注释就推动到下一环节，对应的在流程图上看到
        taskService.complete(task1.getId());
        System.out.println("执行【一次审批】环节，流程推动到【二次审批】环节");

        //processInstanceId
        String processInstanceId = task1.getProcessInstanceId();

        dynamicDiagramService.rtDynamicDiagramResponse(response, processInstanceId);
    }

}
