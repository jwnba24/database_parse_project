package com.jwnba24.database_parse_project.util;

import com.jwnba24.database_parse_project.common.Attribute;
import com.jwnba24.database_parse_project.cpabe.cpabe.Cpabe;

import java.io.File;

/**
 * Created by jiwen on 2018/12/26.
 * 属性加密工具
 */
public class AttribueEncodeUtil {

    public void encode(String policy, String file, String fileName){
        System.out.println("start attribute encode,policy:"+policy+",file:"+file+",fileName:"+fileName);
        File pub_file = new File(Attribute.getPubfile(fileName));
        if(pub_file.exists()) {
            System.out.println("加密密钥已经存在");
            return;
        };
        Cpabe cpabe = new Cpabe();
        try{
            System.out.println("generate pub_key msk_key");
            cpabe.setup(Attribute.getPubfile(fileName),Attribute.getMskfile(fileName));
            if("r1".equals(policy)){
                System.out.println("r1 generate user key");
                cpabe.keygen(Attribute.getPubfile(fileName),Attribute.getPrvfile(fileName),Attribute.getMskfile(fileName),Attribute.USER_R1__ATTR);
                System.out.println("start encode file:"+file);
                cpabe.enc(Attribute.getPubfile(fileName),Attribute.R1_ATTR,file,Attribute.getEncodeFile(fileName));
            }
            if("r2".equals(policy)){
                System.out.println("r2 generate user key");
                cpabe.keygen(Attribute.getPubfile(fileName),Attribute.getPrvfile(fileName),Attribute.getMskfile(fileName),Attribute.USER_R2__ATTR);
                cpabe.enc(Attribute.getPubfile(fileName),Attribute.R2_ATTR,file,Attribute.getEncodeFile(fileName));
            }
        }catch (Exception e){
            System.out.println("加密失败");
        }
    }

    public void decode(String columnName,String prvFileName) throws Exception {
        Cpabe cpabe = new Cpabe();

        cpabe.dec(Attribute.getPubfile(columnName),prvFileName,Attribute.getEncodeFile(columnName),Attribute.getDecodeFile(columnName));

    }
}
