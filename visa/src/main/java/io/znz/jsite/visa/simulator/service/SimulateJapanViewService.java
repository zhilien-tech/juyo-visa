/**
 * SimulateJapanViewService.java
 * io.znz.jsite.visa.simulator.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.download.impl.QiniuUploadServiceImpl;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.enums.OrderVisaApproStatusEnum;
import io.znz.jsite.visa.util.Const;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.SqlManager;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;
import com.uxuexi.core.db.util.DbSqlUtil;

/**
 * TODO(这里用一句话描述这个类的作用)
 * <p>
 * TODO(这里描述这个类补充说明 – 可选)
 *
 * @author   孙斌
 * @Date	 2017年8月8日 	 
 */
@Service
public class SimulateJapanViewService extends NutzBaseService<NewCustomerEntity> {
	@Autowired
	private QiniuUploadServiceImpl qiniuUploadService;
	@Autowired
	protected SqlManager sqlManager;
	@Autowired
	protected Dao nutDao;
	@Autowired
	protected IDbDao dbDao;

	/**查询第一个可提交签证网站的日本客户信息*/
	public ResultObject fetchJapanOrder() {
		List<NewOrderJpEntity> orderList = dbDao.query(NewOrderJpEntity.class,
				Cnd.where("status", "=", OrderVisaApproStatusEnum.DS.intKey()), null);
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		if (!Util.isEmpty(orderList) && orderList.size() > 0) {
			String sqlString = sqlManager.get("newcustomerjapan_list");
			Sql sql = Sqls.create(sqlString);
			long orderid = orderList.get(0).getId();
			sql.setParam("orderId", orderid);
			List<NewCustomerJpEntity> customerList = DbSqlUtil.query(dbDao, NewCustomerJpEntity.class, sql);
			Map<String, Object> map = new TreeMap<String, Object>();
			//处理出行信息中出入境时间
			NewOrderJpEntity order = null;
			if (!Util.isEmpty(orderid) && orderid > 0) {
				order = dbDao.fetch(NewOrderJpEntity.class, orderid);
				List<NewTripJpEntity> list = dbDao.query(NewTripJpEntity.class, Cnd.where("order_jp_id", "=", orderid),
						null);
				if (!Util.isEmpty(list.get(0))) {
					NewTripJpEntity tripJpEntity = list.get(0);
					int oneormore = tripJpEntity.getOneormore();
					if (oneormore == 0) {
						Date startdate = tripJpEntity.getStartdate();
						if (!Util.isEmpty(startdate)) {
							map.put("entryDate", df.format(startdate));
						} else {

						}
						Date returndate = tripJpEntity.getReturndate();
						if (!Util.isEmpty(returndate)) {
							map.put("leaveDate", df.format(returndate));

						} else {

						}
					} else if (oneormore == 1) {
						List<NewDateplanJpEntity> dateplanList = dbDao.query(NewDateplanJpEntity.class,
								Cnd.where("trip_jp_id", "=", tripJpEntity.getId()), null);
						if (!Util.isEmpty(dateplanList) && dateplanList.size() > 0) {
							Date startdate = dateplanList.get(0).getStartdate();
							if (!Util.isEmpty(startdate)) {
								map.put("entryDate", df.format(startdate));
							} else {

							}
							Date returndate = dateplanList.get(dateplanList.size() - 1).getStartdate();
							if (!Util.isEmpty(returndate)) {
								map.put("leaveDate", df.format(returndate));

							} else {

							}
						}
					}
				}

			}

			map.put("visaAccount", "1507-001");
			map.put("visaPasswd", "kintsu2017");
			map.put("agentNo", "GTP-BJ-084-0");
			map.put("visaType1", 1);

			/*	map.put("visaType2", );*/
			if (!Util.isEmpty(customerList) && customerList.size() > 0) {
				NewCustomerJpEntity customer = customerList.get(0);
				if (!Util.isEmpty(customer)) {

					map.put("proposerNameCN", customer.getChinesefullname());
				}
				String chinesexingen = customer.getChinesexingen();
				if (Util.isEmpty(chinesexingen)) {
					chinesexingen = "";
				}
				String chinesenameen = customer.getChinesenameen();
				if (Util.isEmpty(chinesenameen)) {
					chinesenameen = "";
				}
				map.put("proposerNameEN", chinesexingen + chinesenameen);
				map.put("applicantCnt", customerList.size() - 1);//除申请人之外的人数
			}

			if (!Util.isEmpty(order)) {

				map.put("excelUrl", order.getExcelurl());//excel地址
			} else {
				map.put("excelUrl", "");//excel地址

			}
			if (!Util.isEmpty(map)) {
				ResultObject ro = ResultObject.success(map);

				ro.addAttribute("oid", orderid);

				return ro;
			}
		}

		return ResultObject.fail("暂无任务");
	}

	public Object ds160Japan(Long cid) {

		if (Util.isEmpty(cid)) {
			return ResultObject.fail("任务id不能为空！");
		}

		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, cid);
		if (Util.isEmpty(order)) {
			return ResultObject.fail("任务不存在！");
		}

		try {
			Integer status = order.getStatus();
			//验证提交状态
			if (Util.isEmpty(status) || OrderVisaApproStatusEnum.readySubmit.intKey() != status) {
				return ResultObject.fail("准备提交使馆的任务方可提交ds160！");
			}

			//签证状态改为'提交中'
			dbDao.update(NewOrderJpEntity.class, Chain.make("status", OrderVisaApproStatusEnum.submiting.intKey()),
					Cnd.where("id", "=", cid));
		} catch (Exception e) {
			return ResultObject.fail("ds160提交失败,请稍后重试！");
		}
		return ResultObject.success(order);

	}

	public Object UploadJapan(MultipartFile file, Long cid) {
		if (Util.isEmpty(cid)) {
			return ResultObject.fail("任务id不能为空！");
		}

		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, cid);
		if (Util.isEmpty(order)) {
			return ResultObject.fail("任务不存在！");
		}

		String visaFile = null;
		try {
			InputStream inputStream = file.getInputStream();
			visaFile = Const.IMAGES_SERVER_ADDR + qiniuUploadService.uploadImage(inputStream, "zip", null);

			Integer status = order.getStatus();
			//验证提交状态
			if (Util.isEmpty(status) || OrderVisaApproStatusEnum.submiting.intKey() != status) {
				return ResultObject.fail("已提交ds160的任务方可进行文件上传！");
			}

			//为客户设置文件地址，签证状态改为'已提交'
			order.setStatus(OrderVisaApproStatusEnum.submited.intKey());
			order.setFileurl(visaFile);
			dbDao.update(order);
		} catch (Exception e) {
			return ResultObject.fail("文件上传失败,请稍后重试！");
		}
		return ResultObject.success(visaFile);

	}
}
