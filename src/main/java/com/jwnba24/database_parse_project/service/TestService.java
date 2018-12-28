package com.jwnba24.database_parse_project.service;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.common.TableColumnMapper;
import com.jwnba24.database_parse_project.dao.TestDao;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.model.Table1;
import com.jwnba24.database_parse_project.opinion.AESCBCEncoder;
import com.jwnba24.database_parse_project.opinion.AESECBEncoder;
import com.jwnba24.database_parse_project.opinion.CPABEEncoder;
import com.jwnba24.database_parse_project.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by jiwen on 2018/12/27.
 */
@Service
public class TestService {
    private TestDao testDao = new TestDao();

    public void testInsert(String sql) {
        testDao.insert(sql);
    }

    public String virtualUdfSelect(String sql, String prvFileName) {
        try {
            CPABEEncoder cpabeEncoder = new CPABEEncoder();
            boolean flag = cpabeEncoder.judgeIsOk(sql, Attribute.getPrvfile(prvFileName));
            //flag为true代表属性加密解密成功,然后解密一层洋葱
            //获取到的每一列，根据列名称查找所对应的属性密钥进行解密，然后更新
            if (flag) {
                List<Table1> list = testDao.virtualUdfSelect(sql);//未进行属性解密的密文
                //获取key，进行属性解密
                List<String> columnList = null;
                List<String> items = SelectSqlParser.getItems(sql);
                List<String> tables = SelectSqlParser.getTableName(sql);
                String tableName = tables.get(0);
                if (StringUtils.isEmpty(tableName)) {
                    System.out.println("表名称不能为空");
                    return null;
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
                    String column_name = null;

                    for (String s : columnList) {
                        switch (s) {
                            case "col1":
                                column_name = t.getCol1_name();
                                key = FileUtil.readKeyFile(t.getCol1_name());
                                IV = t.getCol1_IV();
                                column_value = t.getCol1_value();
                                String decodeValue = AESCBCEncoder.decrypt(column_value, key, IV);
                                String value = AESECBEncoder.decrypt(decodeValue, column_name);
                                System.out.println(value);
                                t.setCol1_value(decodeValue);
                            case "col2":
                                column_name = t.getCol2_name();
                                key = FileUtil.readKeyFile(t.getCol2_name());
                                IV = t.getCol2_IV();
                                column_value = t.getCol2_value();
                                String decodeValue1 = AESCBCEncoder.decrypt(column_value, key, IV);
                                String value1 = AESECBEncoder.decrypt(decodeValue1, column_name);
                                System.out.println(value1);
                                t.setCol2_value(decodeValue1);
                        }
                    }
                }
                for (String s : columnList) {
                    FileUtil.deleteFile(s);
                }

                //更新密文
            }else{
                return "查询权限不足";
            }

            } catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
        public static void main (String[]args){
            String sql = "select col2 from table1";
            TestService testService = new TestService();
            testService.virtualUdfSelect(sql, "col2");
        }

    }
