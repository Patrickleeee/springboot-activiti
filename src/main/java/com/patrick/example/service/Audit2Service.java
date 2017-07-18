package com.patrick.example.service;

import com.alibaba.fastjson.JSONObject;
import com.patrick.example.dao.AuditRepository;
import com.patrick.example.model.Audit;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/3 13:41
 * UpdateTime 2017/7/3 13:41
 */
@Service
@Transactional
public class Audit2Service {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private DynamicDiagramService dynamicDiagramService;

    /**
     * 开启流程
     */
    public void startProcess(){
        runtimeService.startProcessInstanceByKey("exclusiveGateway666");
    }

    /**
     * 创建审批单
     * @param user
     * @param expense
     */
    public InputStream create(String user, int expense, JSONObject obj)throws Exception{
        Audit audit = new Audit();
        audit.setExpense(expense);
        auditRepository.save(audit);

        // 结束task
        Task task = getTask(user);
        String processId = task.getProcessInstanceId();
        taskService.setVariable(task.getId(), "audit", audit);
        taskService.setVariable(task.getId(), "jsonObj", obj);
        taskService.complete(task.getId());

        return dynamicDiagramService.getDynamicDiagram(processId);
    }

    /**
     * 部门经理审核
     * @param user
     * @param auditResult
     */
    public InputStream dmAudit(String user, String auditResult) throws Exception {

        return audit(auditResult, user, "dm");
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
     * 总经理审核
     * @param user
     * @param auditResult
     */
    public InputStream gmAudit(String user, String auditResult) throws Exception {

        return audit(auditResult, user, "gm");
    }

    /**
     * 审核处理
     * @param auditResult
     * @param user
     * @param auditType
     */
    public InputStream audit(String auditResult, String user, String auditType) throws Exception{
        // 最新一条记录
        Audit audit = getAudit();

        if("dm".equals(auditType)){
            audit.setDm_audit(auditResult);
        }else if("finance".equals(auditType)){
            audit.setFinance_audit(auditResult);
        }else if("gm".equals(auditType)){
            audit.setGm_audit(auditResult);
        }
        auditRepository.save(audit);

        // 结束task
        Task task = getTask(user);
        String processId = task.getProcessInstanceId();
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
    public Audit getAudit(){
        // 获取历史变量
        List<HistoricVariableInstance> list =historyService
                .createHistoricVariableInstanceQuery()//创建一个历史的流程变量查询对象
                .variableName("audit")
                .list();

        // 最新一条记录
        Audit audit = (Audit)list.get(list.size() - 1).getValue();
        return audit;
    }

    //获取符合条件的审批人，演示这里写死，使用应用使用实际代码
    public List<String> createUsers(DelegateExecution execution) {
        return Arrays.asList("leee");
    }
    public List<String> dmUsers(DelegateExecution execution) {
        return Arrays.asList("admin");
    }
    public List<String> gmUsers(DelegateExecution execution) {
        return Arrays.asList("admin");
    }
    public List<String> financeUsers(DelegateExecution execution) {
        return Arrays.asList("admin");
    }

}
