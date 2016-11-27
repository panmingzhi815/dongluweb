package com.donglu.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * spring mvc 返回对象，建议所有的请求返回都使用该对象
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Data
public class Response implements Serializable {
    public static final String OK = "ok";
    public static final String ERROR = "error";

    protected Meta meta;
    protected Object data;

    public Response success(){
        this.setMeta(new Meta(true,OK));
        return this;
    }

    public Response success(Object data) {
        this.meta = new Meta(true, OK);
        this.data = data;
        return this;
    }

    public Response failure() {
        this.meta = new Meta(false, ERROR);
        return this;
    }

    public Response failure(String message) {
        this.meta = new Meta(false, message);
        return this;
    }

    @Data
    public class Meta {
        private boolean success;
        private String message;

        public Meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
