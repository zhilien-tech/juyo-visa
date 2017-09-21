package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.PageFilter;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.form.FilterValueForm;
import io.znz.jsite.visa.service.FlightService;
import io.znz.jsite.visa.util.Const;
import io.znz.jsite.visa.util.ExcelReader;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.google.common.collect.Lists;
import com.uxuexi.core.common.util.Util;
import com.uxuexi.core.db.dao.IDbDao;

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/flight")
public class FlightController extends BaseController {
	@Autowired
	private FlightService flightService;

	/**
	 * 注入容器中的dbDao对象，用于数据库查询、持久操作
	 */
	@Autowired
	private IDbDao dbDao;

	@RequestMapping(value = "list")
	@ResponseBody
	public Object list(@RequestBody(required = false) PageFilter filter) {
		if (filter == null)
			filter = new PageFilter();
		Pageable pageable = filter.getPageable();
		Criterion[] filters = filter.getFilter(Flight.class);
		return flightService.search(pageable, filters);

	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Object save(Flight flight) {
		return ResultObject.success(flightService.save(flight));
	}

	@RequestMapping(value = "del/{id}", method = RequestMethod.POST)
	@ResponseBody
	public Object del(@PathVariable int id) {
		flightService.delete(id);
		return ResultObject.success();
	}

	@RequestMapping(value = "json")
	@ResponseBody
	public Object json(@RequestParam(required = false) String filter) {
		return flightService.findByFilter(filter);
	}

	@RequestMapping(value = "filghtByCity")
	@ResponseBody
	public Object filghtByCity(String fromCity, String toCity) {
		return flightService.filghtByCity(fromCity, toCity);
	}

	@RequestMapping(value = "filghtByCityFilter")
	@ResponseBody
	public Object filghtByCity(FilterValueForm form) {
		return flightService.filghtByCity(form.getFromCity(), form.getToCity(), form);
	}

	//日本航班信息 Excel导入
	@RequestMapping(value = "importExcel")
	@ResponseBody
	public Object importExcel(HttpServletRequest request) throws Exception {
		request.setCharacterEncoding(Const.CHARACTER_ENCODING_PROJECT);//字符编码为utf-8
		//将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());
		//检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {
			//将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			//获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				//一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());

				if (file != null) {
					InputStream is = file.getInputStream();
					ExcelReader excelReader = new ExcelReader();
					SimpleDateFormat FORMAT_DEFAULT_DATE = new SimpleDateFormat("yyyy-MM-dd");
					//获取Excel模板第二行之后的数据
					Map<Integer, String[]> map = excelReader.readExcelContent(is);
					List<Flight> filghts = Lists.newArrayList();
					for (int i = map.size(); i > 0; i--) {
						Flight flight = new Flight();
						String[] row = map.get(i);
						flight.setFrom(row[0]);
						flight.setTo(row[2]);
						flight.setLine(row[4]);
						String departureStr = row[5];
						String landStr = row[6];
						if (!Util.isEmpty(departureStr)) {
							int dLength = departureStr.length();
							if (departureStr.contains(":") && (dLength == 5 || dLength == 7)) {
								if (departureStr.length() == 7) {
									departureStr = departureStr.substring(0, 5);
								}
								String[] split = departureStr.split(":");
								String hours = split[0];
								String mins = split[1];
								int hour = Integer.valueOf(hours);
								int min = Integer.valueOf(mins);
								if (hour >= 0 && hour <= 24 && min >= 0 && min <= 59) {
									Date date = new Date();
									date.setHours(hour);
									date.setMinutes(min);
									flight.setDeparture(date); //起飞时间
								}
							}
						}

						if (!Util.isEmpty(landStr)) {
							int lLength = landStr.length();
							if (landStr.contains(":") && (lLength == 5 || lLength == 7)) {
								if (landStr.length() == 7) {
									landStr = landStr.substring(0, 5);
								}
								String[] split = landStr.split(":");
								String hours = split[0];
								String mins = split[1];
								int hour = Integer.valueOf(hours);
								int min = Integer.valueOf(mins);
								if (hour >= 0 && hour <= 24 && min >= 0 && min <= 59) {
									Date date = new Date();
									date.setHours(hour);
									date.setMinutes(min);
									flight.setLanding(date); //降落时间
								}

							}
						}

						flight.setFromTerminal(row[7]);
						flight.setFromCity(row[8]);
						flight.setToTerminal(row[9]);
						flight.setToCity(row[10]);
						filghts.add(flight);
					}
					dbDao.insert(filghts);
				}

			}

		}
		return null;

	}

	/*public Date string2Date(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	
	}*/
}
