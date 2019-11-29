package com.xhg.config.shiro;

import java.util.Date;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.xhg.pojo.Role;
import com.xhg.pojo.User;
import com.xhg.service.UserService;

	
public class CustomRealm extends AuthorizingRealm {
	
	
	@Value("${salt}")
	private String salt;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        for (Role role : user.getRoleList()) {
            authorizationInfo.addRole(role.getRoleName());
        }
        return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken authenticationToken = (UsernamePasswordToken) token;

        String username = authenticationToken.getUsername();
        
        User user = userService.slelectUserByName(username);

        if (user == null) {
            throw new UnknownAccountException("用户不存在!");
        }

        String pwd = user.getPwd();
        user.setPwd("");

        return new SimpleAuthenticationInfo(
                user, //用户名
                pwd, //密码
                ByteSource.Util.bytes(user.getMobile() + salt),//salt=username+salt
                getName()  //realm name
        );
    
		
	}

}
