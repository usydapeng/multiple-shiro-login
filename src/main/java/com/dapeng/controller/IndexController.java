package com.dapeng.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	@RequestMapping("/index")
	@ResponseBody
	public String index(){

		return "hello index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){

		return "login";
	}

	@RequestMapping("/admin/index")
	@ResponseBody
	public String adminIndex(){

		return "hello admin index";
	}

	@RequestMapping(value = "/admin/login", method = RequestMethod.GET)
	public String adminLogin(){

		return "admin_login";
	}
}
