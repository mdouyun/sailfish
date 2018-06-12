package org.thorn.sailfish.service;

import org.thorn.sailfish.entity.Article;
import org.thorn.sailfish.dao.ArticleDao;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Page;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.thorn.sailfish.enums.ArticleStatusEnum;

/**
 * @Author: yfchenyun
 * @Since: 2013-11-21 10:12:23
 * @Version: 1.0
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    public void save(Article article) {
        articleDao.save(article);
    }

    public void modify(Article article) {
        articleDao.modify(article);
    }

    public void delete(String ids) {
        String[] array = StringUtils.split(ids, ',');
        if(array == null || array.length == 0) {
            return ;
        }

        Map<String, Object> filter = new HashMap<String, Object>();
        List<String> list = Arrays.asList(array);
        filter.put("list", list);
        filter.put("status", ArticleStatusEnum.DELETE.getCode());

        articleDao.modifyStatus(filter);
    }

    public Article queryArticle(Integer id) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("id", id);

        List<Article> list = articleDao.queryArticleContent(filter);
        if(list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public void queryPage(Page<Article> page, String title, Integer status,
                                   String category, Date startTime, Date endTime) {
        Map<String, Object> filter = new HashMap<String, Object>();

		filter.put(Configuration.PAGE_LIMIT, page.getPageSize());
		filter.put(Configuration.PAGE_START, page.getStart());
		filter.put(Configuration.SORT_NAME, "modifyTime");
		filter.put(Configuration.ORDER_NAME, Configuration.ORDER_DESC);

        filter.put("title", title);
        filter.put("status", status);
        filter.put("category", category);
        filter.put("startTime", startTime);
        filter.put("endTime", endTime);

		page.setTotal(articleDao.count(filter));
		if(page.getTotal() > 0) {
			page.setResultSet(articleDao.query(filter));
		}
    }

}