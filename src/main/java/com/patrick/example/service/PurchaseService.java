package com.patrick.example.service;

import com.patrick.example.dao.PurchaseRepository;
import com.patrick.example.model.Purchase;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/3 13:41
 * UpdateTime 2017/7/3 13:41
 */
@Service
@Transactional
public class PurchaseService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private DynamicDiagramService dynamicDiagramService;

    @Autowired
    private TestService testService;

    /**
     * 开启流程
     */
    public void startProcess(){
        runtimeService.startProcessInstanceByKey("purchasingflow");
    }

    /**
     * 创建购物单
     * @param user
     * @param purchaseOrder
     */
    public InputStream create(String user, String purchaseOrder)throws Exception{
        Purchase purchase = new Purchase();
        purchase.setPurchaseOrder(purchaseOrder);
        purchaseRepository.save(purchase);

        // 结束task
        Task task = getTask(user);
        String processId = task.getProcessInstanceId();
        taskService.setVariable(task.getId(), "purchase", purchase);
        taskService.complete(task.getId());
        return dynamicDiagramService.getDynamicDiagram(processId);
//        return testService.generateDiagram(processId);
    }

    /**
     * 总经理审核
     * @param user
     * @param auditResult
     */
    public InputStream gmAudit(String user, String auditResult) throws Exception {

        return audit(auditResult, user, "gm");
    }

    /**
     * 财务审核
     * @param user
     * @param auditResult
     */
    public InputStream financeAudit(String user, String auditResult) throws Exception {

        return audit(auditResult, user, "finance");
    }

    /**
     * 审核处理
     * @param auditResult
     * @param user
     * @param auditType
     */
    public InputStream audit(String auditResult, String user, String auditType) throws Exception{
        // 最新一条记录
        Purchase purchase = getPurchase();
        System.out.println(purchase.toString());
        if("gm".equals(auditType)){
            purchase.setGm_audit(auditResult);
        } else if ("finance".equals(auditType)){
            purchase.setFinance_audit(auditResult);
        }

        purchaseRepository.save(purchase);

        // 结束task
        Task task = getTask(user);
        String processId = task.getProcessInstanceId();

        // 测试添加任务人员
        taskService.addCandidateUser(task.getId(), "Patrick Leee");
        taskService.setVariable(task.getId(), "purchase", purchase);
        // 完成此项任务，进入下一个流程
        taskService.complete(task.getId());

        return dynamicDiagramService.getDynamicDiagram(processId);
    }

    /**
     * 获取最新任务
     * @param assignee
     * @return
     */
    public Task getTask(String assignee){
        List<Task> list = taskService.createTaskQuery().taskCandidateUser(assignee).orderByTaskCreateTime().asc().list();
        if(list.size() == 0){
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取最新历史变量
     * @return
     */
    public Purchase getPurchase(){
        // 获取历史变量
        List<HistoricVariableInstance> list =historyService
                .createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
                .variableName("purchase")
                .list();

        // 最新一条记录
        Purchase purchase = (Purchase)list.get(list.size() - 1).getValue();
        return purchase;
    }

    //获取符合条件的审批人，演示这里写死，使用应用使用实际代码
    public List<String> createUsers(DelegateExecution execution) {
        return Arrays.asList("wyf");
    }
    public List<String> gmUsers(DelegateExecution execution) {
        return Arrays.asList("wtr");
    }
    public List<String> financeUsers(DelegateExecution execution) {
        return Arrays.asList("admin");
    }

    public void testService(){

    }

}
