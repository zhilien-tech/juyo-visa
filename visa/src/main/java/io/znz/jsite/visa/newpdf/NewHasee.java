package io.znz.jsite.visa.newpdf;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import io.znz.jsite.exception.JSiteException;
import io.znz.jsite.util.DateUtils;
import io.znz.jsite.util.SpringUtil;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.bean.Hotel;
import io.znz.jsite.visa.bean.Scenic;
import io.znz.jsite.visa.entity.japan.NewCustomerJpEntity;
import io.znz.jsite.visa.entity.japan.NewDateplanJpEntity;
import io.znz.jsite.visa.entity.japan.NewFinanceJpEntity;
import io.znz.jsite.visa.entity.japan.NewOrderJpEntity;
import io.znz.jsite.visa.entity.japan.NewProposerInfoJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripJpEntity;
import io.znz.jsite.visa.entity.japan.NewTripplanJpEntity;
import io.znz.jsite.visa.enums.GenderEnum;
import io.znz.jsite.visa.enums.OrderJapanVisaType;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.nutz.dao.Cnd;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.uxuexi.core.common.util.EnumUtil;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * Created by Chaly on 2017/5/2.
 */
@Component
public class NewHasee extends NewTemplate {
	private IDbDao dbdao = SpringUtil.getBean("dbDao");

	public String getPrefix() {
		return "hasee/";
	}

	public ByteArrayOutputStream note(NewOrderJpEntity order) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Map<String, String> map = new HashMap<String, String>();
			map.put("company", "北京神舟国际旅行社集团有限公司");
			StringBuilder sb = new StringBuilder("(");
			for (NewCustomerJpEntity customer : order.getCustomerJpList()) {

				if (!Util.isEmpty(customer)) {
					List<NewProposerInfoJpEntity> proposerList = dbdao.query(NewProposerInfoJpEntity.class,
							Cnd.where("customer_jp_id", "=", customer.getId()), null);
					if (!Util.isEmpty(proposerList) && proposerList.size() > 0) {

						sb.append(customer.getChinesexing()).append(customer.getChinesename()).append(":")
								.append(proposerList.get(0).getIsMainProposer() ? "主卡" : "副卡").append("、");
					}

				}

			}
			sb = new StringBuilder(StringUtils.removeEnd(sb.toString(), "、"));
			sb.append(")");
			String content = String.format(
					"　　%s根据与津東通商株式会社的合同约定，组织%d人访日个人旅游，请协助办理" + OrderJapanVisaType.get(order.getVisatype())

					+ "往返赴日签证。%s", map.get("company"), order.getCustomerJpList().size(),
					order.getVisatype() == OrderJapanVisaType.SINGLE.intKey() ? "" : sb.toString().trim());
			map.put("content", content);//旅行社名称
			map.put("id", "1-2");//番号

			//判断是多程还是单程
			NewTripJpEntity tripJp = order.getTripJp();
			Date startDate = null;
			Date endDate = null;
			String startFlightnum = "";
			String endFlightnum = "";
			if (!Util.isEmpty(tripJp)) {
				if (tripJp.getOneormore().intValue() == 1) {
					//多程
					List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {
						startDate = dateplanJpList.get(0).getStartdate();
						endDate = dateplanJpList.get(dateplanJpList.size() - 1).getStartdate();
						startFlightnum = dateplanJpList.get(0).getFlightnum();
						endFlightnum = dateplanJpList.get(dateplanJpList.size() - 1).getFlightnum();
					}
				} else if (tripJp.getOneormore().intValue() == 0) {
					//单程
					startDate = tripJp.getStartdate();
					endDate = tripJp.getReturndate();
					startFlightnum = tripJp.getFlightnum();
					endFlightnum = tripJp.getReturnflightnum();
				}
			}
			NewTripJpEntity entry = order.getTripJp();

			if (entry == null)
				throw new JSiteException("入境信息不能为空!");
			DateTime dt = new DateTime(startDate);
			if (dt.isBeforeNow()) {
				throw new JSiteException("入境时间不能在当前时间之前!");
			}
			map.put("entryDate", df3.format(startDate));//入境日期
			Flight entryFlight = dbdao.fetch(Flight.class, Long.valueOf(startFlightnum));
			map.put("entryFlight", entryFlight.getCompany() + ":" + entryFlight.getLine());//入境口岸/航班
			NewTripJpEntity depart = order.getTripJp();
			//Ticket depart = order.getDepart();
			if (depart == null)
				throw new JSiteException("出境信息不能为空!");
			if (dt.isAfter(endDate.getTime())) {
				throw new JSiteException("出境时间不能在入境时间之前!");
			}
			map.put("departDate", df3.format(endDate));//出境日期
			Flight departFlight = dbdao.fetch(Flight.class, Long.valueOf(endFlightnum));
			map.put("departFlight", departFlight.getCompany() + ":" + departFlight.getLine());//出境口岸/航班

