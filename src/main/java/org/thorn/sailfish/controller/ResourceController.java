package org.thorn.sailfish.controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.JsonResponse;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.entity.*;
import org.thorn.sailfish.enums.OperateEnum;
import org.thorn.sailfish.service.ResourceLogService;
import org.thorn.sailfish.utils.DateTimeUtils;
import org.thorn.sailfish.utils.PathUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: chen.chris
 * @Since: 13-10-16 上午10:03
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am/rs")
public class ResourceController {

    static Logger log = LoggerFactory.getLogger(ResourceController.class);

    private static final String CMS_TAG = "CMS";

    private static final String FLT_TAG = CMS_TAG + "/FLT";

    @Autowired
    private ResourceLogService resourceLogService;

    @RequestMapping("/index")
    public String index(String p, HttpSession session, ModelMap modelMap) {

        if (StringUtils.isEmpty(p)) {
            p = CMS_TAG;
        }

        p = StringUtils.removeStart(p, "/");
        p = StringUtils.removeStart(p, "\\");


        if (!StringUtils.startsWith(p, CMS_TAG)) {
            p = CMS_TAG;
        }

        p = StringUtils.removeEnd(p, "/");
        p = StringUtils.removeEnd(p, "\\");

        String realPath = getRealPath(p, session);

        //获取目录下的文件信息
        //获取目录下的文件夹信息及文件夹的数量
        File folder = new File(realPath);
        if(!folder.exists() && StringUtils.equals(p, CMS_TAG)) {
            folder.mkdirs();
        } else if (!folder.exists()) {
            return "redirect:/am/rs/index";
        }

        File[] files = folder.listFiles();

        List<Resource> resources = new ArrayList<Resource>();
        ResourceFolder resourceFolder = new ResourceFolder();
        resourceFolder.setFileNumber(files.length);
        resourceFolder.setPath(p);

        if (p.equals(CMS_TAG)) {
            resourceFolder.setName("资源库 - CMS");
        } else if (p.equals(FLT_TAG)) {
            resourceFolder.setName("模板库 - CMS/FLT");
            resourceFolder.setParent(CMS_TAG);
        } else {
            resourceFolder.setName(folder.getName());
            resourceFolder.setParent(StringUtils.removeEnd(p, resourceFolder.getName()));
        }

        for (File file : files) {
            if (file.isDirectory()) {
                ResourceFolder childFolder = new ResourceFolder();
                childFolder.setName(file.getName());
                childFolder.setPath(p + "/" + file.getName());
                childFolder.setFileNumber(file.list().length);
                resourceFolder.getChildFolders().add(childFolder);
            } else {
                Resource resource = new Resource();
                resource.setName(file.getName());
                resource.setSize(file.length());
                resource.setLastModifyTime(DateTimeUtils.formatTime(new Date(file.lastModified())));
                resource.setText(needLog(file.getName()));
                resources.add(resource);
            }
        }

        if (resourceFolder.getParent() == null) {
            resourceFolder.setFileNumber(resourceFolder.getFileNumber() + 1);

            ResourceFolder childFolder = new ResourceFolder();
            childFolder.setName("模板库 - CMS/FLT");
            childFolder.setPath(FLT_TAG);

            File fwFile = new File(PathUtils.getContextPath(session) + Configuration.TEMPLATE_PATH);
            childFolder.setFileNumber(fwFile.list().length);
            resourceFolder.getChildFolders().add(childFolder);
        }

        modelMap.put("folder", resourceFolder);
        modelMap.put("resources", resources);
        modelMap.put("p", p);

        return "resource";
    }

    @RequestMapping("/createFolder")
    @ResponseBody
    public Status createFolder(String p, String dir, HttpSession session) {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        String realPath = getRealPath(p, session);

        File folder = new File(realPath);
        if (!directoryExists(folder)) {
            status.setSuccess(false);
            status.setMessage("上级目录不存在");
            return status;
        }

        folder = new File(folder, dir);
        if (folder.exists()) {
            status.setSuccess(false);
            status.setMessage("目录名称已经存在");
            return status;
        }

        folder.mkdir();
        status.setMessage("目录创建成功");

        return status;
    }

    @RequestMapping("/renameFolder")
    @ResponseBody
    public Status renameFolder(String p, String renameDir, HttpSession session) {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        p = StringUtils.removeEnd(p, "/");
        p = StringUtils.removeEnd(p, "\\");

        if (StringUtils.equals(p, CMS_TAG) && StringUtils.equals(p, FLT_TAG)) {
            status.setSuccess(false);
            status.setMessage("系统目录不允许修改");
            return status;
        }

        String realPath = getRealPath(p, session);

        File folder = new File(realPath);
        if (!directoryExists(folder)) {
            status.setSuccess(false);
            status.setMessage("当前目录不存在");
            return status;
        }

        File newFolder = new File(folder.getParent(), renameDir);
        if (newFolder.exists()) {
            status.setSuccess(false);
            status.setMessage("目录名称已经存在");
            return status;
        }

        folder.renameTo(newFolder);
        status.setMessage("目录名称修改成功");

        return status;
    }

