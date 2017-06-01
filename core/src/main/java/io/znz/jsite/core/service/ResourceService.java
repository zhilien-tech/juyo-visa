package io.znz.jsite.core.service;

import io.znz.jsite.ext.upload.FileWrap;
import io.znz.jsite.ext.upload.RealPathAware;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Chaly on 15/11/2.
 */
@Component
public class ResourceService {

    @Autowired
    private RealPathAware realPathAware;

    public void unZipFile(File file) throws IOException {
        // 用默认编码或UTF-8编码解压会乱码！windows7的原因吗？
        //解压之前要检查是否冲突
        ZipFile zip = new ZipFile(file, "GBK");
        ZipEntry entry;
        String name;
        String filename;
        File outFile;
        File pfile;
        byte[] buf = new byte[1024];
        int len;
        InputStream is = null;
        OutputStream os = null;
        Enumeration<ZipEntry> en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            name = entry.getName();
            if (!entry.isDirectory()) {
                name = entry.getName();
                filename = name;
                outFile = new File(realPathAware.getRealPath(filename));
                if (outFile.exists()) {
                    break;
                }
                pfile = outFile.getParentFile();
                if (!pfile.exists()) {
                    //	pfile.mkdirs();
                    createFolder(outFile);
                }
                try {
                    is = zip.getInputStream(entry);
                    os = new FileOutputStream(outFile);
                    while ((len = is.read(buf)) != -1) {
                        os.write(buf, 0, len);
                    }
                } finally {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                    if (os != null) {
                        os.close();
                        os = null;
                    }
                }
            }
        }
        zip.close();
    }

    public void deleteZipFile(File file) throws IOException {
        //根据压缩包删除解压后的文件
        // 用默认编码或UTF-8编码解压会乱码！windows7的原因吗
        ZipFile zip = new ZipFile(file, "GBK");
        ZipEntry entry;
        String name;
        String filename;
        File directory;
        //删除文件
        Enumeration<ZipEntry> en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            if (!entry.isDirectory()) {
                name = entry.getName();
                filename = name;
                directory = new File(realPathAware.getRealPath(filename));
                if (directory.exists()) {
                    directory.delete();
                }
            }
        }
        //删除空文件夹
        en = zip.getEntries();
        while (en.hasMoreElements()) {
            entry = en.nextElement();
            if (entry.isDirectory()) {
                name = entry.getName();
                filename = name;
                directory = new File(realPathAware.getRealPath(filename));
                if (!directoryHasFile(directory)) {
                    directory.delete();
                }
            }
        }
        zip.close();
    }

    private void createFolder(File f) {
        File parent = f.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
            createFolder(parent);
        }
    }

    //文件夹判断是否有文件
    private boolean directoryHasFile(File directory) {
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (directoryHasFile(f)) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public int delete(String[] names) {
        File f;
        int count = 0;
        for (String name : names) {
            f = new File(realPathAware.getRealPath(name));
            if (f.isDirectory()) {
                if (FileUtils.deleteQuietly(f)) {
                    count++;
                }
            } else {
                if (f.delete()) {
                    count++;
                }
            }
        }
        return count;
    }

    // 文件夹和可编辑文件则显示
    private FileFilter filter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() || FileWrap.editableExt(FilenameUtils.getExtension(file.getName()));
        }
    };

    public List<FileWrap> listFile(String path, boolean dirAndEditable) {
        File parent = new File(realPathAware.getRealPath(path));
        if (parent.exists()) {
            File[] files;
            if (dirAndEditable) {
                files = parent.listFiles(filter);
            } else {
                files = parent.listFiles();
            }
            Arrays.sort(files, new FileWrap.FileComparator());
            List<FileWrap> list = new ArrayList<FileWrap>(files.length);
            for (File f : files) {
                if (!f.isHidden())
                    list.add(new FileWrap(f, realPathAware.getRealPath("")));
            }
            return list;
        } else {
            return new ArrayList<FileWrap>(0);
        }
    }

    public void rename(String origName, String destName) {
        File orig = new File(realPathAware.getRealPath(origName));
        File dest = new File(realPathAware.getRealPath(destName));
        orig.renameTo(dest);
    }

}
