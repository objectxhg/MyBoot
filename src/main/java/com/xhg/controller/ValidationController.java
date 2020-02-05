package com.xhg.controller;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xhg.pojo.Activity;
import com.xhg.vo.JsonResult;

@RestController
public class ValidationController {
	
	@GetMapping("/pattern")
	public JsonResult add(@Valid Activity act, BindingResult bind){
		
		if(bind.hasErrors()){
			for(ObjectError error : bind.getAllErrors()){
				return JsonResult.fail(error.getDefaultMessage());
			}
		}
		
		return JsonResult.success("成功！");
	}
}
