package org.thorn.sailfish.enums;

/**
 * TODO.
 *
 * @author chen.chris, 2014-03-13.
 * @version 1.0
 * @since 1.0
 */
public enum RoleEnum {

    ADMIN("ADMIN", "系统管理员");

    private String code;

    private String name;

    private RoleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
