package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;

import com.itheima.bos.domain.Region;
import com.itheima.bos.utils.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	public static final String HOME = "home";
	public static final String LIST = "list";
	// 模型对象
	protected T model;

	@Override
	public T getModel() {
		return model;
	}
	
	//分页重构代码
	protected PageBean pageBean = new PageBean();
	DetachedCriteria detachedCriteria = null;
	
	public void setPage(int page) {
		pageBean.setCurrentPage(page);
	}
	
	public void setRows(int rows) {
		pageBean.setPageSize(rows);
	}
	
	/**
	 * 把指定java对象转为json对象，并相应到客户端页面（针对单个java对象）
	 * @param o
	 * @param excludes
	 */
	public void java2Json(Object o,String[] excludes){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		String json = JSONObject.fromObject(o,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 把指定java对象集合转为json对象，并响应都客户端页面（针对java对象集合）
	 * @param o
	 * @param excludes
	 */
	public void java2Json(List o,String[] excludes){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		String json = JSONArray.fromObject(o,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 在构造方法中动态的获取实体类型，通过反射创建model对象
	public BaseAction() {
		ParameterizedType genericSuperclass = (ParameterizedType) this
				.getClass().getGenericSuperclass();
		// 获得BaseAction上声明的泛型数组
		Type[] actualTypeArguments = genericSuperclass.getActualTypeArguments();
		Class<T> entityClass = (Class<T>) actualTypeArguments[0];
		detachedCriteria = DetachedCriteria.forClass(entityClass);
		pageBean.setDetachedCriteria(detachedCriteria);
		// 创建反射对象
		try {
			model = entityClass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
