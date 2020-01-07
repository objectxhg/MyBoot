package com.xhg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.User;
import com.xhg.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


@RestController
@Transactional
@SuppressWarnings("all")
@RequestMapping("/user")
@Api(value = "用户模块", tags = "用户模块")
public class userController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	public String index() throws Exception {
		
		return "index";
	}
	
	
	@ApiOperation(value = "用户列表", notes = "分页查询用户列表")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "页数", name = "pageNum", required = false, dataType = "int", defaultValue = "0"),
		@ApiImplicitParam(value = "页数", name = "pageNum", required = false, dataType = "int", defaultValue = "10")
	})
	
	@PostMapping("/showAll")
	public PageInfo findAll(@RequestParam(defaultValue = "0") Integer pageNum,
							@RequestParam(defaultValue = "10") Integer pageSize){
		
		
		return userService.findAll(pageNum,pageSize);
	}
	
	@GetMapping("/get")//@PathVariable("id") 获取路径参数。即url/{id}这种形式。
	public User findAll( Integer id){
		System.out.println("111111111");
		
		return userService.get(id);
	}
	
}







