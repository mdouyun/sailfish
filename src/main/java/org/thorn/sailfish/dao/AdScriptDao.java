package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.AdScript;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: chen.chris
 * @Since: 2013-12-17 13:59:49
 * @Version: 1.0
 */
@Service
public class AdScriptDao {

    private static final String NAMESPACE = "AdScriptMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(AdScript adScript) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", adScript);
    }

    public int modify(AdScript adScript) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", adScript);
    }

    public int delete(List<String> ids) {
        return sqlSessionTemplate.delete(NAMESPACE + "delete", ids);
    }

    public long count(Map<String, Object> filter) {
        return (Long) sqlSessionTemplate.selectOne(NAMESPACE + "count", filter);
    }

    public List<AdScript> query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "select", filter);
    }

}