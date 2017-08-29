package com.patrick.example.interceptor;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Iterator;

public class MyAccessDecisionManager implements AccessDecisionManager {
    // In this method, need to compare authentication with configAttributes.
    // 1, A object is a URL, a filter was find permission configuration by this
    // URL, and pass to here.
    // 2, Check authentication has attribute in permission configuration
    // (configAttributes)
    // 3, If not match corresponding authentication, throw a
    // AccessDeniedException.

    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        // System.out.println("***************************===
        // MyAccessDecisionManager decide");
        if (object != null) {
            ((FilterInvocation) object).getResponse().setHeader("Cache-Control", "no-cache");
            ((FilterInvocation) object).getResponse().setHeader("Cache-Control", "no-store");
            ((FilterInvocation) object).getResponse().setDateHeader("Expires", 0);
            ((FilterInvocation) object).getResponse().setHeader("Pragma", "no-cache");
        }
        if (configAttributes == null) {
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if ("ROLE_ANONYMOUS".equals(ga.getAuthority().trim())) {
                    throw new AccessDeniedException("no right ROLE_ANONYMOUS!");
                }
            }
            return;
        }
        // object is a URL.
        Iterator<ConfigAttribute> ite = configAttributes.iterator();

        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRole = ((SecurityConfig) ca).getAttribute();

            // ga 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority().trim())) {
                    return;
                } else if ("ALL".equals(needRole.trim())) {
                    return;
                }
            }
        }

        //
        throw new AccessDeniedException("no right!");
    }

    public boolean supports(ConfigAttribute arg0) {

        return true;
    }

    public boolean supports(Class<?> clazz) {

        return true;
    }
}
