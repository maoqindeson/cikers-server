package com.cikers.wechat.mall.modules.app.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.ProductPriceDao;
import com.cikers.wechat.mall.modules.app.entity.ProductPriceEntity;
import com.cikers.wechat.mall.modules.app.service.ProductPriceService;
import org.springframework.stereotype.Service;


@Service("productPriceService")
public class ProductPriceServiceImpl extends ServiceImpl<ProductPriceDao, ProductPriceEntity> implements ProductPriceService {

    @Override
    public int truncate() {
        return baseMapper.truncate();
    }
}
