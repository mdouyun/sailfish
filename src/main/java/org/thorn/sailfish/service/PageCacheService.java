package org.thorn.sailfish.service;

import org.thorn.sailfish.entity.PageCache;
import org.thorn.sailfish.dao.PageCacheDao;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Page;

import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: chen.chris
 * @Since: 2013-12-5 14:21:44
 * @Version: 1.0
 */
@Service
public class PageCacheService {

    @Autowired
    private PageCacheDao pageCacheDao;

    public void saveCache(int id, String path) {
        PageCache pageCache = new PageCache();
        pageCache.setId(id);
        pageCache.setHtmlPath(path);
        pageCache.setModifyTime(new Date());

        pageCacheDao.save(pageCache);
    }

    public void clearCache(int id) {
        PageCache pageCache = new PageCache();
        pageCache.setId(id);
        pageCache.setHtmlPath(null);
        pageCache.setModifyTime(new Date());

        pageCacheDao.modify(pageCache);
    }

    public void setCache(int id, String path) {
        PageCache pageCache = new PageCache();
        pageCache.setId(id);
        pageCache.setHtmlPath(path);
        pageCache.setModifyTime(new Date());

        pageCacheDao.modify(pageCache);
    }

    public String getCachePage(int id) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("id", id);

        PageCache pageCache = pageCacheDao.query(filter);

        if(pageCache == null) {
            return null;
        }

        return pageCache.getHtmlPath();
    }

}