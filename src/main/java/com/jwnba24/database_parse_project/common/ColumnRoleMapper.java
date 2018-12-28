package com.jwnba24.database_parse_project.common;

import java.util.HashMap;

/**
 * Created by jiwen on 2018/12/26.
 * 将属性列映射到角色中
 */
public class ColumnRoleMapper {
    static HashMap<String,String> map = new HashMap<>();
    static{
        map.put("col1","r1");
        map.put("col2","r2");
        map.put("col3","r1");
        map.put("col4","r2");
        map.put("col5","r2");
        map.put("col6","r2");
    }
    public static String getRole(String column){
        return map.get(column);
    }
}
