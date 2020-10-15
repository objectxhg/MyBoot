package com.xhg.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description = "用户信息")
@Data
public class sysUser implements Serializable{

	private static final long serialVersionUID = 1L;
	@ApiModelProperty("用户id")
	private Integer id;
	@ApiModelProperty(value = "用户姓名")
	private String username;
	@ApiModelProperty(value = "密码")
	private String pwd;
	@ApiModelProperty(value = "手机号")
	private String mobile;
	@ApiModelProperty(value = "生日")
	private String birthday;
	@ApiModelProperty(value = "地址")
	private String address;
	@ApiModelProperty(value = "积分")
	private Integer integral;

	@ApiModelProperty(value = "角色")
	private List<Role> roleList = new ArrayList<Role>();


}
