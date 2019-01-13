package com.itheima.bos.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IDecidedzoneDao;
import com.itheima.bos.dao.ISubareaDao;
import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.IDecidedzoneService;
import com.itheima.bos.utils.PageBean;
@Service
@Transactional
public class DecidedzoneServiceImpl implements IDecidedzoneService {

	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private ISubareaDao subareaDao;
	@Override
	public void save(Decidedzone model, String[] subareaid) {

		for (String id : subareaid) {
			Subarea subarea = subareaDao.findById(id);
			subarea.setDecidedzone(model);
		}
		decidedzoneDao.save(model);
	}
	
	/**
	 * 定区分页查询方法
	 */
	@Override
	public void pageQuery(PageBean pageBean) {
		decidedzoneDao.pageQuery(pageBean);
	}

}
