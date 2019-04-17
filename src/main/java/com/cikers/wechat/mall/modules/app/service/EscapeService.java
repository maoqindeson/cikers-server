package com.cikers.wechat.mall.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.EscapeEntity;

import java.util.List;


public interface EscapeService extends IService<EscapeEntity> {
    String getSearchKeyByKeyword(String keyWord);
    EscapeEntity getByKeyword(String keyWord);
    List<EscapeEntity> getAll();
    int updateByKeyword(String keyWord, String searchKey);

}
