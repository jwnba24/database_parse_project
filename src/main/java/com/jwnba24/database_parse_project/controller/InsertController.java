package com.jwnba24.database_parse_project.controller;

import com.jwnba24.database_parse_project.jsqlparser.InsertSqlParser;
import com.jwnba24.database_parse_project.service.InsertService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwen on 2019/1/1.
 */
@RestController
@RequestMapping("/insert")
public class InsertController {
    InsertService insertService = new InsertService();

    /**
     * 返回改写之后的sql
     *
     * @param data 明文插入sql
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/sql/rewrite",method = RequestMethod.POST)
    public Result reWriteSql(String data) throws Exception {
        //批量插入的sql语句改写
        String[] sqlList = data.split(";");
        StringBuilder sb = new StringBuilder();
        InsertSqlParser insertSqlParser = new InsertSqlParser();
        for (String s : sqlList) {
            s= s.replace("\n","");
            if(StringUtils.isEmpty(s)) {
                continue;
            }
            String encode = insertSqlParser.encodeSQL(s);
            sb.append(encode);
            sb.append(";\n");
        }
        Result r = new Result();
        r.setStatus(true);
        r.setData(sb.toString());
        return r;
    }

    /**
     * 执行密文sql
     * @param data
     */
    @RequestMapping(value = "/sql/insert",method = RequestMethod.POST)
    public void doInsert(String data){
        String[] sqlList = data.split(";");
        for (String s : sqlList) {
            s= s.replace("\n","");
            InsertService insertService = new InsertService();
            insertService.insert(s);
        }
    }
}