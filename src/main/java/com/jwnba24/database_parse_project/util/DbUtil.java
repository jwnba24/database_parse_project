package com.jwnba24.database_parse_project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jiwen on 2018/12/26.
 */
public class DbUtil {

    public Connection getConn(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testDb?characterEncoding=utf-8","root","123456");
            return conn;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConn(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        DbUtil dbUtil = new DbUtil();
        Connection conn = dbUtil.getConn();
        if(conn !=  null){
            System.out.println("success");
            dbUtil.closeConn(conn);
        }else{
            System.out.println("fail");
        }
    }

}
