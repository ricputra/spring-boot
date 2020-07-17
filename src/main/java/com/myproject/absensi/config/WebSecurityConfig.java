package com.myproject.absensi.config;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.dao.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.myproject.absensi.service.UserService;
import com.myproject.absensi.service.UserServiceImpl;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	private UserService userService;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }
    
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
	  
	  @Bean
	  public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
	      return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
	  }
 
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
       		.antMatchers("/Main/index**").permitAll()
            .anyRequest().authenticated()
            .and()
    			.csrf()
    			.disable()
            .formLogin()
            	.loginProcessingUrl("/authenticateTheUser")
            	.successHandler(customAuthenticationSuccessHandler)
				.failureUrl("/login-error")
        		.permitAll()
        	.and()
            	.logout().permitAll()
            .and()
    			.exceptionHandling().accessDeniedPage("/access-denied")
        	.and()
        		.sessionManagement()
        		.maximumSessions(1)
        		.maxSessionsPreventsLogin(true)
        		.expiredUrl("/Main/index?expired=true")
        		.sessionRegistry(sessionRegistry());
        	
    
        
    }
    
    
}