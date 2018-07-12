package org.thorn.sailfish.service;

import org.thorn.sailfish.entity.User;
import org.thorn.sailfish.dao.UserDao;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: chen.chris
 * @Since: 2014-3-13 16:03:33
 * @Version: 1.0
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void save(User user) {
        userDao.save(user);
    }

    public void modify(User user) {
        userDao.modify(user);
    }

    public User findByPassword(String userId, String password) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("userId", userId);
        filter.put("password", password);

        return userDao.queryOne(filter);
    }

    public User findById(String userId) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("userId", userId);

        return userDao.queryOne(filter);
    }

    public void delete(String ids) {
        String[] array = StringUtils.split(ids, ',');
        if(array == null || array.length == 0) {
            return ;
        }

        List<String> list = Arrays.asList(array);
        userDao.delete(list);
    }

    public void queryPage(Page<User> page, String userId, String userName) {
        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put(Configuration.PAGE_LIMIT, page.getPageSize());
        filter.put(Configuration.PAGE_START, page.getStart());
        filter.put(Configuration.SORT_NAME, "modifyTime");
        filter.put(Configuration.ORDER_NAME, Configuration.ORDER_DESC);

        filter.put("userId", userId);
        filter.put("userName", userName);

		page.setTotal(userDao.count(filter));
		if(page.getTotal() > 0) {
			page.setResultSet(userDao.query(filter));
		}
    }

}