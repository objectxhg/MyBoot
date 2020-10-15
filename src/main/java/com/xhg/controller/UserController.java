package com.xhg.controller;

import com.xhg.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.sysUser;
import com.xhg.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;


@RestController
@Transactional
@SuppressWarnings("all")
@RequestMapping("/user")
@Api(value = "用户模块", tags = "用户模块")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "用户列表", notes = "分页查询用户列表")
	@ApiImplicitParams({																		//paramType：表示参数放在哪个地方		
		@ApiImplicitParam(value = "页数", name = "pageNum", required = false,  dataType = "int", paramType = "query", defaultValue = "0"),
		@ApiImplicitParam(value = "条数", name = "pageSize", required = false, dataType = "int", paramType = "query", defaultValue = "10")
	})
	@PostMapping("/showAll")
	public PageInfo findAll(@RequestParam(defaultValue = "0") Integer pageNum,
							@RequestParam(defaultValue = "10") Integer pageSize){
		return userService.findAll(pageNum,pageSize);
	}
	
	@ApiOperation(value = "获取用户信息", notes = "根据id获取单条用户信息")
	@ApiImplicitParams({
		@ApiImplicitParam(value = "页数", name = "id", required = true, dataType = "int"),
	})
	@GetMapping("/get")//@PathVariable("id") 获取路径参数。即url/{id}这种形式。
	public sysUser findAll(Integer id){
		
		return userService.get(id);
	}

	@ApiOperation(value = "用户注册", notes = "新增用户信息")
	@ApiImplicitParam(name = "user", value = "用户注册", required = true, dataType = "sysUser")
	@PostMapping("/addUserInfo")
	public JsonResult addUserInfo(@ModelAttribute @Valid @RequestBody sysUser user){
		System.out.println(user);
		Integer state = userService.addUser(user);
		if(state != 1){
			return JsonResult.fail("添加失败");
		}
		return JsonResult.success("添加成功");
	}

	@PostMapping("/addUserIntegral")//@PathVariable("id") 获取路径参数。即url/{id}这种形式。
	public JsonResult addUserIntegral(Integer id){
		Integer state = userService.incrUserIntegral(id);
		if(state != 1){
			return JsonResult.fail("增加失败");
		}
		return JsonResult.success("增加成功");
	}

}







