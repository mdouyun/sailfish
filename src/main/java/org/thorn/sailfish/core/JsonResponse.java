package org.thorn.sailfish.core;

/**
 * @Author: chen.chris
 * @Since: 13-10-24 下午2:21
 * @Version: 1.0
 */
public class JsonResponse<T> extends Status {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
