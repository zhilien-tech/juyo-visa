/**
 * SimulateService.java
 * io.znz.jsite.visa.simulator.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.controller;

import io.znz.jsite.visa.simulator.service.SimulateViewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * 自动填表控制器:
 * 1，查询可提交签证的客户信息用于自动填写官方签证网站
 * 2，当任务执行完毕，更新客户信息为'提交中'
 *
 * @author   朱晓川
 * @Date	 2017年7月6日 	 
 */
@Controller
@RequestMapping("visa/simulator")
public class SimulateController {

	@Autowired
	private SimulateViewService simulateViewService;

	/**
	 * 获取第一个已经审核完毕的任务提交到美国签证网站
	 */
	@RequestMapping(value = "fetch", method = RequestMethod.GET)
	@ResponseBody
	public Object fetchUSA() {
		return simulateViewService.fetchCustomer4SimulatorUSA();
	}

}