package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.Article;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: yfchenyun
 * @Since: 2013-11-21 10:12:23
 * @Version: 1.0
 */
@Service
public class ArticleDao {

    private static final String NAMESPACE = "ArticleMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(Article article) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", article);
    }

    public int modify(Article article) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", article);
    }

    public int modifyStatus(Map<String, Object> filter) {
        return sqlSessionTemplate.insert(NAMESPACE + "updateStatus", filter);
    }

    public int delete(List<String> ids) {
        return sqlSessionTemplate.delete(NAMESPACE + "delete", ids);
    }

    public long count(Map<String, Object> filter) {
        return (Long) sqlSessionTemplate.selectOne(NAMESPACE + "count", filter);
    }

    public List<Article> query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "select", filter);
    }

    public List<Article> queryArticleContent(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectContent", filter);
    }

}