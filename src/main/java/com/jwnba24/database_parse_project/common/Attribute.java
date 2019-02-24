package com.jwnba24.database_parse_project.common;

import com.jwnba24.database_parse_project.DatabaseParseProjectApplication;

/**
 * Created by jiwen on 2018/12/26.
 */
public class Attribute {
    static String dir = DatabaseParseProjectApplication.class.getClassLoader().getResource("").getPath()+"/cpabe/";
    public static final String R1_ATTR = "r1:r1";
    public static final String R2_ATTR = "r2:r2";
    public static final String USER_R1_R2_ATTR = "r2:r2 r1:r1";
    public static final String USER_R1__ATTR = "r1:r1";
    public static final String USER_R2__ATTR = "r2:r2";

    public static  String getPubfile(String fileName){
        return dir+fileName+"/pub_key";
    }

    public static String getMskfile(String fileName){
        return dir+fileName+"/master_key";
    }

    public static String getPrvfile(String fileName){
        return dir+fileName+"/prv_key";
    }
    public static String getUserPrvfile(String fileName,String userName){
        return dir+fileName+"/"+userName+"_prv_key";
    }
    public static String getEncodeFile(String fileName){
        return dir+fileName+"/key.cpe";
    }

    public static String getDecodeFile(String columnName) {
        return dir+columnName+"/key";
    }

}
