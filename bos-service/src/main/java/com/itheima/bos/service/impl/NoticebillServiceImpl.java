package com.itheima.bos.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.IDecidedzoneDao;
import com.itheima.bos.dao.INoticebillDao;
import com.itheima.bos.dao.IWorkbillDao;
import com.itheima.bos.domain.Decidedzone;
import com.itheima.bos.domain.Noticebill;
import com.itheima.bos.domain.Staff;
import com.itheima.bos.domain.User;
import com.itheima.bos.domain.Workbill;
import com.itheima.bos.service.INoticebillService;
import com.itheima.bos.utils.BOSUtils;
import com.itheima.crm.ICustomerService;

@Service
@Transactional
public class NoticebillServiceImpl implements INoticebillService {

	@Autowired
	private INoticebillDao noticebillDao;
	@Autowired
	private ICustomerService customerService;
	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private IWorkbillDao workbillDao;

	/**
	 * 保存业务通知单，尝试自动分单
	 */
	@Override
	public void save(Noticebill model) {
		User user = BOSUtils.getLoginUser();
		model.setUser(user);// 业务通知单关联客服
		noticebillDao.save(model);
		String address = model.getPickaddress(); // 拿到寄件人地址
		String decidedzoneId = customerService
				.findDecidedzoneIdByAddress(address); // 根据地址拿到定区id
		if (decidedzoneId != null) {
			// 拿到定区id，可以进行自动分单
			model.setOrdertype(Noticebill.ORDERTYPE_AUTO);// 设置分单类型为自动分单
			Decidedzone decidedzone = decidedzoneDao.findById(decidedzoneId);
			Staff staff = decidedzone.getStaff();
			model.setStaff(staff);// 业务通知单关联取派员对象

			// 为员工产生一个工单
			Workbill workbill = new Workbill();
			workbill.setAttachbilltimes(0);// 设置追单次数
			workbill.setBuildtime(new Timestamp(System.currentTimeMillis()));// 创建时间，当前系统时间
			workbill.setNoticebill(model);// 工单关联页面通知单
			workbill.setPickstate(Workbill.PICKSTATE_NO);// 取件状态
			workbill.setRemark(model.getRemark());// 备注信息
			workbill.setStaff(staff);// 工单关联取派员
			workbill.setType(Workbill.TYPE_1);// 工单类型
			workbillDao.save(workbill);
			// 调用短信平台，发送短信
		} else {
			// 没有拿到定区id，不能进行自动分单
			model.setOrdertype(Noticebill.ORDERTYPE_MAN);
		}
	}
}
