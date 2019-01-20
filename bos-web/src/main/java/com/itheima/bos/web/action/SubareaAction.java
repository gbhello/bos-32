package com.itheima.bos.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.Region;
import com.itheima.bos.domain.Subarea;
import com.itheima.bos.service.ISubareaService;
import com.itheima.bos.utils.FileUtils;
import com.itheima.bos.web.action.base.BaseAction;

@Controller
@Scope("prototype")
public class SubareaAction extends BaseAction<Subarea> {

	@Autowired
	private ISubareaService subareaService;

	/**
	 * 添加分区的方法
	 * 
	 * @return
	 */
	public String add() {
		subareaService.add(model);
		return LIST;
	}

	/**
	 * 分区信息分页查询方法
	 */
	public String pageQuery() {
		// 取得分页对象的离线查询对象，为下面的设置分页查询条件做准备
		DetachedCriteria dc = pageBean.getDetachedCriteria();

		// 获取地址关键字
		String addresskey = model.getAddresskey();
		if (StringUtils.isNotBlank(addresskey)) {
			// 如果地址关键字不为空，根据地址进行模糊查询
			dc.add(Restrictions.like("addresskey", "%" + addresskey + "%"));
		}

		// 获取分区所属的区域对象
		Region region = model.getRegion();
		// 如果区域对象不为空
		if (region != null) {
			String province = region.getProvince();
			String city = region.getCity();
			String district = region.getDistrict();
			dc.createAlias("region", "r");
			// 如果省份不为空
			if (StringUtils.isNotBlank(province)) {
				dc.add(Restrictions.like("r.province", "%" + province + "%"));
			}
			// 如果市不为空
			if (StringUtils.isNotBlank(city)) {
				dc.add(Restrictions.like("r.city", "%" + city + "%"));
			}
			// 如果区不为空
			if (StringUtils.isNotBlank(district)) {
				dc.add(Restrictions.like("r.district", "%" + district + "%"));
			}
		}
		subareaService.pageQuery(pageBean);
		this.java2Json(pageBean, new String[] { "currentPage",
				"detachedCriteria", "pageSize", "decidedzone", "subareas" });
		return NONE;
	}

	public String exportXls() throws IOException {
		// 第一步：查询所有数据
		List<Subarea> list = subareaService.findAll();
		// 第二步：使用POI将数据写道Excel文件中
		// 在内存中创建一个Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 创建一个标签页
		HSSFSheet sheet = workbook.createSheet("分区数据");
		// 创建标题行
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("分区编号");
		headRow.createCell(1).setCellValue("开始编号");
		headRow.createCell(2).setCellValue("结束编号");
		headRow.createCell(3).setCellValue("位置信息");
		headRow.createCell(4).setCellValue("省市区");

		for (Subarea subarea : list) {
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getStartnum());
			dataRow.createCell(2).setCellValue(subarea.getEndnum());
			dataRow.createCell(3).setCellValue(subarea.getPosition());
			dataRow.createCell(4).setCellValue(subarea.getRegion().getName());
		}

		// 使用输出流进行文件下载（一个流、两个头）
		String filename = "分区数据.xls";
		String contentType = ServletActionContext.getServletContext()
				.getMimeType(filename);
		ServletOutputStream out = ServletActionContext.getResponse()
				.getOutputStream();
		ServletActionContext.getResponse().setContentType(contentType);

		// 获取客户端浏览器类型
		String agent = ServletActionContext.getRequest()
				.getHeader("User-Agent");
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		ServletActionContext.getResponse().setHeader("content-disposition",
				"attachment;filename=" + filename);
		workbook.write(out);
		return NONE;
	}
	
	/**
	 * 查询所有未未关联到定区的分区
	 */
	public String listajax(){
		List<Subarea> list = subareaService.findListNotAssociation();
		this.java2Json(list, new String[]{"decidedzone","region"});
		return NONE;
	}
	
	//属性驱动，查询定区id
	private String decidedzoneId;

	public void setDecidedzoneId(String decidedzoneId) {
		this.decidedzoneId = decidedzoneId;
	}
	
	/**
	 * 根据定区id查询关联的分区
	 */
	public String findListByDecidedzoneId(){
		List<Subarea> list = subareaService.findListByDecidedzoneId(decidedzoneId);
		this.java2Json(list, new String[]{"decidedzone","subareas"});
		return NONE;
	}
	
	/**
	 * 查询区域分区分布图数据
	 */
	public String findSubareasGroupByProvince(){
		List<Object> list = subareaService.findSubareasGroupByProvince();
		this.java2Json(list, new String[]{});
		return NONE;
	}

}
