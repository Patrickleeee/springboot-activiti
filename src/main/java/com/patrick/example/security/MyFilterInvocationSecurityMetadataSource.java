//package com.patrick.example.security;
//
//
//import com.patrick.example.model.entity.Permission;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.ConfigAttribute;
//import org.springframework.security.access.SecurityConfig;
//import org.springframework.security.web.FilterInvocation;
//import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Iterator;
//
//@Service
//public class MyFilterInvocationSecurityMetadataSource implements
//        FilterInvocationSecurityMetadataSource {
//
//    @Autowired
//    PermissionRepository permissionDao;
//
//    HashMap<String, Collection<ConfigAttribute>> map =null;
//
//    /**
//     * 加载资源，初始化资源变量
//     */
//    public void loadResourceDefine(){
//        map = new HashMap<>();
//        Collection<ConfigAttribute> array;
//        ConfigAttribute cfg;
//        for(Permission permission : permissionDao.findAll()) {
//            try{
//                array = new ArrayList<>();
//                cfg = new SecurityConfig(permission.getName());
//                array.add(cfg);
//                map.put(permission.getUrl(), array);
//            }catch (Exception e){
//            }
//        }
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        if(map ==null) loadResourceDefine();
//        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
//        AntPathRequestMatcher matcher;
//        String resUrl;
//        for(Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
//            resUrl = iter.next();
//            matcher = new AntPathRequestMatcher(resUrl);
//            if(matcher.matches(request)) {
//                return map.get(resUrl);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Collection<ConfigAttribute> getAllConfigAttributes() {
//        return null;
//    }
//
//    @Override
//    public boolean supports(Class<?> clazz) {
//        return true;
//    }
//}
