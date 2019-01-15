package com.itheima.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IFunctionDao;
import com.itheima.bos.domain.Function;
import com.itheima.bos.service.IFunctionService;
import com.itheima.bos.utils.PageBean;
@Service
@Transactional
public class FunctionServiceImpl implements IFunctionService {

	@Autowired
	private IFunctionDao functionDao;
	public List<Function> findAll() {
		return functionDao.findAll();
	}
	
	public void save(Function model) {
		Function parentFunction = model.getParentFunction();
		if(parentFunction!=null && parentFunction.getId().equals("")){
			model.setParentFunction(null);
		}
		functionDao.save(model);
	}

	/**
	 * 分页查询
	 */
	public void pageQuery(PageBean pageBean) {
		functionDao.pageQuery(pageBean);
	}

}
