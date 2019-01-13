package com.itheima.bos.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.service.IDecidedzoneService;
import com.itheima.bos.web.action.base.BaseAction;
import com.itheima.crm.Customer;
import com.itheima.crm.ICustomerService;
@Controller
@Scope("prototype")
public class DecidedzoneAction extends BaseAction<Decidedzone> {
	
	@Autowired
	private IDecidedzoneService decidedzoneService;
	
	//属性驱动，接收页面传递过来的subareaid属性
	private String[] subareaid;
	public void setSubareaid(String[] subareaid) {
		this.subareaid = subareaid;
	}
	
	/**
	 * 添加定区
	 */
	public String add(){
		decidedzoneService.save(model,subareaid);
		return LIST;
	}
	
	/**
	 * 分页查询方法
	 */
	public String pageQuery(){
		decidedzoneService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","subareas","decidedzones"});
		return NONE;
	}
	
	//注入crm代理对象
	@Autowired
	private ICustomerService proxy;
	/**
	 * 远程调用crm服务，获取未关联到定区的客户
	 */
	public String findListNotAssociation(){
		List<Customer> list = proxy.findListNotAssociation();
		this.java2Json(list, new String[]{});
		return NONE;
	}
	
	/**
	 * 远程调用crm服务，获取已经关联到定区的客户
	 */
	public String findListHasAssociation(){
		String id = model.getId();
		List<Customer> list = proxy.findListHasAssociation(id);
		this.java2Json(list, new String[]{});
		return NONE;
	}
	
	//属性驱动，接收页面提交的多个客户id
	private List<Integer> customerIds;
	public void setCustomerIds(List<Integer> customerIds) {
		this.customerIds = customerIds;
	}

	/**
	 * 远程调用crm服务，将客户关联到定区
	 */
	public String assigncustomerstodecidedzone(){
		proxy.assigncustomerstodecidedzone(model.getId(), customerIds);
		System.out.println(customerIds);
		return LIST;
	}

}
