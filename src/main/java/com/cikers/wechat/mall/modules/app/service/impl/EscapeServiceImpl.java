package com.cikers.wechat.mall.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.Escapedao;
import com.cikers.wechat.mall.modules.app.entity.EscapeEntity;
import com.cikers.wechat.mall.modules.app.service.EscapeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("escapeService")
public class EscapeServiceImpl extends ServiceImpl<Escapedao, EscapeEntity> implements EscapeService {

    @Override
    public String getSearchKeyByKeyword(String keyWord) {
        return baseMapper.getSearchKeyByKeyword(keyWord);
    }

    @Override
    public EscapeEntity getByKeyword(String keyWord) {
        return baseMapper.getByKeyword(keyWord);
    }

    @Override
    public List<EscapeEntity> getAll() {
        return baseMapper.getAll();
    }

    @Override
    public int updateByKeyword(String keyWord, String searchKey) {
        return baseMapper.updateByKeyword(keyWord,searchKey);
    }

//    public static void main(String[] args) {
//        JsonParser parser = new JsonParser();
//        JsonObject json = null;
//        try {
//            json = (JsonObject) parser.parse(new FileReader( "escape.json"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            log.error("搜索关键字初始化异常");
//        }
//        // log.warn("json接口getjson is ++" + json);
//        String jsonstr = json.toString();
//        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(jsonstr);
//    }
}
