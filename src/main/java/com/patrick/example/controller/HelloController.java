package com.patrick.example.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Desciption
 * Create By  li.bo
 * CreateTime 2017/8/25 14:37
 * UpdateTime 2017/8/25 14:37
 */
@Controller
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "patrick");
        return "hello";
    }
}
