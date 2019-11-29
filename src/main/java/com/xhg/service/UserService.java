package com.xhg.service;

import com.github.pagehelper.PageInfo;
import com.xhg.pojo.User;


public interface UserService {
	
public PageInfo findAll(Integer pageNum, Integer pageSize);

	public User slelectUserByName(String username);
	
	public User get(Integer id);
	
	public void insert(User user);
	
	public void update(User user);
	
	public void delete(Integer id);
	
}
