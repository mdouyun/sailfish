package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.ResourceLog;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: yfchenyun
 * @Since: 2013-10-29 15:33:00
 * @Version: 1.0
 */
@Service
public class ResourceLogDao {

    private static final String NAMESPACE = "ResourceLogMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(ResourceLog resourceLog) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", resourceLog);
    }

    public int modify(ResourceLog resourceLog) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", resourceLog);
    }

    public int delete(List<String> ids) {
        return sqlSessionTemplate.delete(NAMESPACE + "delete", ids);
    }

    public long count(Map<String, Object> filter) {
        return (Long) sqlSessionTemplate.selectOne(NAMESPACE + "count", filter);
    }

    public List<ResourceLog> query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "select", filter);
    }

}