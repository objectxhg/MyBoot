package com.xhg.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.xhg.pojo.sysUser;


public interface SysUserMapper {
	
	@Select("select * from sys_user where id=#{id}")
	sysUser get(@Param("id") Integer id);

	@Insert("insert into sys_user (name,birthday,address)"
			+ " values (#{name},#{birthday},#{address})")
	void insert(sysUser user);
	
	@Update("update sys_user "
			+ " set name=#{name},birthday=#{birthday},address=#{address}"
			+ " where id=#{id}")
	void update(sysUser user);
	
	@Delete("delete from sys_user where id=#{id}")
	void delete(@Param("id") Integer id);

	Integer addUser (sysUser user);

	sysUser selectUserById(Integer id);

	List<sysUser> findAll();

	Integer incrUserIntegral (Integer userId);

	Integer updateName(@Param("name") String name, @Param("id") Integer id);
}
