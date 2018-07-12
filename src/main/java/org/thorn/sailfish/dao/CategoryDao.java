package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.Category;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: chen.chris
 * @Since: 2013-10-30 13:58:11
 * @Version: 1.0
 */
@Service
public class CategoryDao {

    private static final String NAMESPACE = "CategoryMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(Category category) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", category);
    }

    public int modify(Category category) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", category);
    }

    public int delete(String id) {
        return sqlSessionTemplate.delete(NAMESPACE + "delete", id);
    }

    public int deleteByIds(Map<String, Object> filter) {
        return sqlSessionTemplate.delete(NAMESPACE + "deleteNoParent", filter);
    }

    public List<String> queryNoParent(String root) {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectNoParent", root);
    }

    public long count(Map<String, Object> filter) {
        return (Long) sqlSessionTemplate.selectOne(NAMESPACE + "count", filter);
    }

    public List<Category> query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "select", filter);
    }

}