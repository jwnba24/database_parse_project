package com.jwnba24.database_parse_project.opinion;

import com.jwnba24.database_parse_project.common.TableColumnMapper;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.util.AttribueEncodeUtil;
import com.jwnba24.database_parse_project.common.ColumnRoleMapper;
import com.jwnba24.database_parse_project.util.FileUtil;

import java.util.List;

/**
 * 属性加密 加密AES加密密钥
 * Created by jiwen on 2018/12/26.
 */
public class CPABEEncoder {
    /**
     * 传入AES加密密钥加密
     * @param key
     */
    public void encode(String key,String columnName){
        FileUtil fileUtil = new FileUtil();
        //1. 将密钥封装成密钥文件
        String fileName = fileUtil.createKeyFile(columnName, key);
        //2. 将密钥文件进行属性加密
        AttribueEncodeUtil attribueEncodeUtil = new AttribueEncodeUtil();
        attribueEncodeUtil.encode(ColumnRoleMapper.getRole(columnName),fileName,columnName);
        //3. 将原密钥删除
        FileUtil.deleteFile(columnName);

    }

    public static boolean decode(String column,String prvFileName){
        AttribueEncodeUtil attribueEncodeUtil =new AttribueEncodeUtil();
        try {
            attribueEncodeUtil.decode(column,prvFileName);
        } catch (Exception e) {
            System.out.println(column+"解密失败");
            e.printStackTrace();
            return false;
        }
        return true;
        //删除属性加密文件
//        FileUtil.deleteEncryptFile(column);
    }

    /**
     * 判断是否拥有访问的权限,如果有访问权限的话则解密AESCBC的密钥
     * @param sql
     * @param prv_file
     * @return
     */
    public boolean judgeIsOk(String sql,String prv_file) throws Exception{
        List<String> tableList = SelectSqlParser.getTableName(sql);
        List<String> items = SelectSqlParser.getItems(sql);
        String tableName = tableList.get(0);
        boolean flag = true;
        for(String item:items){
            //如果包含*则需要根据表名称去找所有的列
            if(item.contains("*")){
                TableColumnMapper tableColumnMapper = new TableColumnMapper();
                List<String> columnList = tableColumnMapper.getColumnList(tableName);
                if(columnList.size()==0) {
                    throw new Exception("error happen");
                }
                for(String s:columnList){
                    flag = CPABEEncoder.decode(s,prv_file);
                    if(!flag) break;
                }
            }else{
                if("id".equals(item)) continue;
                flag = CPABEEncoder.decode(item,prv_file);
                if(!flag) break;
            }
        }
        //如果存在解密失败的情况，遍历所有列，删除其key文件
        if(!flag){
            for(String item:items){
                //如果包含*则需要根据表名称去找所有的列
                if(item.contains("*")){
                    TableColumnMapper tableColumnMapper = new TableColumnMapper();
                    List<String> columnList = tableColumnMapper.getColumnList(tableName);
                    for(String s:columnList){
                        FileUtil.deleteFile(s);
                    }
                }else{
                    FileUtil.deleteFile(item);
                }
            }
        }

        return flag;
    }

}
