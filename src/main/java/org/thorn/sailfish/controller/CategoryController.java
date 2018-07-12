package org.thorn.sailfish.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.JsonResponse;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.service.CategoryService;
import org.thorn.sailfish.service.ResourceService;
import org.thorn.sailfish.utils.PathUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: chen.chris
 * @Since: 13-10-30 下午2:01
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am/category")
public class CategoryController {

    static Logger log = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/index")
    public String index(String parent, HttpSession session, ModelMap modelMap) {

        if(StringUtils.isBlank(parent)) {
            parent = Configuration.CATEGORY_ROOT;
        }

        String grandparent = parent;
        if(!grandparent.equals(Configuration.CATEGORY_ROOT)) {
            Category category = categoryService.queryById(parent);
            if(category != null) {
                grandparent = category.getParent();
            }
        }


        List<Category> list = categoryService.queryByParent(parent);
        modelMap.put("list", list);
        modelMap.put("parent", parent);
        modelMap.put("grandparent", grandparent);

        List<String> templates = resourceService.getAllTemplates(PathUtils.getContextPath(session));
        modelMap.put("templates", templates);

        return "category";
    }

    @RequestMapping("/create")
    @ResponseBody
    public Status createCategory(Category category) {
        Status status = new Status();

        if(StringUtils.isBlank(category.getParent())) {
            category.setParent(Configuration.CATEGORY_ROOT);
        }

        try {
            categoryService.save(category);
            status.setMessage("栏目创建成功");
        } catch (Exception e) {
            log.error("createCategory" + category.toString(), e);
            status.setSuccess(false);
            status.setMessage("栏目创建失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Status editCategory(Category category) {
        Status status = new Status();

        if(StringUtils.isBlank(category.getParent())) {
            category.setParent(Configuration.CATEGORY_ROOT);
        }

        try {
            categoryService.modify(category);
            status.setMessage("栏目修改成功");
        } catch (Exception e) {
            log.error("editCategory" + category.toString(), e);
            status.setSuccess(false);
            status.setMessage("栏目修改失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Status deleteCategory(String enName) {
        Status status = new Status();

        if(StringUtils.isBlank(enName)) {
            status.setMessage("栏目代码为空");
            status.setSuccess(false);
            return status;
        }

        try {
            categoryService.delete(enName, Configuration.CATEGORY_ROOT);
            status.setMessage("栏目删除成功");
        } catch (Exception e) {
            log.error("deleteCategory[" + enName + "]", e);
            status.setSuccess(false);
            status.setMessage("栏目删除失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping("/get")
    @ResponseBody
    public JsonResponse<Category> getCategory(String enName) {
        JsonResponse<Category> jsonResponse = new JsonResponse<Category>();

        try {
            Category category = categoryService.queryById(enName);

            if(category == null) {
                jsonResponse.setSuccess(false);
                jsonResponse.setMessage("栏目未找到");
            } else {
                jsonResponse.setData(category);
                jsonResponse.setMessage("数据加载成功");
            }

        } catch (Exception e) {
            log.error("getCategory[" + enName + "]", e);
            jsonResponse.setSuccess(false);
            jsonResponse.setMessage("数据加载失败：" + e.getMessage());
        }

        return jsonResponse;
    }


}
