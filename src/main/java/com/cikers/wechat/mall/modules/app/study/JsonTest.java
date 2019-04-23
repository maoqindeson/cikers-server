package com.cikers.wechat.mall.modules.app.study;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JsonTest {
    @Test
    public void  testJson(){
        JSONObject  object = new JSONObject();
        object.put("string","string");
        object.put("int",2);
        object.put("boolean",true);
        List<Integer>list = Arrays.asList(1,2,3);
        object.put("list",list);
        object.put("null",null);
        System.out.println(object);
    }

    @Test
    public void testJson1(){
        JSONObject object = JSONObject.parseObject("{\"boolean\":true,\"string\":\"string\",\"list\":[1,2,3],\"int\":2}");
        String string = object.getString("string");
        System.out.println(string);
        Integer integer = object.getInteger("int");
        System.out.println(integer);
        boolean b = object.getBoolean("boolean");
        System.out.println(b);
        //parseArray解析成数组,其中Integer.class是必须加的
        List<Integer>list = JSON.parseArray(object.getJSONArray("list").toJSONString(),Integer.class);
        System.out.println(object.getJSONArray("list"));
        System.out.println(object.getJSONArray("list").toJSONString());
        System.out.println(Integer.class);
        System.out.println(JSON.parseArray(object.getJSONArray("list").toJSONString(),Integer.class));
        list.forEach(System.out::println);
        System.out.println(object.getString("null"));
    }
}
