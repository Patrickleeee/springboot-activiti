package com.patrick.example.controller;

import com.patrick.example.service.AuditService;
import com.patrick.example.service.DynamicDiagramService;
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
public class AuditController {

//    测试地址：http://localhost:8087/process/audit
//              http://localhost:8087/process/audit/create/leee/9000
//              http://localhost:8087/process/audit/financeAudit/admin/ok
    @Autowired
    private AuditService auditService;

    @Autowired
    private DynamicDiagramService dynamicDiagramService;

    @RequestMapping(value = "/process/audit", method = RequestMethod.GET)
    public void startProcessInstance(){
        auditService.startProcess();
    }

    /**
     * 创建task
     * @param response
     * @param user
     * @param expense
     * @throws Exception
     */
    @RequestMapping(value = "/process/audit/create/{user}/{expense}", method = RequestMethod.GET)
    public void create(HttpServletResponse response, @PathVariable String user, @PathVariable int expense) throws Exception{
        // 判断权限
        if(!"leee".equals(user)){
            System.out.println("用户" + user + "没有create任务!");
            return;
        }
        InputStream imageStream = auditService.create(user, expense);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 总经理审核task
     * @param auditResult
     */
    @RequestMapping(value = "/process/audit/gmAudit/{user}/{auditResult}", method = RequestMethod.GET)
    public void gmAudit(HttpServletResponse response, @PathVariable String user, @PathVariable String auditResult) throws Exception{
        // 判断权限
        if(!"admin".equals(user)){
            System.out.println("用户" + user + "没有gmAudit任务!");
            return;
        }
        InputStream imageStream = auditService.gmAudit(user, auditResult);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 财务审核task
     * @param auditResult
     */
    @RequestMapping(value = "/process/audit/financeAudit/{user}/{auditResult}", method = RequestMethod.GET)
    public void financeAudit(HttpServletResponse response, @PathVariable String user, @PathVariable String auditResult) throws Exception {
        // 判断权限
        if(!"admin".equals(user)){
            System.out.println("用户" + user + "没有financeAudit任务!");
            return;
        }
        InputStream imageStream = auditService.financeAudit(user, auditResult);
        dynamicDiagramService.printDiagram(response, imageStream);
    }

    /**
     * 财务审核task
     * @param auditResult
     */
    @RequestMapping(value = "/process/audit/dmAudit/{user}/{auditResult}", method = RequestMethod.GET)
    public void dmAudit(HttpServletResponse response, @PathVariable String user, @PathVariable String auditResult) throws Exception {
        // 判断权限
        if(!"admin".equals(user)){
            System.out.println("用户" + user + "没有financeAudit任务!");
            return;
        }
        InputStream imageStream = auditService.dmAudit(user, auditResult);
        dynamicDiagramService.printDiagram(response, imageStream);
    }
}
