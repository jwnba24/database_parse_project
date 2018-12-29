package com.jwnba24.database_parse_project.jsqlparser;

import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.util.TableColumnUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiwen on 2018/12/25.
 * 用户改写sql语句
 */
public class SelectSqlParser {

    /**
     * 加密sql语句
     * @param sql 明文sql
     * @return
     */
    public String encryptSQL(String sql) throws Exception{
        List<String> tableList = getTableName(sql);
        List<String> items = getItems(sql);
        String where = getWhere(sql);
        //拼接sql
        PlainSelect plainSelect = new PlainSelect();
        List<SelectItem> selectItems = new ArrayList<>();
        for(String item : items){
            if("*".equals(item)) {continue;}
            selectItems.add(new SelectExpressionItem(CCJSqlParserUtil.parseExpression(TableColumnUtil.encodeColumnName(item))));
            selectItems.add(new SelectExpressionItem(CCJSqlParserUtil.parseExpression(item+"_IV")));
        }
        selectItems.add(new SelectExpressionItem(CCJSqlParserUtil.parseExpression("id")));
        plainSelect.setSelectItems(selectItems);

        plainSelect.setFromItem(new Table(TableColumnUtil.encodeTableName(tableList.get(0))));

        if(where!=null){
            plainSelect.setWhere(CCJSqlParserUtil.parseCondExpression(where));
        }
        return plainSelect.toString();
    }

    /**
     * 获取所有查询列名称
     * @param sql
     * @return
     */
    public static List<String> getItems(String sql) throws Exception{
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        List<String> str_items = new ArrayList<>();
        if (selectItems != null) {
            for (int i = 0; i < selectItems.size(); i++) {
                str_items.add(selectItems.get(i).toString());
            }
        }
        return str_items;
    }

    /**
     * 获取表名称
     * @param sql
     * @return
     */
    public static List<String> getTableName(String sql) throws Exception{
        Statement statement = CCJSqlParserUtil.parse(sql);
        Select selectStatement = (Select) statement;
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(selectStatement);

        //加密表名称
        List<String> result = new ArrayList<>();
        for(String tableName : tableList){
            result.add(tableName);
        }
        return result;
    }

    public static String getWhere(String sql) throws JSQLParserException {
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
        Expression where = plainSelect.getWhere();
        if(ObjectUtils.isEmpty(where)){
            return null;
        }
        // 此处根据where实际情况强转 最外层
        EqualsTo equalsTo = (EqualsTo)where;
        String column = equalsTo.getLeftExpression().toString();
        String value = equalsTo.getRightExpression().toString();
        if(value.startsWith("\'")){
            System.out.println("选中我了啊");
            value = value.substring(1,value.length()-1);
        }
        ((EqualsTo) where).setLeftExpression(new Column(TableColumnUtil.encodeColumnName(column)));
        try {
            String encode_value = AESECBEncoder.encrypt(value,column);
            ((EqualsTo) where).setRightExpression(new StringValue(encode_value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return where.toString();
    }

    public static void main(String[] args) throws Exception {
        String sql = "select col2 from table1 where col1 = jiwen";
        SelectSqlParser selectSqlParser = new SelectSqlParser();
        String s = selectSqlParser.encryptSQL(sql);
        System.out.println(s);
    }
}
