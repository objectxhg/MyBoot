package com.xhg.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeIndexController {

	@GetMapping("/login")
	@ResponseBody
	public String defaultLogin() {
		return "首页";
	}

	@PostMapping("/login")
	@ResponseBody
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
		// 从SecurityUtils里边创建一个 subject
		Subject subject = SecurityUtils.getSubject();
		// 在认证提交前准备 token（令牌）
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		// 执行认证登陆
		try {
			subject.login(token);
		} catch (UnknownAccountException uae) {
			return "未知账户";
		} catch (IncorrectCredentialsException ice) {
			return "密码不正确";
		} catch (LockedAccountException lae) {
			return "账户已锁定";
		} catch (ExcessiveAttemptsException eae) {
			return "用户名或密码错误次数过多";
		} catch (AuthenticationException ae) {
			return "用户名或密码不正确！";
		}
		if (subject.isAuthenticated()) {
			return "登录成功";
		} else {
			token.clear();
			return "登录失败";
		}
	}

}
