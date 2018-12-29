package com.jwnba24.database_parse_project.service;

import com.jwnba24.database_parse_project.common.TableColumnMapper;
import com.jwnba24.database_parse_project.dao.TestDao;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.jsqlparser.UpdateSqlParser;
import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.opinion.AESCBCEncoder;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.util.FileUtil;
import com.jwnba24.database_parse_project.util.TableColumnUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.update.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jiwen on 2018/12/27.
 */
@Service
public class OninionService {
    private TestDao testDao = new TestDao();

    /**
     * 剥rnd层洋葱
     *
     * @param sql
     * @param prvFileName
     * @return 返回解洋葱后的密文
     */
    public void peelingRndOnion(String sql, String prvFileName) {
        try {
            List<Table1> list = testDao.virtualUdfSelect(sql);//未进行属性解密的密文
            //获取key，进行属性解密
            List<String> columnList = null;
            List<String> items = SelectSqlParser.getItems(sql);
            List<String> tables = SelectSqlParser.getTableName(sql);
            String tableName = tables.get(0);
            if (StringUtils.isEmpty(tableName)) {
                System.out.println("表名称不能为空");
            }
            if (items.get(0).contains("*")) {
                TableColumnMapper tableColumnMapper = new TableColumnMapper();
                columnList = tableColumnMapper.getColumnList(tableName);
            } else {
                columnList = items;
            }

            for (Table1 t : list) {
                String key = null;
                String IV = null;
                String column_value = null;

                for (String s : columnList) {
                    switch (s) {
                        case "col1":
                            key = FileUtil.readKeyFile(t.getCol1_name());
                            IV = t.getCol1_IV();
                            column_value = t.getCol1_value();
                            String decodeValue = AESCBCEncoder.decrypt(column_value, key, IV);
                            t.setCol1_value(decodeValue);
                        case "col2":
                            key = FileUtil.readKeyFile(t.getCol2_name());
                            IV = t.getCol2_IV();
                            column_value = t.getCol2_value();
                            String decodeValue1 = AESCBCEncoder.decrypt(column_value, key, IV);
                            t.setCol2_value(decodeValue1);
                    }
                }
            }
            for (String s : columnList) {
                FileUtil.deleteFile(s);
            }
            //更新密文 使用jsqlparser构造update语句
            UpdateSqlParser updateSqlParser = new UpdateSqlParser();
            TestDao testDao = new TestDao();
            for (Table1 t : list) {
                String toUpdateSql = updateSqlParser.turnSelectToUpdate(sql, t);
                testDao.peelOpinionUpdate(toUpdateSql);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //加上洋葱
    public String addRndOpinion(String sql, Table1 table1) throws Exception {

        List<String> items = SelectSqlParser.getItems(sql);
        List<String> tables = SelectSqlParser.getTableName(sql);
        String tableName = tables.get(0);
        Update update = new Update();
        //添加表名称
        List<Table> tableList = new ArrayList<>();
        tableList.add(new Table(TableColumnUtil.encodeTableName(tableName)));
        update.setTables(tableList);
        //添加列名称
        List<Column> columns = new ArrayList<>();
        List<Expression> expressions = new ArrayList<>();
        for (String item : items) {
            switch (item) {
                case "col1":
                    columns.add(new Column(table1.getCol1_name_encode()));
                    String column_value1 = table1.getCol1_value();
                    HashMap<String, String> keysMap1 = AESCBCEncoder.encrypt(column_value1, item);
                    expressions.add(new StringValue(keysMap1.get("value")));
                    columns.add(new Column(TableColumnUtil.encodeIV(item)));
                    expressions.add(new StringValue(keysMap1.get("IV")));
                case "col2":
                    columns.add(new Column(table1.getCol2_name_encode()));
                    String column_value2 = table1.getCol2_value();
                    HashMap<String, String> keysMap2 = AESCBCEncoder.encrypt(column_value2, item);
                    expressions.add(new StringValue(keysMap2.get("value")));
                    columns.add(new Column(TableColumnUtil.encodeIV(item)));
                    expressions.add(new StringValue(keysMap2.get("IV")));
            }
        }
        update.setColumns(columns);
        update.setExpressions(expressions);
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("id"));
        equalsTo.setRightExpression(new StringValue(table1.getId().toString()));
        update.setWhere(equalsTo);
        return update.toString();
    }

    public static void main(String[] args) throws Exception {
        //首先是剥除洋葱
        String sql = "select col2 from table1";
        OninionService testService = new OninionService();
        testService.peelingRndOnion(sql, "col2");
        //然后是真正的查询
        String selectSql = "select col2 from table1 where col2='test_2_1'";
        TestDao testDao = new TestDao();
        List<Table1> table1List = testDao.virtualUdfSelect(selectSql);
        List<String> items = SelectSqlParser.getItems(selectSql);
        List<Table1> plainTable = new ArrayList<>();
        for (Table1 t : table1List) {
            Table1 table1 = new Table1();
            for (String item : items) {
                switch (item) {
                    case "col1":
                        table1.setCol1_name(t.getCol1_name());
                        table1.setCol1_value(AESECBEncoder.decrypt(t.getCol1_value(), t.getCol1_name()));
                    case "col2":
                        table1.setCol2_name(t.getCol2_name());
                        table1.setCol2_value(AESECBEncoder.decrypt(t.getCol2_value(), t.getCol2_name()));
                }
            }
            plainTable.add(table1);
        }
        for (Table1 t : plainTable) {
            System.out.println(t.toString());
        }
        //最后再将洋葱给加回去
        List<Table1> newTable = testDao.virtualUdfSelect(sql);
        OninionService opinionService = new OninionService();
        for (Table1 t : newTable) {
            String updateSql = opinionService.addRndOpinion(selectSql, t);
            testDao.peelOpinionUpdate(updateSql);
        }
    }

}
