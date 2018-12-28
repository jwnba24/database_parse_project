package com.jwnba24.database_parse_project.jsqlparser;

import com.jwnba24.database_parse_project.opinion.AESCBCEncoder;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.util.TableColumnUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据插入sql改写
 * Created by jiwen on 2018/12/25.
 */
public class InsertSqlParser {
    public static void main(String[] args) throws Exception{
        String sql = "insert into table1 (col1,col2) values (jiwen1,jiwen2)";
        InsertSqlParser sqlParser = new InsertSqlParser();
        String encode_sql = sqlParser.encodeSQL(sql);
        System.out.println(encode_sql);
    }

    public String encodeSQL(String sql) throws Exception{
        System.out.println("start encode the sql:"+sql);
        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        List<Column> columnList = insert.getColumns();
        ExpressionList itemsList = (ExpressionList) insert.getItemsList();
        List<Expression> expressionList = itemsList.getExpressions();
        Table table = insert.getTable();

        HashMap<String,Object> colsAndValues = new HashMap<>();
        try{
            for(int i=0;i<columnList.size();i++){
                colsAndValues.put(columnList.get(i).getColumnName(),expressionList.get(i).toString());
            }
        }catch (Exception e){
            System.out.println("遍历异常");
            return null;
        }

        List<Column> columnList_new = new ArrayList<>();
        List<Expression> expressionList_new = new ArrayList<>();
        HashMap<String, Object> expandColumns = expandColumn(colsAndValues);
        for(Map.Entry<String, Object> entry: expandColumns.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            columnList_new.add(new Column(key));
            String temp = value.toString();
            StringValue stringValue = new StringValue(" "+temp+" ");
            expressionList_new.add(stringValue);

        }
        insert.setColumns(columnList_new);
        itemsList.setExpressions(expressionList_new);
        insert.setItemsList(itemsList);
        insert.setTable(new Table(TableColumnUtil.encodeTableName(table+"")));
        return insert.toString();
    }

    private HashMap<String, Object> expandColumn(HashMap<String, Object> colsAndValues) throws Exception{
        HashMap<String,Object> result = new HashMap<>();
        //遍历所有column，扩展至洋葱加密列
        for(Map.Entry<String, Object> entry: colsAndValues.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue()+"";
            //加密所有的value值,value值经过多重洋葱加密
            //使用ECB模式AES加密，回传密钥值，将密钥存入数据库中meta表中（ID,COLUMN_NAME,DET_kEY）
            String ecb_value = AESECBEncoder.encrypt(value,key);

            //使用CBC模式AES加密，回传IV值
            HashMap<String,String> cbc_result = AESCBCEncoder.encrypt(ecb_value,key);
            //将一扩展至多个洋葱层，以下对应不同的加密操作
            result.put(key+"_column_opinion1",cbc_result.get("value"));
//            result.put(key+"_column_opinion2",cbc_result.get("value"));
//            result.put(key+"_column_opinion3",cbc_result.get("value"));
//            result.put(key+"_column_opinion4",cbc_result.get("value"));
            result.put(key+"_IV",cbc_result.get("IV"));
        }

        return result;
    }
}