    @RequestMapping("/deleteFolder")
    @ResponseBody
    public Status deleteFolder(String p, HttpSession session) {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        p = StringUtils.removeEnd(p, "/");
        p = StringUtils.removeEnd(p, "\\");

        if (StringUtils.equals(p, CMS_TAG) && StringUtils.equals(p, FLT_TAG)) {
            status.setSuccess(false);
            status.setMessage("系统目录不允许删除");
            return status;
        }

        String realPath = getRealPath(p, session);

        File folder = new File(realPath);
        if (!directoryExists(folder)) {
            status.setSuccess(false);
            status.setMessage("当前目录不存在");
            return status;
        }

        String[] children = folder.list();
        if (children != null && children.length > 0) {
            status.setSuccess(false);
            status.setMessage("非空目录不允许删除");
            return status;
        }

        folder.delete();

        status.setMessage("目录删除成功");
        return status;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public Status uploadFile(String p, @RequestParam("file") CommonsMultipartFile file,
                           HttpServletResponse response, HttpSession session) throws IOException {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        String realPath = getRealPath(p, session);

        File folder = new File(realPath);
        if (!directoryExists(folder)) {
            status.setSuccess(false);
            status.setMessage("当前目录不存在");
            return status;
        }

        FileItem fileItem = file.getFileItem();
        String resourceName = fileItem.getName();

        // 模板只允许上传.flt文件
        if(StringUtils.contains(realPath, Configuration.TEMPLATE_PATH)
                && !StringUtils.endsWithIgnoreCase(resourceName, Configuration.TEMPLATE_SUFFIX)) {
            status.setSuccess(false);
            status.setMessage("模板文件只允许上传" + Configuration.TEMPLATE_SUFFIX + "文件");
            return status;
        }

        File resource = new File(realPath, resourceName);
        if(resource.exists()) {
            status.setSuccess(false);
            status.setMessage("文件名已经存在");
            return status;
        }

        try {
            resource.createNewFile();
            OutputStream os = new FileOutputStream(resource);
            os.write(file.getBytes());
            os.flush();
            os.close();

            status.setMessage("文件上传成功！");

            if(needLog(resourceName)) {
                ResourceLog resourceLog = new ResourceLog();
                User sessionData = (User) session.getAttribute(Configuration.SESSION_USER);
                resourceLog.setModifier(sessionData.getUserId());
                resourceLog.setName(resourceName);
                resourceLog.setPath(p);
                resourceLog.setOperateType(OperateEnum.SAVE.getCode());
                resourceLog.setContent(new String(file.getBytes(), "UTF-8"));
                resourceLog.setModifyTime(new Date());
                resourceLogService.save(resourceLog);
            }

        } catch (IOException e) {
            status.setSuccess(false);
            status.setMessage("文件上传失败：" + e.getMessage());
            log.error("uploadFile[File] - " + e.getMessage(), e);
        }

        return status;
    }

    @RequestMapping("/deleteFile")
    @ResponseBody
    public Status deleteFile(String p, String name, HttpSession session) {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        if(StringUtils.equals(p, FLT_TAG)
                && ArrayUtils.contains(Configuration.TEMPLATE_SYSTEM, name)) {
            status.setSuccess(false);
            status.setMessage("系统级模板不允许删除");
            return status;
        }

        String realPath = getRealPath(p, session);

        File file = new File(realPath, name);
        if(!fileExists(file)) {
            status.setSuccess(false);
            status.setMessage("文件不存在");
            return status;
        }

        file.delete();

        if(needLog(name)) {
            ResourceLog resourceLog = new ResourceLog();
            User sessionData = (User) session.getAttribute(Configuration.SESSION_USER);
            resourceLog.setModifier(sessionData.getUserId());
            resourceLog.setName(name);
            resourceLog.setPath(p);
            resourceLog.setOperateType(OperateEnum.DELETE.getCode());
            resourceLog.setModifyTime(new Date());
            resourceLogService.save(resourceLog);
        }

        status.setMessage("文件删除成功");

        return status;
    }

    @RequestMapping("/loadFile")
    @ResponseBody
    public JsonResponse<String> loadFile(String p, String name, HttpSession session) throws IOException {
        JsonResponse<String> jsonResponse = new JsonResponse<String>();

        Status status = validatePath(p);

        if(!status.isSuccess()) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage(status.getMessage());
            return jsonResponse;
        }

        if(!needLog(name)) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("非文本文件不能打开");
            return jsonResponse;
        }

