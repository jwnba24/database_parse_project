package com.jwnba24.database_parse_project.util;

import com.jwnba24.database_parse_project.DatabaseParseProjectApplication;

/**
 * Created by jiwen on 2018/12/26.
 */
public class PathUtil {

    public static String getABEPath(){
        return DatabaseParseProjectApplication.class.getClassLoader().getResource("").getPath()+"cpabe/";
    }
}
