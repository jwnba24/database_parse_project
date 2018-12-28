package com.jwnba24.database_parse_project.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiwen on 2018/12/27.
 */
public class TableColumnMapper {
    public static HashMap<String, List<String>> map = new HashMap<>();
    public TableColumnMapper(){
        List<String> columns = new ArrayList<>();
        columns.add("col1");
        columns.add("col2");
        columns.add("id");
        map.put("table1",columns);
    }

    public List<String> getColumnList(String tableName){
        List<String> result = map.get(tableName);
        return result;
    }
}
