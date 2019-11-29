package com.xhg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.User;
import com.xhg.service.UserService;


@RestController
@Transactional
@RequestMapping("/user")
public class userController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/index")
	public String index(){
		return "index";
	}
	
	@RequestMapping("/showAll")
	public PageInfo findAll(Integer pageNum, Integer pageSize){
		System.err.println("aaaaa");
		return userService.findAll(pageNum,pageSize);
	}
	//restful 传参
	@RequestMapping("/get")//@PathVariable("id") 获取路径参数。即url/{id}这种形式。
	public User findAll( Integer id){
		System.out.println("111111111");
		
		return userService.get(id);
	}
	
//	@RequestMapping("/get") //以前的形式
//	public User findAll(HttpServletRequest req){
//		Integer id = Integer.parseInt(req.getParameter("id"));
//		return userService.get(id);
//	}
	
	@PostMapping("/insert")
	public String insert(User user){
		try{
			userService.insert(user);
			return "insert success";
		}catch(Exception e){
			return "insert error";
		}
	}
	
	@RequestMapping("/update")
	public String update(User user){
		try{
			userService.update(user);
			return "update success";
		}catch(Exception e){
			return "update error";
		}
	}
	@RequestMapping("delete/{id}")
	public String delete(@PathVariable("id") Integer id){
		try{
			userService.delete(id);
			return "delete success";
		}catch(Exception e){
			return "delete error";
		}
		
	}
}







