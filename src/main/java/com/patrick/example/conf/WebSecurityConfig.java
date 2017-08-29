//package com.patrick.example.conf;
//
//import com.patrick.example.interceptor.SecurityInterceptor;
//import com.patrick.example.security.MyUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
//
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Autowired
//    private SecurityInterceptor securityInterceptor;
//
//    @Bean
//    UserDetailsService userService(){
//        return new MyUserDetailsService();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userService());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable().authorizeRequests()
//                    .antMatchers( "/kaptcha","/logout","/captcha-image","/modules/**","/doc/**","/css/**", "/img/**","/images/**","imgs-test/**","/email_templates/**","/font-awesome/**","/fonts/**","/js/**","/locales/**")
//                    .permitAll()
//                    .anyRequest()
//                    .authenticated()
//                .and()
//                    .formLogin().failureUrl("/login").loginProcessingUrl("/")
//                    .loginPage("/login")/*.successForwardUrl("/index").failureForwardUrl("/logout")*/
//                    .permitAll()
//                .and()
//                    .logout()
//                    .permitAll().logoutUrl("/logout").logoutSuccessUrl("/login")
//                    .invalidateHttpSession(true)
//                .and()
//                    .exceptionHandling().accessDeniedPage("/404");
//
//        http.addFilterBefore(securityInterceptor, FilterSecurityInterceptor.class);
//    }
//}
//
