/**
 * SimulateJapanViewService.java
 * io.znz.jsite.visa.simulator.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.service;

import io.znz.jsite.base.NutzBaseService;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.download.impl.QiniuUploadServiceImpl;
import io.znz.jsite.visa.entity.japan.NewComeBabyJpEntity;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.usa.NewCustomerEntity;
import io.znz.jsite.visa.enums.ErrorCodeEnum;
import io.znz.jsite.visa.enums.VisaJapanApproStatusEnum;
import io.znz.jsite.visa.simulator.form.JapanErrorHandleForm;
import io.znz.jsite.visa.simulator.form.JapanSimulatorForm;
import io.znz.jsite.visa.util.Const;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.lang.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uxuexi.core.common.util.Util;
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

	/**查询第一个可提交签证网站的日本客户信息*/
	@Transactional
	public ResultObject fetchJapanOrder() {
		/*List<NewOrderJpEntity> orderListSubmiting = dbDao.query(NewOrderJpEntity.class,
				Cnd.where("status", "=", VisaJapanApproStatusEnum.japancoming.intKey()), null);
		if (!Util.isEmpty(orderListSubmiting) && orderListSubmiting.size() > 0) {
			Date updatetimeOld = orderListSubmiting.get(0).getUpdatetime();
			Date newTime = new Date();
			int minutes = (int) (newTime.getTime() - updatetimeOld.getTime()) / 1000 / 60;
			if (minutes > 15) {
				//如果没响应将订单状态改为提交失败
				dbDao.update(NewOrderJpEntity.class,
						Chain.make("updatetime", new Date()).add("status", VisaJapanApproStatusEnum.fail.intKey()),
						Cnd.where("id", "=", orderListSubmiting.get(0).getId()));
				ResultObject japanOrderInfo = getJapanOrderInfo();
				return japanOrderInfo;
			}
		} else {*/
		ResultObject japanOrderInfo = getJapanOrderInfo();
		return japanOrderInfo;
		/*	}

			return ResultObject.fail("暂无任务");*/
	}

	@SuppressWarnings("null")
	@Transactional
	public ResultObject getJapanOrderInfo() {
		List<NewOrderJpEntity> orderList = dbDao.query(NewOrderJpEntity.class,
				Cnd.where("status", "=", VisaJapanApproStatusEnum.readySubmit.intKey()), null);
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
			long sendComId = order.getSendComId();
			if (!Util.isEmpty(sendComId) && sendComId > 0) {
				NewComeBabyJpEntity comeBaby = dbDao.fetch(NewComeBabyJpEntity.class, sendComId);
				map.put("visaAccount", "1507-001");
				map.put("visaPasswd", "kintsu0821");
				map.put("agentNo", comeBaby.getCompletedNumber());
				if (!Util.isEmpty(order)) {
					map.put("visaType1", order.getVisatype() + "");
					map.put("VISA_STAY_PREF_2", false);
					map.put("VISA_STAY_PREF_3", false);
					map.put("VISA_STAY_PREF_4", false);
					map.put("VISA_STAY_PREF_5", false);
					map.put("VISA_STAY_PREF_6", false);
					map.put("VISA_STAY_PREF_7", false);
					map.put("VISA_STAY_PREF_47", false);
					String sixnum = order.getSixnum();
					if (!Util.isEmpty(sixnum)) {
						String[] split = sixnum.split(",");
						if (split.length > 0) {
							for (int i = 0; i < split.length; i++) {
								map.put("VISA_STAY_PREF_" + split[i], true);
							}
						}
					}
				}
			} else {

				map.put("visaAccount", "1507-001");
				map.put("visaPasswd", "kintsu0821");
				map.put("agentNo", " ");

				if (!Util.isEmpty(order)) {
					map.put("visaType1", order.getVisatype() + "");
					map.put("VISA_STAY_PREF_2", false);
					map.put("VISA_STAY_PREF_3", false);
					map.put("VISA_STAY_PREF_4", false);
					map.put("VISA_STAY_PREF_5", false);
					map.put("VISA_STAY_PREF_6", false);
					map.put("VISA_STAY_PREF_7", false);
					map.put("VISA_STAY_PREF_47", false);
					String sixnum = order.getSixnum();
					if (!Util.isEmpty(sixnum)) {
						String[] split = sixnum.split(",");
						if (split.length > 0) {
							for (int i = 0; i < split.length; i++) {
								map.put("VISA_STAY_PREF_" + split[i], true);
							}
						}
					}
				}
			}

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
				map.put("applicantCnt", (customerList.size() - 1) + "");//除申请人之外的人数
			}

			if (!Util.isEmpty(order)) {

				map.put("excelUrl", order.getExcelurl());//excel地址
			} else {
				map.put("excelUrl", "");//excel地址

			}
			if (!Util.isEmpty(map)) {
				ResultObject ro = ResultObject.success(map);
				String ordernumber = orderList.get(0).getOrdernumber();

				ro.addAttribute("oid", orderid + "");
				/*	if (!Util.isEmpty(ordernumber)) {
						ro.addAttribute("oid", ordernumber);

					} else {
						ro.addAttribute("oid", "");

					}*/
				return ro;
			}
		}
		return ResultObject.fail("暂无任务");
	}

	@Transactional
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
			if (Util.isEmpty(status) || VisaJapanApproStatusEnum.readySubmit.intKey() != status) {
				return ResultObject.fail("准备提交使馆的任务方可提交！");
			}

			//签证状态改为'提交中'
			dbDao.update(NewOrderJpEntity.class, Chain.make("status", VisaJapanApproStatusEnum.japancoming.intKey())
					.add("updatetime", new Date()), Cnd.where("id", "=", cid));
		} catch (Exception e) {
			return ResultObject.fail("提交失败,请稍后重试！");
		}
		return ResultObject.success(order);

	}

	@Transactional
	public Object UploadJapan(MultipartFile file, Long cid, JapanSimulatorForm jpForm) {
		if (Util.isEmpty(cid)) {
			return ResultObject.fail("任务id不能为空！");
		}

		NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, cid);

		if (Util.isEmpty(order)) {
			return ResultObject.fail("任务不存在！");
		}
		//保存受付番号
		order.setCompletedNumber(jpForm.getAcceptanceNumber());
		dbDao.update(order, null);

		String visaFile = null;
		try {
			InputStream inputStream = file.getInputStream();
			String originalFilename = file.getOriginalFilename();
			String suffix = Files.getSuffix(originalFilename);
			if (Util.isEmpty(suffix) || suffix.length() < 2) {
				return ResultObject.fail("文件名错误");
			}
			Integer status = order.getStatus();
			//验证提交状态
			if (Util.isEmpty(status) || VisaJapanApproStatusEnum.japancoming.intKey() != status) {
				return ResultObject.fail("已提交的任务方可进行文件上传！");
			}
			suffix = suffix.substring(1);
			visaFile = Const.IMAGES_SERVER_ADDR + qiniuUploadService.uploadImage(inputStream, suffix, null);

			//为客户设置文件地址，签证状态改为'已提交'
			order.setStatus(VisaJapanApproStatusEnum.japanAlreadySend.intKey());
			order.setFileurl(visaFile);
			dbDao.update(order);
		} catch (Exception e) {
			return ResultObject.fail("文件上传失败,请稍后重试！");
		}
		return ResultObject.success(visaFile);

	}

	/**
	 * 代理下载文件
	 * @param jpForm
	 * @param response 
	 */
	@Transactional
	public void agentDownload(final JapanSimulatorForm jpForm, HttpServletResponse response) {
		InputStream is = null;
		OutputStream out = null;
		try {
			String fileUrl = jpForm.getFileUrl();
			URL url = new URL(fileUrl);
			URLConnection conn = url.openConnection();
			is = conn.getInputStream();

			String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
			if (Util.isEmpty(filename)) {//如果获取不到文件名称 
				for (int i = 0;; i++) {
					String mine = conn.getHeaderField(i);
					if (mine == null)
						break;
					if ("content-disposition".equals(conn.getHeaderFieldKey(i).toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
						if (m.find()) {
							filename = m.group(1);
						}
					}
				}
			}
			if (Util.isEmpty(filename)) {
				filename = UUID.randomUUID() + ".tmp";//默认取一个文件名	
			}
			out = response.getOutputStream();
			response.reset();
			response.setContentType("application/octet-stream");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", filename));
			byte[] buffer = new byte[2048];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				out.write(buffer, 0, count);
			}
			out.flush();
			response.flushBuffer();
			out.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!Util.isEmpty(is)) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (!Util.isEmpty(out)) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//end of outter try
	}

	@Transactional
	public void japanErrorHandle(JapanErrorHandleForm jpForm, HttpServletResponse response, Long cid) {
		if (!Util.isEmpty(cid) && cid > 0) {
			NewOrderJpEntity order = dbDao.fetch(NewOrderJpEntity.class, cid);
			int errorCode = jpForm.getErrorCode();
			String errorMsg = jpForm.getErrorMsg();
			order.setErrorCode(errorCode);
			order.setErrorMsg(errorMsg);
			dbDao.update(order, null);

			if (errorCode == ErrorCodeEnum.completedNumberFail.intKey()) {
				dbDao.update(NewOrderJpEntity.class,
						Chain.make("updatetime", new Date()).add("status", VisaJapanApproStatusEnum.fail.intKey()),
						Cnd.where("id", "=", cid.longValue()));
			} else if (errorCode == ErrorCodeEnum.persionNameList.intKey()) {
				dbDao.update(
						NewOrderJpEntity.class,
						Chain.make("updatetime", new Date()).add("status",
								VisaJapanApproStatusEnum.japanSendFail.intKey()), Cnd.where("id", "=", cid.longValue()));
			} else if (errorCode == ErrorCodeEnum.comeReport.intKey()) {
				dbDao.update(
						NewOrderJpEntity.class,
						Chain.make("updatetime", new Date()).add("status",
								VisaJapanApproStatusEnum.japanReportFail.intKey()),
						Cnd.where("id", "=", cid.longValue()));

			} else {
				dbDao.update(NewOrderJpEntity.class,
						Chain.make("updatetime", new Date()).add("status", VisaJapanApproStatusEnum.fail.intKey()),
						Cnd.where("id", "=", cid.longValue()));

			}

		}
	}
}
