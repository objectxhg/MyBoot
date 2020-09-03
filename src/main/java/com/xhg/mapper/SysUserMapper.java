package com.xhg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xhg.pojo.sysUser;


public interface SysUserMapper {
	
	public sysUser selectUserById(Integer id);
	
	public List<sysUser> findAll();
	
	@Select("select * from sys_user where id=#{id}")
	public sysUser get(@Param("id") Integer id);
	
	@Insert("insert into sys_user (name,birthday,address)"
			+ " values (#{name},#{birthday},#{address})")
	public void insert(sysUser user);
	
	@Update("update sys_user "
			+ " set name=#{name},birthday=#{birthday},address=#{address}"
			+ " where id=#{id}")
	public void update(sysUser user);
	
	@Delete("delete from sys_user where id=#{id}")
	public void delete(@Param("id") Integer id);

	Integer incrUserIntegral (Integer userId);

	Integer updateName(@Param("name") String name, @Param("id") Integer id);
}
