package org.thorn.sailfish.core;

/**
 * @Author: yfchenyun
 * @Since: 13-10-14 下午6:18
 * @Version: 1.0
 */
public class Status {

    private boolean success = true;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
