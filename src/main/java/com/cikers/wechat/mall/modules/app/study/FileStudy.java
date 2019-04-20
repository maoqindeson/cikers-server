package com.cikers.wechat.mall.modules.app.study;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
@Slf4j
public class FileStudy {
    public static void main(String[] args) {
        String pathname = "D:"+ File.separator+"test.txt";
        File file = new File(pathname);
        File file1 = new File("D:"+File.separator,"test1.txt");
        File file2 =new File( "D:" + File.separator +
                "JavaCode" + File.separator,"test.txt");
        File file3 = new File("C:");
        File[]files = file3.listFiles();
        for (File f:files){
            System.out.println(f.getAbsolutePath());
        }
        try {
            file.createNewFile();
            file1.createNewFile();
            file2.mkdirs();
        }catch (Exception e){
            log.warn("创建文件异常");
            e.printStackTrace();
        }
        System.out.println(file.delete());
        System.out.println(file1.exists());
        System.out.println(file.length());
        System.out.println(file.isDirectory());
        System.out.println(file2.getAbsolutePath());
        System.out.println(file2.getAbsoluteFile());
    }
}
