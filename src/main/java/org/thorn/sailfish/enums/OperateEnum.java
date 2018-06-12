package org.thorn.sailfish.enums;

/**
 * @Author: yfchenyun
 * @Since: 13-10-29 下午4:59
 * @Version: 1.0
 */
public enum OperateEnum {

    SAVE("新增", 0), MODIFY("修改", 1), DELETE("删除", 2);

    private String name;

    private int code;

    private OperateEnum(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
