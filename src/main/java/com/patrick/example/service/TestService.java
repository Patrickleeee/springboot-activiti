package com.patrick.example.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/7/5 17:03
 * UpdateTime 2017/7/5 17:03
 */
@Service
public class TestService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private HistoryService historyService;

    public InputStream generateDiagram(String processInstanceId) {
        //方法中用到的参数是流程实例ID，其实TaskId也可以转为这个。调用taskService查询即可。
        ProcessInstanceDiagramCmd test = new ProcessInstanceDiagramCmd(processInstanceId, runtimeService, repositoryService, processEngine, historyService);
        return test.executeDyanmicDiagram();
    }
}
