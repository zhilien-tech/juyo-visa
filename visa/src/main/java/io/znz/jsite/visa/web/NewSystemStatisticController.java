/**
 * SystemStatisticController.java
 * io.znz.jsite.visa.web
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.web;

import io.znz.jsite.visa.form.NewSysStatisticSqlForm;
import io.znz.jsite.visa.service.NewSystemStatisticService;

import org.nutz.dao.Dao;
import org.nutz.dao.pager.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.web.base.page.Pagination;

/**
 * 系统平台 统计列表
 * <p>
 *
 * @author   彭辉
 * @Date	 2017年8月22日 	 
 */
@Controller
@RequestMapping("visa/systemstatistic")
public class NewSystemStatisticController {

	@Autowired
	protected IDbDao dbDao;
	@Autowired
	protected Dao nutDao;

	@Autowired
	private NewSystemStatisticService newSystemStatisticService;

	//统计列表
	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(@RequestBody NewSysStatisticSqlForm form) {
		Pager pager = new Pager();
		int sqsCount = 0; //送签社记录数
		int djsCount = 0; //地接社记录数
		pager.setPageNumber(form.getPageNumber());
		pager.setPageSize(form.getPageSize());
		Pagination listPage = newSystemStatisticService.listPage(form, pager);
		/*	int recordCount = listPage.getRecordCount(); //总记录数

			String comIds = "";
			List<String> comIdList = new ArrayList<String>();
			List<NewOrderJpEntity> orderList = nutDao.query(NewOrderJpEntity.class, null);
			for (NewOrderJpEntity order : orderList) {
				long comid = order.getComId();
				comIds += comid + ",";
				comIdList.add(comid + "");
			}

			if (comIds.length() > 1) {
				comIds = comIds.substring(0, comIds.length() - 1);
				List<CompanyEntity> compList = nutDao.query(CompanyEntity.class, Cnd.where("id", "in", comIds));
				for (CompanyEntity comp : compList) {
					if (comp.getComType() == 1) {
						String ids = comp.getId() + "";
						for (String oComId : comIdList) {
							if (ids.endsWith(oComId)) {
								//送签社数量
								sqsCount++;
							}
						}
					}
				}

			}
			djsCount = recordCount - sqsCount;*/
		return listPage;
	}

	//下拉框初始化
	@RequestMapping(value = "compSelectfind")
	@ResponseBody
	public Object compSelectfind(int compType) {
		return newSystemStatisticService.compSelectfind(compType);
	}
}
