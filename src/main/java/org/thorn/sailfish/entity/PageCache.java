package org.thorn.sailfish.entity;

import java.util.Date;

/**
 * @Author: yfchenyun
 * @Since: 2013-12-5 14:21:44
 * @Version: 1.0
 */
public class PageCache {

    /**
     * 
     */
    private Integer id;

    /**
     * 静态页面地址
     */
    private String htmlPath;

    /**
     * 更新时间
     */
    private Date modifyTime;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
         return this.id;
    }

    public void setHtmlPath(String htmlPath) {
        this.htmlPath = htmlPath;
    }

    public String getHtmlPath() {
         return this.htmlPath;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() {
         return this.modifyTime;
    }

}