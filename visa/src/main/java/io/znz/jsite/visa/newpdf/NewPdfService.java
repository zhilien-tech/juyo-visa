package io.znz.jsite.visa.newpdf;

/**
 * Created by Chaly on 2017/4/13.
 */

import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.SpringUtil;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripplanJpEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.springframework.stereotype.Service;

import com.uxuexi.core.common.util.Util;

@Service
public class NewPdfService {

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
			List<NewTripplanJpEntity> tripplanJpList = order.getTripplanJpList();
			if (!Util.isEmpty(tripplanJpList) && tripplanJpList.size() > 0) {

				for (NewTripplanJpEntity trip : tripplanJpList) {
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
			}
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