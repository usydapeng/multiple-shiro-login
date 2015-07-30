package com.dapeng.config;

import com.dapeng.core.CustomSecurityRealm;
import com.google.common.collect.Maps;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Order(200)
public class SecurityConfig {

	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public CustomSecurityRealm customSecurityRealm(){
		return new CustomSecurityRealm();
	}

	@Bean
	@Autowired
	public WebSecurityManager securityManager(CustomSecurityRealm customSecurityRealm, RememberMeManager rememberMeManager, SessionManager sessionManager){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(customSecurityRealm);
		securityManager.setRememberMeManager(rememberMeManager);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	@Bean
	@Autowired
	public MethodInvokingFactoryBean methodInvokingFactoryBean(WebSecurityManager securityManager){
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		methodInvokingFactoryBean.setArguments(new Object[]{securityManager});
		return methodInvokingFactoryBean;
	}

	@Bean
	@Autowired
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(WebSecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean(name = "shiroFilter")
	@Autowired
	public ShiroFilterFactoryBean shiroFilter(WebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setLoginUrl("/login");
		shiroFilter.setUnauthorizedUrl("/login");
		shiroFilter.setSuccessUrl("/index");
		shiroFilter.setSecurityManager(securityManager);

		Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
		filterChainDefinitionMap.put("/**", "authc");
		filterChainDefinitionMap.put("/favicon.ico", "anon");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilter;
	}

	@Bean
	@Autowired
	public CookieRememberMeManager rememberMeManager(SimpleCookie simpleCookie){
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(simpleCookie);
		return cookieRememberMeManager;
	}

	@Bean
	public DefaultWebSessionManager sessionManager(SimpleCookie sessionIdCookie){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionIdCookie(sessionIdCookie);
		return sessionManager;
	}

	@Bean
	public SimpleCookie simpleCookie(){
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("zhangsan");
		simpleCookie.setPath("/");
		simpleCookie.setMaxAge(60 * 60 * 24 * 7);
		return simpleCookie;
	}

	@Bean
	public SimpleCookie sessionIdCookie(){
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("ckb_re_a");
		simpleCookie.setMaxAge(-1);
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		return simpleCookie;
	}

}
