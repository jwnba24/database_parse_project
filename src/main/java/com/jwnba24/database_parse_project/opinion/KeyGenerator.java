package com.jwnba24.database_parse_project.opinion;

/**
 * Created by jiwen on 2018/12/28.
 */
public class KeyGenerator {
    public static String generateKey(String column){
        switch (column){
            case "col1":
                return "col1";
            case "col2":
                return "col2";
            case "col3":
                return "col3";
            case "col4":
                return "col4";
            case "col5":
                return "col6";
            case "col6":
                return "col6";
            case "column":
                return "columnName";
            case "table":
                return "tableName";
            default: return "";
        }

    }
}
