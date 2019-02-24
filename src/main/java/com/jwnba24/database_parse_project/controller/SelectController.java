package com.jwnba24.database_parse_project.controller;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.jsqlparser.SelectSqlParser;
import com.jwnba24.database_parse_project.service.SelectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result reWrite(String sql) throws Exception{
        String encode_sql = selectSqlParser.encryptSQL(sql);
        Result result = new Result();
        result.setStatus(true);
        result.setData(encode_sql);
        return result;
    }

    @RequestMapping("/sql/select")
    public Result select(String sql,String user) throws Exception{
        //得到检索结果
        String fileName = Attribute.getUserPrvfile("col2",user);
        File file = new File(fileName);
        Result result = new Result();
        if(!file.exists()){
            result.setStatus(false);
            result.setData("私钥文件不存在！");
        }else{
            StringBuilder sb = new StringBuilder();
            List<HashMap<String,String>> list = selectService.select(sql,"col2");
            for(HashMap<String,String> hashMap:list){
                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    sb.append(key+":"+value+"  ");
                }
                sb.append("\r\n");
            }
            result.setStatus(true);
            result.setData(sb.toString());
        }
        return result;
    }
}
