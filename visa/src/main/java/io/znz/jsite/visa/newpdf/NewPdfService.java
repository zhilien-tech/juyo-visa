package io.znz.jsite.visa.newpdf;

/**
 * Created by Chaly on 2017/4/13.
 */

import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.SpringUtil;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripplanJpEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Service;

import com.ibm.icu.util.Calendar;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

@Service
public class NewPdfService {
	private IDbDao dbDao = SpringUtil.getBean("dbDao");

	//生成临时文件
	private File createTempFile(ByteArrayOutputStream out) {
		try {
			File tmp = new File(FileUtils.getTempDirectory() + "/" + UUID.randomUUID().toString() + ".tmp");
			FileUtils.writeByteArrayToFile(tmp, out.toByteArray());
			IOUtils.closeQuietly(out);
			return tmp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new JSiteException("临时文件输出异常!");
		}
	}

	//导出资料整合的PDF
	public ByteArrayOutputStream export(NewOrderJpEntity order) {
		try {
			NewTemplate t = SpringUtil.getBean(order.getTemplate());
			Map<String, File> fileMap = new HashMap<String, File>();
			List<ByteArrayOutputStream> identity = new ArrayList<ByteArrayOutputStream>();
			//身元保证书
			fileMap.put("身元.docx", createTempFile(t.guarantee(order)));
			//滞在予定表
			identity.add(t.trip(order));
			//査証申請人名簿
			identity.add(t.book(order));
			//合并输出身元
			fileMap.put("滞在予定表.pdf", createTempFile(t.merge(identity)));
			//照会
			List<ByteArrayOutputStream> note = new ArrayList<ByteArrayOutputStream>();
			note.add(t.note(order));
			StringBuilder guest = new StringBuilder();
			//生成申请表
			for (NewCustomerJpEntity customer : order.getCustomerJpList()) {
				guest.append(customer.getChinesexingen()).append(" ").append(customer.getChinesenameen()).append("\n");
				note.add(t.apply(order, customer));
			}
			//酒店确认单
			List<NewTripplanJpEntity> trips = new ArrayList<NewTripplanJpEntity>();
			//根据计划生产行程安排的表
			/*	List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
				if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {

					for (int i=0;i<tripplanJpList.size();i++) {
						NewTripplanJpEntity trip=tripplanJpList.get(i);
						if (trips.size() == 0) {
							trips.add(trip);
						} else {
							NewTripplanJpEntity prev = trips.get(trips.size() - 1);
							if (prev.getHotelid() == trip.getHotelid()) {
								DateTime dt = new DateTime(trip.getNowdate());
								if (!Util.isEmpty(trip.getOuttime())) {

									dt = dt.withField(DateTimeFieldType.hourOfDay(), trip.getOuttime().getHours());
									dt = dt.withField(DateTimeFieldType.minuteOfHour(), trip.getOuttime().getMinutes());
								}
								prev.setOuttime(dt.toDate());
							} else {
								trips.add(trip);
							}
						}
					}
				}*/
			//==========================================开始============================
			List<NewTripJpEntity> tripList = dbDao.query(NewTripJpEntity.class,
					Cnd.where("order_jp_id", "=", order.getId()), null);
			NewTripJpEntity tripJp1 = tripList.get(0);
			int oneormore2 = tripJp1.getOneormore().intValue();
			if (oneormore2 == 0) {
				List<NewTripplanJpEntity> query = dbDao.query(NewTripplanJpEntity.class,
						Cnd.where("order_jp_id", "=", order.getId()), null);
				NewTripplanJpEntity trip1 = new NewTripplanJpEntity();
				if (!Util.isEmpty(query) && query.size() > 0) {
					trip1.setHotelid(query.get(0).getHotelid());
					trip1.setNowdate(tripJp1.getStartdate());
					trip1.setEndDate(tripJp1.getReturndate());
					trip1.setOrder_jp_id(order.getId());
				}
				trips.add(trip1);
			} else if (oneormore2 == 1) {

				List<NewDateplanJpEntity> datePlanList = dbDao.query(NewDateplanJpEntity.class,
						Cnd.where("trip_jp_id", "=", tripJp1.getId()), null);
				List<NewTripplanJpEntity> tripPlanList = dbDao.query(NewTripplanJpEntity.class,
						Cnd.where("order_jp_id", "=", order.getId()), null);
				if (!Util.isEmpty(datePlanList) && datePlanList.size() > 0) {
					for (int i = 0; i < datePlanList.size(); i++) {
						if (i < datePlanList.size() - 2) {

							String arrivecity = datePlanList.get(i).getArrivecity();
							NewTripplanJpEntity trip1 = new NewTripplanJpEntity();
							for (int j = 0; j < tripPlanList.size(); j++) {
								if (!Util.isEmpty(arrivecity)) {
									if (arrivecity.equalsIgnoreCase(tripPlanList.get(j).getCity())) {

										trip1.setHotelid(tripPlanList.get(j).getHotelid());
										break;
									}
								}
							}

							trip1.setNowdate(datePlanList.get(i).getStartdate());

							Calendar cal = Calendar.getInstance();
							cal.setTime(datePlanList.get(i + 1).getStartdate());
							cal.add(Calendar.DATE, -1);
							trip1.setOrder_jp_id(order.getId());
							trip1.setEndDate(cal.getTime());
							trips.add(trip1);
						} else if (i == datePlanList.size() - 2) {
							String arrivecity = datePlanList.get(i).getArrivecity();
							NewTripplanJpEntity trip1 = new NewTripplanJpEntity();
							for (int j = 0; j < tripPlanList.size(); j++) {
								if (!Util.isEmpty(arrivecity)) {
									if (arrivecity.equalsIgnoreCase(tripPlanList.get(j).getCity())) {

										trip1.setHotelid(tripPlanList.get(j).getHotelid());
										break;
									}
								}
							}

							trip1.setNowdate(datePlanList.get(i).getStartdate());
							trip1.setEndDate(datePlanList.get(i + 1).getStartdate());
							trip1.setOrder_jp_id(order.getId());
							trips.add(trip1);

						}
					}
				}

			}

			//==========================================结束============================
			if (!Util.isEmpty(trips) && trips.size() > 0) {

				for (NewTripplanJpEntity trip : trips) {
					note.add(t.hotel(trip, guest.toString().toUpperCase()));
				}
			}
			//关系表
			note.add(t.relation(order));
			//机票信息
			note.add(t.flight(order));
			//合并输出照会
			fileMap.put("照会.pdf", createTempFile(t.merge(note)));
			//导出人员名单的电子表格
			fileMap.put(t.getExcelFileName(order), createTempFile(t.excel(order)));
			//导出日本提交成功的归国报告
			String fileurl = order.getFileurl();
			if (!Util.isEmpty(fileurl)) {

				String str = System.getProperty("java.io.tmpdir");
				String extendName = fileurl.substring(fileurl.lastIndexOf(".") + 1, fileurl.length());
				File file11 = new File(str + File.separator + "12" + extendName);
				InputStream is = null;
				OutputStream os = null;
				os = new FileOutputStream(file11);
				URL url = new URL(fileurl);// 生成url对象
				URLConnection urlConnection = url.openConnection();
				//urlConnection.getInputStream();
				is = urlConnection.getInputStream();
				byte[] buffer = new byte[1024];
				int byteread = 0;
				while ((byteread = is.read(buffer)) != -1) {
					os.write(buffer, 0, byteread);

				}
				os.flush();
				fileMap.put("归国报告.pdf", file11);
			}

			//合并输出为一个压缩包
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ZipArchiveOutputStream zip = new ZipArchiveOutputStream(out);
			Iterator<Map.Entry<String, File>> set = fileMap.entrySet().iterator();
			while (set.hasNext()) {
				Map.Entry<String, File> entry = set.next();
				File file = entry.getValue();
				ZipArchiveEntry zipEntry = new ZipArchiveEntry(entry.getKey());
				zipEntry.setSize(file.length());
				zip.putArchiveEntry(zipEntry);
				FileInputStream in = new FileInputStream(file);
				IOUtils.copy(in, zip);
				IOUtils.closeQuietly(in);
				FileUtils.deleteQuietly(file);
			}
			zip.closeArchiveEntry();
			IOUtils.closeQuietly(zip);
			IOUtils.closeQuietly(out);
			return out;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("结果文件压缩包生成异常!");
			}
		}
	}
}