package com.jwnba24.database_parse_project.controller;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.common.ColumnRoleMapper;
import com.jwnba24.database_parse_project.cpabe.cpabe.Cpabe;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jiwen on 2019/1/1.
 * 为用户分配权限
 */
@RestController
@RequestMapping("/privilege")
public class PrivilegeController {
    @RequestMapping("/generate")
    public void generatePrvKey(String user, String columns) throws Exception{
        String[] column = columns.split(",");
        Set<String> roles = new HashSet<>();
        for(String s: column){
            //遍历所有权限列得到其角色集合
            roles.add(ColumnRoleMapper.getRole(s));
        }
        //根据角色集合产生访问属性策略
        StringBuilder attribute = new StringBuilder();
        for(String r:roles){
            attribute.append(r+":"+r+" ");
        }
        String attributePolicy = attribute.toString();
        //根据访问属性策略生成私钥
        Cpabe cpabe = new Cpabe();
        cpabe.keygen(Attribute.getPubfile("col2"),Attribute.getUserPrvfile("col2",user),Attribute.getMskfile("col2"),attributePolicy.substring(0,attributePolicy.length()-1));

    }
}
