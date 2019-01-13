package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.User;
import com.itheima.bos.service.IUserService;
import com.itheima.bos.utils.BOSUtils;
import com.itheima.bos.web.action.base.BaseAction;
import com.itheima.crm.Customer;
import com.itheima.crm.ICustomerService;

@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {
	// 属性驱动，接收页面收入的验证码
	private String checkcode;

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	@Autowired
	private IUserService userService;

	/**
	 * 用户登录
	 */
	public String login() {
		// 从session中获取生成的验证码
		String validatecode = (String) ServletActionContext.getRequest()
				.getSession().getAttribute("key");
		if (StringUtils.isNoneBlank(checkcode) && checkcode.equals(validatecode)) {
			// 输入的验证码正确
			User user = userService.login(model);
			if (user != null) {
				// 登陆成功，将user对象放入session，跳转到首页
				ServletActionContext.getRequest().getSession()
						.setAttribute("loginUser", user);
				return HOME;
			}else{
				this.addActionError("用户名或者密码输入错误！");
				return LOGIN;
			}

		} else {
			// 输入的验证码错误，设置提示信息，跳转到登录界面
			this.addActionError("输入的验证码错误！");
			return LOGIN;
		}
	}
	
	/**
	 * 用户注销
	 */
	public String logout(){
		ServletActionContext.getRequest().getSession().invalidate();
		return LOGIN;
	}
	
	/**
	 * 修改当前用户密码
	 * @throws IOException 
	 */
	public String editPassword() throws IOException{
		String f = "1";
		//获取当前登陆用户
		User user = BOSUtils.getLoginUser();
		try {
			userService.editPassword(user.getId(),model.getPassword());
		} catch (Exception e) {
			f = "0";
			e.printStackTrace();
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		ServletActionContext.getResponse().getWriter().print(f);
		return NONE;
	}

}
