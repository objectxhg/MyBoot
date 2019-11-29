package com.xhg.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhg.mapper.UserMapper;
import com.xhg.pojo.User;
import com.xhg.service.UserService;
import com.xhg.utils.RedisUtil;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public PageInfo findAll(Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<User> list = userMapper.findAll();
		
		PageInfo<User> pagelist = PageInfo.of(list);
		return pagelist;
	}

	@Override
	public User get(Integer id) {
		User user = null;
		String redisJson = (String) redisUtil.get("id"+id);
		
		try {
			
			if(StringUtils.isEmpty(redisJson)) {
				user = userMapper.get(id);
				redisUtil.set("id"+id, objectMapper.writeValueAsString(user), 30l);
			}else {
				user = objectMapper.readValue(redisJson, User.class);
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public void insert(User user) {
		userMapper.insert(user);
	}

	@Override
	public void update(User user) {
		userMapper.update(user);
	}

	@Override
	public void delete(Integer id) {
		userMapper.delete(id);
	}

	@Override
	public User slelectUserByName(String userName) {
		
		
		
		return null;
	}
}
