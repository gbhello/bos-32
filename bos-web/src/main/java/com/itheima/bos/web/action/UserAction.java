package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.User;
import com.itheima.bos.service.IUserService;
import com.itheima.bos.utils.BOSUtils;
import com.itheima.bos.utils.MD5Utils;
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
			//验证码输入正确，使用shiro提供的方式进行认证
			//获得shiro框架提供的subject对象
			Subject subject = SecurityUtils.getSubject();//代表当前用户对象，状态为“未认证”
			//创建一个用户名密码令牌
			UsernamePasswordToken token = new UsernamePasswordToken(model.getUsername(), MD5Utils.md5(model.getPassword()));
			try {
				subject.login(token);
				//获取user对象
				User user = (User) subject.getPrincipal();
				//登陆成功，将user对象放入session，跳转到系统首页
				ServletActionContext.getRequest().getSession().setAttribute("loginUser", user);
			} catch (Exception e) {
				this.addActionError("用户名或者密码错误！");
				e.printStackTrace();
				return LOGIN;
			}
			return HOME;
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
	
	//属性驱动，接收多个角色id
	private String[] roleIds;

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	
	/**
	 * 添加用户
	 */
	public String add(){
		userService.save(model,roleIds);
		return LIST;
	}
	
	/**
	 * 用户数据分页查询
	 */
	public String pageQuery(){
		userService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[]{"noticebills","roles"});
		return NONE;
	}

}
