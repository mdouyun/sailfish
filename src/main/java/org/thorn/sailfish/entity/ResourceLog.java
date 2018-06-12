package org.thorn.sailfish.entity;

import java.util.Date;

/**
 * @Author: yfchenyun
 * @Since: 2013-10-29 15:33:00
 * @Version: 1.0
 */
public class ResourceLog {

    /**
     *
     */
    private Integer id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 文本内容
     */
    private String content;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 0：新增；1：修改；2：删除
     */
    private Integer operateType;

    /**
     * 原名称（修改名称使用）
     */
    private String lastName;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getModifier() {
        return this.modifier;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getModifyTime() {
        return this.modifyTime;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getOperateType() {
        return this.operateType;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }

}