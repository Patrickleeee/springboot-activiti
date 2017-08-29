//package com.patrick.example.security;
//
//
//import com.patrick.example.model.entity.Permission;
//import com.patrick.example.model.entity.SysUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    UserRepository userDao;
//
//    @Autowired
//    PermissionRepository permissionDao;
//
//    public UserDetails loadUserByUsername(String username) {
//        SysUser user = userDao.findByUserName(username);
//        if (user != null) {
//            List<Permission> permissions = permissionDao.findByUserId(user.getId());
//            List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
//            for (Permission permission : permissions) {
//                if (permission != null && permission.getName()!=null) {
//                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getName());
//                    grantedAuthorities.add(grantedAuthority);
//                }
//            }
//            return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
//        } else {
//            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
//        }
//    }
//
//
//
//}
