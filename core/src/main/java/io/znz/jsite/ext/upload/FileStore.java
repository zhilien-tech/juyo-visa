package io.znz.jsite.ext.upload;

import javaxt.io.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * 本地文件存储
 */
@Component
public class FileStore {

    private Logger log = LoggerFactory.getLogger(FileStore.class);

    @Autowired
    private RealPathAware realPathAware;

    public String store(String path, MultipartFile file, boolean rename) {
        String originalName = file.getOriginalFilename();
        int index = originalName.lastIndexOf(".");
        String ext = index > -1 ? originalName.substring(index + 1) : ContentType.getExtension(file.getContentType());
        String fileName;
        if (rename) {
            fileName = UploadUtils.generateFilename(realPathAware.getRealPath(path), ext);
        } else {
            fileName = UploadUtils.generateByFilename(realPathAware.getRealPath(path), originalName);
        }
        File dest = new File(fileName);
        dest = store(file, dest);
        fileName = dest.getAbsolutePath();
        return fileName.substring(fileName.indexOf(path));
    }

    private File store(MultipartFile file, File dest) {
        UploadUtils.checkDirAndCreate(dest.getParentFile());
        try {
            //先尝试吧原来的文件删除
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("Transfer file error when upload file", e);
        }
        //如果是图片尝试纠图片方向
        if (isImage(dest)) {
            dest = orientation(dest);
        }
        return dest;
    }

    /**
     * 纠正图片文件的方向
     */
    private File orientation(File dest) {
        try {
            // Browsers today can't handle images with Exif Orientation tag
            Image image = new Image(dest.getAbsolutePath());
            // 尝试获取方向信息
            int orientation = (Integer) image.getExifTags().get(0x0112);
            // 按照EXIF信息中的方向自动旋转，并删除所有的EXIF标签
            if (orientation > 1) {
                image.rotate();
                //如果太大,顺便再压缩一下
                if (image.getWidth() > 1024 || image.getHeight() > 1024) {
                    image.setOutputQuality(70);
                    image.resize(1024, 1024, true);
                }
                image.saveAs(dest);
            }
        } catch (Exception e) {
        }
        return dest;
    }

    private boolean isImage(File file) {
        try {
            java.awt.Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException ex) {
            return false;
        }
    }
}
