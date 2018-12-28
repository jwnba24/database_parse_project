package com.jwnba24.database_parse_project.controller;

import com.jwnba24.database_parse_project.jsqlparser.InsertSqlParser;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.service.TestService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwen on 2018/12/27.
 */
public class TestController {
    private TestService testService = new TestService();

    public void testInsert(){
        String sql = "insert into table1 (col1,col2) values (jiwen1,jiwen2)";
        testService.testInsert(sql);
    }

    public void testSelect(){
        //模拟只有r1权限的用户去查询权限以外的列
    }

    public static void main(String[] args) {
        new TestController().webTestInsert();
    }

    public void webTestInsert(){
        //模拟插入10条数据
        //1. 展示原插入sql
        TestService testService = new TestService();
        try{
            InsertSqlParser insertSqlParser = new InsertSqlParser();
            List<String> sqlList = new ArrayList<>();
            List<String> encodeSqlList = new ArrayList<>();
            for(int i=0;i<10;i++){
                StringBuilder sb = new StringBuilder("insert into table1 (col1,col2) values ("+"test_1_"+0+","+"test_2_"+0+")");
                sqlList.add(sb.toString());
            }
            sqlList.forEach(s->{
                System.out.println("加密前sql:"+s);
                //加密之后的sql语句
                try {
                   String encode_sql = insertSqlParser.encodeSQL(s);
                    System.out.println(encode_sql);
                    testService.testInsert(s);
                   encodeSqlList.add(encode_sql);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            //将每一列扩展为4列不同的洋葱，每一列选用不同的加密方法进行加密
        }catch (Exception e){

        }
    }

    //模拟查询过程
    public void webTestUpdate(){
        String sql = "select col2 from table1 where col1 = test";
        try{
            SelectSqlParser selectSqlParser = new SelectSqlParser();
            //1.展示查询sql
            String encode_sql = selectSqlParser.encryptSQL(sql);
            System.out.println(encode_sql);
            //2.展示加密后的查询sql

            //3.显示剥去洋葱后的数据库密文

            //4. 显示查询结果
        }catch (Exception e){

        }
    }

}
