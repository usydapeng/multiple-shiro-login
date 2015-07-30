package com.dapeng.config;

import com.dapeng.core.CustomSecurityRealm;
import com.google.common.collect.Maps;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Map;


@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Order(100)
public class AdminSecurityConfig {

	@Bean
	public ShiroFilterFactoryBean adminShiroFilter(){
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setLoginUrl("/admin/login");
		shiroFilter.setUnauthorizedUrl("/admin/login");
		shiroFilter.setSuccessUrl("/admin/index");
		shiroFilter.setSecurityManager(adminSecurityManager());

		Map<String, String> filterChainDefinitionMap = Maps.newLinkedHashMap();
		filterChainDefinitionMap.put("/admin/**", "authc");
		filterChainDefinitionMap.put("/admin/favicon.ico", "anon");
		shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilter;
	}

	@Bean
	public CustomSecurityRealm adminCustomSecurityRealm(){
		return new CustomSecurityRealm();
	}

	@Bean
	@Autowired
	public WebSecurityManager adminSecurityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(adminCustomSecurityRealm());
		securityManager.setRememberMeManager(adminRememberMeManager());
		securityManager.setSessionManager(adminSessionManager());
		return securityManager;
	}

	@Bean
	@Autowired
	public CookieRememberMeManager adminRememberMeManager(){
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(adminSimpleCookie());
		return cookieRememberMeManager;
	}

	@Bean
	public DefaultWebSessionManager adminSessionManager(){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionIdCookie(adminSessionIdCookie());
		return sessionManager;
	}

	@Bean
	public SimpleCookie adminSimpleCookie(){
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("admin_zhangsan");
		simpleCookie.setPath("/admin");
		simpleCookie.setMaxAge(60 * 60 * 24 * 7);
		return simpleCookie;
	}

	@Bean
	public SimpleCookie adminSessionIdCookie(){
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("admin_ckb_re_a");
		simpleCookie.setMaxAge(-1);
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/admin");
		return simpleCookie;
	}
}
