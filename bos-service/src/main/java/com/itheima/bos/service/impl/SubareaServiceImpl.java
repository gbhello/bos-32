package com.itheima.bos.service.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.ISubareaDao;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.ISubareaService;
import com.itheima.bos.utils.PageBean;

@Service
@Transactional
public class SubareaServiceImpl implements ISubareaService {

	@Autowired
	private ISubareaDao subareaDao;

	/**
	 * 添加分区方法
	 */
	@Override
	public void add(Subarea model) {
		subareaDao.saveOrUpdate(model);
	}

	/**
	 * 分区分页查询方法
	 */
	@Override
	public void pageQuery(PageBean pageBean) {
		subareaDao.pageQuery(pageBean);
	}

	/**
	 * 查询所有分区
	 */
	@Override
	public List<Subarea> findAll() {
		return subareaDao.findAll();
	}

	/**
	 * 查询所有未关联到定区的分区
	 */
	@Override
	public List<Subarea> findListNotAssociation() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subarea.class);
		//添加过滤条件，分区对象中decidedzone属性未null
		detachedCriteria.add(Restrictions.isNull("decidedzone"));
		return subareaDao.findByCriteria(detachedCriteria);
	}

	/**
	 * 根据定区id查询关联的分区
	 */
	@Override
	public List<Subarea> findListByDecidedzoneId(String decidedzoneId) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Subarea.class);
		//添加过滤条件
		detachedCriteria.add(Restrictions.eq("decidedzone.id", decidedzoneId));
		return subareaDao.findByCriteria(detachedCriteria);
	}

	@Override
	public List<Object> findSubareasGroupByProvince() {
		List<Object> list = subareaDao.findSubareasGroupByProvince();
		return list;
	}

	/**
	 * 查询区域分区分布图数据
	 */
	

	

}
