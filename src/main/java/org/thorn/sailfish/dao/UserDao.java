package org.thorn.sailfish.dao;

import org.thorn.sailfish.entity.User;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: yfchenyun
 * @Since: 2014-3-13 16:03:33
 * @Version: 1.0
 */
@Service
public class UserDao {

    private static final String NAMESPACE = "UserMapper.";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public int save(User user) {
        return sqlSessionTemplate.insert(NAMESPACE + "insert", user);
    }

    public int modify(User user) {
        return sqlSessionTemplate.insert(NAMESPACE + "update", user);
    }

    public int delete(List<String> ids) {
        return sqlSessionTemplate.delete(NAMESPACE + "delete", ids);
    }

    public long count(Map<String, Object> filter) {
        return (Long) sqlSessionTemplate.selectOne(NAMESPACE + "count", filter);
    }

    public List<User> query(Map<String, Object> filter) {
        return sqlSessionTemplate.selectList(NAMESPACE + "select", filter);
    }

    public User queryOne(Map<String, Object> filter) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "select", filter);
    }

}