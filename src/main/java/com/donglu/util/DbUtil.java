package com.donglu.util;

import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DbUtil {
    public Statement st ;

    public DbUtil(){

    }
    /**
     * paraMap参数
     * driverClassName 驱动
     * JdbcURL  jdbc    该字符串为：  config.ini 的JdbcURL+IP地址(含端口号)+dbStr+数据库名称
     * userName 数据库名称
     * userpwd 数据库密码
     * */
    public  Statement getStatement(Map<String,String> paraMap) {
        //加载数据库驱动
        try {
            Class.forName(paraMap.get("driver"));
            Connection conn = DriverManager.getConnection(paraMap.get("jdbc"), paraMap.get("userName"), paraMap.get("userpwd"));
            if(!conn.isClosed()){
            }else{
                return null;
            }
            // statement用来执行SQL语句
            Statement statement = conn.createStatement();
            return statement;
            // 要执行的SQL语句
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return null;
        }
    }
    //读取数据库表信息
    public List<String> getTableNames(Map<String,String> dbInfoMap,String dbName){
        List<String> tableList = new ArrayList<String>(); //存储表名
        try {
            st = this.getStatement(dbInfoMap);
            if(st == null){
                return null;
            }
            //替换数据库名字占位符
            String selTableSql = dbInfoMap.get("showTable").toString().replace("%", dbName);
            ResultSet tabRs = st.executeQuery(selTableSql);
            //保存表名
            while(tabRs.next()){
                tableList.add(tabRs.getString(1));
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return null;
        }
        return tableList;
    }
    //读取表字段信息
    public List<Map<String,String>> getColumnNames(Map<String,String> dbInfoMap,String tabName){
        List<Map<String,String>> colList = new ArrayList<Map<String,String>>(); //存储字段信息
        try {
            st = this.getStatement(dbInfoMap);
            if(st == null){
                return null;
            }
            //替换表名占位符
            String selColumnSql = dbInfoMap.get("showColumns").toString().replace("%", tabName);
            ResultSet columnRs = st.executeQuery(selColumnSql);
            while(columnRs.next()){
                Map<String,String> colMap =  new HashMap<String,String>();
                colMap.put("filed", columnRs.getString(1));
                colMap.put("type", columnRs.getString(2));
                colList.add(colMap);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return null;
        }
        return colList;
    }

    // 读取配置
    public Map<String,  HashMap<String, String>> getDbConfigMap() {
        HashMap<String, HashMap<String,String>> objectObjectHashMap = Maps.newHashMap();
        HashMap<String, String> sqlserver = Maps.newHashMap();
        sqlserver.put("driverClassName","net.sourceforge.jtds.jdbc.Driver");
        sqlserver.put("JdbcURL","jdbc:jtds:sqlserver://localhost:1433/");
        sqlserver.put("showTable","select Name from sysobjects where xtype='u' and status>=0");
        sqlserver.put("showColumns","select column_name,data_type from information_schema.columns where table_name = '%'");
        objectObjectHashMap.put("SQL_Server", sqlserver);

        return objectObjectHashMap;
    }
}
