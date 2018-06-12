package org.thorn.sailfish.controller;

import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.entity.Article;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.enums.ArticleStatusEnum;
import org.thorn.sailfish.service.ArticleService;
import org.thorn.sailfish.service.CategoryService;
import org.thorn.sailfish.service.PageCacheService;
import org.thorn.sailfish.utils.PathUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @Author: yfchenyun
 * @Since: 13-12-3 下午9:14
 * @Version: 1.0
 */
@Controller
@RequestMapping("/web")
public class RenderController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private PageCacheService cacheService;

    @Autowired
    private FreeMarkerConfigurer config;

    static Logger log = LoggerFactory.getLogger(RenderController.class);

    private void initCommonMap(HttpServletRequest request, ModelMap modelMap) {

        String ctxPath = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ctxPath + "/";

        modelMap.put("path", ctxPath);
        modelMap.put("basePath", basePath);
        modelMap.put("res", request.getContextPath() + Configuration.STATIC_RESOURCE_URL);
        modelMap.put("ctUrl", request.getContextPath() + "/web");
        modelMap.put("atUrl", request.getContextPath() + "/web/content");
        modelMap.put("tpUrl", request.getContextPath() + "/web/page?t=");

        Enumeration<String> e = request.getParameterNames();

        while(e.hasMoreElements()) {
            String name = e.nextElement();
            String[] values = request.getParameterValues(name);

            if(values == null || values.length == 0) {
                continue;
            }

            if(values.length == 1) {
                modelMap.put(name, values[0]);
            } else {
                modelMap.put(name, values);
            }
        }
    }

    @RequestMapping("/{categoryPath}/index")
    public String categoryIndex(@PathVariable("categoryPath") String categoryPath,
                                HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String path = null;

        try {
            Category currentCategory = categoryService.queryByPath(categoryPath);

            if (currentCategory == null) {
                // 路径错误
                return "redirect:/web/page?t=404";
            }

            modelMap.put("category", currentCategory);
            initCommonMap(request, modelMap);

            path = currentCategory.getIndexTemplate();
        } catch (Exception e) {
            log.error("categoryIndex Exception[" + categoryPath + "]", e);
            request.setAttribute("exception", e);
            path = "forward:/web/page?t=500";
        }

        return Configuration.TEMPLATE_VIEW_DIR + StringUtils.removeEnd(path, Configuration.TEMPLATE_SUFFIX);
    }

    @RequestMapping("/{category}")
    public String category(@PathVariable("category") String category) {
        return "redirect:/web/" + category + "/index";
    }

    @RequestMapping("/content/{id}")
    public void article(@PathVariable("id") int id, ModelMap modelMap, HttpSession session,
                        HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Article article = articleService.queryArticle(id);

            if (article == null || article.getStatus() - ArticleStatusEnum.DELETE.getCode() == 0) {
                // 路径错误
                response.sendRedirect("/web/page?t=404");
                return ;
            }

            String path = cacheService.getCachePage(id);

            if(StringUtils.isNotEmpty(path)) {
                File html = new File(PathUtils.getContextPath(session) + path);

                if(html.exists()) {
                    response.sendRedirect(path);
                    return ;
                }
                cacheService.clearCache(id);
            }

            Category category = categoryService.queryById(article.getCategory());

            modelMap.put("article", article);
            modelMap.put("category", category);
            initCommonMap(request, modelMap);

            String templatePath = Configuration.TEMPLATE_VIEW_DIR + category.getArticleTemplate();

            // create html file
            StringBuilder htmlName = new StringBuilder(id);
            htmlName.append(System.currentTimeMillis()).append(Thread.currentThread().getId());
            htmlName.append(RandomUtils.nextInt(1000)).append(".html");

            File file = new File(PathUtils.getContextPath(session) +
                    Configuration.STATIC_CACHE_PATH, htmlName.toString());
            if(!file.exists()) {
                file.createNewFile();
            }

            Template template = config.getConfiguration().getTemplate(templatePath);
            template.process(modelMap, new FileWriter(file));
            String cacheUrl = Configuration.STATIC_CACHE_URL + "/" +htmlName.toString();
            cacheService.setCache(id, cacheUrl);

            response.sendRedirect(cacheUrl);
        } catch (Exception e) {
            log.error("article Exception[" + id + "]", e);
            request.setAttribute("exception", e);
            request.getRequestDispatcher("/web/page?t=500").forward(request, response);
        }
    }

    @RequestMapping("/page")
    public String alone(String t, HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        initCommonMap(request, modelMap);

        return Configuration.TEMPLATE_VIEW_DIR + t;
    }

}
