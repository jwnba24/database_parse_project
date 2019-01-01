package com.jwnba24.database_parse_project.service;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.dao.TestDao;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.opinion.CPABEEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiwen on 2018/12/29.
 */
public class TestService {
    private TestDao testDao = new TestDao();
    private OninionService opinionService = new OninionService();

    public void testInsert(String sql) {
        testDao.insert(sql);
    }

    public List<HashMap<String,String>> testSelect(String selectSql,String prvFileName) throws Exception{
        List<HashMap<String,String>> result = null;

        CPABEEncoder cpabeEncoder = new CPABEEncoder();
        String sql = "select col2 from table1";
        boolean flag = cpabeEncoder.judgeIsOk(sql, Attribute.getPrvfile(prvFileName));
        if(flag){
            //1. 解除rnd层洋葱
            opinionService.peelingRndOnion(sql);
            //2. 真正的查询
            List<Table1> table1List = testDao.virtualUdfSelect(selectSql);
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
//                plainTable.add(table1);
                result.add(hashMap);
            }
//            for (Table1 t : plainTable) {
//                System.out.println(t.toString());
//            }
            //最后再将洋葱给加回去
            List<Table1> newTable = testDao.virtualUdfSelect(sql);
            OninionService opinionService = new OninionService();
            for (Table1 t : newTable) {
                String updateSql = opinionService.addRndOpinion(selectSql, t);
                testDao.peelOpinionUpdate(updateSql);
            }
        }else{
            System.out.println("查询权限不足");
        }
        return result;
    }

    public static void main(String[] args) {
        String selectSql = "select col2 from table1 where col2='test_2_1'";
        TestService testService = new TestService();
        try {
            List<HashMap<String,String>> result = testService.testSelect(selectSql,"col2");
            for(HashMap<String,String> hashMap:result){
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    System.out.println(key+":"+value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