        String realPath = getRealPath(p, session);

        File file = new File(realPath, name);
        if(!fileExists(file)) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("文件不存在");
            return jsonResponse;
        }

        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        try {
            streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufferedReader = new BufferedReader(streamReader);

            StringBuilder builder = new StringBuilder();
            String str;
            while((str = bufferedReader.readLine()) != null) {
                builder.append(str).append("\n");
            }

            jsonResponse.setData(builder.toString());
        }catch (IOException e) {
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("读取文件出错");
            log.error("loadFile[" + file.getAbsolutePath() + "]", e);
        } finally {
            if(streamReader != null) {
                streamReader.close();
            }
            if(bufferedReader != null) {
                bufferedReader.close();
            }
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/editFile", method = RequestMethod.POST)
    @ResponseBody
    public Status editFile(String p, String name, String newName, String content, HttpSession session) throws IOException {
        Status status = validatePath(p);

        if(!status.isSuccess()) {
            return status;
        }

        if(!needLog(name)) {
            status.setSuccess(false);
            status.setMessage("非文本文件不能编辑");
            return status;
        }

        String realPath = getRealPath(p, session);

        File file = new File(realPath, name);
        if(!fileExists(file)) {
            status.setSuccess(false);
            status.setMessage("文件不存在");
            return status;
        }

        // rename
        if(!StringUtils.equals(name, newName)) {

            File newFile = new File(realPath, newName);
            if(fileExists(newFile)) {
                status.setSuccess(false);
                status.setMessage("文件名已经存在");
                return status;
            }

            file.renameTo(newFile);
            file = new File(realPath, newName);
        }

        // write file
        BufferedOutputStream os = null;

        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte[] b = content.getBytes("UTF-8");
            os.write(b);
            os.flush();

            status.setMessage("文件修改成功");

            if(needLog(name)) {
                ResourceLog resourceLog = new ResourceLog();
                User sessionData = (User) session.getAttribute(Configuration.SESSION_USER);
                resourceLog.setModifier(sessionData.getUserId());
                resourceLog.setName(newName);
                resourceLog.setLastName(name);
                resourceLog.setPath(p);
                resourceLog.setOperateType(OperateEnum.MODIFY.getCode());
                resourceLog.setContent(content);
                resourceLog.setModifyTime(new Date());
                resourceLogService.save(resourceLog);
            }

        } catch (IOException e) {
            status.setSuccess(false);
            status.setMessage("更新文件出错");
            log.error("loadFile[" + file.getAbsolutePath() + "]", e);
        } finally {
            if (os != null) {
                os.close();
            }
        }

        return status;
    }

    private Status validatePath(String p) {
        Status status = new Status();

        //校验p格式是否正确
        if (!StringUtils.startsWith(p, CMS_TAG)) {
            status.setSuccess(false);
            status.setMessage("目录路径格式错误");
            return status;
        }

        return status;
    }

    private String getRealPath(String p, HttpSession session) {
        p = StringUtils.removeEnd(p, "/");
        p = StringUtils.removeEnd(p, "\\");

        //特殊处理 {CMS}、{CMS}/{FLT}
        String realPath = PathUtils.getContextPath(session);
        if (StringUtils.startsWith(p, FLT_TAG)) {
            realPath = realPath + Configuration.TEMPLATE_PATH + StringUtils.removeStart(p, FLT_TAG);
        } else {
            realPath = realPath + Configuration.STATIC_RESOURCE_PATH + StringUtils.removeStart(p, CMS_TAG);
        }

        return realPath;
    }

    private boolean directoryExists(String parent, String name) {
        File file = new File(parent, name);
        return directoryExists(file);
    }

    private boolean directoryExists(File file) {
        if(!file.exists() || !file.isDirectory()) {
            return false;
        }

        return true;
    }

    private boolean fileExists(String parent, String name) {
        File file = new File(parent, name);

        return fileExists(file);
    }

    private boolean fileExists(File file) {
        if(!file.exists() || !file.isFile()) {
            return false;
        }

        return true;
    }

    private boolean needLog(String name) {

        if(StringUtils.endsWithIgnoreCase(name, "css") ||
                StringUtils.endsWithIgnoreCase(name, "js") ||
                StringUtils.endsWithIgnoreCase(name, "txt") ||
                StringUtils.endsWithIgnoreCase(name, "html") ||
                StringUtils.endsWithIgnoreCase(name, "flt") ||
                StringUtils.endsWithIgnoreCase(name, "htm")) {
            return true;
        }

        return false;
    }

}
