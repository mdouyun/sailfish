package org.thorn.sailfish.service;

import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.entity.Category;
import org.thorn.sailfish.dao.CategoryDao;
import org.thorn.sailfish.core.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.thorn.sailfish.enums.YesOrNoEnum;

/**
 * @Author: chen.chris
 * @Since: 2013-10-30 13:58:11
 * @Version: 1.0
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public void save(Category category) {
        categoryDao.save(category);
    }

    public void modify(Category category) {
        categoryDao.modify(category);
    }

    public void delete(String enName, String root) {
        categoryDao.delete(enName);

       List<String> list = categoryDao.queryNoParent(root);
       Map<String, Object> filter = new HashMap<String, Object>();
       filter.put("root", root);
       while (list != null && list.size() > 0) {
           filter.put("list", list);
           if(categoryDao.deleteByIds(filter) <= 0) {
               break;
           }

           list = categoryDao.queryNoParent(root);
       }
    }

    public Category queryById(String enName) {
        List<Category> list = queryList(enName, null, null, null);

        if(list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    public Category queryByPath(String path) {
        List<Category> list = queryList(null, null, null, path);

        if(list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    public List<Category> queryByParent(String parent) {
        return queryList(null, parent, null, null);
    }

    public List<Category> queryAllShowByParent(String parent) {
        return queryList(null, parent, YesOrNoEnum.NO.getCode(), null);
    }

    public List<Category> queryAll() {
        return queryList(null, null, null, null);
    }

    private List<Category> queryList(String enName, String parent, Integer hidden, String path) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("parent", parent);
        filter.put("enName", enName);
        filter.put("hidden", hidden);
        filter.put("path", path);

        return categoryDao.query(filter);
    }


    public Page<Category> queryPage(long start, long limit, String sort, String dir) {
        Map<String, Object> filter = new HashMap<String, Object>();

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		filter.put(Configuration.SORT_NAME, sort);
		filter.put(Configuration.ORDER_NAME, dir);

		Page<Category> page = new Page<Category>();

		page.setTotal(categoryDao.count(filter));
		if(page.getTotal() > 0) {
			page.setResultSet(categoryDao.query(filter));
		}

		return page;
    }

}