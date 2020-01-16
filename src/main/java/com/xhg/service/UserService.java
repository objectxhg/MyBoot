package com.xhg.service;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.sysUser;


public interface UserService {
	
public PageInfo findAll(Integer pageNum, Integer pageSize);
	
	public sysUser get(Integer id);
	
	public void insert(sysUser user);
	
	public void update(sysUser user);
	
	public void delete(Integer id);
	
}
