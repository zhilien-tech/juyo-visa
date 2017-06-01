package io.znz.jsite.visa.web;

import io.znz.jsite.base.BaseController;
import io.znz.jsite.base.bean.ResultObject;
import io.znz.jsite.visa.bean.Photo;
import io.znz.jsite.visa.bean.PhotoOption;
import io.znz.jsite.visa.bean.helper.Range;
import io.znz.jsite.visa.service.PhotoOptionService;
import io.znz.jsite.visa.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Chaly on 2017/3/7.
 */
@Controller
@RequestMapping("visa/photo")
public class PhotoController extends BaseController {
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoOptionService photoOptionService;

    @RequestMapping(value = "option/list", method = RequestMethod.GET)
    @ResponseBody
    public Object optionList(HttpServletRequest request) {
        Pageable pageable = getPageable(request);
        return ResultObject.success(photoOptionService.getAll(pageable));
    }

    @RequestMapping(value = "option/save", method = RequestMethod.POST)
    @ResponseBody
    public Object save(PhotoOption option) {
        return ResultObject.success(photoOptionService.save(option));
    }

    @RequestMapping(value = "option/del/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Object del(@PathVariable int id) {
        photoOptionService.delete(id);
        return ResultObject.success();
    }

    @RequestMapping(value = "options", method = RequestMethod.GET)
    @ResponseBody
    public Object options(@RequestParam(defaultValue = "USA") Range range) {
        List<PhotoOption> options = photoService.listOptionsByRange(range);
        return ResultObject.success(options);
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Object customer(@RequestBody List<Photo> photos) {
        return ResultObject.success(photoService.saveAll(photos));
    }

    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(long cid) {
        return ResultObject.success(photoService.findByCustomer(cid));
    }
}
