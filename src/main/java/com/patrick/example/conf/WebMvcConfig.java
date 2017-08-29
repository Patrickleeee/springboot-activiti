//package com.patrick.example.conf;
//
//import com.patrick.example.interceptor.MenuInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurerAdapter{
//
//    @Autowired
//    MenuInterceptor menuInterceptor;
//
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/khgl/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/jddz/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/ywcx/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/xtgl/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        super.addResourceHandlers(registry);
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(menuInterceptor).addPathPatterns("/login","/dashboard","/khgl/**","/jddz/**","/ywcx/**","/xtgl/**");
//        super.addInterceptors(registry);
//
//    }
//}
