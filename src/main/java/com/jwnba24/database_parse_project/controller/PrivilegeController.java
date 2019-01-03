package com.jwnba24.database_parse_project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiwen on 2019/1/1.
 * 为用户分配权限
 */
@RestController
@RequestMapping("/privilege")
public class PrivilegeController {
    @RequestMapping("/generate")
    public void generatePrvKey(String user, String columns){
        String[] column = columns.split(",");
        for(String s: column){
            //遍历所有权限列得到其角色集合
        }
        //根据角色集合产生访问属性策略
        //根据访问属性策略生成私钥
    }
}
