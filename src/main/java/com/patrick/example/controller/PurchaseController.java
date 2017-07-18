package com.patrick.example.controller;

import com.patrick.example.service.DynamicDiagramService;
import com.patrick.example.service.PurchaseService;
import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;


/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/3 13:37
 * UpdateTime 2017/7/3 13:37
 */
@RestController
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private DynamicDiagramService dynamicDiagramService;

    @RequestMapping(value = "/process/purchase", method = RequestMethod.GET)
    public void startProcessInstance(){
        purchaseService.startProcess();
    }

    /**
     * 取最新的一条任务
     * @param assignee
     * @return
     */
    @RequestMapping(value = "/process/purchase/tasks/{assignee}", method = RequestMethod.GET)
    public MyRestController.TaskRepresentation tasks(@PathVariable String assignee){
        Task task = purchaseService.getTask(assignee);
        if(task == null){
            return new MyRestController.TaskRepresentation("", "");
        }
        return new MyRestController.TaskRepresentation(task.getId(), task.getName());
    }

    /**
     * 创建task
     * @param purchaseOrder
     */
    @RequestMapping(value = "/process/purchase/create/{user}/{purchaseOrder}", method = RequestMethod.GET)
    public void create(HttpServletResponse response, @PathVariable String user, @PathVariable String purchaseOrder) throws Exception{
        // 判断权限
        if(!"wyf".equals(user)){
            System.out.println("用户" + user + "没有create任务!");
            return;
        }
        InputStream imageStream = purchaseService.create(user, purchaseOrder);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 总经理审核task
     * @param auditResult
     */
    @RequestMapping(value = "/process/purchase/gmAudit/{user}/{auditResult}", method = RequestMethod.GET)
    public void gmAudit(HttpServletResponse response, @PathVariable String user, @PathVariable String auditResult) throws Exception{
        // 判断权限
        if(!"wtr".equals(user)){
            System.out.println("用户" + user + "没有gmAudit任务!");
            return;
        }
        InputStream imageStream = purchaseService.gmAudit(user, auditResult);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 财务审核task
     * @param auditResult
     */
    @RequestMapping(value = "/process/purchase/financeAudit/{user}/{auditResult}", method = RequestMethod.GET)
    public void financeAudit(HttpServletResponse response, @PathVariable String user, @PathVariable String auditResult) throws Exception {
        // 判断权限
        if(!"admin".equals(user)){
            System.out.println("用户" + user + "没有financeAudit任务!");
            return;
        }
        InputStream imageStream = purchaseService.financeAudit(user, auditResult);
        dynamicDiagramService.printDiagram(response, imageStream);
    }
}
