package io.znz.jsite.ext.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import io.znz.jsite.core.bean.Permission;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.PermissionService;
import io.znz.jsite.core.util.UserUtil;
import io.znz.jsite.ext.freemarker.util.BeanWrapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaly on 2017/4/27.
 */
@Component
public class MenuDirective implements TemplateDirectiveModel {

    @Autowired
    protected PermissionService permissionService;

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        List<Permission> list = new ArrayList<Permission>();
        User user = UserUtil.getUser();
        if (user != null) {
            list = permissionService.getMenus(user.getId(), 0);
        }
        env.setVariable("menus", BeanWrapperFactory.get().wrap(list));
        body.render(env.getOut());
    }
}
