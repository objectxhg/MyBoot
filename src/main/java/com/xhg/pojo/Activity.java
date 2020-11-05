package com.xhg.pojo;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Activity {
	
	
	private Integer id;
	
	@NotBlank(message="姓名不能为空")
	private String  realName;
	
	@Pattern(regexp="^[1][3,4,5,7,8][0-9]{9}$", message="手机号错误")
	private String  telephone;
	
	@Length(min = 10, max = 50, message="简介字数限制在10-50之间")
	private String  introduce;
	
	@Email(message="邮箱错误不能为空")
	private String  email;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@Past(message="生日不符合要求")
	private Date birthday;
	
	
}