			map.put("stay", (diffDays(startDate, endDate) + 1) + "天");//停留周期
			//受理日和发给日没有入口？？？？
			if (!Util.isEmpty(order.getSenddate())) {

				map.put("startDate", df4.format(order.getSenddate()));//受理日
			} else {
				map.put("startDate", "");//受理日

			}
			if (!Util.isEmpty(order.getOutdate())) {
				map.put("endDate", df4.format(order.getOutdate()));//发给日

			} else {
				map.put("endDate", "");//发给日

			}

			map.put("autograph", "北京神舟国际旅行社集团有限公司出境旅游公司");//最后的签名
			map.put("createDate", df3.format(new Date()));//介绍日期
			//1 不进行密码验证
			PdfReader.unethicalreading = true;
			//2 读入pdf表单
			PdfReader reader = new PdfReader(getClass().getClassLoader().getResource(getPrefix() + "note.pdf"));
			//3 根据表单生成一个新的pdf
			PdfStamper ps = new PdfStamper(reader, stream);
			//4 获取pdf表单
			AcroFields fields = ps.getAcroFields();
			//5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
			fields.addSubstitutionFont(getBaseFont());
			//6遍历pdf表单表格，同时给表格赋值
			Iterator<Map.Entry<String, AcroFields.Item>> iterator = fields.getFields().entrySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().getKey();
				if (map.containsKey(key)) {
					fields.setField(key, map.get(key));
				}
			}
			ps.setFormFlattening(true); // 这句不能少
			ps.close();
			reader.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("照会生成异常!");
			}
		}
	}

	public ByteArrayOutputStream flight(NewOrderJpEntity order) {
		List<NewTripJpEntity> trips = Lists.newArrayList(order.getTripJp());
		StringBuilder guest = new StringBuilder();
		//获取个人名单
		for (NewCustomerJpEntity customer : order.getCustomerJpList()) {
			guest.append(customer.getChinesexingen()).append(" ").append(customer.getChinesenameen()).append("\n");
		}
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Map<String, String> map = new HashMap<String, String>();
			map.put("guest", guest.toString().toUpperCase());
			//1 不进行密码验证
			PdfReader.unethicalreading = true;
			//2 读入pdf表单
			PdfReader reader = new PdfReader(getClass().getClassLoader().getResource(getPrefix() + "flight.pdf"));
			//3 根据表单生成一个新的pdf
			PdfStamper ps = new PdfStamper(reader, stream);
			//4 获取pdf表单
			AcroFields fields = ps.getAcroFields();
			//5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
			//6遍历pdf表单表格，同时给表格赋值
			Iterator<Map.Entry<String, AcroFields.Item>> iterator = fields.getFields().entrySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().getKey();
				if (map.containsKey(key)) {
					fields.setField(key, map.get(key));
				}
			}
			ps.setFormFlattening(true); // 这句不能少

			//循环表格
			PdfContentByte cbq = ps.getOverContent(1);
			Font font = getFont();
			String titles[] = { "始发地/目的地", "航班", "座位等级", "日期", "起飞时间", "到达时间", "有效期", "客票状态", "行李", "航站楼\n起飞　到站" };
			float[] columns = { 3, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
			PdfPTable table = new PdfPTable(columns);
			float width = PageSize.A4.getWidth() * 0.9f;
			float padding = (PageSize.A4.getWidth() - width) / 2;
			table.setTotalWidth(width);
			//设置表头
			for (String title : titles) {
				PdfPCell cell = new PdfPCell(new Paragraph(title, font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setUseVariableBorders(true);
				cell.setBorderWidthLeft(0);
				cell.setBorderWidthRight(0);
				cell.setBorderWidthTop(1.1f);
				cell.setBorderWidthBottom(1.1f);
				cell.setFixedHeight(34f);
				table.addCell(cell);
			}
			//设置表体
			NewTripJpEntity tripJp = order.getTripJp();
			Date startDate = null;
			Date endDate = null;
			String startFlightnum = "";
			String endFlightnum = "";
			if (!Util.isEmpty(tripJp)) {
				if (tripJp.getOneormore().intValue() == 1) {
					//多程
					List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {
						startDate = dateplanJpList.get(0).getStartdate();
						endDate = dateplanJpList.get(dateplanJpList.size() - 1).getStartdate();
						startFlightnum = dateplanJpList.get(0).getFlightnum();
						endFlightnum = dateplanJpList.get(dateplanJpList.size() - 1).getFlightnum();
						//多程相对应的处理
						for (int i = 0; i < dateplanJpList.size(); i++) {
							/*NewTripJpEntity trip = trips.get(0);*/
							NewDateplanJpEntity newDateplanJpEntity = dateplanJpList.get(i);
							Flight flight = null;

							flight = dbdao.fetch(Flight.class, Long.valueOf(newDateplanJpEntity.getFlightnum()));
							//trip.getSeat()方式
							String datas[] = {
									flight.getFrom(),
									flight.getLine(),
									"",
									df6.format(newDateplanJpEntity.getStartdate()).toUpperCase(),
									df7.format(flight.getDeparture()),
									df7.format(flight.getLanding()),
									newDateplanJpEntity.getReturndate() != null ? df7.format(newDateplanJpEntity
											.getReturndate()) : "", "OK", "2PC",
									(i == 0 ? "--　" : "") + flight.getFromTerminal() + (i == 0 ? "" : "　--") };
							for (int j = 0; j < datas.length; j++) {
								String data = datas[j];
								PdfPCell cell = new PdfPCell(new Paragraph(data, font));
								cell.setHorizontalAlignment(j == 0 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
								cell.setUseVariableBorders(true);
								cell.setBorderWidthLeft(0);
								cell.setBorderWidthRight(0);
								cell.setBorderWidthTop(0);
								cell.setBorderWidthBottom(0);
								table.addCell(cell);
							}
							//添加一个空行dbdao.fetch(Flight.class, Long.valueOf(trips.get(0).getFlightnum()));

						}
						Flight flight1 = dbdao.fetch(Flight.class, Long.valueOf(dateplanJpList.get(0).getFlightnum()));
						String datas[] = { flight1.getFrom(), "", "", "", "", "", "", "", "", "", };
						for (int j = 0; j < datas.length; j++) {
							String data = datas[j];
							PdfPCell cell = new PdfPCell(new Paragraph(data, font));
							cell.setHorizontalAlignment(j == 0 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
							cell.setUseVariableBorders(true);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(0);
							cell.setBorderWidthTop(0);
							cell.setBorderWidthBottom(0);
							table.addCell(cell);
						}

					}
				} else if (tripJp.getOneormore().intValue() == 0) {
					//单程
					startDate = tripJp.getStartdate();
					endDate = tripJp.getReturndate();
					startFlightnum = tripJp.getFlightnum();
					endFlightnum = tripJp.getReturnflightnum();

					//单程相对应的处理
					for (int i = 0; i < 2; i++) {
						NewTripJpEntity trip = trips.get(0);
						Flight flight = null;
						if (i == 0) {

							flight = dbdao.fetch(Flight.class, Long.valueOf(trip.getFlightnum()));
						} else {
							flight = dbdao.fetch(Flight.class, Long.valueOf(trip.getReturnflightnum()));
						}
						//trip.getSeat()方式
						String datas[] = { flight.getFrom(), flight.getLine(), "",
								df6.format(trip.getStartdate()).toUpperCase(), df7.format(flight.getDeparture()),
								df7.format(flight.getLanding()),
								trip.getReturndate() != null ? df7.format(trip.getReturndate()) : "", "OK", "2PC",
								(i == 0 ? "--　" : "") + flight.getFromTerminal() + (i == 0 ? "" : "　--") };
						for (int j = 0; j < datas.length; j++) {
							String data = datas[j];
							PdfPCell cell = new PdfPCell(new Paragraph(data, font));
							cell.setHorizontalAlignment(j == 0 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
							cell.setUseVariableBorders(true);
							cell.setBorderWidthLeft(0);
							cell.setBorderWidthRight(0);
							cell.setBorderWidthTop(0);
							cell.setBorderWidthBottom(0);
							table.addCell(cell);
						}
					}
					//添加一个空行dbdao.fetch(Flight.class, Long.valueOf(trips.get(0).getFlightnum()));

					Flight flight1 = dbdao.fetch(Flight.class, Long.valueOf(trips.get(0).getFlightnum()));
					String datas[] = { flight1.getFrom(), "", "", "", "", "", "", "", "", "", };
					for (int j = 0; j < datas.length; j++) {
						String data = datas[j];
						PdfPCell cell = new PdfPCell(new Paragraph(data, font));
						cell.setHorizontalAlignment(j == 0 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
						cell.setUseVariableBorders(true);
						cell.setBorderWidthLeft(0);
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0);
						table.addCell(cell);
					}

				}
			}

			table.writeSelectedRows(0, -1, padding, 493, cbq);
			ps.close();
			reader.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("酒店信息生成异常!");
			}
		}
	}

	public ByteArrayOutputStream relation(NewOrderJpEntity order) {
		try {
			List<NewCustomerJpEntity> customers = order.getCustomerJpList();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4.rotate(), 0, 0, 36, 36);
			PdfWriter.getInstance(document, stream);
			document.open();

			Font font = getFont();

			Paragraph p = new Paragraph("签证申请人名单", font);
			p.setSpacingBefore(5);
			p.setSpacingAfter(5);
			p.setIndentationLeft(50);
			p.setIndentationRight(50);
			document.add(p);

			float[] columns = { 2, 3, 4, 2, 3, 3, 3, 3, 3, 2, 3, 4, 3, 2, 4, };
			PdfPTable table = new PdfPTable(columns);
			table.setWidthPercentage(95);
			table.setTotalWidth(PageSize.A4.rotate().getWidth());

			//设置表头
			String titles[] = { "编号", "姓名(中文)", "姓名(英文)", "性别", "出生日期", "护照发行地", "职业", "居住地点", "出境记录", "婚姻", "身份确认",
					"经济能力确认", "内容", "备注", "旅行社意见", };
			for (String title : titles) {
				PdfPCell cell = new PdfPCell(new Paragraph(title, font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
			//生成表体
			for (int i = 0; i < customers.size(); i++) {
				NewCustomerJpEntity c = customers.get(i);
				StringBuilder sbt = new StringBuilder();
				StringBuilder sbv = new StringBuilder();
				if (!Util.isEmpty(c.getFinanceJpList()) && c.getFinanceJpList().size() > 0) {

					for (NewFinanceJpEntity option : c.getFinanceJpList()) {
						if (Util.isEmpty(option)) {

							sbt.append(option.getBusiness()).append("\n");
							sbv.append(option.getDetail()).append("\n");
						}
					}
				}

				if (Util.isEmpty(c.getChinesexingen())) {
					c.setChinesexingen("");
				}
				if (Util.isEmpty(c.getChinesenameen())) {
					c.setChinesenameen("");
				}

				if (!Util.isEmpty(c)) {
					List<NewProposerInfoJpEntity> proposerList = dbdao.query(NewProposerInfoJpEntity.class,
							Cnd.where("customer_jp_id", "=", c.getId()), null);
					if (!Util.isEmpty(proposerList) && proposerList.size() > 0) {
						String gender = "";
						if (!Util.isEmpty(c.getGender())) {
							gender = EnumUtil.getValue(GenderEnum.class, c.getGender().intValue());
						}
						String birthday = "";
						if (!Util.isEmpty(c.getGender())) {
							birthday = df1.format(c.getBirthday());
						}
						String passportsendplace = "";
						if (!Util.isEmpty(c.getGender())) {
							passportsendplace = c.getPassportsendplace().toUpperCase();
						}
						String datas[] = { "1-" + (i + 1),
								c.getChinesexing() + c.getChinesename(),
								(c.getChinesexingen() + "\n" + c.getChinesenameen()).toUpperCase(),
								gender,
								birthday,
								passportsendplace,
								!Util.isEmpty(c.getWorkinfoJp()) ? c.getWorkinfoJp().getMyjob() : "",
								!Util.isEmpty(c.getNowprovince()) ? c.getNowprovince().toUpperCase() : "",
								"良好",
								c.getMarrystate() + "",
								"身份证\n户口本",
								StringUtils.isBlank(sbt.toString()) ? "" : sbt.toString(),
								StringUtils.isBlank(sbv.toString()) ? "" : sbv.toString(),
								//主卡和副卡的关系
								proposerList.get(0).getIsMainProposer() ? "主卡" : getMasterName(order) + "的"
										+ "主卡和副卡的关系", "推荐" };

						for (String title : datas) {
							PdfPCell cell = new PdfPCell(new Paragraph(title, font));
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
							table.addCell(cell);
						}
					}

				}

			}
			document.add(table);
			document.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("关系表生成异常!");
			}
		}
	}

	public ByteArrayOutputStream trip(NewOrderJpEntity order) {
		try {
			List<NewTripplanJpEntity> trips = order.getTripplanJpList();

			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 0, 0, 36, 36);
			PdfWriter.getInstance(document, stream);
			document.open();
			Font font = getFont();
			font.setSize(15);
			{
				Paragraph p = new Paragraph("滞　在　予　定　表", font);
				p.setSpacingBefore(5);
				p.setSpacingAfter(5);
				p.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(p);
			}
			font.setSize(10);
			//判断
			NewTripJpEntity tripJp = order.getTripJp();
			Date startDate = null;
			Date endDate = null;
			if (!Util.isEmpty(tripJp)) {
				if (tripJp.getOneormore().intValue() == 1) {
					//多程
					List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {
						startDate = dateplanJpList.get(0).getStartdate();
						endDate = dateplanJpList.get(dateplanJpList.size() - 1).getStartdate();
					}
				} else if (tripJp.getOneormore().intValue() == 0) {
					//单程
					startDate = tripJp.getStartdate();
					endDate = tripJp.getReturndate();
				}
			}

			{
				if (!Util.isEmpty(startDate) && !Util.isEmpty(endDate)) {

					String text = String.format("（平成%sから平成%s）", format(startDate, "yy年MM月dd日"),
							format(endDate, "yy年MM月dd日"));
					Paragraph p = new Paragraph(text, font);
					p.setSpacingBefore(5);
					p.setIndentationRight(20);
					p.setAlignment(Paragraph.ALIGN_RIGHT);
					document.add(p);
				}
			}
			{
				String text = String.format("（旅行参加者 %s 他%s名、計%s名）", getMasterName(order), order.getCustomerJpList()
						.size() - 1, order.getCustomerJpList().size());
				Paragraph p = new Paragraph(text, font);
				p.setSpacingAfter(15);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}

			float[] columns = { 1, 3, 5, 5, };
			PdfPTable table = new PdfPTable(columns);
			table.setWidthPercentage(95);
			table.setTotalWidth(PageSize.A4.getWidth());

			//设置表头
			String titles[] = { "日数", "年月日", "行　動　予　定", "宿　泊　先", };
			font.setSize(12);
			for (String title : titles) {
				PdfPCell cell = new PdfPCell(new Paragraph(title, font));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
			font.setSize(10);
			//生成表体
			//旅游计划空处理
			if (!Util.isEmpty(trips) && trips.size() > 0) {

				for (int i = 0; i < trips.size(); i++) {
					NewTripplanJpEntity trip = trips.get(i);
					StringBuilder scenics = new StringBuilder();
					/*for (Scenic scenic : trip.getViewid()) {
					scenics.append(scenic.getNameJP()).append("、");
					}*/
					String viewid = trip.getViewid();
					String[] split = viewid.split(",");
					for (String string : split) {

						Scenic scenic = this.dbdao.fetch(Scenic.class, Long.valueOf(string));
						scenics.append(scenic.getNameJP()).append("、");
					}
					StringBuilder hotel = new StringBuilder();
					//宾馆空处理
					if (!Util.isEmpty(trip.getHotelid())) {

						Hotel h = this.dbdao.fetch(Hotel.class, Long.valueOf(trip.getHotelid()));
						if (!Util.isEmpty(h)) {

							hotel = new StringBuilder(h.getNameJP()).append("\n").append(h.getAddressJP()).append("\n")
									.append(h.getPhone());
						}
					}
					String datas[] = { String.valueOf(i + 1), format(trip.getNowdate(), df9), scenics.toString(),
							hotel.toString() };
					for (int j = 0; j < datas.length; j++) {
						String data = datas[j];
						PdfPCell cell = new PdfPCell(new Paragraph(data, font));
						cell.setHorizontalAlignment(j > 1 ? Element.ALIGN_LEFT : Element.ALIGN_CENTER);
						cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
						table.addCell(cell);
					}
				}
			}
			document.add(table);
			//文字添加
			/*{
				String text = String.format("会社名：%s", "dhfasdf");
				Paragraph p = new Paragraph(text, font);
				p.setSpacingBefore(5);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}
			{
				String text = String.format("住  所：%s", "sdfasdf");
				Paragraph p = new Paragraph(text, font);
				p.setSpacingBefore(5);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}

			{
				String text = String.format("担当者：%s", "崔建平");
				Paragraph p = new Paragraph(text, font);
				p.setSpacingBefore(5);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}
			{
				String text = String.format("电话：%s", "88888888");
				Paragraph p = new Paragraph(text, font);
				p.setSpacingBefore(5);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}*/

			//添加盖章
			document.add(getSeal());
			document.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("行程计划表生成异常!");
			}
		}
	}

	public ByteArrayOutputStream book(NewOrderJpEntity order) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Document document = new Document(PageSize.A4, 0, 0, 36, 36);
			PdfWriter.getInstance(document, stream);
			document.open();

			Font font = getFont();

			{
				font.setSize(20);
				Paragraph p = new Paragraph("査 証 申 請 人 名 簿", font);
				p.setSpacingBefore(5);
				p.setSpacingAfter(5);
				p.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(p);
			}
			font.setSize(10);
			//判断
			NewTripJpEntity tripJp = order.getTripJp();
			Date startDate = null;
			Date endDate = null;
			if (!Util.isEmpty(tripJp)) {
				if (tripJp.getOneormore().intValue() == 1) {
					//多程
					List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {
						startDate = dateplanJpList.get(0).getStartdate();
						endDate = dateplanJpList.get(dateplanJpList.size() - 1).getStartdate();
					}
				} else if (tripJp.getOneormore().intValue() == 0) {
					//单程
					startDate = tripJp.getStartdate();
					endDate = tripJp.getReturndate();
				}
			}

			{
				if (!Util.isEmpty(startDate) && !Util.isEmpty(endDate)) {
					String text = String.format("（平成%sから平成%s）", format(startDate, "yy年MM月dd日"),
							format(endDate, "yy年MM月dd日"));
					Paragraph p = new Paragraph(text, font);
					p.setSpacingBefore(5);
					p.setIndentationRight(20);
					p.setAlignment(Paragraph.ALIGN_RIGHT);
					document.add(p);
				}
			}
			{
				String text = String.format("（旅行参加者 %s 他%s名、計%s名）", getMasterName(order), order.getCustomerJpList()
						.size() - 1, order.getCustomerJpList().size());
				Paragraph p = new Paragraph(text, font);
				p.setSpacingAfter(15);
				p.setIndentationRight(20);
				p.setAlignment(Paragraph.ALIGN_RIGHT);
				document.add(p);
			}
			font.setSize(11);
			float[] columns = { 1, 3, 3, 1, 2.5f, 3, 2, 3 };
			PdfPTable table = new PdfPTable(columns);
			table.setWidthPercentage(95);
			table.setTotalWidth(PageSize.A4.getWidth());

			//设置表头
			String titles[] = { "", "氏名（中文）", "（英文）", "性別", "生年月日", "职业", "発行地", "旅券番号", };
			for (int i = 0; i < titles.length; i++) {
				String title = titles[i];
				PdfPCell cell = new PdfPCell(new Paragraph(title, font));
				cell.setFixedHeight(30);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}
			//生成表体
			for (int i = 0; i < order.getCustomerJpList().size(); i++) {
				NewCustomerJpEntity c = order.getCustomerJpList().get(i);
				//				EnumUtil.getValue(GenderEnum.class, c.getGender());
				String birthDay = "";
				if (!Util.isEmpty(c.getBirthday())) {
					birthDay = df1.format(c.getBirthday());
				}
				String gender = "";
				if (!Util.isEmpty(c.getGender())) {
					gender = EnumUtil.getValue(GenderEnum.class, c.getGender());
				}
				String passportsendplace = "";
				if (!Util.isEmpty(c.getPassportsendplace())) {
					passportsendplace = c.getPassportsendplace().toUpperCase();
				}
				if (Util.isEmpty(c.getChinesexing())) {
					c.setChinesexing("");
				}
				if (Util.isEmpty(c.getChinesename())) {
					c.setChinesename("");
				}
				if (Util.isEmpty(c.getChinesexingen())) {
					c.setChinesexingen("");
				}
				if (Util.isEmpty(c.getChinesenameen())) {
					c.setChinesenameen("");
				}
				String datas[] = { String.valueOf(i + 1), c.getChinesexing() + c.getChinesename(),
						(c.getChinesexingen() + "\n" + c.getChinesenameen()).toUpperCase(), gender, birthDay,
						Util.isEmpty(c.getWorkinfoJp()) ? "" : c.getWorkinfoJp().getMyjob(), passportsendplace,
						c.getPassport(), };
				for (String data : datas) {
					PdfPCell cell = new PdfPCell(new Paragraph(data, font));
					cell.setFixedHeight(30);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}
			}
			document.add(table);

			//添加盖章
			document.add(getSeal());
			document.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("人员名单生成异常!");
			}
		}
	}

	public ByteArrayOutputStream guarantee(NewOrderJpEntity order) {
		try {
			// 1) Load ODT file and set Velocity template engine and cache it to the registry
			InputStream in = getClass().getClassLoader().getResourceAsStream(getPrefix() + "word.docx");
			IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
			// 2) Create Java model context
			IContext context = report.createContext();

			String now[] = format(new Date(), df9).split("\\.");
			context.put("year", now[0]);
			context.put("mouth", now[1]);
			context.put("day", now[2]);

			String mainName = "";
			for (NewCustomerJpEntity c : order.getCustomerJpList()) {
				//客户主申请人以及他们的名字处理
				if (!Util.isEmpty(c)) {
					List<NewProposerInfoJpEntity> proposerList = dbdao.query(NewProposerInfoJpEntity.class,
							Cnd.where("customer_jp_id", "=", c.getId()), null);
					if (!Util.isEmpty(proposerList) && proposerList.size() > 0) {

						if (proposerList.get(0).getIsMainProposer() || StringUtils.isBlank(mainName)) {
							if (Util.isEmpty(c.getChinesexing())) {
								c.setChinesexing("");
							}
							if (Util.isEmpty(c.getChinesename())) {
								c.setChinesename("");
							}
							mainName = c.getChinesexing() + c.getChinesename();
						}
					}

				}
			}

			context.put("name", mainName);
			//客户人数空处理
			if (!Util.isEmpty(order.getCustomerJpList()) && order.getCustomerJpList().size() > 0) {

				context.put("count", String.valueOf(order.getCustomerJpList().size()));
			} else {
				context.put("count", 0 + "");

			}
			//TODO 面签类型  被写死
			switch (order.getVisatype()) {
			case 0:
				context.put("reason", "");
				context.put("type", "单次");
				break;
			case 5:
				context.put("reason", "  十分な経済力保有者向け数次査証（個人観光）・1回目");
				context.put("type", "個人");
				break;
			case 2:
				context.put("reason", "（沖縄及び東北三県数次査証）");
				context.put("type", "三年");
				break;
			case 3:
				context.put("reason", "  沖縄及び東北六県数次査証（個人観光）・1回目");
				context.put("type", "三年");
				break;
			case 6:
				context.put("reason", "（相当な高所得者向け数次査証）");
				context.put("type", "相当な高所得者向け数次査証(個人観光)・1回目");
				break;
			}
			//生成身元保证书日期区分多程与单程
			NewTripJpEntity tripJp = order.getTripJp();
			if (!Util.isEmpty(tripJp)) {
				if (tripJp.getOneormore().intValue() == 1) {
					//多程
					List<NewDateplanJpEntity> dateplanJpList = order.getDateplanJpList();
					if (!Util.isEmpty(dateplanJpList) && dateplanJpList.size() > 0) {

						String entry[] = format(dateplanJpList.get(0).getStartdate(), df9).split("\\.");
						context.put("sYear", entry[0]);
						context.put("sMouth", entry[1]);
						context.put("sDay", entry[2]);

						String depart[] = format(dateplanJpList.get(dateplanJpList.size() - 1).getStartdate(), df9)
								.split("\\.");
						context.put("eYear", depart[0]);
						context.put("eMouth", depart[1]);
						context.put("eDay", depart[2]);
					}
				} else if (tripJp.getOneormore().intValue() == 0) {
					//单程
					String entry[] = format(order.getTripJp().getStartdate(), df9).split("\\.");
					context.put("sYear", entry[0]);
					context.put("sMouth", entry[1]);
					context.put("sDay", entry[2]);

					String depart[] = format(order.getTripJp().getReturndate(), df9).split("\\.");
					context.put("eYear", depart[0]);
					context.put("eMouth", depart[1]);
					context.put("eDay", depart[2]);
				}
			} else {
				context.put("sYear", "");
				context.put("sMouth", "");
				context.put("sDay", "");

				context.put("eYear", "");
				context.put("eMouth", "");
				context.put("eDay", "");
			}

			// 3) Set PDF as format converter

			//            PdfOptions pdfOptions = PdfOptions.create().fontProvider(new IFontProvider() {
			//                public com.lowagie.text.Font getFont(String familyName, String encoding, float size, int style, Color color) {
			//                    try {
			//                        BaseFont bf = BaseFont.createFont(getFontURL(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			//                        com.lowagie.text.Font f = new com.lowagie.text.Font(bf, size, style, color);
			//                        f.setFamily(familyName);
			//                        return f;
			//                    } catch (Exception e) {
			//                        e.printStackTrace();
			//                    }
			//                    return null;
			//                }
			//            });
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			//            Options options = Options.getTo(ConverterTypeTo.PDF).subOptions(pdfOptions);
			//            report.convert(context, options, out);
			report.process(context, out);
			return out;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("身元保证书生成异常!");
			}
		}
	}

	public ByteArrayOutputStream excel(NewOrderJpEntity order) {
		try {
			//导出人员名单的电子表格
			List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
			entity.add(new ExcelExportEntity("氏名", "name"));
			entity.add(new ExcelExportEntity("ピンイン", "name_en"));
			entity.add(new ExcelExportEntity("性別", "gender"));
			entity.add(new ExcelExportEntity("居住地域", "city"));
			entity.add(new ExcelExportEntity("生年月日", "birthday"));
			entity.add(new ExcelExportEntity("旅券番号", "passport"));
			entity.add(new ExcelExportEntity("備考", "remark"));
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			String mainName = "";
			for (NewCustomerJpEntity c : order.getCustomerJpList()) {

				if (!Util.isEmpty(c)) {
					List<NewProposerInfoJpEntity> proposerList = dbdao.query(NewProposerInfoJpEntity.class,
							Cnd.where("customer_jp_id", "=", c.getId()), null);
					if (!Util.isEmpty(proposerList) && proposerList.size() > 0) {

						if (proposerList.get(0).getIsMainProposer() || StringUtils.isBlank(mainName)) {
							mainName = c.getChinesexing() + c.getChinesename();
						}
					}
				}

				String birthDay = "";
				if (!Util.isEmpty(c.getBirthday())) {
					birthDay = DateUtils.formatDate(c.getBirthday(), "yyyy/M/d");
				}
				String gender = "";
				if (!Util.isEmpty(c.getGender())) {
					gender = c.getGender() == GenderEnum.female.intKey() ? "2" : "1";
				}
				String nowcity = "";
				if (!Util.isEmpty(c.getNowcity())) {
					nowcity = c.getNowcity().toLowerCase();
				}
				if (Util.isEmpty(c.getChinesexingen())) {
					c.setChinesexingen("");
				}
				if (Util.isEmpty(c.getChinesenameen())) {
					c.setChinesenameen("");
				}

				Map<String, String> map = new HashMap<String, String>();
				map.put("name", c.getChinesexing() + c.getChinesename());
				map.put("name_en", (c.getChinesexingen() + " " + c.getChinesenameen()).toUpperCase());
				map.put("gender", gender);
				map.put("city", nowcity);
				map.put("birthday", birthDay);
				map.put("passport", c.getPassport());
				map.put("remark", "");
				list.add(map);
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			ExportParams params = new ExportParams(null, "名单");
			params.setType(ExcelType.XSSF);
			Workbook workbook = ExcelExportUtil.exportExcel(params, entity, list);
			workbook.write(stream);
			workbook.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("人员名单电子表格生成异常!");
			}
		}
	}

	public ByteArrayOutputStream hotel(NewTripplanJpEntity trip, String guest) {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			Map<String, String> map = new HashMap<String, String>();
			if (!Util.isEmpty(trip.getHotelid())) {

				Hotel hotel = this.dbdao.fetch(Hotel.class, Long.valueOf(trip.getHotelid()));
				if (!Util.isEmpty(hotel)) {

					map.put("hotel", hotel.getNameJP() + "\n" + hotel.getAddressJP() + "\n" + hotel.getPhone());
					map.put("city", hotel.getCity());
				}
			}
			map.put("guest", guest);
			DateTime dt = new DateTime(trip.getNowdate());
			if (!Util.isEmpty(trip.getIntime())) {

				dt = dt.withField(DateTimeFieldType.hourOfDay(), trip.getIntime().getHours());
				dt = dt.withField(DateTimeFieldType.minuteOfHour(), trip.getIntime().getMinutes());
			}
			map.put("checkInDate", df5.format(dt.toDate()));
			if (!Util.isEmpty(trip.getOuttime())) {

				map.put("checkOutDate", df5.format(trip.getOuttime()));
			}
			map.put("room", trip.getHometype() + "");
			map.put("breakfast", trip.getBreakfast() + "");
			map.put("dinner", trip.getDinner() + "");
			//1 不进行密码验证
			PdfReader.unethicalreading = true;
			//2 读入pdf表单
			PdfReader reader = new PdfReader(getClass().getClassLoader().getResource(getPrefix() + "hotel.pdf"));
			//3 根据表单生成一个新的pdf
			PdfStamper ps = new PdfStamper(reader, stream);
			//4 获取pdf表单
			AcroFields fields = ps.getAcroFields();
			//5给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
			fields.addSubstitutionFont(getBaseFont());
			//6遍历pdf表单表格，同时给表格赋值
			Iterator<Map.Entry<String, AcroFields.Item>> iterator = fields.getFields().entrySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().getKey();
				if (map.containsKey(key)) {
					fields.setField(key, map.get(key));
				}
			}
			ps.setFormFlattening(true); // 这句不能少
			ps.close();
			reader.close();
			IOUtils.closeQuietly(stream);
			return stream;
		} catch (Exception e) {
			if (e instanceof JSiteException) {
				throw (JSiteException) e;
			} else {
				e.printStackTrace();
				throw new JSiteException("酒店信息生成异常!");
			}
		}
	}

}
