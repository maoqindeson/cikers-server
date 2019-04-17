package com.cikers.wechat.mall.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.ScreenEntity;

import java.util.List;


public interface ScreenService extends IService<ScreenEntity> {
    List<String> getArticleNumbers();
}
