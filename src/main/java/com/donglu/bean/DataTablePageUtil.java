package com.donglu.bean;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

@Data
public class DataTablePageUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataTablePageUtil.class);
    public static final String I_DISPLAY_START = "iDisplayStart";
    public static final String I_DISPLAY_LENGTH = "iDisplayLength";
    public static final String I_SORTING_COLS = "iSortingCols";
    public static final String I_SORT_COL = "iSortCol_";
    public static final String S_SORT_DIR = "sSortDir_";
    public static final String M_DATA_PROP = "mDataProp_";
    private int draw; // 第几次请求
    private int start = 0;// 起止位置'0'
    private int length = 10; // 数据长度'10'

    private Map<String,String> sortMap = new HashMap<>();
    private Map<String,String> conditionMap = new HashMap<>();
    public DataTablePageUtil(){}

    public DataTablePageUtil(String tableParam,String searchParam){
        LOGGER.debug("DataTablePageUtil tableParam:{}",tableParam);
        LOGGER.debug("DataTablePageUtil searchParam:{}",searchParam);
        try {
            if (!Strings.isNullOrEmpty(tableParam)) {
                JSONArray tableJsonArray = new JSONArray(tableParam);
                parseTableJsonArray(tableJsonArray);
            }
            if (!Strings.isNullOrEmpty(searchParam)){
                JSONArray searchJsonArray = new JSONArray(searchParam);
                parseSearchJsonArray(searchJsonArray);
            }
        } catch (JSONException e) {
            LOGGER.error("解析参数失败",e);
        }
    }

    private void parseTableJsonArray(JSONArray tableJsonArray) {
        Map<String,Object> tableParamMap = new HashMap<>();
        for (int i = 0; i < tableJsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) tableJsonArray.get(i);
            String key = jsonObject.getString("name");
            Object value = jsonObject.get("value");
            tableParamMap.put(key,value);
        }

        this.start = Optional.ofNullable(tableParamMap.get(I_DISPLAY_START)).map(m->(Integer)m).orElse(0);
        this.length = Optional.ofNullable(tableParamMap.get(I_DISPLAY_LENGTH)).map(m->(Integer)m).orElse(0);

        Integer iSortingCols = Optional.ofNullable(tableParamMap.get(I_SORTING_COLS)).map(m -> (Integer) m).orElse(0);
        for (int i = 0; i < iSortingCols; i++) {
            Integer sortCol = Optional.of(tableParamMap.get(I_SORT_COL + i)).map(m -> (Integer) m).get();
            String sortDir = Optional.of(tableParamMap.get(S_SORT_DIR + i)).map(m -> (String) m).get();
            String sortName = Optional.of(tableParamMap.get(M_DATA_PROP + sortCol)).map(m->(String)m).get();
            sortMap.put(sortName,sortDir);
        }
    }

    private void parseSearchJsonArray(JSONArray tableJsonArray) {
        for (int i = 0; i < tableJsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) tableJsonArray.get(i);
            String key = jsonObject.getString("name");
            String value = jsonObject.getString("value");
            conditionMap.put(key,value);
        }
    }

    public String getsortStr() {
        Set<Map.Entry<String, String>> entries = sortMap.entrySet();
        if (entries.isEmpty()) {
            return "id asc";
        }
        return entries.stream().map(m->m.getKey() + " " + m.getValue()).reduce((r1,r2)->r1 + "," +r2).orElseGet(() -> "");
    }
}

