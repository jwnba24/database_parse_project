package com.jwnba24.database_parse_project.dao;

import com.jwnba24.database_parse_project.common.TableColumnMapper;
import com.jwnba24.database_parse_project.jsqlparser.InsertSqlParser;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.model.Model1;
import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.util.DbUtil;
import com.jwnba24.database_parse_project.util.TableColumnUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwen on 2018/12/27.
 */
@Repository
public class TestDao {

    public void insert(String sql) {
        DbUtil dbUtil = new DbUtil();
        Connection conn = null;
        Statement st = null;
        InsertSqlParser insertSqlParser = new InsertSqlParser();
        try {
            String encode_sql = insertSqlParser.encodeSQL(sql);
            conn = dbUtil.getConn();
            st = conn.createStatement();
            st.execute(encode_sql);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void peelOpinionUpdate(String sql){
        DbUtil dbUtil = new DbUtil();
        Connection conn = null;
        Statement st = null;
        try {
            conn = dbUtil.getConn();
            st = conn.createStatement();
            int result = st.executeUpdate(sql);
            if(result>0){
                System.out.println("更新成功");
            }else{
                System.out.println("更新失败");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                st.close();
                dbUtil.closeConn(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public List<Table1> virtualUdfSelect(String sql){
        DbUtil dbUtil = new DbUtil();
        Connection conn = null;
        Statement st = null;
        SelectSqlParser selectSqlParser = new SelectSqlParser();
        List<Table1> list = new ArrayList<>();
        try {
            String encode_sql = selectSqlParser.encryptSQL(sql);
            List<String> columnList = null;
            List<String> items = selectSqlParser.getItems(sql);
            List<String> tables = selectSqlParser.getTableName(sql);
            String tableName = tables.get(0);
            if(StringUtils.isEmpty(tableName)){
                System.out.println("表名称不能为空");
                return null;
            }
            if(items.get(0).contains("*")){
                TableColumnMapper tableColumnMapper = new TableColumnMapper();
                columnList = tableColumnMapper.getColumnList(tableName);
            }else{
                columnList=items;
            }
            conn = dbUtil.getConn();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(encode_sql);
            //将查询到的结果根据列名称进行解密
            while (rs.next()){
                Table1 table1 = new Table1();
                table1.setId(rs.getInt("id"));
                for(String s:columnList){
                    switch (s){
                        case "col1":
                            table1.setCol1_name(s);
                            table1.setCol1_name_encode(TableColumnUtil.encodeColumnName(s));
                            table1.setCol1_value(rs.getString(TableColumnUtil.encodeColumnName(s)));
                            table1.setCol1_IV(rs.getString(TableColumnUtil.encodeIV(s)));
                        case "col2":
                            table1.setCol2_name(s);
                            table1.setCol2_name_encode(TableColumnUtil.encodeColumnName(s));
                            table1.setCol2_value(rs.getString(TableColumnUtil.encodeColumnName(s)));
                            table1.setCol2_IV(rs.getString(TableColumnUtil.encodeIV(s)));
                    }
                }
                list.add(table1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<Table1> proxySelect(String sql){
        DbUtil dbUtil = new DbUtil();
        Connection conn = null;
        Statement st = null;
        SelectSqlParser selectSqlParser = new SelectSqlParser();
        List<Table1> list = new ArrayList<>();
        try {
            String encode_sql = selectSqlParser.encryptSQL(sql);
            List<String> columnList = null;
            List<String> items = selectSqlParser.getItems(sql);
            List<String> tables = selectSqlParser.getTableName(sql);
            String tableName = tables.get(0);
            if(items.get(0).contains("*")){
                TableColumnMapper tableColumnMapper = new TableColumnMapper();
                columnList = tableColumnMapper.getColumnList(tableName);
            }else{
                columnList=items;
            }
            conn = dbUtil.getConn();
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(encode_sql);
            //将查询到的结果根据列名称进行解密
            while (rs.next()){
                Table1 table1 = new Table1();
                table1.setId(rs.getInt("id"));
                for(String s:columnList){
                    switch (s){
                        case "col1":
                            table1.setCol1_name(s);
                            table1.setCol1_name_encode(TableColumnUtil.encodeColumnName(s));
                            table1.setCol1_value(AESECBEncoder.decrypt(rs.getString(TableColumnUtil.encodeColumnName(s)),s));
                            table1.setCol1_IV(rs.getString(TableColumnUtil.encodeIV(s)));
                        case "col2":
                            table1.setCol2_name(s);
                            table1.setCol2_name_encode(TableColumnUtil.encodeColumnName(s));
                            table1.setCol2_value(AESECBEncoder.decrypt(rs.getString(TableColumnUtil.encodeColumnName(s)),s));
                            table1.setCol2_IV(rs.getString(TableColumnUtil.encodeIV(s)));
                    }
                }
                list.add(table1);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
