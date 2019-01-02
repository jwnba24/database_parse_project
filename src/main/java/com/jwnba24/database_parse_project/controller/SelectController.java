package com.jwnba24.database_parse_project.controller;

import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.service.SelectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiwen on 2019/1/1.
 * 查询
 */
@RestController
@RequestMapping("/select")
public class SelectController {
    SelectSqlParser selectSqlParser = new SelectSqlParser();
    SelectService selectService = new SelectService();
    @RequestMapping("/sql/rewrite")
    public String reWrite(String sql) throws Exception{
        String encode_sql = selectSqlParser.encryptSQL(sql);
        return encode_sql;
    }

    @RequestMapping("/sql/select")
    public void select(String sql,String fileName) throws Exception{
        //得到检索结果
        List<HashMap<String,String>> list = selectService.select(sql,fileName);
    }
}
