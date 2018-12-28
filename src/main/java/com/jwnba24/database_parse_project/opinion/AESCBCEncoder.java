package com.jwnba24.database_parse_project.opinion;

import com.jwnba24.database_parse_project.opinion.CPABEEncoder;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Random;

import static com.jwnba24.database_parse_project.opinion.KeyGenerator.generateKey;

/**
 * AES加解密工具
 * Created by steadyjack on 2018/2/9.
 */
public class AESCBCEncoder {
 
    private static final String CipherMode="AES/CBC/PKCS5Padding";

    private static final Integer IVSize=16;
 
    private static final String EncryptAlg ="AES";
 
    private static final String Encode="UTF-8";
 
    private static final int SecretKeySize=32;

    private static String generateIV(){
        StringBuffer sb = new StringBuffer();
        String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int min = 10;
        int max = 20;
        int length = new Random().nextInt(max-min)+min;
        for (int i = 0; i < length; i++) {
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
        }
        return sb.toString();
    }
    /**
     * 创建密钥
     * @return
     */
    private static SecretKeySpec createRealKey(String key){
        StringBuilder sb=new StringBuilder(SecretKeySize);
        sb.append(key);
        if (sb.length()>SecretKeySize){
            sb.setLength(SecretKeySize);
        }
        if (sb.length()<SecretKeySize){
            while (sb.length()<SecretKeySize){
                sb.append(" ");
            }
        }
        try {
            byte[] data=sb.toString().getBytes(Encode);
            return new SecretKeySpec(data, EncryptAlg);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
 
    /**
     * 创建16位向量: 不够则用0填充
     * @return
     */
    private static IvParameterSpec createRealIV(String IV) {
        StringBuffer sb = new StringBuffer(IVSize);
        sb.append(IV);
        if (sb.length()>IVSize){
            sb.setLength(IVSize);
        }
        if (sb.length()<IVSize){
            while (sb.length()<IVSize){
                sb.append("0");
            }
        }
        byte[] data=null;
        try {
             data=sb.toString().getBytes(Encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }
 
    /**
     * 加密：有向量16位，结果转base64
     * @param context
     * @return
     */
    public static HashMap<String,String> encrypt(String context, String column) {
        HashMap<String,String> result = new HashMap<>();
        String key = KeyGenerator.generateKey(column);
        String IV = generateIV();
        System.out.println("start aes cbc, column:"+column+",key:"+key+"IV:"+IV);
        //将密钥进行属性加密
        CPABEEncoder cpabeEncoder = new CPABEEncoder();
        cpabeEncoder.encode(key,column);
        result.put("IV",IV);

        try {
            byte[] content=context.getBytes(Encode);
            SecretKeySpec secretKeySpec = createRealKey(key);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, createRealIV(IV));
            byte[] data = cipher.doFinal(content);
            String str=Base64.encodeBase64String(data);
            result.put("value",str);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 解密
     * @param context
     * @return
     */
    public static String decrypt(String context,String column, String IV) {
        String secretKey = generateKey(column);
        try {
            byte[] data=Base64.decodeBase64(context);
            SecretKeySpec key = createRealKey(secretKey);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createRealIV(IV));
            byte[] content = cipher.doFinal(data);
            String result=new String(content,Encode);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public static void main(String[] args) throws Exception{
        //密钥 加密内容(对象序列化后的内容-json格式字符串)
//        String content="{\"domain\":{\"method\":\"getDetails\",\"url\":\"http://www.baidu.com\"},\"name\":\"steadyjack_age\",\"age\":\"23\",\"address\":\"Canada\",\"id\":\"12\",\"phone\":\"15627284601\"}";
//
//        AESCBCEncoder aescbcUtil = new AESCBCEncoder();
//        String secretKey = aescbcUtil.generateIV();
//        String IV = aescbcUtil.generateKey("a");
//        String encryptText=aescbcUtil.encrypt(content,secretKey,IV);
//        System.out.println("密钥key:"+secretKey+";IV:"+IV);
//        String decryptText=aescbcUtil.decrypt(encryptText,secretKey,IV);
//        System.out.println(String.format("明文：%s \n加密结果：%s \n解密结果：%s ",content,encryptText,decryptText));
    }
}
