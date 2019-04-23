package com.cikers.wechat.mall.modules.app.study;

import java.io.*;

public class ReadText {
    public static void main(String[] args) {
        try {
            String pathname = "D:"+ File.separator+"新建文本文档.txt";
//            FileInputStream fileInputStream = new FileInputStream(pathname);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReader =new BufferedReader(inputStreamReader);
            FileReader fileReader =new FileReader(pathname);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = " ";
            while ((line = bufferedReader.readLine())!=null){
                System.out.println(line);
            }
            bufferedReader.close();

            FileOutputStream fileOutputStream = new FileOutputStream(pathname);
            OutputStreamWriter outputStreamWriter =new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter =new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("我会写文件1\r\n");
            bufferedWriter.write("我会写文件2\r\n");
            bufferedWriter.flush();
            bufferedWriter.close();


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
