package io.znz.jsite.visa.web;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.PageFilter;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.util.RequestUtil;
import io.znz.jsite.util.StringUtils;
import io.znz.jsite.util.ZipUtil;
import io.znz.jsite.visa.bean.Customer;
import io.znz.jsite.visa.bean.helper.State;
import io.znz.jsite.visa.service.CustomerService;
import io.znz.jsite.visa.service.HelpService;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/customer")
public class CustomerController extends BaseController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private HelpService helpService;

    /**
     * 二维码
     */
    @RequestMapping(value = "qrcode", method = RequestMethod.GET)
    public void get(HttpServletRequest req, HttpServletResponse resp, String cid) throws WriterException, IOException {
        String content = RequestUtil.getServerPath(req) + "/m/upload.html?cid=" + cid;
        int width = 268, height = 268;
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // 容错级别
        hints.put(EncodeHintType.MARGIN, 1);//二维码留白边距
        BitMatrix m = writer.encode(content, BarcodeFormat.QR_CODE, height, width, hints);
        OutputStream stream = resp.getOutputStream();
        MatrixToImageWriter.writeToStream(m, "jpg", stream);
        if (stream != null) {
            stream.flush();
            stream.close();
        }
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(@RequestBody(required = false) PageFilter filter) {
        if (filter == null) filter = new PageFilter();
        Pageable pageable = filter.getPageable();
        Conjunction filters = Restrictions.conjunction(filter.getFilter(Customer.class));
        //过滤无用的未关联的客户
        Criterion c = Restrictions.sqlRestriction("{alias}.id IN (SELECT customer_id FROM visa_order_customer)");
        return customerService.search(pageable, filters.add(c));
    }

    /**
     * 加载客户详情
     *
     * @param cid 客户编号
     */
    @RequestMapping(value = "show", method = RequestMethod.GET)
    @ResponseBody
    public Object show(long cid) {
        Customer customer = customerService.get(cid);
        if (customer == null) {
            return ResultObject.fail("客户订单不存在,请核实后再试!");
        }
        customer.getFinances().size();
        String json = JSON.toJSONStringWithDateFormat(customer, "yyyy-MM-dd");
        return JSON.parseObject(json);
    }

    /**
     * 提交客户详情到服务端
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Object customer(@RequestBody Customer customer) {
        customer = customerService.saveOrUpdate(customer);//直接保存如果有问题直接有统一的 JSON 异常抛出
        String json = JSON.toJSONStringWithDateFormat(customer, "yyyy-MM-dd");
        return JSON.parseObject(json);
    }

    @RequestMapping(value = "/{action}/{cid}", method = RequestMethod.POST)
    @ResponseBody
    public Object process(@PathVariable String action, @PathVariable long cid, @RequestParam(required = false) String reason) {
        Customer customer = customerService.get(cid);
        if (customer == null) {
            return ResultObject.fail("订单客户不存在,请重试!");
        }
        if ("agree".equalsIgnoreCase(action)) {
            customer.setState(State.CONFIRMED);
            helpService.deleteByCustomer(customer.getId());
        } else if ("refuse".equalsIgnoreCase(action)) {
            customer.setState(State.REFUSE);
        }
        customer.setReason(reason);
        customerService.save(customer);
        return ResultObject.success();
    }

    /**
     * 实现文件的下载
     */
    @ResponseBody
    @RequestMapping(value = "download/{cid}")
    public Object download(@PathVariable long cid, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        Customer c = customerService.get(cid);
        if (c == null || StringUtils.isBlank(c.getFiles())) {
            return ResultObject.fail("DS160文件不存在!");
        }
        //压缩 DS160文件
        String root = request.getServletContext().getRealPath("/");
        StringBuilder sb = new StringBuilder("DS160-").append(c.getLastName() + c.getFirstName()).append("-");
        List<ZipUtil.FileEntry> fileEntrys = new ArrayList<ZipUtil.FileEntry>();
        for (String f : c.getFiles().split(",")) {
            File file = new File(root + File.separator + f);
            if (!file.exists()) return ResultObject.fail("DS160文件不存在!");
            fileEntrys.add(new ZipUtil.FileEntry("", "", file));
            String fileName = file.getName();
            if (StringUtils.endsWithIgnoreCase(fileName, "dat")) {
                sb.append(fileName.substring(0, fileName.indexOf(".")));
            }
        }
        sb.append(".zip");
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(sb.toString(), "UTF-8"));// 设置文件名
        try {
            // 模板一般都在windows下编辑，所以默认编码为GBK
            ZipUtil.zip(response.getOutputStream(), fileEntrys, "GBK");
        } catch (IOException e) {
            logger.error("export db error!", e);
        }
        return ResultObject.fail("暂无可用DS160文件!");
    }

    /**
     * 随机获取一个已经审核完毕的任务提交到签证网站
     */
    @RequestMapping(value = "fetch", method = RequestMethod.GET)
    @ResponseBody
    public Object fetch() {
        return customerService.findByState();
    }

    @RequestMapping(value = "finish/{cid}", method = RequestMethod.POST)
    @ResponseBody
    public Object finish(@PathVariable long cid, String[] file) {
        if (customerService.updateState(cid, State.FINISH, StringUtils.join(file, ","))) {
            return ResultObject.success();
        } else {
            return ResultObject.fail("编号为" + cid + "的客户不存在!");
        }
    }
}
