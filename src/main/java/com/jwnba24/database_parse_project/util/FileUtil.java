package com.jwnba24.database_parse_project.util;

import java.io.*;

public class FileUtil {
	public static void deleteFile(String columnName) {
		File file = new File(PathUtil.getABEPath()+columnName+"/key");
		if(file.exists()) file.delete();
	}

	public static void deleteEncryptFile(String column) {
		File file = new File(PathUtil.getABEPath()+column+"/key.cpe");
		if(file.exists()) file.delete();
	}

	/**
	 * 返回加密文件路径
	 * @param fileName
	 * @param content
	 * @return
	 */
	public String createKeyFile(String fileName, String content){
		String result = "";
		try {
			File file = new File(PathUtil.getABEPath()+fileName);
			if(!file.exists()) file.mkdir();
			String name = PathUtil.getABEPath()+fileName+"/key";
			File writeName = new File(name);
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			try (FileWriter writer = new FileWriter(writeName);
				 BufferedWriter out = new BufferedWriter(writer)
			) {
				out.write(content);
				out.flush();
			}
			result = PathUtil.getABEPath()+fileName+"/key";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String readKeyFile(String fileName){
		StringBuilder sb = new StringBuilder();
		try (FileReader reader = new FileReader(PathUtil.getABEPath()+fileName+"/key");
			 BufferedReader br = new BufferedReader(reader);
		) {
			String line;
			//网友推荐更加简洁的写法
			while ((line = br.readLine()) != null) {
				// 一次读入一行数据
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileUtil fileUtil = new FileUtil();
//		fileUtil.createKeyFile("name_key","abdskdjsakhf21394302");
		String str = fileUtil.readKeyFile("name_key");
		System.out.println(str);
	}
}
