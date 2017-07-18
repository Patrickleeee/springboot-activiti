package com.patrick.example.test;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/6/29 15:27
 * UpdateTime 2017/6/29 15:27
 */
public class DemoTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private IdentityService identityService;

    public void deploymentProcessDefinition() {
        //创建核心引擎对象
       /* ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署对象相关的service
                .createDeployment()// 创建一个部署对象
                .name("helloworld入门程序")// 添加部署的名称
                .addClasspathResource("processes/purchase.bpmn")// classpath的资源中加载，一次只能加载
                // 一个文件
                .addClasspathResource("processes/purchase.bpmn.png")// classpath的资源中加载，一次只能加载
                // 一个文件
                .deploy();// 完成部署
        System.out.println("部署ID:" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());*/

       /* repositoryService.createDeployment();
        repositoryService.createProcessDefinitionQuery();
        repositoryService.addCandidateStarterGroup("test", "test");
        runtimeService.startProcessInstanceByKey("demo");
        runtimeService.createProcessInstanceQuery();*/


    }

    public static void main(String[] args){
        DemoTest demoTest = new DemoTest();
        demoTest.deploymentProcessDefinition();
    }
}
