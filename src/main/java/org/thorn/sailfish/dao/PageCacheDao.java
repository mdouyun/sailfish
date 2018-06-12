package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.PageCache;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: yfchenyun
 * @Since: 2013-12-5 14:21:44
 * @Version: 1.0
 */
@Service
public class PageCacheDao {

    private static final String NAMESPACE = "PageCacheMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(PageCache pageCache) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", pageCache);
    }

    public int modify(PageCache pageCache) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", pageCache);
    }

    public PageCache query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "select", filter);
    }

}