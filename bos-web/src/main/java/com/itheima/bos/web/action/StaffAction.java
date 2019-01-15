package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Staff;
import com.itheima.bos.service.IStaffService;
import com.itheima.bos.utils.PageBean;
import com.itheima.bos.web.action.base.BaseAction;

@Controller
@Scope("prototype")
public class StaffAction extends BaseAction<Staff> {

	@Autowired
	private IStaffService staffService;

	/**
	 * 添加取派员
	 */
	public String add() {
		staffService.save(model);
		return LIST;
	}

	// 属性驱动，接受页面提交的分页参数
	private int page;
	private int rows;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	/**
	 * 分页查询方法
	 * 
	 * @return
	 * @throws IOException
	 */
	public String pageQuery() throws IOException {
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(page);
		pageBean.setPageSize(rows);
		// 创建离线提交查询对象
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(Staff.class);
		pageBean.setDetachedCriteria(detachedCriteria);
		//对传递来的pageBean对象进行一系列的加工，转了一圈之后，pageBean对象的各个属性就都有了
		staffService.pageQuery(pageBean);

		// 使用json-lib将PageBean对象转为json，通过输出流写回页面中
		// JSONObject---将单一对象转为json
		// JSONArray---将数组或者集合对象转为json
		JsonConfig jsonConfig = new JsonConfig();
		// 指定哪些属性不需要转json
		jsonConfig.setExcludes(new String[] { "currentPage",
				"detachedCriteria", "pageSize","decidedzones" });
		String json = JSONObject.fromObject(pageBean, jsonConfig).toString();
		ServletActionContext.getResponse().setContentType(
				"text/json;charset=utf-8");
		ServletActionContext.getResponse().getWriter().print(json);
		return NONE;
	}

	/**
	 * 取派员的批量删除
	 */
	// 属性驱动，接收页面提交的ids参数
	private String ids;

	@RequiresPermissions("staff-delete")//执行这个方法，需要当前用户具有staff-delete这个权限
	public String deleteBatch() {
		staffService.deleteBatch(ids);
		return LIST;
	}

	/**
	 * 修改取派员信息
	 */

	public String edit() {
		// 先查询数据库，根据id查询原始数据
		Staff staff = staffService.findById(model.getId());
		// 使用页面提交的数据进行覆盖
		staff.setName(model.getName());
		staff.setTelephone(model.getTelephone());
		staff.setHaspda(model.getHaspda());
		staff.setStandard(model.getStandard());
		staff.setStation(model.getStation());
		staffService.update(staff);
		return LIST;
	}
	
	/**
	 * 查询缩头未删除的取派员，返回json
	 */
	public String listajax(){
		List<Staff> list = staffService.findListNotDelete();
		this.java2Json(list, new String[]{"decidedzones"});
		return NONE;
	}
}
