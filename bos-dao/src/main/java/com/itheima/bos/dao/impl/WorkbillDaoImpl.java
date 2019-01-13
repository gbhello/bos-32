package com.itheima.bos.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.itheima.bos.dao.IWorkbillDao;
import com.itheima.bos.dao.base.impl.BaseDaoImpl;
import com.itheima.bos.domain.Workbill;
import com.itheima.bos.utils.PageBean;
@Repository
public class WorkbillDaoImpl extends BaseDaoImpl<Workbill> implements IWorkbillDao {

}
