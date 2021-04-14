package com.xhg.service.Impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

	/**
	 *
	 * @param pageNum
	 * @param pageSize
	 *
	 * key 和 keyGenerator 不能同时存在 一个是自定义key 一个是根据方法路径配置好key
	 *
	 * unless 返回值条件判断 不缓存的
	 *
	 * condition 参数列表条件判断 缓存符合条件的
	 *
	 * @return
	 */
	@Cacheable(cacheNames="userList", keyGenerator = "keyGenerator", unless = "#result.list.size() == 0")
	@Override
	public PageInfo findAll(Integer pageNum,Integer pageSize) {

		PageHelper.startPage(pageNum, pageSize);

		List<sysUser> list = sysUserMapper.findAll();

		PageInfo<sysUser> pagelist = PageInfo.of(list);

		return pagelist;
	}

	@Override
	@Cacheable(cacheNames="getUser", key = "'id-' + #id", condition = "#id > 2")
	public sysUser get(Integer id) {

		sysUser user =  user = sysUserMapper.selectUserById(id);
		
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
	@CacheEvict
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
