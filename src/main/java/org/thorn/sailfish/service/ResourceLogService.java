package org.thorn.sailfish.service;

import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.entity.ResourceLog;
import org.thorn.sailfish.dao.ResourceLogDao;
import org.thorn.sailfish.core.Configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @Author: yfchenyun
 * @Since: 2013-10-29 15:33:00
 * @Version: 1.0
 */
@Service
public class ResourceLogService {

    @Autowired
    private ResourceLogDao resourceLogDao;

    public void save(ResourceLog resourceLog) {
        resourceLogDao.save(resourceLog);
    }

    public Page<ResourceLog> queryPage(long start, long limit, String sort, String dir) {
        Map<String, Object> filter = new HashMap<String, Object>();

		filter.put(Configuration.PAGE_LIMIT, limit);
		filter.put(Configuration.PAGE_START, start);
		filter.put(Configuration.SORT_NAME, sort);
		filter.put(Configuration.ORDER_NAME, dir);

		Page<ResourceLog> page = new Page<ResourceLog>();

		page.setTotal(resourceLogDao.count(filter));
		if(page.getTotal() > 0) {
			page.setResultSet(resourceLogDao.query(filter));
		}

		return page;
    }

}