package io.znz.jsite.core.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.core.util.Const;
import io.znz.jsite.ext.upload.FileStore;
import io.znz.jsite.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 站点设置controller
 *
 * @author Chaly
 */
@Controller
@RequestMapping("upload")
public class UploadController extends BaseController {

    @Autowired
    protected FileStore fileRepository;

    /**
     * 上传
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Object uploadSubmit(
        @RequestParam(required = false) String path,
        @RequestParam(required = false) MultipartFile file,
        @RequestParam(defaultValue = "true") boolean rename
    ) {
        String fileUrl;
        try {
            String root = Const.UPLOAD_PATH;
            if (StringUtils.isNotBlank(path)) root += path;
            fileUrl = fileRepository.store(root, file, rename);
        } catch (Exception e) {
            return ResultObject.fail("文件上传错误！");
        }
        return ResultObject.success(fileUrl);
    }
}
