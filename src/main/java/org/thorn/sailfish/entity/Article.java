package org.thorn.sailfish.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.thorn.sailfish.enums.ArticleStatusEnum;

import java.util.Date;

/**
 * @Author: yfchenyun
 * @Since: 2013-11-21 10:12:23
 * @Version: 1.0
 */
public class Article {

    /**
     * 
     */
    private Integer id;

    /**
     * 栏目代码
     */
    private String category;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 0:未发表 1:已发表 2:已删除
     */
    private Integer status;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 星级
     */
    private Integer starLevel;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
         return this.id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
         return this.category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
         return this.title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
         return this.content;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
         return this.createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() {
         return this.modifyTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
         return this.status;
    }

    public String getArticleStatus() {
        return ArticleStatusEnum.getNameByCode(this.status);
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCreater() {
         return this.creater;
    }

    public void setStarLevel(Integer starLevel) {
        this.starLevel = starLevel;
    }

    public Integer getStarLevel() {
         return this.starLevel;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}