package com.donglu.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * spring mvc 返回对象，建议所有的请求返回都使用该对象
 * Created by panmingzhi on 2016/11/27 0027.
 */
@Data
public class ResponseTableList extends Response {

    private Long recordsTotal;
    private Long recordsFiltered;

    public ResponseTableList(Long recordsTotal, Object data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsTotal;
        super.success(data);
    }

}
