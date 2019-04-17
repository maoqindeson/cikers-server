package com.cikers.wechat.mall.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.ProductDao;
import com.cikers.wechat.mall.modules.app.entity.ProductEntity;
import com.cikers.wechat.mall.modules.app.entity.PropertyEntity;
import com.cikers.wechat.mall.modules.app.service.ProductService;
import com.cikers.wechat.mall.modules.app.service.PropertyRelationService;
import com.cikers.wechat.mall.modules.app.service.PropertyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductDao, ProductEntity> implements ProductService {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyRelationService propertyRelationService;



    @Override
    public int truncate() {
        return baseMapper.truncate();
    }

    @Override
    public List<ProductEntity> maoGetProductByEqptId(Long eqptId) {
        return baseMapper.getProductByEqptId(eqptId);
    }


    @Override
    public List<ProductEntity> getProductByEqptId(Long eqptId) {
        List<ProductEntity> productEntities = baseMapper.getProductByEqptId(eqptId);
        if (productEntities != null) {
            for (ProductEntity productEntity : productEntities) {
                Map<String, PropertyEntity> propertyEntityMap = propertyRelationService.selectByProductId(productEntity.getId());
                if (!propertyEntityMap.isEmpty()) {
                    productEntity.setProps(propertyEntityMap);
                }
            }
            return productEntities;
        }
        return null;
    }

    @Override
    public ProductEntity getProductById(Long id) {
        return baseMapper.getProductById(id);
    }
    @Override
    public ProductEntity getAllById(Long id) {
        return baseMapper.getAllById(id);
    }

    @Override
    public List<Long> selectEqpTIdsByProductIds(List<Long> Ids) {
        return baseMapper.selectEqpTIdsByProductIds(Ids);
    }
}
