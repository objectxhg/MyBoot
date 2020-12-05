package com.xhg.service;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.sysUser;


public interface UserService {
	
	 PageInfo findAll(Integer pageNum, Integer pageSize);
	
	 sysUser get(Integer id);
	
	 void insert(sysUser user);
	
	 void update(sysUser user);
	
	 void delete(Integer id);

	 Integer addUser (sysUser user);

	 Integer incrUserIntegral(Integer userId);

	 sysUser getUserInfo(Integer userId);
}
