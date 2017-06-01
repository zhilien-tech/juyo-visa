package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.core.bean.User;
import io.znz.jsite.core.service.UserService;
import io.znz.jsite.util.AvatarUtil;
import io.znz.jsite.util.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 站点设置controller
 * @author Chaly
 */
@Controller
@RequestMapping("avatar")
public class AvatarController extends BaseController {

    @Autowired
    protected UserService userService;

    /**
     * 上传
     */
    @RequestMapping(value = "{uid}", method = RequestMethod.GET)
    public Object get(HttpServletRequest req, HttpServletResponse resp, @PathVariable String uid) throws IOException {
        User user = userService.get(uid);
        File file = RequestUtil.getRealPath(req, "/upload/avatar/" + uid + ".png");
        if (user == null) {
            file = RequestUtil.getRealPath(req, "/res/sys/img/default_avatar.png");
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            BufferedImage image = AvatarUtil.getInstance().create(user.getName(), 512);
            ImageIO.write(image, "PNG", file);
        }
        resp.reset();
        resp.setContentType("image/png");//设置相应信息的类型
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(bis, resp.getOutputStream());
        return null;
    }

}
