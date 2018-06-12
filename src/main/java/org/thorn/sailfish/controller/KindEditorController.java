package org.thorn.sailfish.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.kindeditor.KindEditorComparator;
import org.thorn.sailfish.kindeditor.KindManageResult;
import org.thorn.sailfish.kindeditor.KindResource;
import org.thorn.sailfish.kindeditor.KindUploadResult;
import org.thorn.sailfish.utils.DateTimeUtils;
import org.thorn.sailfish.utils.PathUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yfchenyun
 * @Since: 13-11-28 上午10:00
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am/ke")
public class KindEditorController {

    static Logger log = LoggerFactory.getLogger(KindEditorController.class);

    private static Map<String, String[]> extMap = new HashMap<String, String[]>();

    static {
        extMap.put("image", new String[]{"gif","jpg","jpeg","png","bmp"});
        extMap.put("flash", new String[]{"swf","flv"});
        extMap.put("media", new String[]{"swf","flv","mp3","wav","wma",
                "wmv","mid","avi","mpg","asf","rm","rmvb"});
        extMap.put("file", new String[]{"doc","docx","xls","xlsx","ppt",
                "htm","html","txt","zip","rar","gz","bz2"});
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public KindUploadResult upload(String dir, @RequestParam("imgFile") CommonsMultipartFile file,
                             HttpServletRequest request, HttpSession session) throws IOException {
        KindUploadResult result = new KindUploadResult();

        if(StringUtils.isEmpty(dir)) {
            dir = "image";
        }

        if(!extMap.containsKey(dir)) {
            result.setError("目录名称不正确。");
            return result;
        }

        //检查文件类型，后缀检查
        FileItem fileItem = file.getFileItem();
        String resourceName = fileItem.getName();
        String resourceType = null;
        for(String type : extMap.get(dir)) {
            if(StringUtils.endsWithIgnoreCase(resourceName, type)) {
                resourceType = type;
                break;
            }
        }

        if(resourceType == null) {
            result.setError("文件类型不允许上传。");
            return result;
        }

        resourceName = StringUtils.removeEndIgnoreCase(resourceName, "." + resourceType);

        //检查目录
        String date = DateTimeUtils.getCurrentDate();
        String dirName = PathUtils.getContextPath(session) + Configuration.STATIC_ATTACH_PATH
                + File.separator + dir + File.separator + date;
        File folder = new File(dirName);

        if(!folder.exists()) {
            folder.mkdirs();
        }

        File resource = new File(folder, resourceName + "." + resourceType);
        int fileSuffix = 1;
        while(resource.exists()) {
            resource = new File(folder, resourceName + "_" + fileSuffix + "." + resourceType);
            fileSuffix++;
        }

        try {
            resource.createNewFile();
            OutputStream os = new FileOutputStream(resource);
            os.write(file.getBytes());
            os.flush();
            os.close();

            String httpUrl = StringUtils.replace(resource.getAbsolutePath(),
                    PathUtils.getContextPath(session), request.getContextPath());
            httpUrl = httpUrl.replaceAll("\\\\", "/");

            result.setUrl(httpUrl);
        } catch (IOException e) {
            result.setError("文件上传失败：" + e.getMessage());
            log.error("upload[File] - " + e.getMessage(), e);
        }

        return result;
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public KindManageResult listFiles(String order, String dir, String path,
                                      HttpServletRequest request, HttpSession session) {
        KindManageResult result = new KindManageResult();

        if(StringUtils.isEmpty(dir) || !extMap.containsKey(dir)) {
            dir = "image";
        }

        if(StringUtils.isEmpty(path)) {
            path = "";

        } else if(!StringUtils.endsWith(path, "/")) {
            path += "/";
        }

        if(path.length() > 0) {
            String lastDirPath = path.substring(0, path.length() - 1);
            lastDirPath = lastDirPath.lastIndexOf("/") >= 0 ? lastDirPath.substring(0, lastDirPath.lastIndexOf("/") + 1) : "";
            result.setMoveup_dir_path(lastDirPath);
        } else {
            result.setMoveup_dir_path("");
        }

        result.setCurrent_dir_path(path);


        String url = request.getContextPath() + Configuration.STATIC_ATTACH_PATH + "/" + dir + "/" + path;
        String filePath = PathUtils.getContextPath(session) + Configuration.STATIC_ATTACH_PATH + File.separator +
                dir + File.separator + path;
        url = url.replaceAll("\\\\", "/");

        result.setCurrent_url(url);

        File folder = new File(filePath);
        if(!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }

        if(folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                KindResource resource = new KindResource();
                resource.setFilename(file.getName());
                resource.setDatetime(DateTimeUtils.formatTime(file.lastModified()));
                if(file.isDirectory()) {
                    resource.setIs_dir(true);
                    resource.setHas_file(file.listFiles() != null);
                    resource.setFilesize(0L);
                    resource.setIs_photo(false);
                    resource.setFiletype("");
                } else if(file.isFile()){
                    String fileExt = resource.getFilename().substring(resource.getFilename().lastIndexOf(".") + 1).toLowerCase();
                    resource.setIs_dir(false);
                    resource.setHas_file(false);
                    resource.setFilesize(file.length());
                    resource.setIs_photo(StringUtils.equals(dir, "image"));
                    resource.setFiletype(fileExt);
                }
                result.getFile_list().add(resource);
            }
        }

        result.setTotal_count(result.getFile_list().size());
        Comparator<KindResource> comparator = new KindEditorComparator(order);
        Collections.sort(result.getFile_list(), comparator);

        return result;
    }


}
