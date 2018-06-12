package org.thorn.sailfish.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @Author: yfchenyun
 * @Since: 13-11-20 上午9:55
 * @Version: 1.0
 */
public enum YesOrNoEnum {

    YES(0, "是"), NO(1, "否");

    private int code;

    private String name;

    private YesOrNoEnum(int code, String name) {
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
        if(code == YES.getCode()) {
            return YES.getName();
        } else if(code == NO.getCode()) {
            return NO.getName();
        } else {
            return null;
        }
    }

    public static Integer getCodeByName(String name) {
        if(StringUtils.equals(name, YES.getName())) {
            return YES.getCode();
        } else if(StringUtils.equals(name, NO.getName())) {
            return NO.getCode();
        } else {
            return null;
        }
    }

}
