//package com.patrick.example.interceptor;
//
//
//import com.patrick.example.model.entity.Permission;
//import com.patrick.example.model.entity.SysUser;
//import com.patrick.example.model.vo.PermissionVo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class MenuInterceptor implements HandlerInterceptor {
//
//    public static Map<String,String> invalidateSessionMap=new HashMap() ;
//    public static Map<String,List<PermissionVo>> userMenuMap=new HashMap() ;
//
//    @Autowired
//    UserRepository userDao;
//
//    @Autowired
//    RoleRepository roleDao;
//
//    @Autowired
//    PermissionRepository permissionDao;
//
//    @Override
//    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        //System.out.println(">>>MyInterceptor1>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//
//        try{
//            HttpSession session = httpServletRequest.getSession();
//            String servletPath = httpServletRequest.getServletPath();
//            SysUser user=userDao.findByUserName(session.getAttribute("userName").toString());
//            modelAndView.getModel().put("user",user );
//
//            if(invalidateSessionMap.get(session.getAttribute("userName").toString())!=null){
//                invalidateSessionMap.remove(session.getAttribute("userName").toString());
//                userMenuMap.remove(session.getAttribute("userName").toString());
//                session.invalidate();
//                RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/logout");
//                dispatcher.forward(httpServletRequest, httpServletResponse);
//            }
//
//            if(userMenuMap.get(session.getAttribute("userName").toString())!=null){
//                modelAndView.getModel().put("menuList", userMenuMap.get(session.getAttribute("userName").toString()));
//                try {
//                    modelAndView.getModel().put("currentMenu1", servletPath.substring(1, servletPath.lastIndexOf("/")));
//                    modelAndView.getModel().put("currentMenu2", servletPath.substring(servletPath.lastIndexOf("/") + 1));
//                }catch (Exception e){
//
//                }
//            }else{
//                List<PermissionVo> menuList=new ArrayList<PermissionVo>();
//                for(Permission menu1Permission:permissionDao.findValidMenu1PermissionByUserId(user.getId())){
//                    PermissionVo permissionVo=new PermissionVo();
//                    permissionVo.setM1(menu1Permission);
//                    //根据用户和Menu1查询权限
//                    Map<String, Object> queryPara = new HashMap<String, Object>();
//                    queryPara.put("m1",menu1Permission.getMenu2());
//                    queryPara.put("userId",user.getId());
//                    permissionVo.setM2List(permissionDao.findValidPermissionByM1AndUserId(queryPara));
//                    menuList.add(permissionVo);
//                }
//                userMenuMap.put(session.getAttribute("userName").toString(),menuList);
//                modelAndView.getModel().put("menuList", menuList);
//                try {
//                    modelAndView.getModel().put("currentMenu1", servletPath.substring(1, servletPath.lastIndexOf("/")));
//                    modelAndView.getModel().put("currentMenu2", servletPath.substring(servletPath.lastIndexOf("/") + 1));
//                }catch (Exception e){
//
//                }
//            }
//
//
//
//
//
//        }catch (Exception e){
//
//        }
//
//
//
//}
//
//    @Override
//    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        //System.out.println(">>>MyInterceptor1>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
//    }
//}
