package com.patrick.example.controller.sys;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2017/2/26.
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(HttpServletRequest httpServletRequest,Model model){
        return "login";
    }

    @RequestMapping("/404")
    public String error(HttpServletRequest httpServletRequest,Model model){
        return "404";
    }

    @RequestMapping("/index")
    public void captchaCheck(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model){

        String kaptchaExpected = (String)httpServletRequest.getSession() .getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String kaptchaReceived = httpServletRequest.getParameter("captcha");

        RequestDispatcher dispatcher;

        if (kaptchaReceived == null || !kaptchaReceived.equalsIgnoreCase(kaptchaExpected)){
            dispatcher = httpServletRequest.getRequestDispatcher("/login");
            httpServletRequest.setAttribute("captchaerror","1");
        }else{
            dispatcher = httpServletRequest.getRequestDispatcher("/dashboard");
        }
        try {
            dispatcher .forward(httpServletRequest, httpServletResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/dashboard")
    public String dashboard(HttpServletRequest httpServletRequest,Model model){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        String username=userDetails.getUsername();
        httpServletRequest.getSession().setAttribute("userName",username);
        /*Msg msg =  new Msg("测试标题","测试内容","欢迎来到HOME页面,您拥有 ROLE_HOME 权限");
        model.addAttribute("msg", msg);*/
        model.addAttribute("userName", username);
        return "/khgl/khgl";
    }

}
