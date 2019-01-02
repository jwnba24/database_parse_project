package com.jwnba24.database_parse_project.dao;

import com.jwnba24.database_parse_project.jsqlparser.InsertSqlParser;
import com.jwnba24.database_parse_project.util.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by jiwen on 2019/1/1.
 */
public class InsertDao {
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

}
