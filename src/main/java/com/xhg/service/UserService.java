package com.xhg.service;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.sysUser;


public interface UserService {
	
public PageInfo findAll(Integer pageNum, Integer pageSize);
	
	 sysUser get(Integer id);
	
	 void insert(sysUser user);
	
	 void update(sysUser user);
	
	 void delete(Integer id);

	 Integer addUserIntegral(Integer userId);
	
}
