package com.dapeng.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class CustomSecurityRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(CustomSecurityRealm.class);

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.info("authorization: 授权回调函数 " + principals.getRealmNames());

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

		simpleAuthorizationInfo.setRoles(Sets.newHashSet("student", "teacher"));
		simpleAuthorizationInfo.addStringPermissions(Lists.newArrayList("hello:index"));

		return simpleAuthorizationInfo;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.info("authentication: 认证回调函数");
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
		return new SimpleAuthenticationInfo(usernamePasswordToken.getUsername(), "world", getName());
	}

	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		credentialsMatcher = new SimpleCredentialsMatcher();
		super.setCredentialsMatcher(credentialsMatcher);
	}
}
