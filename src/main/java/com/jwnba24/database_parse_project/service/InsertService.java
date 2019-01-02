package com.jwnba24.database_parse_project.service;

import com.jwnba24.database_parse_project.dao.InsertDao;

/**
 * Created by jiwen on 2019/1/1.
 * 插入数据服务
 */
public class InsertService {

    public InsertDao insertDao = new InsertDao();
    public void insert(String sql) {
        insertDao.insert(sql);
    }
}
