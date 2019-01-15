package com.itheima.bos.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.SimpleAutowireCandidateResolver;

import com.itheima.bos.dao.IUserDao;
import com.itheima.bos.domain.User;

public class BOSRealm extends AuthorizingRealm {

	@Autowired
	private IUserDao userDao;

	//认证方法
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken passwordToken = (UsernamePasswordToken)token;
		String username = passwordToken.getUsername();
		//根据页面传递的用户名查询数据库中的密码
		User user = userDao.findUserByUsername(username);
		if(user == null){
			//页面传递的账号不存在
			return null;
		}
		
		//用户名存在
		AuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
		return info;
	}
	//授权方法
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//为用户授权
		info.addStringPermission("staff-list");
		
		//TODO 后期需要修改为根据当前用户查询数据库，获取实际对应的权限
		
		return info;
	}

	
	
}
