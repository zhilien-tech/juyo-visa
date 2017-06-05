package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.visa.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/test")
public class TestController extends BaseController {

	@Autowired
	private TestService testService;

	/**
	 * 二维码
	 */
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public Object get() {
		return testService.test();
	}

}
