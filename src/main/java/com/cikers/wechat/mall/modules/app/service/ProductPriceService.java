package com.cikers.wechat.mall.modules.app.service;


import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.ProductPriceEntity;


public interface ProductPriceService extends IService<ProductPriceEntity> {
    int truncate();
}
