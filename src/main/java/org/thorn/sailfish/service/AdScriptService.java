package org.thorn.sailfish.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thorn.sailfish.core.Configuration;
import org.thorn.sailfish.core.Page;
import org.thorn.sailfish.dao.AdScriptDao;
import org.thorn.sailfish.entity.AdScript;
import org.thorn.sailfish.enums.YesOrNoEnum;

import java.util.*;

/**
 * @Author: chen.chris
 * @Since: 2013-12-17 13:59:49
 * @Version: 1.0
 */
@Service
public class AdScriptService {

    @Autowired
    private AdScriptDao adScriptDao;

    public void save(AdScript adScript) {
        adScript.setModifyTime(new Date());
        adScript.setCreateTime(new Date());
        adScriptDao.save(adScript);
    }

    public void modify(AdScript adScript) {
        adScript.setModifyTime(new Date());
        adScriptDao.modify(adScript);
    }

    public void delete(String ids) {
        String[] array = StringUtils.split(ids, ',');
        if(array == null || array.length == 0) {
            return ;
        }

        List<String> list = Arrays.asList(array);
        adScriptDao.delete(list);
    }

    public AdScript queryById(String code) {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("code", code);

        List<AdScript> list = adScriptDao.query(filter);

        if(list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);
    }

    public void queryPage(Page<AdScript> page, String code, Integer status) {
        Map<String, Object> filter = new HashMap<String, Object>();

        filter.put(Configuration.PAGE_LIMIT, page.getPageSize());
        filter.put(Configuration.PAGE_START, page.getStart());
        filter.put(Configuration.SORT_NAME, "modifyTime");
        filter.put(Configuration.ORDER_NAME, Configuration.ORDER_DESC);

        filter.put("code", code);
        filter.put("status", status);

        page.setTotal(adScriptDao.count(filter));
        if(page.getTotal() > 0) {
            page.setResultSet(adScriptDao.query(filter));
        }
    }

}