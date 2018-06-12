package org.thorn.sailfish.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.core.Status;
import org.thorn.sailfish.entity.Article;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.entity.User;
import org.thorn.sailfish.enums.ArticleStatusEnum;
import org.thorn.sailfish.service.ArticleService;
import org.thorn.sailfish.service.CategoryService;
import org.thorn.sailfish.service.PageCacheService;
import org.thorn.sailfish.utils.DateTimeUtils;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @Author: yfchenyun
 * @Since: 13-11-21 上午10:34
 * @Version: 1.0
 */
@Controller
@RequestMapping("/am/article")
public class ArticleController {

    static Logger log = LoggerFactory.getLogger(ArticleController.class);

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PageCacheService cacheService;

    @RequestMapping("/index")
    public String index(Long pageIndex, Long pageSize, String title, Integer status, String category,
                        String startTime, String endTime, ModelMap modelMap, HttpSession session) {
        Page<Article> page = new Page<Article>(pageIndex, pageSize);

        Date start = null;
        Date end = null;
        try {
            start = DateTimeUtils.formatDate(startTime);
            end = DateTimeUtils.formatDate(endTime);
        } catch (Exception e) {
            log.warn("Format date exception[startTime:?,endTime:?]", startTime, endTime);
        }

        try {
            articleService.queryPage(page, title, status, category, start, end);
            modelMap.put("page", page);

            List<Category> categories = categoryService.queryAll();
            modelMap.put("categories", categories);

        } catch (Exception e) {
            log.error("Query article page", e);
        }

        return "article";
    }

    @RequestMapping("/index/one")
    public String addArticle(ModelMap modelMap) {
        try {
            List<Category> categories = categoryService.queryAll();
            modelMap.put("categories", categories);

        } catch (Exception e) {
            log.error("addArticle", e);
        }

        return "articleEditor";
    }

    @RequestMapping("/index/{id}")
    public String editArticle(@PathVariable("id") Integer id, ModelMap modelMap) {

        try {
            if(id != null) {
                Article article = articleService.queryArticle(id);
                if(article != null) {
                    id = article.getId();
                    modelMap.put("id", id);
                    modelMap.put("article", article);
                }
            }

            List<Category> categories = categoryService.queryAll();
            modelMap.put("categories", categories);

        } catch (Exception e) {
            log.error("editArticle[" + id + "]", e);
        }

        return "articleEditor";
    }

    @RequestMapping(value = "/saveOrModify", method = RequestMethod.POST)
    @ResponseBody
    public Status saveOrModifyArticle(Article article, HttpSession session, Integer opType) {
        Status status = new Status();

        article.setModifyTime(new Date());

        ArticleStatusEnum statusEnum = ArticleStatusEnum.getEnumByCode(opType);
        if(statusEnum == null) {
            statusEnum = ArticleStatusEnum.UN_PUBLISH;
        }

        try {
            if(article.getId() == null) {
                article.setCreateTime(new Date());
                User sessionData = (User) session.getAttribute(Configuration.SESSION_USER);
                article.setCreater(sessionData.getUserId());
                article.setStatus(statusEnum.getCode());

                articleService.save(article);
                status.setMessage("文章保存成功");

                cacheService.saveCache(article.getId(), "");
            } else {
                if(statusEnum == ArticleStatusEnum.PUBLISH) {
                    article.setStatus(statusEnum.getCode());
                }

                articleService.modify(article);
                status.setMessage("文章修改成功");

                cacheService.clearCache(article.getId());
            }
        } catch (Exception e) {
            log.error("saveOrModifyArticle" + article.toString(), e);
            status.setSuccess(false);
            status.setMessage("文章编辑失败：" + e.getMessage());
        }

        return status;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Status delete(String ids) {
        Status status = new Status();

        try {
            articleService.delete(ids);
            status.setMessage("文章删除成功");
        } catch (Exception e) {
            log.error("delete[" + ids + "]", e);
            status.setSuccess(false);
            status.setMessage("文章删除失败：" + e.getMessage());
        }

        return status;
    }


}
