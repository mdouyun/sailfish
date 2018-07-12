package org.thorn.sailfish.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.thorn.sailfish.enums.YesOrNoEnum;

import java.util.Date;

/**
 * @Author: chen.chris
 * @Since: 2013-12-17 13:59:49
 * @Version: 1.0
 */
public class AdScript {

    /**
     * 广告编号
     */
    private String code;

    /**
     * 说明
     */
    private String remark;

    /**
     * 广告代码
     */
    private String html;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 0:隐藏 1:显示
     */
    private Integer hidden;


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
         return this.code;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
         return this.remark;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
         return this.html;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() {
         return this.modifyTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
         return this.createTime;
    }

    public void setHidden(Integer hidden) {
        this.hidden = hidden;
    }

    public Integer getHidden() {
         return this.hidden;
    }

    public String getIsHidden() {

        if(hidden != null) {
            return YesOrNoEnum.getNameByCode(hidden);
        }

        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}