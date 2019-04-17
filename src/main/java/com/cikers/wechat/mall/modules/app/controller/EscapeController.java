package com.cikers.wechat.mall.modules.app.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cikers.wechat.mall.modules.app.entity.EscapeEntity;
import com.cikers.wechat.mall.modules.app.service.EscapeService;
import com.cikers.wechat.mall.modules.app.utils.BaseResp;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/escape")
public class EscapeController {
    @Autowired
    private EscapeService escapeService;

    @PostMapping("/getEscapeEntity")
    public BaseResp getEscapeEntity(){
    List<EscapeEntity>list = escapeService.getAll();
    if(list== null||list.isEmpty()){
        return BaseResp.error("查询结果为空");
    }
        Map<String,String> map = new HashMap<>();
        for(EscapeEntity escapeEntity:list){
            map.put(escapeEntity.getKeyWord(),escapeEntity.getSearchKey());
        }
    return BaseResp.ok("查询成功",map);
    }

    @PostMapping("/insertEscapeEntity")
    public BaseResp insertEscapeEntity(String keyword,String seachKey){
//      List<EscapeEntity>list =  escapeService.selectList(new EntityWrapper<EscapeEntity>().eq("key_word",keyword));
//      if(list!=null&&!list.isEmpty()){        //校验list非null和非empty的时候用&&
//          return BaseResp.error("已存在");
//      }
        EscapeEntity escapeEntity =escapeService.selectOne(new EntityWrapper<EscapeEntity>().eq("key_word",keyword));
        if (escapeEntity==null){
            escapeEntity = new EscapeEntity();
        }
        escapeEntity.setKeyWord(keyword);
        escapeEntity.setSearchKey(seachKey);
        escapeEntity.setCreatedDate(new Date());
        //系统自带insertOrUpdate方法，当keyword存在就update，不存在就insert,45-48行必须要写，这里keyword在表中需要是唯一
        //因为接受的是对象
        boolean b = escapeService.insertOrUpdate(escapeEntity);
        if(!b){
            return BaseResp.error("插入失败");
        }
        return BaseResp.ok("插入成功",b);
    }
}

