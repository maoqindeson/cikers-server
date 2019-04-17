package com.cikers.wechat.mall.modules.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.cikers.wechat.mall.modules.app.entity.ProductEntity;
import com.cikers.wechat.mall.modules.app.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * @author hwp
 * @email weiping_david@foxmail.com
 * @date 2018-06-10 15:14:01
 */
public interface ProductService extends IService<ProductEntity> {


    int truncate();
    List<ProductEntity> maoGetProductByEqptId(Long eqptId);
    List<ProductEntity> getProductByEqptId(Long eqptId);
    ProductEntity getProductById(Long id);
    ProductEntity getAllById(Long id);
    List<Long> selectEqpTIdsByProductIds(List<Long> Ids);
}

