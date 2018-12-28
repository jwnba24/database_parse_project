package com.jwnba24.database_parse_project.util;

/**
 * Created by jiwen on 2018/12/28.
 */
public class TableColumnUtil {
    public static String encodeTableName(String tableName){
        return tableName+"_encode";
    }
    public static String decodeTableName(String tableName){
        return tableName.replace("_encode","");
    }

    public static String encodeColumnName(String columnName){
        return columnName+"_column_opinion1";
    }
    public static String decodeColumnName(String columnName){
        return columnName.replace("_column","");
    }

    public static String encodeIV(String s) {
        return s+"_IV";
    }

}
