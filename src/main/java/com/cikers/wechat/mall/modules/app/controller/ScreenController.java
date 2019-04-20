package com.cikers.wechat.mall.modules.app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cikers.wechat.mall.modules.app.entity.ScreenEntity;
import com.cikers.wechat.mall.modules.app.entity.UserEntity;
import com.cikers.wechat.mall.modules.app.form.ProductForm;
import com.cikers.wechat.mall.modules.app.service.ScreenService;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/screen")
public class ScreenController {
    @Autowired
    private ScreenService screenService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/getAllScreen")
    public BaseResp getAllScreen() {
        List<ScreenEntity> list = screenService.selectList(new EntityWrapper<>());
        if (list == null || list.isEmpty()) {               //list校验null和空，用||
            return BaseResp.error("查询结果为空");
        }
        List<String> list1 = new ArrayList<>();     //新建返回结果list，为了设置按需要得到返回值
        for (ScreenEntity screenEntity : list) {          //遍历查询出来的结果list
            list1.add(screenEntity.getArticleNumber());     //只返回货号，放到新list中
        }
        return BaseResp.ok("屏蔽货号查询成功", list1);
    }

    @PostMapping("/insertScreen")
    public BaseResp insertScreen(String articleNumber) {
        //select * from tb_screen where article_number = #{articleNumber}) SQL语句
        // 下面这行代码用的是系统自带的selectOne方法，通过articleNumber查询出对象
        ScreenEntity entity = screenService.selectOne(new EntityWrapper<ScreenEntity>().eq("article_number", articleNumber));
        if (entity != null) {             //对象校验null
            return BaseResp.error("货号已存在");
        }
        ScreenEntity screenEntity = new ScreenEntity();     //新建插入对象，为了调用系统自带插入方法
        screenEntity.setArticleNumber(articleNumber);      //给对象设值
        screenEntity.setCreatedDate(new Date());
        boolean b = screenService.insert(screenEntity);  //系统自带插入方法，需要插入对象就，返回值是boolean类型
        if (!b) {                                    //校验boolean类型
            return BaseResp.error("插入失败");
        }
        return BaseResp.ok(b);
    }

    @PostMapping("/testRedis")
    public BaseResp testRedis() {
        //set name leo
        redisTemplate.opsForValue().set("names", "leo");
        UserEntity userEntity = new UserEntity();
        userEntity.setNickName("leomao");
        redisTemplate.opsForValue().set("User", userEntity);
        List<String> list1 = new ArrayList<String>();
        list1.add("a1");
        list1.add("a2");
        list1.add("a3");
        redisTemplate.opsForList().leftPush("listkey1", list1);
        stringRedisTemplate.opsForValue().set("string", "char");
        return BaseResp.ok("sucess");
    }

    @PostMapping("/getRedis")
    public BaseResp getRedis(String key) {
        Object ob = redisTemplate.boundValueOps(key).get();
//        Object object = redisTemplate.opsForList().leftPop(key);            //list的取值
        return BaseResp.ok(ob);
    }
}
