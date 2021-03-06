/**
 * SimulateService.java
 * io.znz.jsite.visa.simulator.service
 * Copyright (c) 2017, 北京科技有限公司版权所有.
*/

package io.znz.jsite.visa.simulator.controller;

import io.znz.jsite.visa.simulator.form.JapanErrorHandleForm;
import io.znz.jsite.visa.simulator.form.JapanSimulatorForm;
import io.znz.jsite.visa.simulator.service.SimulateJapanViewService;
import io.znz.jsite.visa.simulator.service.SimulateViewService;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	private SimulateJapanViewService simulateJapanViewService;

	/**
	 * 获取第一个已经审核完毕的任务提交到美国签证网站
	 */
	@RequestMapping(value = "fetch", method = RequestMethod.GET)
	@ResponseBody
	public Object fetchUSA() {
		return simulateViewService.fetchCustomer4SimulatorUSA();
	}

	/**
	 * 将准备提交的任务修改为'提交中'
	 */
	@RequestMapping(value = "ds160/{cid}", method = RequestMethod.GET)
	@ResponseBody
	public Object ds160(@PathVariable Long cid) {
		return simulateViewService.ds160(cid);
	}

	/**
	 * 美国，签证文件上传，上传成功 将签证状态改为已提交使馆
	 */
	@RequestMapping(value = "usaUpload/{cid}", method = RequestMethod.POST)
	@ResponseBody
	public Object usaUpload(@RequestParam(required = false) MultipartFile file, @PathVariable Long cid) {
		return simulateViewService.usaUpload(file, cid);
	}

	/**
	 * 日本：获取第一个已经审核完毕的任务提交到日本签证网站
	 */
	@RequestMapping(value = "fetchJapan", method = RequestMethod.GET)
	@ResponseBody
	public Object fetchJapan() {
		return simulateJapanViewService.fetchJapanOrder();
	}

	/**
	 * 日本;将准备提交的任务修改为'提交中'
	 */
	@RequestMapping(value = "ds160Japan/{cid}", method = RequestMethod.GET)
	@ResponseBody
	public Object ds160Japan(@PathVariable Long cid) {
		return simulateJapanViewService.ds160Japan(cid);
	}

	/**
	 * 日本，签证文件上传，上传成功 将签证状态改为已提交使馆
	 */
	@RequestMapping(value = "UploadJapan/{cid}", method = RequestMethod.POST)
	@ResponseBody
	public Object UploadJapan(@RequestParam(required = false) MultipartFile file, @PathVariable Long cid,
			JapanSimulatorForm jpForm) {
		return simulateJapanViewService.UploadJapan(file, cid, jpForm);
	}

	/**
	 * 日本;将准备提交的任务修改为'提交中'
	 */
	@RequestMapping(value = "agentDownload", method = RequestMethod.POST)
	public void agentDownload(final JapanSimulatorForm jpForm, HttpServletResponse response) {
		simulateJapanViewService.agentDownload(jpForm, response);
	}

	/**
	 * 日本;接收自动填表的错误信息
	 */
	@RequestMapping(value = "japanErrorHandle/{cid}", method = RequestMethod.POST)
	public void japanErrorHandle(final JapanErrorHandleForm jpForm, HttpServletResponse response, @PathVariable Long cid) {
		simulateJapanViewService.japanErrorHandle(jpForm, response, cid);
	}
}
