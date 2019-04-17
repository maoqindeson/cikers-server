package com.cikers.wechat.mall.modules.app.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cikers.wechat.mall.modules.app.dao.PropertyDao;
import com.cikers.wechat.mall.modules.app.entity.PropertyEntity;
import com.cikers.wechat.mall.modules.app.service.PropertyService;
import com.cikers.wechat.mall.modules.app.utils.PageUtils;
import com.cikers.wechat.mall.modules.app.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("propertyService")
public class PropertyServiceImpl extends ServiceImpl<PropertyDao, PropertyEntity> implements PropertyService {


    @Override
    public boolean insert(PropertyEntity propertyEntity) {
        PropertyEntity result = selectById(propertyEntity.getId());
        if (result == null) {
            baseMapper.insert(propertyEntity);
        }
        return true;
    }

    @Override
    public int truncate() {
        return baseMapper.truncate();
    }

    @Override
    public List<PropertyEntity> getByPropertyId(Long propertyId) {
        return baseMapper.getByPropertyId(propertyId);
    }


    @Override
    public List<PropertyEntity> queryTypes() {
        return baseMapper.queryTypes();
    }

    @Override
    public List<PropertyEntity> selectByEqptIds(List<Long> Ids) {
        return baseMapper.selectByEqptIds(Ids);
    }

}
