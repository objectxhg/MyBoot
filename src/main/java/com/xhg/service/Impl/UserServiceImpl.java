package com.xhg.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xhg.mapper.SysUserMapper;
import com.xhg.pojo.sysUser;
import com.xhg.service.UserService;
import com.xhg.utils.RedisUtil;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService{
	
	@Resource
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private RedisUtil redisUtil;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public PageInfo findAll(Integer pageNum,Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);
		List<sysUser> list = sysUserMapper.findAll();
		System.out.println(list);
		PageInfo<sysUser> pagelist = PageInfo.of(list);
		return pagelist;
	}

	@Override
	public sysUser get(Integer id) {
		sysUser user = null;
		String redisJson = (String) redisUtil.get("id"+id);
		
		try {
			
			if(StringUtils.isEmpty(redisJson)) {
				user = sysUserMapper.selectUserById(id);
				redisUtil.set("id"+id, objectMapper.writeValueAsString(user), 30L);
			}else {
				user = objectMapper.readValue(redisJson, sysUser.class);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return user;
	}

	@Override
	public void insert(sysUser user) {
		sysUserMapper.insert(user);
	}

	@Override
	public void update(sysUser user) {
		sysUserMapper.update(user);
	}

	@Override
	public void delete(Integer id) {
		sysUserMapper.delete(id);
	}

	@Override
	public Integer addUser(sysUser user) {
		return sysUserMapper.addUser(user);
	}

	@Override
	public Integer incrUserIntegral(Integer userId) {

		return sysUserMapper.incrUserIntegral(userId);
	}

	@Override
	public sysUser getUserInfo(Integer userId) {

		return sysUserMapper.getUserInfo(userId);
	}
}
