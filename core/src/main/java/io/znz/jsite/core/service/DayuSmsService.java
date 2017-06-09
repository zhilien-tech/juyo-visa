package io.znz.jsite.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import io.znz.jsite.base.PropertyFilter;
import io.znz.jsite.core.bean.Dict;
import io.znz.jsite.ext.plugin.MessagePlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chaly on 15/11/2.
 */
@Service
@Transactional(readOnly = true)
public class DayuSmsService implements MessagePlugin {

    @Value("${qfx.sms.appkey}")
    private String appkey = "23535998";
    @Value("${qfx.sms.secret}")
    private String secret = "6ef5b8a873bc4a2895d22b8c8b940bcb";
    @Value("${qfx.sms.url}")
    private String url = "http://gw.api.taobao.com/router/rest";
    @Value("${qfx.sms.signName}")
    private String signName = "师门";
    @Value("${qfx.sms.templateCode}")
    private String templateCode = "SMS_26455021";
    @Autowired
    private DictService dictService;

    /**
     * @param targets :手机号之间按逗号分隔
     * @param code    :需要发送的内容
     * @param args    :其他参数
     */

    public String send(String targets, String code, Object... args) {
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setRecNum(targets);
        req.setSmsType("normal");
        req.setSmsFreeSignName(signName);
        req.setSmsParamString("{\"code\":\"" + code + "\",\"product\":\"" + args[0] + "\"}");
        req.setSmsTemplateCode(templateCode);
        try {
            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            JSONObject jo = JSON.parseObject(rsp.getBody());
            if (jo.get("error_response") != null) {
                return jo.getJSONObject("error_response").getString("sub_msg");
            }
            return "success";
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "发送失败请稍候再试!";
    }

    @Transactional(readOnly = false)
    public boolean init() {
        Dict dict = new Dict();
        dict.setSort(2);
        dict.setKey("verify");
        dict.setDelFlag(false);
        dict.setCategory("SMS");
        dict.setLabel("短信验证");
        dict.setValue("dayuSmsService");
        dict.setDescription("阿里大鱼的短信发送服务插件");
        dict = dictService.save(dict);
        return dict.getId() != null;
    }

    public String config() {
        return "";
    }

    public List<PluginMenu> more() {
        return new ArrayList<PluginMenu>();
    }

    public boolean clean() {
        return false;
    }

    @Transactional(readOnly = false)
    public boolean clear() {
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_value", "dayuSmsService"));
        filters.add(new PropertyFilter("EQS_key", "verify"));
        List<Dict> dicts = dictService.search(filters);
        dictService.deleteAll(dicts);
        return true;
    }

    public static void main(String[] arg) {
        String msg = new DayuSmsService().send("15210203617", "2508", "注册");
        System.out.println(msg);
    }
}
