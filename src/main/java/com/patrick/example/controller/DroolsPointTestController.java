package com.patrick.example.controller;

import com.patrick.example.model.PointDomain;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/8/21 14:01
 * UpdateTime 2017/8/21 14:01
 */
@RestController
public class DroolsPointTestController {

   /* @Resource
    private KieSession kieSession;*/

    @RequestMapping("/point")
    public void test(){

        // load up the knowledge base
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("ksession-rules");

        final PointDomain pointDomain = new PointDomain();
        pointDomain.setUserName("hello kitty");
        pointDomain.setBackMondy(100d);
        pointDomain.setBuyMoney(500d);
        pointDomain.setBackNums(1);
        pointDomain.setBuyNums(5);
        pointDomain.setBillThisMonth(5);
        pointDomain.setBirthDay(true);
        pointDomain.setPoint(0l);

        kieSession.insert(pointDomain);
        int ruleFiredCount = kieSession.fireAllRules();
        System.out.println("触发了" + ruleFiredCount + "条规则");

    }
}
