package com.jwnba24.database_parse_project.service;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.dao.SelectDao;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.opinion.CPABEEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiwen on 2019/1/1.
 */
public class SelectService {
    OninionService opinionService = new OninionService();
    SelectDao selectDao = new SelectDao();
    public List<HashMap<String,String>> select(String selectSql, String prvFileName) throws Exception{
        List<HashMap<String,String>> result = null;

        CPABEEncoder cpabeEncoder = new CPABEEncoder();
        String sql = "select col2 from table1";
        boolean flag = cpabeEncoder.judgeIsOk(sql, Attribute.getPrvfile(prvFileName));
        if(flag){
            //1. 解除rnd层洋葱
            opinionService.peelingRndOnion(sql);
            //2. 真正的查询
            List<Table1> table1List = selectDao.virtualUdfSelect(selectSql);
            SelectSqlParser selectSqlParser = new SelectSqlParser();
            List<String> items = selectSqlParser.getItems(selectSql);
            result = new ArrayList<>();
            for (Table1 t : table1List) {
                HashMap<String,String> hashMap = new HashMap<>();
                Table1 table1 = new Table1();
                for (String item : items) {
                    switch (item) {
                        case "col1":
                            table1.setCol1_name(t.getCol1_name());
                            table1.setCol1_value(AESECBEncoder.decrypt(t.getCol1_value(), t.getCol1_name()));
                            hashMap.put(t.getCol1_name(),AESECBEncoder.decrypt(t.getCol1_value(), t.getCol1_name()));
                        case "col2":
                            table1.setCol2_name(t.getCol2_name());
                            table1.setCol2_value(AESECBEncoder.decrypt(t.getCol2_value(), t.getCol2_name()));
                            hashMap.put(t.getCol2_name(),AESECBEncoder.decrypt(t.getCol2_value(), t.getCol2_name()));
                    }
                }
                result.add(hashMap);
            }
            //最后再将洋葱给加回去
            List<Table1> newTable = selectDao.virtualUdfSelect(sql);
            OninionService opinionService = new OninionService();
            for (Table1 t : newTable) {
                String updateSql = opinionService.addRndOpinion(selectSql, t);
                selectDao.peelOpinionUpdate(updateSql);
            }
        }else{
            System.out.println("查询权限不足");
        }
        return result;
    }
}
