package com.donglu.bean;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by panmingzhi on 2016/11/29 0029.
 */
public class DataTablePageUtilTest {

    @Test
    public void test(){
        String tableParam = "[{\"name\":\"sEcho\",\"value\":9},{\"name\":\"iColumns\",\"value\":11},{\"name\":\"sColumns\",\"value\":\",,,,,,,,,,\"},{\"name\":\"iDisplayStart\",\"value\":0},{\"name\":\"iDisplayLength\",\"value\":10},{\"name\":\"mDataProp_0\",\"value\":\"id\"},{\"name\":\"bSortable_0\",\"value\":true},{\"name\":\"mDataProp_1\",\"value\":\"deviceName\"},{\"name\":\"bSortable_1\",\"value\":true},{\"name\":\"mDataProp_2\",\"value\":\"cardIdentifier\"},{\"name\":\"bSortable_2\",\"value\":true},{\"name\":\"mDataProp_3\",\"value\":\"cardSerialNumber\"},{\"name\":\"bSortable_3\",\"value\":true},{\"name\":\"mDataProp_4\",\"value\":\"userIdentifier\"},{\"name\":\"bSortable_4\",\"value\":true},{\"name\":\"mDataProp_5\",\"value\":\"userName\"},{\"name\":\"bSortable_5\",\"value\":true},{\"name\":\"mDataProp_6\",\"value\":\"accessControlState\"},{\"name\":\"bSortable_6\",\"value\":true},{\"name\":\"mDataProp_7\",\"value\":\"checkCardEnabled\"},{\"name\":\"bSortable_7\",\"value\":true},{\"name\":\"mDataProp_8\",\"value\":\"validTo\"},{\"name\":\"bSortable_8\",\"value\":true},{\"name\":\"mDataProp_9\",\"value\":\"createTime\"},{\"name\":\"bSortable_9\",\"value\":true},{\"name\":\"mDataProp_10\",\"value\":\"uploadTime\"},{\"name\":\"bSortable_10\",\"value\":true},{\"name\":\"iSortCol_0\",\"value\":0},{\"name\":\"sSortDir_0\",\"value\":\"asc\"},{\"name\":\"iSortingCols\",\"value\":1}]";
        String searchParam = "[{\"name\":\"deviceName\",\"value\":\"\"},{\"name\":\"cardIdentifier\",\"value\":\"\"},{\"name\":\"userName\",\"value\":\"\"},{\"name\":\"accessControlState\",\"value\":\"全部\"}]";

        DataTablePageUtil dataTablePageUtil = new DataTablePageUtil(tableParam, searchParam);

    }
}