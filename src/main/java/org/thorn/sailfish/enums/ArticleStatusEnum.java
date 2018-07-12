package org.thorn.sailfish.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @Author: chen.chris
 * @Since: 13-11-20 上午9:55
 * @Version: 1.0
 */
public enum ArticleStatusEnum {

    UN_PUBLISH(0, "未发布"), PUBLISH(1, "已发布"), DELETE(2, "删除");

    private int code;

    private String name;

    private ArticleStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(int code) {
        if(code == UN_PUBLISH.getCode()) {
            return UN_PUBLISH.getName();
        } else if(code == PUBLISH.getCode()) {
            return PUBLISH.getName();
        } else if(code == DELETE.getCode()) {
            return DELETE.getName();
        } else {
            return null;
        }
    }

    public static Integer getCodeByName(String name) {
        if(StringUtils.equals(name, UN_PUBLISH.getName())) {
            return UN_PUBLISH.getCode();
        } else if(StringUtils.equals(name, PUBLISH.getName())) {
            return PUBLISH.getCode();
        } else if(StringUtils.equals(name, DELETE.getName())) {
            return DELETE.getCode();
        } else {
            return null;
        }
    }

    public static ArticleStatusEnum getEnumByCode(int code) {
        if(code == UN_PUBLISH.getCode()) {
            return UN_PUBLISH;
        } else if(code == PUBLISH.getCode()) {
            return PUBLISH;
        } else if(code == DELETE.getCode()) {
            return DELETE;
        } else {
            return null;
        }
    }
}
