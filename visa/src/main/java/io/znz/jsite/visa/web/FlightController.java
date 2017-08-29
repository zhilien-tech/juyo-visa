package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.PageFilter;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.bean.Flight;
import io.znz.jsite.visa.service.FlightService;

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

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/flight")
public class FlightController extends BaseController {
	@Autowired
	private FlightService flightService;

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

}
