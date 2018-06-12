package org.thorn.sailfish.kindeditor;

/**
 * @Author: yfchenyun
 * @Since: 13-11-22 下午7:17
 * @Version: 1.0
 */
public class KindUploadResult {

    /**
     * 0 成功 1失败
     */
    private Integer error = 0;

    private String url;

    private String message;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String message) {
        this.error = 1;
        this.message = message;
    }

    public void setSuccess(String url) {
        this.error = 0;
        this.url = url;
    }
}
