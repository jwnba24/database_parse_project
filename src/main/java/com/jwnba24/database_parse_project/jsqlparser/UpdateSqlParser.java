package com.jwnba24.database_parse_project.jsqlparser;

import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.util.TableColumnUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

import static com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser.getItems;
import static com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser.getTableName;

/**
 * Created by jiwen on 2018/12/28.
 */
public class UpdateSqlParser {
    public String encodeSql(String sql) throws Exception{
        Statement statement = CCJSqlParserUtil.parse(sql);
        Update updateStatement = (Update) statement;
        List<Table> update_table = updateStatement.getTables();
        String tableName = update_table.get(0).getName();
        List<Table> tables = new ArrayList<>();
        tables.add(new Table(tableName));
        updateStatement.setTables(tables);

        List<Column> columnList = updateStatement.getColumns();
        List<Column> columns = new ArrayList<>();
        for(Column c:columnList){
            String name = c.getName(false);
            columns.add(new Column(name+"_encode"));
        }
        updateStatement.setColumns(columns);
        List<Expression> update_values = updateStatement.getExpressions();
        List<Expression> values = new ArrayList<>();
        for(Expression e:update_values){
            values.add(new StringValue(e.toString()+"_encode"));
        }
        updateStatement.setExpressions(values);

        Expression where = updateStatement.getWhere();
        EqualsTo equalsTo = (EqualsTo)where;
        String column = ((EqualsTo) where).getLeftExpression().toString();
        String value = ((EqualsTo) where).getRightExpression().toString();
        ((EqualsTo) where).setLeftExpression(new Column("col1_abc"));
        try {
            String encode_value = "aaaaa";
            ((EqualsTo) where).setRightExpression(new StringValue(encode_value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateStatement.setWhere(CCJSqlParserUtil.parseCondExpression(where.toString()));
        System.out.println(updateStatement.toString());
        return null;
    }

    //在解密洋葱更新密文的时候根据select语句构造更新语句，达到更新密文的目的
    public String turnSelectToUpdate(String sql, Table1 table1) throws Exception{
        List<String> tableList = getTableName(sql);
        List<String> items = getItems(sql);

        Update update = new Update();
        List<Table> tables = new ArrayList<>();
        for(String s: tableList){
            tables.add(new Table(TableColumnUtil.encodeTableName(s)));
        }
        update.setTables(tables);

        List<Column> columns = new ArrayList<>();
        List<Expression> expressions = new ArrayList<>();
        for(String item:items){
            switch (item){
                case "col1":
                    columns.add(new Column(table1.getCol1_name_encode()));
                    expressions.add(new StringValue(table1.getCol1_value()));
                case "col2":
                    columns.add(new Column(table1.getCol2_name_encode()));
                    expressions.add(new StringValue(table1.getCol2_value()));
            }
        }
        update.setColumns(columns);
        update.setExpressions(expressions);
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column("id"));
        equalsTo.setRightExpression(new LongValue(table1.getId()));
        update.setWhere(equalsTo);
        System.out.println(update.toString());
        return update.toString();
    }
}
